# ZViewPager
自定义轮播（一）DecoratorLayout，轮播器的大小随子控件的大小而变化。自定义轮播（二）ChirdHeightAutoLayout，轮播器宽度不变，轮播器的高度随子控件的高度而变化。自定义轮播（三）GeneralLayout，轮播器的自动轮播。使用工具Android Studio

>作者：邹峰立，微博：zrunker，邮箱：zrunker@yahoo.com，微信公众号：书客创作，个人平台：[www.ibooker.cc](http://www.ibooker.cc)。

>本文选自[书客创作](http://www.ibooker.cc)平台第137篇文章。[阅读原文](http://www.ibooker.cc/article/137/detail) 。

![书客创作](http://upload-images.jianshu.io/upload_images/3480018-5b74a7749da3528f..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

轮播图，几乎所有的APP都会有的一个功能，多用于广告栏。在每一个APP当中可能最终呈现的效果不同，甚至在一些APP当中轮播图做的非常炫酷。所以轮播图功能对一个APP来说还是相当重要的。本篇文章提供三种不同处理方式的轮播图，主要针对轮播图的大小显示问题作出不同的处理方式，本框架已开源。

在开始讲解之前，先看一下，三种处理方式所实现的最终效果：

![自定义轮播图](http://upload-images.jianshu.io/upload_images/3480018-31824c22d1f33419..gif?imageMogr2/auto-orient/strip)

三种处理方式分别是：1、DecoratorLayout，轮播器的大小随子控件的大小而变化。2、ChirdHeightAutoLayout，轮播器宽度不变，轮播器的高度随子控件的高度而变化。3、GeneralLayout，设置宽和高，轮播器的自动轮播。

那么该如何集成该框架呢？

#### 首先引入资源

这里提供两种方式集成。

1、gradle引入：
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
2、maven引入：
```
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.zrunker</groupId>
	<artifactId>ZViewPager</artifactId>
	<version>v1.0.4</version>
</dependency>
```
#### 使用

**一、DecoratorLayout，轮播器的大小随子控件的大小而变化。**

1、在布局文件中引入DecoratorLayout。
```
<cc.ibooker.zviewpagerlib.DecoratorLayout
        android:id="@+id/decoratorLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```
2、代码使用：（案例）
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
**二、ChirdHeightAutoLayout，轮播器宽度不变，轮播器的高度随子控件的高度而变化。**

1、在布局文件中引入ChirdHeightAutoLayout。
```
<cc.ibooker.zviewpagerlib.ChirdHeightAutoLayout
        android:id="@+idirdmaxheightLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```
2、代码使用：（案例）
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
**三、GeneralLayout，设置宽和高，轮播器的自动轮播。**

1、在布局文件中引入GeneralVpLayout。
```
<cc.ibooker.zviewpagerlib.GeneralVpLayout
        android:id="@+id/generalVpLayout"
        android:layout_width="match_parent"
        android:layout_height="200dp" />
```
2、代码使用：（案例）
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

[Github地址](https://github.com/zrunker/ZViewPager)
[阅读原文](http://www.ibooker.cc/article/137/detail) 

----------
![微信公众号：书客创作](http://upload-images.jianshu.io/upload_images/3480018-f36cbc05f8549cf1..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
