package com.hujz.mvvedemo.ui.base

import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hujz.mvvedemo.App
import com.hujz.mvvedemo.bridge.callback.SharedViewModel
import com.kunminx.architecture.data.manager.NetState
import com.kunminx.architecture.data.manager.NetworkStateManager
import com.kunminx.architecture.utils.AdaptScreenUtils
import com.kunminx.architecture.utils.ScreenUtils

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/24
 *     desc   :
 * </pre>
 */
open class BaseActivity : AppCompatActivity() {

    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = getAppViewModelProvider().get(SharedViewModel::class.java)

        NetworkStateManager.getInstance().networkStateCallback.observe(
            this,
            Observer<NetState> {
                //TODO 这里可以执行统一的网络状态通知和处理
            })
    }

    override fun getResources(): Resources {
        return if (ScreenUtils.isPortrait()) {
            AdaptScreenUtils.adaptWidth(super.getResources(), 360)
        } else {
            AdaptScreenUtils.adaptHeight(super.getResources(), 640)
        }
    }

    fun showLongToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    private fun getAppViewModelProvider(): ViewModelProvider {
        return (applicationContext as App).getAppViewModelProvider(this)
    }
}