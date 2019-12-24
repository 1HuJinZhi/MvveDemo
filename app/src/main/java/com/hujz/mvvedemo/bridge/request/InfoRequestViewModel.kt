package com.hujz.mvvedemo.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hujz.mvvedemo.data.bean.LibraryInfo
import com.hujz.mvvedemo.data.repository.HttpRequestManager
import com.hujz.mvvedemo.data.usecase.TestUseCase
import com.hujz.mvvedemo.data.usecase.base.UseCase
import com.hujz.mvvedemo.data.usecase.base.UseCaseHandler

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/24
 *     desc   :
 * </pre>
 */
class InfoRequestViewModel : ViewModel() {

    val libraryLiveData by lazy { MutableLiveData<List<LibraryInfo>?>() }

    val testUseCase by lazy { TestUseCase() }

    val testXXX: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun requestLibraryInfo() {
        HttpRequestManager.getInstance().getLibraryInfo(libraryLiveData)
    }

    fun requestTestXXX() {
        UseCaseHandler.getInstance().execute(testUseCase,
            TestUseCase.RequestValues(0, 0),
            object : UseCase.UseCaseCallback<TestUseCase.ResponseValue> {
                override fun onSuccess(response: TestUseCase.ResponseValue) {
                    testXXX.value = response.result
                }

                override fun onError() {
                    //TODO 此处使用相应的 LiveDate 通知 UI 层
                }
            })
    }

}