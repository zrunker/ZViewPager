package cc.ibooker.zviewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cc.ibooker.zviewpagerlib.ChirdHeightAutoLayout;
import cc.ibooker.zviewpagerlib.Holder;
import cc.ibooker.zviewpagerlib.HolderCreator;
import cc.ibooker.zviewpagerlib.OnItemClickListener;

/**
 * 测试 ChirdMaxHeightViewPager
 * Created by 邹峰立 on 2017/7/5.
 */
public class ChirdHeightAutoActivity extends AppCompatActivity {
    // ChirdMaxHeightLayout<T> T泛型
    private ChirdHeightAutoLayout<Integer> chirdMaxHeightLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chirdheightauto);

        chirdMaxHeightLayout = findViewById(R.id.chirdmaxheightLayout);

        List<Integer> datas = new ArrayList<>();
        datas.add(R.drawable.b);
        datas.add(R.drawable.d);

        // 初始化chirdMaxHeightLayout
        chirdMaxHeightLayout.init(new HolderCreator<ImageViewHolder>() {
            @Override
            public ImageViewHolder createHolder() {
                return new ImageViewHolder();
            }
        }, datas)
                // 设置指示器，第一个代表选中，第二个代表未选中
                .setPageIndicator(R.mipmap.icon_focusdot, R.mipmap.icon_defaultdot)
                // 设置轮播停顿时间
                .setDuration(3000)
                // 指示器位置，居左、居中、居右
                .setPageIndicatorAlign(ChirdHeightAutoLayout.PageIndicatorAlign.CENTER_HORIZONTAL)
                // 设置指示器是否可见
//                .setPointViewVisible(true)
                // 设置ViewPager是否可以滚动
//                .setScrollble(true)
                // 点击事件监听
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        Toast.makeText(ChirdHeightAutoActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                    }
                })
                // 开启轮播
                .start();
        // ViewPager状态改变监听
        chirdMaxHeightLayout.setOnViewPagerChangeListener(new ChirdHeightAutoLayout.OnViewPagerChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chirdMaxHeightLayout.stop();
    }

    // 自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
    private class ImageViewHolder implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            // 创建数据
            imageView = new ImageView(ChirdHeightAutoActivity.this);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            // 加载数据
            imageView.setImageResource(data);
        }
    }
}
