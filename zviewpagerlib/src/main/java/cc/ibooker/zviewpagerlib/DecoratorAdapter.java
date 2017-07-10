package cc.ibooker.zviewpagerlib;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * DecoratorViewPager的Adapter文件
 * Created by 邹峰立 on 2017/7/3.
 */
public class DecoratorAdapter<T> extends PagerAdapter {
    private final int MULTIPLE_COUNT = 200;// 将Count扩大200倍实现无限滑动轮播
    private List<T> mDatas;
    private HolderCreator holderCreator;

    public DecoratorAdapter(HolderCreator holderCreator, List<T> list) {
        this.holderCreator = holderCreator;
        this.mDatas = list;
    }

    // 刷新数据
    public void reflashData(HolderCreator holderCreator, List<T> list) {
        this.holderCreator = holderCreator;
        this.mDatas = list;
        this.notifyDataSetChanged();
    }

    // 计算真正的Position，无效循环
    public int toRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0)
            return 0;
        return position % realCount;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        View view = (View) object;
//        container.removeView(view);
    }

    @Override
    public int getCount() {
        return getRealCount() * MULTIPLE_COUNT;
    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int realPosition = toRealPosition(position);
        View view = getView(realPosition, null, container);
        if (view != null) {// 控件点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ZViewPagerClickUtil.isFastClick())
                        return;
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClickListener(realPosition);
                }
            });
        }
        // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        if (view != null) {
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // 获取View
    private View getView(int position, View view, ViewGroup container) {
        Holder<T> holder;
        if (view == null) {
            holder = (Holder<T>) holderCreator.createHolder();
            view = holder.createView(container.getContext());
            view.setTag(holder);
        } else {
            holder = (Holder<T>) view.getTag();
        }
        if (mDatas != null && !mDatas.isEmpty())
            holder.UpdateUI(container.getContext(), position, mDatas.get(position));
        return view;
    }

    // 对外接口实现点击事件
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
