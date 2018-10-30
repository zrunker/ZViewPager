package cc.ibooker.zviewpager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cc.ibooker.zviewpagerlib.DecoratorLayout;
import cc.ibooker.zviewpagerlib.Holder;
import cc.ibooker.zviewpagerlib.HolderCreator;
import cc.ibooker.zviewpagerlib.OnItemClickListener;

public class MainActivity extends AppCompatActivity {
    // DecoratorLayout<T> T泛型
    private DecoratorLayout<Integer> decoratorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decoratorLayout = findViewById(R.id.decoratorLayout);

        List<Integer> datas = new ArrayList<>();
        datas.add(R.drawable.b);
        datas.add(R.drawable.d);

        decoratorLayout.destory();
        // 初始化decoratorLayout
        decoratorLayout.init(new HolderCreator<ImageViewHolder>() {
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
                .setPageIndicatorAlign(DecoratorLayout.PageIndicatorAlign.CENTER_HORIZONTAL)
                // 设置指示器是否可见
//                .setPointViewVisible(true)
                // 设置是否可以手动滚动
//                .setCanScroll(true)
                // 点击事件监听
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                    }
                })
                // 解决与父控件listView/ScrollView..滑动冲突
//                .setViewPagerParent(parent)
                // 开启轮播
                .start();
        // ViewPager改变监听
        decoratorLayout.setOnViewPagerChangeListener(new DecoratorLayout.OnViewPagerChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        decoratorLayout.stop();
    }

    // 自定义构成
    private class ImageViewHolder implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            // 创建数据
            imageView = new ImageView(MainActivity.this);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            // 加载数据
            imageView.setImageResource(data);
        }
    }

    /**
     * 跳转到ChirdHeightAutoActivity
     */
    public void onChirdMaxHeight(View view) {
        Intent intent = new Intent(this, ChirdHeightAutoActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转GeneralActivity
     */
    public void onGeneral(View view) {
        Intent intent = new Intent(this, GeneralActivity.class);
        startActivity(intent);
    }
}
