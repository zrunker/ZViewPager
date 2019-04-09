package cc.ibooker.zviewpagerlib;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GeneralViewPager父控件
 * Created by 邹峰立 on 2017/7/5.
 */
public class GeneralVpLayout<T> extends FrameLayout {
    private Context mContext;
    private GeneralViewPager generalViewPager;// ViewPager轮播栏
    private GeneralVpAdapter<T> generalVpAdapter;// ViewPager适配器
    private List<T> mDatas = new ArrayList<>();// 数据源
    private int totalCount = 0;// 记录总Item的数量
    private int realCount = 0;// 真正记录数

    private LinearLayout indicatorLayout;// 指示器布局
    private ImageView[] mImageViews;// 保存指示器标签
    private long duration = 5000;// 间隔时间，默认5s
    private boolean isContinue = false;// ViewPager是否跳转
    private int selectedRes;// 选中时候游标图片
    private int defalutRes;// 未选中时候游标图片
    private boolean isIndicatorVisible = true;// 标记指示器是否可见
    private boolean isOpenInfiniteWheel = true;// 是否开启无限轮播

    private AtomicInteger what = new AtomicInteger(0);
    // 定义一个单线程池，最多容纳1个线程
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Thread thread;

    public enum PageIndicatorAlign {
        ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
    }

    public GeneralViewPager getGeneralViewPager() {
        return generalViewPager;
    }

    public GeneralVpLayout(Context context) {
        this(context, null);
    }

    public GeneralVpLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeneralVpLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    // 初始化
    private void init() {
        // 设置父控件布局
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

        // 构造ViewPager
        generalViewPager = new GeneralViewPager(mContext);
        // 设置ViewPager页面切换监听事件
        generalViewPager.addOnPageChangeListener(new ViewPageChangeListener());
        this.addView(generalViewPager);

        // 构造指示器布局
        LayoutParams bottomParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomParams.gravity = Gravity.BOTTOM;
        bottomParams.setMargins(0, 0, 0, 20);

        indicatorLayout = new LinearLayout(mContext);
        indicatorLayout.setOrientation(LinearLayout.HORIZONTAL);
        indicatorLayout.setGravity(Gravity.CENTER);

        this.addView(indicatorLayout, bottomParams);
    }

    /**
     * ViewPager设置Adapter，初始化数据
     *
     * @param holderCreator ViewHolder构造类
     * @param datas         数据源
     */
    public GeneralVpLayout init(HolderCreator holderCreator, List<T> datas) {
        this.mDatas = datas;
        // 设置Adapter
        if (generalVpAdapter == null) {
            generalVpAdapter = new GeneralVpAdapter<>(holderCreator, mDatas, isOpenInfiniteWheel);
            generalViewPager.setAdapter(generalVpAdapter);
        } else {
            generalVpAdapter.reflushData(holderCreator, mDatas, isOpenInfiniteWheel);
        }
        // 初始化数据
        totalCount = generalVpAdapter.getCount();
        realCount = generalVpAdapter.getRealCount();
        // 从中间开始显示
        what.set(totalCount / 2);
        generalViewPager.setCurrentItem(totalCount / 2);
        return this;
    }

    /**
     * 底部指示器资源图片
     *
     * @param selectedRes 选中图标地址
     * @param defalutRes  未选中图标地址
     */
    public GeneralVpLayout setPageIndicator(int selectedRes, int defalutRes) {
        this.selectedRes = selectedRes;
        this.defalutRes = defalutRes;
        if (mDatas != null && mDatas.size() > 0) {
            indicatorLayout.removeAllViews();
            mImageViews = new ImageView[mDatas.size()];
            // 小图标
            for (int k = 0; k < mDatas.size(); k++) {
                ImageView mImageView = new ImageView(mContext);
                LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dotParams.setMargins(10, 0, 10, 0);
                dotParams.gravity = Gravity.CENTER_VERTICAL;
                mImageView.setLayoutParams(dotParams);

                mImageViews[k] = mImageView;
                if (k == 0) {// 选中
                    mImageViews[k].setBackgroundResource(selectedRes);
                } else {// 未选中
                    mImageViews[k].setBackgroundResource(defalutRes);
                }
                indicatorLayout.addView(mImageViews[k]);
            }
        }
        return this;
    }

    /**
     * ViewPager开始跳转
     */
    public void start() {
        if (mImageViews == null || selectedRes == 0 || defalutRes == 0) {
            setPointViewVisible(false);
        }
        isContinue = true;
        // 开启线程实现ViewPager的跳转
        if (thread == null)
            thread = new VpThread();
        if (executorService == null || executorService.isShutdown())
            executorService = Executors.newSingleThreadExecutor();
        executorService.execute(thread);
    }

    /**
     * ViewPager停止跳转
     */
    public void stop() {
        isContinue = false;
        if (executorService != null)
            executorService.shutdownNow();
    }

