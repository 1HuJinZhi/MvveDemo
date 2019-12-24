package com.hujz.mvvedemo.bridge.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    val openDrawer = MutableLiveData<Boolean>()

    val allowDrawerOpen = MutableLiveData<Boolean>()

    init {
        allowDrawerOpen.value = true
    }

}