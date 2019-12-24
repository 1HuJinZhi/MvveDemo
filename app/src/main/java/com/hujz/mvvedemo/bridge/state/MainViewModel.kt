package com.hujz.mvvedemo.bridge.state

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/24
 *     desc   :
 * </pre>
 */
class MainViewModel : ViewModel() {

    val initTabAndPage = ObservableBoolean()

    val pageAssetPath = ObservableField<String>()

}