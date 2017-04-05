# Imagecache-bannerview
## 安卓图片缓存和bannerview结合
email: 1659290725@qq.com<br>
author: chenpengfei

## 一、引导页使用
``` java
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <cpf.view.banner.RecycleView
        android:id="@+id/recycle"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
        
    //如不使用图片替换可不用
    <ImageView
        android:id="@+id/dot"
        android:layout_width="100dp"
        android:src="@mipmap/page1_dot"
        android:scaleType="fitCenter"
        android:layout_centerHorizontal="true"
        android:layout_alignParentBottom="true"
        android:layout_marginBottom="30dp"
        android:layout_height="15dp" />

</RelativeLayout>
        
        //是否循环滚动
        recycle.setRepate(false);
        //是否显示指示器
        recycle.setDotShow(false);
        //加载布局（也可以加载图片）
        ArrayList<View> list = new ArrayList<>();
        list.add(getLayoutInflater().inflate(R.layout.guide_page1, null));
        list.add(getLayoutInflater().inflate(R.layout.guide_page2, null));
        list.add(getLayoutInflater().inflate(R.layout.guide_page3, null));
        list.add(page4 = getLayoutInflater().inflate(R.layout.guide_page4, null));
        recycle.setViews(list);
        //结束引导页按钮
        page4.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        //如果不使用默认指示器样式可以自定义
        recycle.getViewpager().setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        dot.setImageResource(R.mipmap.page1_dot);
                        break;
                    case 1:
                        dot.setImageResource(R.mipmap.page2_dot);
                        break;
                    case 2:
                        dot.setImageResource(R.mipmap.page3_dot);
                        break;
                    case 3:
                        dot.setImageResource(R.mipmap.page4_dot);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
 ```
 
 ## 二、轮播图使用
 ``` java
         <cpf.view.banner.RecycleView
                    android:id="@+id/banner"
                    android:layout_width="match_parent"
                    android:layout_height="160dp" />
 
        //设置指示器大小
        banner.setDotRadius((int) (getResources().getDisplayMetrics().scaledDensity * 3));
        //设置指示器样式（可参考源码自己修改，默认有三种）
        banner.setDotStyle(1);
        //加载本地图片（可加载网络图片：List中的实体改为String；list中添加图片地址；banner.setUrlList()；加载网络图片时自带缓存）
        ArrayList<Bitmap> list = new ArrayList<>();
        list.add(BitmapFactory.decodeResource(getResources(), R.mipmap.banner01));
        list.add(BitmapFactory.decodeResource(getResources(), R.mipmap.banner02));
        list.add(BitmapFactory.decodeResource(getResources(), R.mipmap.banner03));
        list.add(BitmapFactory.decodeResource(getResources(), R.mipmap.banner04));
        banner.setBitmapList(list);
        //自动滚动时间间隔
        banner.setScrollInterval(3);
        //为每张图片设置点击事件
        banner.setOnItemClickListenr(new RecycleView.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
  ```
  
## 三、图片缓存
   ``` java
   //使用方法一
   <cpf.util.scache.SImageView
        android:id="@+id/img"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:scaleType="fitXY" />
        
   imageView.setImageViewUrl(url);
        
   //使用方法二
   //直接使用imageview
   ImageCache.getInstance(getContext()).setImageUrl(imageView, url);
   
  ```
## 四、滑动动画
   ``` java
    //提供三种额外的动画
        recycleView.setRecycleViewAnim(RecycleViewAnim.DepthPageTransformer);
        recycleView.setRecycleViewAnim(RecycleViewAnim.ZoomOutPageTransformer);
        recycleView.setRecycleViewAnim(RecycleViewAnim.RotateDownPageTransformer);
   ```
