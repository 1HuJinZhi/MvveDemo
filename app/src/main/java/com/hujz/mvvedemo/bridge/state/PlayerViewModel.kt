package com.hujz.mvvedemo.bridge.state

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.hujz.mvvedemo.R
import com.hujz.mvvedemo.player.PlayerManager
import com.kunminx.architecture.utils.Utils
import com.kunminx.player.PlayingInfoManager
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/25
 *     desc   :
 * </pre>
 */
class PlayerViewModel : ViewModel() {

    val title = ObservableField<String>()

    val artist = ObservableField<String>()

    val coverImg = ObservableField<String>()

    val placeHolder = ObservableField<Drawable>()

    val maxSeekDuration = ObservableInt()

    val currentSeekPosition = ObservableInt()

    val isPlaying = ObservableBoolean()

    val playModeIcon = ObservableField<MaterialDrawableBuilder.IconValue>()

    init {
        title.set(Utils.getApp().getString(R.string.app_name))
        artist.set(Utils.getApp().getString(R.string.app_name))
        placeHolder.set(ContextCompat.getDrawable(Utils.getApp(), R.drawable.bg_album_default))

        when {
            PlayerManager.instance.repeatMode == PlayingInfoManager.RepeatMode.LIST_LOOP -> playModeIcon.set(
                MaterialDrawableBuilder.IconValue.REPEAT
            )
            PlayerManager.instance.repeatMode == PlayingInfoManager.RepeatMode.ONE_LOOP -> playModeIcon.set(
                MaterialDrawableBuilder.IconValue.REPEAT_ONCE
            )
            else -> playModeIcon.set(MaterialDrawableBuilder.IconValue.SHUFFLE)
        }
    }
}