package com.hujz.mvvedemo.ui.helper

import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.hujz.mvvedemo.bridge.callback.SharedViewModel
import com.hujz.mvvedemo.ui.base.BaseFragment
import com.kunminx.architecture.bridge.callback.UnPeekLiveData
import kotlin.math.abs

/**
 * <pre>
 *     @author : 18000
 *     time   : 2019/12/24
 *     desc   :
 * </pre>
 */
class DrawerCoordinateHelper : DefaultLifecycleObserver, View.OnTouchListener {

    private var downX = 0f
    private var downY = 0f

    val openDrawer = UnPeekLiveData<Boolean>()

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { DrawerCoordinateHelper() }
    }

    override fun onCreate(owner: LifecycleOwner) {

        SharedViewModel.tagOfSecondaryPages.add(owner.javaClass.simpleName)

        (owner as BaseFragment).sharedViewModel.enableSwipeDrawer.value =
            SharedViewModel.tagOfSecondaryPages.size == 0
    }

    override fun onDestroy(owner: LifecycleOwner) {

        SharedViewModel.tagOfSecondaryPages.remove(owner.javaClass.simpleName)

        (owner as BaseFragment).sharedViewModel.enableSwipeDrawer.value =
            SharedViewModel.tagOfSecondaryPages.size == 0
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = x
                downY = y
            }
            MotionEvent.ACTION_UP -> {
                val dx = x - downX
                val dy = y - downY
                if (abs(dx) > 8 && abs(dy) > 8) {
                    when (getOrientation(dx, dy)) {
                        'r' -> openDrawer.setValue(true)
                        'l' -> {
                        }
                        't' -> {
                        }
                        'b' -> {
                        }
                    }
                }
            }
        }
        return false
    }

    private fun getOrientation(dx: Float, dy: Float): Char {
        return if (abs(dx) > abs(dy)) {
            if (dx > 0) 'r' else 'l'
        } else {
            if (dy > 0) 'b' else 't'
        }
    }
}