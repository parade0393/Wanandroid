package me.parade.lib_common.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.ScrollView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import me.parade.lib_common.R
import me.parade.lib_common.ext.logd
import me.parade.lib_common.ext.px
import kotlin.math.abs

class CustomCoordinatorRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
):CoordinatorLayout(context, attrs, defStyleAttr) {
    private val headerHeight = 80
    private var  refreshHeader: RefreshHeaderView = RefreshHeaderView(context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, headerHeight.px).apply {
            gravity = Gravity.TOP
        }
        translationY = -headerHeight.px.toFloat()
        elevation = Float.MAX_VALUE
    }
    private var refreshing = false
    private var totalDragDistance = 0f
    private var refreshListener: (() -> Unit)? = null
    private var initialY = 0f
    private var currentY = 0f


    // 上一次触摸时的X坐标
    private var mPrevX = 0f
    private var mTouchSlop:Int ? = null
    init {
        addView(refreshHeader, 0)  // Add refreshHeader as the first child
        totalDragDistance = headerHeight.px.toFloat()
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (refreshing) return false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mPrevX = ev.x
                initialY = ev.y
                currentY = initialY
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaY = ev.y - initialY
                val xDiff = abs(ev.x-mPrevX)

                if(deltaY > 0 &&  !canChildScrollUp()){
                    return true//拦截进行刷新
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (refreshing) return false

        when (ev.action) {
            MotionEvent.ACTION_MOVE -> {
                currentY = ev.y
                val deltaY = currentY - initialY
                if (deltaY > 0 && !canChildScrollUp()) {
                    moveSpinner(deltaY)
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (refreshHeader.translationY >= 0) {
                    setRefreshing(true)
                    refreshListener?.invoke()
                } else {
                    moveSpinner(0f)
                }
                return true
            }
        }

        return super.onTouchEvent(ev)
    }

    private fun canChildScrollUp(): Boolean {
        val appBarLayout = findViewById<AppBarLayout>(R.id.appBar)
        appBarLayout?.let {
            if (it.height != it.bottom){
                return true
            }
        }
        val child = getChildAt(1) ?: return false
        return when (child) {
            is NestedScrollView -> child.canScrollVertically(-1)
            is RecyclerView -> child.canScrollVertically(-1)
            is ScrollView -> child.scrollY > 0
            is AbsListView -> canAbsListViewScrollUp(child)
            is WebView -> child.scrollY > 0
            is ViewPager -> canViewPagerScrollUp(child)
            is ViewPager2 -> canViewPager2ScrollUp(child)
            else -> child.canScrollVertically(-1)
        }
    }

    private fun moveSpinner(overscrollTop: Float) {
        val newTranslationY = (-totalDragDistance + overscrollTop).coerceAtMost(0f)
        refreshHeader.translationY = newTranslationY
        refreshHeader.updateProgress(1+overscrollTop / totalDragDistance)
//        refreshHeader.visibility = if (newTranslationY < -totalDragDistance) View.INVISIBLE else View.VISIBLE
        "moveSpinner-$newTranslationY".logd()
    }

    fun setRefreshing(refreshing: Boolean) {
        if (this.refreshing != refreshing) {
            this.refreshing = refreshing
            if (refreshing) {
                refreshHeader.translationY = 0f
                refreshHeader.startAnimation()
            } else {
                refreshHeader.stopAnimation()
                moveSpinner(0f)
            }
        }
    }

    private fun canAbsListViewScrollUp(listView: AbsListView): Boolean {
        return if (listView.childCount > 0) {
            (listView.firstVisiblePosition > 0) || (listView.getChildAt(0).top < listView.paddingTop)
        } else {
            false
        }
    }

    private fun canViewPagerScrollUp(viewPager: ViewPager): Boolean {
        val currentView = viewPager.getChildAt(viewPager.currentItem)
        return currentView != null && currentView.canScrollVertically(-1)
    }

    private fun canViewPager2ScrollUp(viewPager: ViewPager2): Boolean {
        val currentView = viewPager.getChildAt(0)
        return if (currentView is RecyclerView) {
            currentView.canScrollVertically(-1)
        } else {
            false
        }
    }

    fun setOnRefreshListener(listener: () -> Unit) {
        refreshListener = listener
    }
}