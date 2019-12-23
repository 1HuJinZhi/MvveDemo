package com.hujz.mvvedemo

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.blankj.utilcode.util.Utils
import com.hujz.mvvedemo.player.PlayerManager

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/23
 *     desc   :
 * </pre>
 */
class App : Application(), ViewModelStoreOwner {

    private val mAppViewModelStore: ViewModelStore by lazy { ViewModelStore() }
    private val mFactory: ViewModelProvider.Factory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)
        PlayerManager.instance.init(this, null)
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    fun getAppViewModelProvider(activity: Activity): ViewModelProvider {
        checkNotNull(activity.application) { "Your activity/fragment is not yet attached to Application. You can't request ViewModel before onCreate call." }
        return ViewModelProvider(this, mFactory)
    }

}
