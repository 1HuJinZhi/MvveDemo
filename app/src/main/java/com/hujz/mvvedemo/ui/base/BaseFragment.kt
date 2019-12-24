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

    protected lateinit var mActivity: AppCompatActivity
    protected lateinit var mSharedViewModel: SharedViewModel
    protected var mAnimationEnterLoaded: Boolean = false
    protected var mAnimationLoaded: Boolean = false
    protected var mInitDataCame: Boolean = false

    private val mHandler = Handler()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedViewModel = getAppViewModelProvider().get(SharedViewModel::class.java)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        mHandler.postDelayed({
            mAnimationLoaded = true
            if (mInitDataCame && !mAnimationEnterLoaded) {
                mAnimationEnterLoaded = true
                loadInitData()
            }
        }, 280)
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    open fun loadInitData() {

    }

    fun showLongToast(text: String) {
        Toast.makeText(mActivity.applicationContext, text, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(text: String) {
        Toast.makeText(mActivity.applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(stringRes: Int) {
        showLongToast(mActivity.applicationContext.getString(stringRes))
    }

    fun showShortToast(stringRes: Int) {
        showShortToast(mActivity.applicationContext.getString(stringRes))
    }

    private fun getAppViewModelProvider(): ViewModelProvider {
        return (mActivity.applicationContext as App).getAppViewModelProvider(mActivity)
    }

    protected fun nav(): NavController {
        return NavHostFragment.findNavController(this)
    }

}