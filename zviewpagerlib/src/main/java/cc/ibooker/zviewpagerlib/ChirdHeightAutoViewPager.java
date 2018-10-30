package cc.ibooker.zviewpagerlib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 子View宽度占满高度自动viewPager
 * <p>
 * Created by 邹峰立 on 2017/7/1.
 */
public class ChirdHeightAutoViewPager extends ViewPager {
    private ViewGroup parent;
    private boolean isCanScroll = true;
    private float oldX = 0, oldY = 0;

    public ChirdHeightAutoViewPager(Context context) {
        super(context);
    }

    public ChirdHeightAutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        // 下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            // 采用最大的view的高度
            if (h > height) {
                height = h;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // 父控件赋值
    public void setViewPagerParent(ViewGroup parent) {
        this.parent = parent;
    }

    public boolean isCanScroll() {
        return isCanScroll;
    }

    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return isCanScroll && super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (parent != null) {
            if (isCanScroll) {
                //请求不被拦截触摸事件
                parent.requestDisallowInterceptTouchEvent(true);
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldX = ev.getX();
                        oldY = ev.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float newX = ev.getX();
                        float newY = ev.getX();

                        float distanceX = newX - oldX;
                        float distanceY = newY - oldY;
                        if (Math.abs(distanceX) * 3 < Math.abs(distanceY))
                            //请求被拦截触摸事件
                            parent.requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_OUTSIDE:
                        oldX = 0;
                        oldY = 0;
                        break;
                }
                return super.onTouchEvent(ev);
            } else {
                return false;
            }
        } else {
            return super.onTouchEvent(ev);
        }
    }

}
