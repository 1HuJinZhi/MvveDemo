package com.hujz.mvvedemo.ui.page

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hujz.mvvedemo.R
import com.hujz.mvvedemo.bridge.request.MusicRequestViewModel
import com.hujz.mvvedemo.bridge.state.MainViewModel
import com.hujz.mvvedemo.data.bean.TestAlbum
import com.hujz.mvvedemo.databinding.AdapterPlayItemBinding
import com.hujz.mvvedemo.databinding.FragmentMainBinding
import com.hujz.mvvedemo.player.PlayerManager
import com.hujz.mvvedemo.ui.base.BaseFragment
import com.hujz.mvvedemo.ui.helper.DrawerCoordinateHelper
import com.kunminx.architecture.ui.adapter.SimpleBaseBindingAdapter

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/24
 *     desc   :
 * </pre>
 */
class MainFragment : BaseFragment() {

    private lateinit var mBinding: FragmentMainBinding
    private lateinit var mAdapter: SimpleBaseBindingAdapter<TestAlbum.TestMusic, AdapterPlayItemBinding>
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mMusicRequestViewModel: MusicRequestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mBinding = FragmentMainBinding.bind(view)
        mBinding.apply {
            click = ClickProxy()
            vm = mMainViewModel
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMainViewModel.initTabAndPage.set(true)
        mMainViewModel.pageAssetPath.set("summary.html")
        mAdapter = object : SimpleBaseBindingAdapter<TestAlbum.TestMusic, AdapterPlayItemBinding>(
            context,
            R.layout.adapter_play_item
        ) {
            override fun onSimpleBindItem(
                binding: AdapterPlayItemBinding,
                item: TestAlbum.TestMusic,
                holder: RecyclerView.ViewHolder
            ) {
                binding.tvTitle.text = item.title
                binding.tvArtist.text = item.artist.name
                Glide.with(binding.ivCover.context).load(item.coverImg).into(binding.ivCover)
                val currentIndex = PlayerManager.instance.albumIndex
                binding.ivPlayStatus.setColor(
                    if (currentIndex == holder.adapterPosition) resources.getColor(
                        R.color.gray
                    ) else Color.TRANSPARENT
                )
                binding.root.setOnClickListener {
                    PlayerManager.instance.playAudio(holder.adapterPosition)
                }
            }
        }
        mBinding.rv.adapter = mAdapter

        // TODO tip 1：所有播放状态的改变，都要通过这个 作为 唯一可信源 的 PlayerManager 来统一分发，

        // 如此才能方便 追溯事件源，以及 避免 不可预期的 推送和错误。
        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249
        PlayerManager.instance.changeMusicLiveData.observe(this, Observer {
            mAdapter.notifyDataSetChanged()
        })

        mMusicRequestViewModel.freeMusicsLiveData.observe(this, Observer {
            if (it == null || it.musics == null) {
                return@Observer
            }
            mAdapter.list = it.musics
            mAdapter.notifyDataSetChanged()
            val album = PlayerManager.instance.album

            // TODO tip 4：未作 UnPeek 处理的 用于 request 的 LiveData，在视图控制器重建时会自动倒灌数据

            // 一定要记住这一点，因为如果没有妥善处理，这里就会出现预期外的错误，一定要记得它在重建时 是一定会倒灌的。

            // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/0129483567

            if (album == null || album.albumId == it.albumId)
                PlayerManager.instance.loadAlbum(it)
        })

        val album = PlayerManager.instance.album
        if (album == null)
            mMusicRequestViewModel.requestFreeMusics()
        else {
            mAdapter.list = album.musics
            mAdapter.notifyDataSetChanged()
        }

        DrawerCoordinateHelper.instance.openDrawer.observe(this, Observer {
            mSharedViewModel.openOrCloseDrawer.setValue(true)
        })
    }

    // TODO tip 2：此处通过 DataBinding 来规避 在 setOnClickListener 时存在的 视图调用的一致性问题，

    // 也即，有绑定就有绑定，没绑定也没什么大不了的，总之 不会因一致性问题造成 视图调用的空指针。
    // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

    inner class ClickProxy {

        fun openMenu() {

            // TODO tip 3：此处演示通过 UnPeekLiveData 来发送 生命周期安全的、事件源可追溯的 通知。

            // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249
            // --------
            // 与此同时，此处传达的另一个思想是 最少知道原则，
            // Activity 内部的事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部的东西。
            // 因为 Activity 端的处理后续可能会改变，并且可受用于更多的 fragment，而不单单是本 fragment。

            mSharedViewModel.openOrCloseDrawer.value = true
        }

        fun search() {
            nav().navigate(R.id.action_mainFragment_to_searchFragment)
        }

    }
}