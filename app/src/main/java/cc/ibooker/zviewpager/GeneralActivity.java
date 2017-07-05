package cc.ibooker.zviewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cc.ibooker.zviewpagerlib.GeneralVpLayout;
import cc.ibooker.zviewpagerlib.Holder;
import cc.ibooker.zviewpagerlib.HolderCreator;
import cc.ibooker.zviewpagerlib.OnItemClickListener;

/**
 * 测试General
 * Created by 邹峰立 on 2017/7/5.
 */
public class GeneralActivity extends AppCompatActivity {
    // GeneralVpLayout<T> T泛型
    private GeneralVpLayout<Integer> generalVpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        generalVpLayout = (GeneralVpLayout<Integer>) findViewById(R.id.generalVpLayout);

        List<Integer> datas = new ArrayList<>();
        datas.add(R.drawable.b);
        datas.add(R.drawable.d);

        // 初始化generalVpLayout
        generalVpLayout.init(new HolderCreator<ImageViewHolder>() {
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
                .setPageIndicatorAlign(GeneralVpLayout.PageIndicatorAlign.CENTER_HORIZONTAL)
                // 设置指示器是否可见
//                .setPointViewVisible(true)
                // 设置ViewPager是否可以滚动
//                .setScrollble(true)
                // 点击事件监听
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        Toast.makeText(GeneralActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                    }
                })
                // 开启轮播
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        generalVpLayout.stop();
    }

    // 自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
    private class ImageViewHolder implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            // 创建数据
            imageView = new ImageView(GeneralActivity.this);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            // 加载数据
            imageView.setImageResource(data);
        }
    }
}

