package me.parade.lib_common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @author : parade
 * date : 2021/8/14
 * description : 解决SwipeRefreshLayout与横向列表滑动冲突，增加60的容差
 */
public class VerticalSwipeRefreshLayout extends SwipeRefreshLayout {

    private final int mTouchSlop;
    // 上一次触摸时的X坐标
    private float mPrevX;
    private float mPrevY;

    public VerticalSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 触发移动事件的最短距离，如果小于这个距离就不触发移动控件
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = event.getX();
                mPrevY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                final float eventY = event.getY();
                float xDiff = Math.abs(eventX - mPrevX);
                float yDiff = Math.abs(eventY - mPrevY);
                // 增加60的容差，让下拉刷新在竖直滑动时就可以触发
                if (xDiff > mTouchSlop + 60) {
                    return false;//此时认为是横向滑动
                }
//                if (yDiff < mTouchSlop + 60){
//                    return false;
//                }
        }

        return super.onInterceptTouchEvent(event);
    }
}
