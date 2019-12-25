package com.hujz.mvvedemo.ui.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.hujz.mvvedemo.App
import com.hujz.mvvedemo.bridge.callback.SharedViewModel

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/24
 *     desc   :
 * </pre>
 */
open class BaseFragment : Fragment() {

    lateinit var activity: AppCompatActivity
    lateinit var sharedViewModel: SharedViewModel
    var animationEnterLoaded: Boolean = false
    var animationLoaded: Boolean = false
    var initDataCame: Boolean = false

    private val mHandler = Handler()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = getAppViewModelProvider().get(SharedViewModel::class.java)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        mHandler.postDelayed({
            animationLoaded = true
            if (initDataCame && !animationEnterLoaded) {
                animationEnterLoaded = true
                loadInitData()
            }
        }, 280)
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    open fun loadInitData() {

    }

    fun showLongToast(text: String) {
        Toast.makeText(activity.applicationContext, text, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(text: String) {
        Toast.makeText(activity.applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(stringRes: Int) {
        showLongToast(activity.applicationContext.getString(stringRes))
    }

    fun showShortToast(stringRes: Int) {
        showShortToast(activity.applicationContext.getString(stringRes))
    }

    private fun getAppViewModelProvider(): ViewModelProvider {
        return (activity.applicationContext as App).getAppViewModelProvider(activity)
    }

    protected fun nav(): NavController {
        return NavHostFragment.findNavController(this)
    }

}