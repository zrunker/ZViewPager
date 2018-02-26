# ZViewPager
自定义轮播（一）DecoratorLayout，轮播器的大小随子控件的大小而变化。自定义轮播（二）ChirdHeightAutoLayout，轮播器宽度不变，轮播器的高度随子控件的高度而变化。自定义轮播（三）GeneralLayout，轮播器的自动轮播。使用工具Android Studio


引入Android Studio：
在build.gradle文件中添加以下代码：
```
allprojects {
	repositories {
		maven { url 'https://www.jitpack.io' }
	}
}

dependencies {
	compile 'com.github.zrunker:ZViewPager:v1.0.4'
}
```
自定义轮播（一）DecoratorLayout，轮播器的大小随子控件的大小而变化。
在布局文件中引入DecoratorLayout。
```
<cc.ibooker.zviewpagerlib.DecoratorLayout
        android:id="@+id/decoratorLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```
代码使用：（案例）
```
public class MainActivity extends AppCompatActivity {
    // DecoratorLayout<T> T泛型
    private DecoratorLayout<Integer> decoratorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        decoratorLayout = (DecoratorLayout<Integer>) findViewById(R.id.decoratorLayout);

        List<Integer> datas = new ArrayList<>();
        datas.add(R.drawable.b);
        datas.add(R.drawable.d);

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
                //  .setPointViewVisible(true)
                // 点击事件监听
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                    }
                })
                // 解决与父控件listView/ScrollView..滑动冲突
                // .setViewPagerParent(parent)
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

   // 自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
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
}
```
自定义轮播（二）ChirdHeightAutoLayout，轮播器宽度不变，轮播器的高度随子控件的高度而变化。
1、布局XML文件引入：
```
<cc.ibooker.zviewpagerlib.ChirdHeightAutoLayout
        android:id="@+idirdmaxheightLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```
2、Java文件操作：
```
public class ChirdHeightAutoActivity extends AppCompatActivity {
    // ChirdMaxHeightLayout<T> T泛型
    private ChirdHeightAutoLayout<Integer> chirdMaxHeightLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chirdheightauto);

        chirdMaxHeightLayout = (ChirdHeightAutoLayout<Integer>) findViewById(R.id.chirdmaxheightLayout);

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
```
自定义轮播（三）GeneralLayout，轮播器的自动轮播。
1、XML布局文件引入：
```
<cc.ibooker.zviewpagerlib.GeneralVpLayout
        android:id="@+id/generalVpLayout"
        android:layout_width="match_parent"
        android:layout_height="200dp" />
```
2、Java文件操作：
```
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
        // ViewPager状态改变监听
        generalVpLayout.setOnViewPagerChangeListener(new GeneralVpLayout.OnViewPagerChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }
        });
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
```
