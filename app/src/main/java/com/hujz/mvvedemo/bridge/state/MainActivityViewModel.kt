package com.hujz.mvvedemo.bridge.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * TODO tip：每个页面都要单独准备一个 statusViewModel，
 * 来托管 DataBinding 绑定的临时状态，以及视图控制器重建时状态的恢复。
 * <p>
 * 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350
 * <p>
 * Create by KunMinX at 19/10/29
 */
class MainActivityViewModel : ViewModel() {

    val openDrawer = MutableLiveData<Boolean>()

    val allowDrawerOpen = MutableLiveData<Boolean>()

    init {
        allowDrawerOpen.value = true
    }

}