    /**
     * 设置跳转停顿时间，默认5s
     *
     * @param duration 停顿时间
     */
    public GeneralVpLayout setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    /**
     * 指示器的方向
     *
     * @param align 三个方向：居左 ，居中 ，居右
     */
    public GeneralVpLayout setPageIndicatorAlign(PageIndicatorAlign align) {
        if (align == PageIndicatorAlign.ALIGN_PARENT_LEFT) {
            indicatorLayout.setGravity(Gravity.START);
        } else if (align == PageIndicatorAlign.ALIGN_PARENT_RIGHT) {
            indicatorLayout.setGravity(Gravity.END);
        } else if (align == PageIndicatorAlign.CENTER_HORIZONTAL) {
            indicatorLayout.setGravity(Gravity.CENTER);
        }
        return this;
    }

    /**
     * 设置指示器是否可见
     *
     * @param isVisible true 可见  false 不可见 默认true
     */
    public GeneralVpLayout setPointViewVisible(boolean isVisible) {
        if (indicatorLayout != null) {
            isIndicatorVisible = isVisible;
            indicatorLayout.setVisibility(isVisible ? VISIBLE : GONE);
        }
        return this;
    }

    /**
     * 设置是否开启无限轮播
     *
     * @param bool true/false 默认true
     */
    public GeneralVpLayout setOpenInfiniteWheel(boolean bool) {
        if (generalVpAdapter != null) {
            isOpenInfiniteWheel = bool;
            generalVpAdapter.reflushData(isOpenInfiniteWheel);
        }
        return this;
    }

    /**
     * 设置ViewPager是否能够手动滚动
     *
     * @param scrollble true 可以  false 不可以 默认true
     */
    public GeneralVpLayout setScrollble(boolean scrollble) {
        if (generalViewPager != null)
            generalViewPager.setCanScroll(scrollble);
        return this;
    }

    /**
     * 解决滑动冲突 ListView ScrollView SwipeRefreshLayout
     *
     * @param parent 父控件
     */
    public GeneralVpLayout setViewPagerParent(ViewGroup parent) {
        if (generalViewPager != null)
            generalViewPager.setViewPagerParent(parent);
        return this;
    }

    // 销毁
    public GeneralVpLayout destory() {
        stop();
        if (vPagerHandler != null) {
            vPagerHandler.removeCallbacksAndMessages(null);
            vPagerHandler = null;
        }
        return this;
    }

    // 判断线程是否继续
    public boolean isContinue() {
        return isContinue && !executorService.isTerminated() && !executorService.isShutdown();
    }

    /**
     * 设置ViewPager点击事件
     *
     * @param onItemClickListener 点击事件
     */
    public GeneralVpLayout setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (generalVpAdapter != null)
            generalVpAdapter.setOnItemClickListener(onItemClickListener);
        return this;
    }

    /**
     * 定义一个线程-处理ViewPager
     */
    private class VpThread extends Thread {
        @Override
        public void run() {
            while (isContinue) {
                synchronized (VpThread.class) {
                    if (vPagerHandler != null) {
                        vPagerHandler.sendEmptyMessage(what.get());
                        whatOption();
                    }
                }
            }
        }
    }

    /**
     * 设置定时器实现顶部viewPager的切换duration秒
     */
    private void whatOption() {
        if (mDatas != null && mDatas.size() > 0) {
            what.incrementAndGet();
            if (what.get() > totalCount - 1) {
                what.getAndAdd(-totalCount);
            }
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过handler改变顶部viewPager的状态
     */
    IVPagerHandler vPagerHandler = new IVPagerHandler(this);

    private static class IVPagerHandler extends Handler {

        private final WeakReference<GeneralVpLayout> mDecoratorLayout;

        IVPagerHandler(GeneralVpLayout layout) {
            mDecoratorLayout = new WeakReference<>(layout);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GeneralVpLayout current = mDecoratorLayout.get();
            try {
                // 实现ViewPager跳转
                int index = msg.what;
                int position;
                if (index > 0 && index < current.totalCount) {
                    position = index;
                } else {
                    position = current.totalCount / 2;
                }
                current.generalViewPager.setCurrentItem(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 私有类实现顶部viewPager的页面切换
     */
    private class ViewPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            what.getAndSet(position);
            // 修改指示器
            int realPosition = position % realCount;
            if (isIndicatorVisible && mImageViews != null && realCount > 0) {
                for (int i = 0; i < mImageViews.length; i++) {
                    mImageViews[realPosition].setBackgroundResource(selectedRes);
                    if (realPosition != i) {
                        mImageViews[i].setBackgroundResource(defalutRes);
                    }
                }
            }
            // 设置ViewPager状态改变事件监听
            if (onViewPagerChangeListener != null)
                onViewPagerChangeListener.onPageSelected(realPosition);
        }
    }

    // 定义ViewPager改变事件监听
    public interface OnViewPagerChangeListener {
        void onPageSelected(int position);
    }

    private OnViewPagerChangeListener onViewPagerChangeListener;

    public void setOnViewPagerChangeListener(OnViewPagerChangeListener onViewPagerChangeListener) {
        this.onViewPagerChangeListener = onViewPagerChangeListener;
    }

}
