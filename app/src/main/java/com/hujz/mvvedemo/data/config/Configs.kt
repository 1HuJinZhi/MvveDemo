package com.hujz.mvvedemo.data.config

import com.kunminx.architecture.utils.Utils

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/23
 *     desc   :
 * </pre>
 */
class Configs {

    companion object {
        @JvmField
        val CACHE_PATH: String = Utils.getApp().cacheDir.absolutePath
        @JvmField
        val MUSIC_DOWNLOAD_PATH = "$CACHE_PATH/"
    }

}