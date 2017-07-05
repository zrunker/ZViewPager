package cc.ibooker.zviewpagerlib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 是否可以手动滚动ViewPager
 * Created by 邹峰立 on 2017/7/5.
 */
public class GeneralViewPager extends ViewPager {
    // 标记是否能够滚动
    private boolean scrollble = true;

    public GeneralViewPager(Context context) {
        super(context);
    }

    public GeneralViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !scrollble || super.onTouchEvent(ev);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }

}
