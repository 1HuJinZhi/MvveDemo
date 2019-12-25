package com.hujz.mvvedemo.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hujz.mvvedemo.R
import com.hujz.mvvedemo.bridge.callback.SharedViewModel
import com.hujz.mvvedemo.bridge.state.PlayerViewModel
import com.hujz.mvvedemo.databinding.FragmentPlayerBinding
import com.hujz.mvvedemo.player.PlayerManager
import com.hujz.mvvedemo.ui.base.BaseFragment
import com.hujz.mvvedemo.ui.view.PlayerSlideListener
import com.kunminx.player.PlayingInfoManager
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/25
 *     desc   :
 * </pre>
 */
class PlayerFragment : BaseFragment() {

    private lateinit var mBinding: FragmentPlayerBinding
    private lateinit var mPlayerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPlayerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player, container, false)
        // TODO tip 1: 此处通过 DataBinding 来规避 潜在的 视图调用的一致性问题，

        // 因为本项目采用 横、竖 两套布局，且不同布局的控件存在差异，
        // 在 DataBinding 的适配器模式加持下，有绑定就有绑定，没绑定也没什么大不了的，
        // 总之 不会因一致性问题造成 视图调用的空指针。

        // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350
        mBinding = FragmentPlayerBinding.bind(view)
        mBinding.apply {
            click = ClickProxy()
            vm = mPlayerViewModel
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.timeToAddSlideListener.observe(this, Observer {
            (view.parent.parent as? SlidingUpPanelLayout)?.apply {
                addPanelSlideListener(PlayerSlideListener(mBinding, this))
                addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
                    override fun onPanelSlide(panel: View?, slideOffset: Float) {

                    }

                    override fun onPanelStateChanged(
                        panel: View?,
                        previousState: SlidingUpPanelLayout.PanelState?,
                        newState: SlidingUpPanelLayout.PanelState?
                    ) {
                        if (newState === SlidingUpPanelLayout.PanelState.EXPANDED)
                            SharedViewModel.tagOfSecondaryPages.add(this.javaClass.name)
                        else
                            SharedViewModel.tagOfSecondaryPages.remove(this.javaClass.name)
                        sharedViewModel.enableSwipeDrawer.value =
                            SharedViewModel.tagOfSecondaryPages.size == 0
                    }
                })
            }
        })

        PlayerManager.instance.changeMusicLiveData.observe(this, Observer {

            // TODO tip 3：同 tip 2.

            // 切歌时，音乐的标题、作者、封面 状态的改变
            mPlayerViewModel.title.set(it.title)
            mPlayerViewModel.artist.set(it.summary)
            mPlayerViewModel.coverImg.set(it.img)
        })

        PlayerManager.instance.playingMusicLiveData.observe(this, Observer {

            // TODO tip 4：同 tip 2.

            // 播放进度 状态的改变
            mPlayerViewModel.maxSeekDuration.set(it.duration)
            mPlayerViewModel.currentSeekPosition.set(it.playerPosition)
        })

        PlayerManager.instance.pauseLiveData.observe(this, Observer {

            // TODO tip 2：所有播放状态的改变，都要通过这个 作为 唯一可信源 的 PlayerManager 来统一分发，

            // 如此才能方便 追溯事件源、保证 全应用范围内 所有状态的正确和及时，以及 避免 不可预期的 推送和错误。

            // 👆👆👆 划重点

            // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249

            // 播放按钮 状态的改变
            mPlayerViewModel.isPlaying.set(!it)
        })

        PlayerManager.instance.playModeLiveData.observe(this, Observer {
            val tip = when (it) {
                PlayingInfoManager.RepeatMode.LIST_LOOP -> {
                    mPlayerViewModel.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT)
                    R.string.play_repeat
                }
                PlayingInfoManager.RepeatMode.ONE_LOOP -> {
                    mPlayerViewModel.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT_ONCE)
                    R.string.play_repeat_once
                }
                else -> {
                    mPlayerViewModel.playModeIcon.set(MaterialDrawableBuilder.IconValue.SHUFFLE)
                    R.string.play_shuffle
                }
            }
            if ((view.parent.parent as? SlidingUpPanelLayout)?.panelState === SlidingUpPanelLayout.PanelState.EXPANDED) {
                showShortToast(tip)
            }
        })

        sharedViewModel.closeSlidePanelIfExpanded.observe(this, Observer {

            // 按下返回键，如果此时 slide 面板是展开的，那么只对面板进行 slide down
            val parent = view.parent.parent as? SlidingUpPanelLayout
            if (parent != null) {
                if (parent.panelState === SlidingUpPanelLayout.PanelState.EXPANDED) {
                    parent.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                } else {

                    // TODO tip 6：此处演示通过 UnPeekLiveData 来发送 生命周期安全的、事件源可追溯的 通知。

                    // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249
                    // --------
                    // 与此同时，此处传达的另一个思想是 最少知道原则，
                    // Activity 内部的事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部的东西。
                    // 因为 Activity 端的处理后续可能会改变，并且可受用于更多的 fragment，而不单单是本 fragment。

                    // TODO: yes:
                    sharedViewModel.activityCanBeClosedDirectly.value = true
                }
            } else {
                sharedViewModel.activityCanBeClosedDirectly.value = true
            }
        })
    }

    // TODO tip 7：此处通过 DataBinding 来规避 在 setOnClickListener 时存在的 视图调用的一致性问题，

    // 也即，有绑定就有绑定，没绑定也没什么大不了的，总之 不会因一致性问题造成 视图调用的空指针。
    // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

    inner class ClickProxy : SeekBar.OnSeekBarChangeListener {

        fun playMode() {
            PlayerManager.instance.changeMode()
        }

        fun previous() {
            PlayerManager.instance.playPrevious()
        }

        fun togglePlay() {
            PlayerManager.instance.togglePlay()
        }

        operator fun next() {
            PlayerManager.instance.playNext()
        }

        fun showPlayList() {
            showShortToast(R.string.unfinished)
        }

        fun slideDown() {
            sharedViewModel.closeSlidePanelIfExpanded.value = true
        }

        fun more() {}

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            PlayerManager.instance.setSeek(seekBar.progress)
        }
    }
}