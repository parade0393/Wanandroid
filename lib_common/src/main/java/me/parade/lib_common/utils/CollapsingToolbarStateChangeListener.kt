package me.parade.lib_common.utils

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class CollapsingToolbarStateChangeListener(
    private val onStateChanged: (ToolbarState,verticalOffset: Int) -> Unit
) : AppBarLayout.OnOffsetChangedListener {

    enum class ToolbarState {
        EXPANDED,
        COLLAPSED,
        INTERMEDIATE
    }

    private var currentState = ToolbarState.EXPANDED

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        when {
            verticalOffset == 0 -> {
                if (currentState != ToolbarState.EXPANDED) {
                    onStateChanged(ToolbarState.EXPANDED,verticalOffset)
                }
                currentState = ToolbarState.EXPANDED
            }
            abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                if (currentState != ToolbarState.COLLAPSED) {
                    onStateChanged(ToolbarState.COLLAPSED,verticalOffset)
                }
                currentState = ToolbarState.COLLAPSED
            }
            else -> {
                if (currentState != ToolbarState.INTERMEDIATE) {
                    onStateChanged(ToolbarState.INTERMEDIATE,verticalOffset)
                }
                currentState = ToolbarState.INTERMEDIATE
            }
        }
    }
}