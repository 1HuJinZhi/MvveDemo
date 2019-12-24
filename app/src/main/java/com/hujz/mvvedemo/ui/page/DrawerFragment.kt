package com.hujz.mvvedemo.ui.page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.hujz.mvvedemo.R
import com.hujz.mvvedemo.bridge.request.InfoRequestViewModel
import com.hujz.mvvedemo.bridge.state.DrawerViewModel
import com.hujz.mvvedemo.data.bean.LibraryInfo
import com.hujz.mvvedemo.databinding.AdapterLibraryBinding
import com.hujz.mvvedemo.databinding.FragmentDrawerBinding
import com.hujz.mvvedemo.ui.base.BaseFragment
import com.kunminx.architecture.ui.adapter.SimpleBaseBindingAdapter

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/24
 *     desc   :
 * </pre>
 */
class DrawerFragment : BaseFragment() {

    private lateinit var mBinding: FragmentDrawerBinding
    private lateinit var mDrawerViewModel: DrawerViewModel
    private lateinit var mInfoRequestViewModel: InfoRequestViewModel
    private lateinit var mAdapter: SimpleBaseBindingAdapter<LibraryInfo, AdapterLibraryBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInfoRequestViewModel = ViewModelProviders.of(this).get(InfoRequestViewModel::class.java)
        mDrawerViewModel = ViewModelProviders.of(this).get(DrawerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drawer, container, false)
        mBinding = FragmentDrawerBinding.bind(view)
        mBinding.apply {
            click = ClickProxy()
            vm = mDrawerViewModel
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(mInfoRequestViewModel.testUseCase)

        mAdapter = object : SimpleBaseBindingAdapter<LibraryInfo, AdapterLibraryBinding>(
            context,
            R.layout.adapter_library
        ) {
            override fun onSimpleBindItem(
                binding: AdapterLibraryBinding,
                item: LibraryInfo,
                holder: RecyclerView.ViewHolder
            ) {
                binding.tvTitle.text = item.title
                binding.tvSummary.text = item.summary
                binding.root.setOnClickListener {
                    val uri = Uri.parse(item.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
        }

        mBinding.rv.adapter = mAdapter

        mInfoRequestViewModel.libraryLiveData.observe(this, Observer {
            mInitDataCame = true
            if (mAnimationLoaded && it != null) {
                mAdapter.list = it
                mAdapter.notifyDataSetChanged()
            }
        })

        mInfoRequestViewModel.requestLibraryInfo()

        mInfoRequestViewModel.testXXX.observe(this, Observer {
            //TODO tip3：暂无实际功能，仅演示 UseCase 流程

            //接收来自 可感知生命周期的 UseCase 处理的结果
        })

        //TODO tip2：暂无实际功能，仅演示 UseCase 流程
        mInfoRequestViewModel.requestTestXXX()
    }


    override fun loadInitData() {
        super.loadInitData()
        mInfoRequestViewModel.libraryLiveData.value?.let {
            mAdapter.list = it
            mAdapter.notifyDataSetChanged()
        }
    }

    inner class ClickProxy {

        fun logoClick() {
            val u = "https://github.com/KunMinX/Jetpack-MVVM-Best-Practice"
            val uri = Uri.parse(u)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

}