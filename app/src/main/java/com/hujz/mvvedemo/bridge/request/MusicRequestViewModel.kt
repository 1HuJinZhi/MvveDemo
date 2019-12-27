package com.hujz.mvvedemo.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hujz.mvvedemo.data.bean.TestAlbum
import com.hujz.mvvedemo.data.repository.HttpRequestManager

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/24
 *     desc   :
 * </pre>
 */
class MusicRequestViewModel : ViewModel() {

    val freeMusicsLiveData by lazy { MutableLiveData<TestAlbum?>() }

    fun requestFreeMusics() {
        HttpRequestManager.getInstance().getFreeMusic(freeMusicsLiveData)
    }

}