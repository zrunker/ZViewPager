package cc.ibooker.zviewpagerlib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义ViewPager大小随子控件变化而变化（宽/高）
 * 1.自定义ViewPager解决VPager与ListView/ScrollView/其他父控件滑动冲突问题。
 * 2.ViewPager的宽充满屏幕，高根据图片的宽高比自动生成。
 *
 * @author 邹峰立
 */
public class DecoratorViewPager extends ViewPager {
    private ViewGroup parent;
    private boolean isCanScroll = true;
    private float oldX = 0, oldY = 0;

    public DecoratorViewPager(Context context) {
        super(context);
    }

    public DecoratorViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        if (isCanScroll)
            return super.dispatchTouchEvent(ev);
        else
            return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll)
            return super.onInterceptTouchEvent(ev);
        else
            return false;
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

    /**
     * ViewPager的宽充满屏幕，高根据图片的宽高比自动生成。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        int with = 0;
        float scale;// 宽度比（最大子控件的宽度/屏幕的宽度）
        //下面遍历所有child的高度，获得子控件的最大宽和高
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //自己测量宽高
            child.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            int w = child.getMeasuredWidth();
            if (w > with)//采用最大的view的宽度
                with = w;
            if (h > height)//采用最大的view的高度。
                height = h;
        }

        if (with > 0) {
            //获得屏幕的宽度
            int dWidth = getScreenW(getContext());
            //得到屏幕宽和图片宽的比例
            scale = (float) dWidth / (float) with;
            //根据上边的比例来求出ViewPager应该有的高度
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (height * scale), MeasureSpec.EXACTLY);
            //ViewPager的宽为屏幕的宽度
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(dWidth, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 获取屏幕宽度
     */
    private int getScreenW(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

}
