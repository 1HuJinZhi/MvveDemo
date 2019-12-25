package com.hujz.mvvedemo.player

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.hujz.mvvedemo.data.bean.TestAlbum
import com.hujz.mvvedemo.player.notification.PlayerService
import com.kunminx.player.PlayerController
import com.kunminx.player.bean.base.BaseAlbumItem
import com.kunminx.player.bean.base.BaseMusicItem
import com.kunminx.player.bean.dto.ChangeMusic
import com.kunminx.player.bean.dto.PlayingMusic
import com.kunminx.player.contract.IPlayController
import com.kunminx.player.contract.IServiceNotifier

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/23
 *     desc   :
 * </pre>
 */
class PlayerManager : IPlayController<TestAlbum, TestAlbum.TestMusic> {

    private lateinit var mContext: Context

    companion object {
        val instance: PlayerManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { PlayerManager() }
        private val mController: PlayerController<TestAlbum, TestAlbum.TestMusic> =
            PlayerController()
    }

    override fun init(context: Context, iServiceNotifier: IServiceNotifier?) {
        mContext = context.applicationContext
        mController.init(mContext, null) { startOrStop: Boolean ->
            val intent = Intent(mContext, PlayerService::class.java)
            if (startOrStop) mContext.startService(intent)
            else mContext.stopService(intent)
        }
    }

    override fun loadAlbum(musicAlbum: TestAlbum) {
        mController.loadAlbum(mContext, musicAlbum)
    }

    override fun loadAlbum(musicAlbum: TestAlbum, playIndex: Int) {
        mController.loadAlbum(mContext, musicAlbum, playIndex)
    }

    override fun playAudio() {
        mController.playAudio(mContext)
    }

    override fun playAudio(albumIndex: Int) {
        mController.playAudio(mContext, albumIndex)
    }

    override fun playNext() {
        mController.playNext(mContext)
    }

    override fun playPrevious() {
        mController.playPrevious(mContext)
    }

    override fun playAgain() {
        mController.playAgain(mContext)
    }

    override fun pauseAudio() {
        mController.pauseAudio()
    }

    override fun resumeAudio() {
        mController.resumeAudio()
    }

    override fun clear() {
        mController.clear(mContext)
    }

    override fun changeMode() {
        mController.changeMode()
    }

    override fun isPlaying(): Boolean {
        return mController.isPlaying
    }

    override fun isPaused(): Boolean {
        return mController.isPaused
    }

    override fun isInited(): Boolean {
        return mController.isInited
    }

    override fun requestLastPlayingInfo() {
        mController.requestLastPlayingInfo()
    }

    override fun setSeek(progress: Int) {
        mController.setSeek(progress)
    }

    override fun getTrackTime(progress: Int): String {
        return mController.getTrackTime(progress)
    }

    override fun getAlbum(): TestAlbum? {
        return mController.album
    }

    override fun getAlbumMusics(): List<TestAlbum.TestMusic> {
        return mController.albumMusics
    }

    override fun setChangingPlayingMusic(changingPlayingMusic: Boolean) {
        mController.setChangingPlayingMusic(mContext, changingPlayingMusic)
    }

    override fun getAlbumIndex(): Int {
        return mController.albumIndex
    }

    override fun getChangeMusicLiveData(): MutableLiveData<ChangeMusic<*, *, *>> {
        return mController.changeMusicLiveData
    }

    override fun getPlayingMusicLiveData(): MutableLiveData<PlayingMusic<*, *>> {
        return mController.playingMusicLiveData
    }

    override fun getPauseLiveData(): MutableLiveData<Boolean> {
        return mController.pauseLiveData
    }

    override fun getPlayModeLiveData(): MutableLiveData<Enum<*>> {
        return mController.playModeLiveData
    }

    override fun getRepeatMode(): Enum<*>? {
        return mController.repeatMode
    }

    override fun togglePlay() {
        mController.togglePlay(mContext)
    }

    override fun getCurrentPlayingMusic(): TestAlbum.TestMusic {
        return mController.currentPlayingMusic
    }

}