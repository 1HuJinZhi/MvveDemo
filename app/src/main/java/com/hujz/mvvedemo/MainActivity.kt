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

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var mBinding: ActivityMainBinding
    private var isListened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.apply {
            mBinding.lifecycleOwner = this@MainActivity
            mBinding.vm = mainActivityViewModel
        }

        sharedViewModel.activityCanBeClosedDirectly.observe(
            this,
            Observer<Boolean> {
                val nav = Navigation.findNavController(this, R.id.main_fragment_host)
                when {
                    nav.currentDestination?.id == R.id.mainFragment -> nav.navigateUp()
                    mBinding.dl?.isDrawerOpen(GravityCompat.START) == true -> mBinding.dl?.closeDrawer(
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
            // TODO tip 2：此处演示通过 UnPeekLiveData 来发送 生命周期安全的、事件源可追溯的 通知。

            // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249
            // --------
            // 与此同时，此处传达的另一个思想是 最少知道原则，
            // fragment 内部的事情在 fragment 内部消化，不要试图在 Activity 中调用和操纵 Fragment 内部的东西。
            // 因为 fragment 端的处理后续可能会改变，并且可受用于更多的 Activity，而不单单是本 Activity。

            sharedViewModel.timeToAddSlideListener.value = true

            isListened = true
        }
    }

    override fun onBackPressed() {
        sharedViewModel.closeSlidePanelIfExpanded.value = true
    }

}
