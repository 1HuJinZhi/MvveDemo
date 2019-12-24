package com.hujz.mvvedemo

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.hujz.mvvedemo.bridge.state.MainActivityViewModel
import com.hujz.mvvedemo.databinding.ActivityMainBinding
import com.hujz.mvvedemo.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var mBinding: ActivityMainBinding
    private var isListened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this
        mBinding.vm = mainActivityViewModel

        sharedViewModel.activityCanBeClosedDirectly.observe(
            this,
            Observer<Boolean> {
                val nav = Navigation.findNavController(this, R.id.main_fragment_host)
                when {
                    nav.currentDestination?.id == R.id.mainFragment -> nav.navigateUp()
                    mBinding.dl?.isDrawerOpen(GravityCompat.START) == true -> mBinding.dl.closeDrawer(
                        GravityCompat.START
                    )
                    else -> super.onBackPressed()
                }
            })

        sharedViewModel.openOrCloseDrawer.observe(
            this,
            Observer { mainActivityViewModel.openDrawer.value = it })

        sharedViewModel.enableSwipeDrawer.observe(this,
            Observer { mainActivityViewModel.allowDrawerOpen.value = it })

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!isListened) {
            sharedViewModel
        }
    }

    override fun onBackPressed() {
        sharedViewModel.closeSlidePanelIfExpanded.value = true
    }

}
