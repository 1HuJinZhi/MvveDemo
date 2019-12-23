package com.hujz.mvvedemo.data.bean

import com.kunminx.player.bean.base.BaseAlbumItem
import com.kunminx.player.bean.base.BaseArtistItem
import com.kunminx.player.bean.base.BaseMusicItem

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/23
 *     desc   :
 * </pre>
 */
data class TestAlbum(val albumMid: String) :
    BaseAlbumItem<TestAlbum.TestMusic, TestAlbum.TestArtist>() {

    data class TestMusic(val songMid: String) : BaseMusicItem<TestArtist>()

    data class TestArtist(val birthday: String) : BaseArtistItem()

}
