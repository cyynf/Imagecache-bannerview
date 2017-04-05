package cpf.view.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cpf.util.scache.ImageCache;

/**
 * 锟街诧拷图
 *
 * @author cpf chenpengfei
 */
public class RecycleView extends RelativeLayout {

    private DotGroup dotGroup;
    private ViewPager pager;
    private MyViewPagerAdapter adapter;
    private List<Bitmap> imgList;
    private Timer timer;
    private int mSec;
    private OnItemClickListener clickListenr;
    private RecycleViewAnim recycleViewAnim;
    private MyPageTransformer myPageTransformer;
    private int dotRadius;
    private List<View> views;
    private List<String> urls;
    private int pageSize;
    private boolean isRepate = true;
    private int dotStyle;

    public void setDotShow(boolean show) {
        isShow = show;
    }

    private boolean isShow = true;

    /**
     * 默锟斤拷viewpager锟斤拷锟斤拷锟斤拷锟斤拷
     *
     * @author CPF
     */
    public enum RecycleViewAnim {
        DepthPageTransformer, ZoomOutPageTransformer, RotateDownPageTransformer;
    }

    public RecycleView(Context context) {
        super(context);
        init();
    }

    public RecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 锟斤拷始锟斤拷
     */
    @SuppressWarnings("deprecation")
    public void init() {
        /**
         * 锟斤拷锟斤拷锟斤拷色
         */
        setBackgroundColor(Color.LTGRAY);
        /**
         * 锟斤拷始锟斤拷图片锟斤拷锟斤拷
         */
        views = new ArrayList<>();
        /**
         * viewpager锟斤拷锟斤拷锟斤拷锟斤拷始锟斤拷
         */
        adapter = new MyViewPagerAdapter();
        /**
         * viewpager锟斤拷始锟斤拷
         */
        pager = new ViewPager(getContext()) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mSec > 0) {
                            setScrollInterval(mSec);
                        }
                        break;

                    default:
                        break;
                }
                return super.onTouchEvent(event);
            }
        };
        pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (dotGroup != null && dotGroup.size > position % views.size()) {
                    dotGroup.setCurrentItem(position % views.size());
                }
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });
        LayoutParams pagerParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        pager.setLayoutParams(pagerParams);
        pager.setAdapter(adapter);

        //pager.setCurrentItem((Integer.MAX_VALUE >> 2) + 1);
        addView(pager);
        /**
         * 锟斤拷某锟绞硷拷锟�
         */
        dotRadius = getWidth() / 50;
        dotGroup = new DotGroup(getContext());
        LayoutParams dotParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        dotParams.topMargin = getHeight() / 20;
        dotParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        dotParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        dotParams.addRule(Gravity.CENTER);
        dotGroup.setLayoutParams(dotParams);
        addView(dotGroup);
    }

    public ViewPager getViewpager(){
        return pager;
    }

    public void setDotRadius(int dotRadius) {
        this.dotRadius = dotRadius;
    }

    /**
     * 锟斤拷锟斤拷锟街诧拷图锟斤拷源
     *
     * @param imgList
     */
    public void setBitmapList(List<Bitmap> imgList) {
        this.imgList = imgList;
        pageSize = imgList.size();
        adapter.notifyDataSetChanged();
        if (dotGroup != null) {
            dotGroup.setDotNum(RecycleView.this.imgList.size());
        }
    }

    public void setViews(List<View> views) {
        this.views = views;
        pageSize = views.size();
        adapter.notifyDataSetChanged();
        if (dotGroup != null) {
            dotGroup.setDotNum(RecycleView.this.views.size());
        }
    }

    public void setUrlList(List<String> urls) {
        this.urls = urls;
        pageSize = urls.size();
        adapter.notifyDataSetChanged();
        if (dotGroup != null) {
            dotGroup.setDotNum(RecycleView.this.urls.size());
        }
    }

    /**
     * 锟斤拷锟斤拷锟街诧拷图锟叫伙拷时锟斤拷锟斤拷
     *
     * @param sec 锟斤拷位为锟斤拷
     */
    public void setScrollInterval(int sec) {
        if (pageSize<2)
            return;
        mSec = sec;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (sec > 0 && views.size() > 1) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, sec * 1000, sec * 1000);
        }
    }

    public void setOnItemClickListenr(OnItemClickListener clickListenr) {
        this.clickListenr = clickListenr;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    /**
     * 锟斤拷锟斤拷锟街诧拷图锟斤拷锟斤拷
     *
     * @param recycleViewAnim <br>
     *                        RecycleViewAnim.DepthPageTransformer<br>
     *                        RecycleViewAnim.ZoomOutPageTransformer<br>
     *                        RecycleViewAnim.RotateDownPageTransformer<br>
     */
    public void setRecycleViewAnim(RecycleViewAnim recycleViewAnim) {
        this.recycleViewAnim = recycleViewAnim;
        if (myPageTransformer == null) {
            myPageTransformer = new MyPageTransformer();
        }
        setRecycleViewDefineAnim(myPageTransformer);
        // switch (recycleViewAnim) {
        // case DepthPageTransformer:
        // setRecycleViewDefineAnim(new DepthPageTransformer());
        // break;
        // case ZoomOutPageTransformer:
        // setRecycleViewDefineAnim(new ZoomOutPageTransformer());
        // break;
        // case RotateDownPageTransformer:
        // setRecycleViewDefineAnim(new RotateDownPageTransformer());
        // break;
        // default:
        // break;
        // }
    }

    /**
     * 锟斤拷锟斤拷锟街诧拷图锟皆讹拷锟藉动锟斤拷
     *
     * @param pageTransformer
     */
    public void setRecycleViewDefineAnim(PageTransformer pageTransformer) {
        pager.setPageTransformer(true, pageTransformer);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pager.setCurrentItem(1 + pager.getCurrentItem());
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 锟斤拷view锟诫开锟斤拷幕时锟斤拷锟斤拷锟绞憋拷锟�
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private class MyPageTransformer implements PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            switch (recycleViewAnim) {
                case DepthPageTransformer:
                    float MIN_SCALEs = 0.75f;

                    if (position < -1) { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        view.setAlpha(0);

                    } else if (position <= 0) { // [-1,0]
                        // Use the default slide transition when moving to the left
                        // page
                        view.setAlpha(1);
                        view.setTranslationX(0);
                        view.setScaleX(1);
                        view.setScaleY(1);

                    } else if (position <= 1) { // (0,1]
                        // Fade the page out.
                        view.setAlpha(1 - position);

                        // Counteract the default slide transition
                        view.setTranslationX(pageWidth * -position);

                        // Scale the page down (between MIN_SCALE and 1)
                        float scaleFactor = MIN_SCALEs + (1 - MIN_SCALEs)
                                * (1 - Math.abs(position));
                        view.setScaleX(scaleFactor);
                        view.setScaleY(scaleFactor);

                    } else { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        view.setAlpha(0);
                    }
                    break;
                case ZoomOutPageTransformer:
                    float MIN_SCALE = 0.85f;
                    float MIN_ALPHA = 0.5f;

                    if (position < -1) { // [-Infinity,-1)
                        // This page is way off-screen to the
                        // left.
                        view.setAlpha(0);

                    } else if (position <= 1) // a页锟斤拷锟斤拷锟斤拷b页 锟斤拷 a页锟斤拷 0.0 -1 锟斤拷b页锟斤拷1 ~
                    // 0.0
                    { // [-1,1]
                        // Modify the default slide transition to shrink the page as
                        // well
                        float scaleFactor = Math.max(MIN_SCALE,
                                1 - Math.abs(position));
                        float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                        float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                        if (position < 0) {
                            view.setTranslationX(horzMargin - vertMargin / 2);
                        } else {
                            view.setTranslationX(-horzMargin + vertMargin / 2);
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        view.setScaleX(scaleFactor);
                        view.setScaleY(scaleFactor);

                        // Fade the page relative to its size.
                        view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                                / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                    } else { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        view.setAlpha(0);
                    }
                    break;
                case RotateDownPageTransformer:
                    float ROT_MAX = 20.0f;
                    float mRot;

                    if (position < -1) { // [-Infinity,-1)
                        // This page is way off-screen to the
                        // left.
                        view.setRotation(0);

                    } else if (position <= 1) // a页锟斤拷锟斤拷锟斤拷b页 锟斤拷 a页锟斤拷 0.0 ~ -1 锟斤拷b页锟斤拷1
                    // ~ 0.0
                    { // [-1,1]
                        // Modify the default slide transition to shrink the page as
                        // well
                        if (position < 0) {

                            mRot = (ROT_MAX * position);
                            view.setPivotX(pageWidth * 0.5f);
                            view.setPivotY(pageHeight);
                            view.setRotation(mRot);
                        } else {

                            mRot = (ROT_MAX * position);
                            view.setPivotX(pageWidth * 0.5f);
                            view.setPivotY(pageHeight);
                            view.setRotation(mRot);
                        }

                        // Scale the page down (between MIN_SCALE and 1)

                        // Fade the page relative to its size.

                    } else { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        view.setRotation(0);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * viewpager锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
     *
     * @author CPF
     */
    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                        * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    /**
     * viewpager锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷平锟斤拷
     *
     * @author CPF
     */
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private float MIN_SCALE = 0.85f;
        private float MIN_ALPHA = 0.5f;

        @SuppressLint("NewApi")
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) // a页锟斤拷锟斤拷锟斤拷b页 锟斤拷 a页锟斤拷 0.0 -1 锟斤拷b页锟斤拷1 ~ 0.0
            { // [-1,1]
                // Modify the default slide transition to shrink the page as
                // well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                        / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    /**
     * 是否循环播放，默认true
     *
     * @param isRepate
     */
    public void setRepate(boolean isRepate) {
        this.isRepate = isRepate;
    }

    /**
     * viewpager锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
     *
     * @author CPF
     */
    public class RotateDownPageTransformer implements ViewPager.PageTransformer {

        private float ROT_MAX = 20.0f;
        private float mRot;

        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setRotation(0);

            } else if (position <= 1) // a页锟斤拷锟斤拷锟斤拷b页 锟斤拷 a页锟斤拷 0.0 ~ -1 锟斤拷b页锟斤拷1 ~
            // 0.0
            { // [-1,1]
                // Modify the default slide transition to shrink the page as
                // well
                if (position < 0) {

                    mRot = (ROT_MAX * position);
                    view.setPivotX(view.getMeasuredWidth() * 0.5f);
                    view.setPivotY(view.getMeasuredHeight());
                    view.setRotation(mRot);
                } else {

                    mRot = (ROT_MAX * position);
                    view.setPivotX(view.getMeasuredWidth() * 0.5f);
                    view.setPivotY(view.getMeasuredHeight());
                    view.setRotation(mRot);
                }

                // Scale the page down (between MIN_SCALE and 1)

                // Fade the page relative to its size.

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setRotation(0);
            }
        }
    }

    /**
     * viewpager锟斤拷锟斤拷锟斤拷
     *
     * @author cpf
     */
    private class MyViewPagerAdapter extends PagerAdapter {

        public MyViewPagerAdapter() {
            init();
        }

        private void init() {
            if (imgList != null && !imgList.isEmpty()) {
                if (!views.isEmpty()) {
                    views.clear();
                }
                for (int i = 0; i < imgList.size(); i++) {
                    ImageView imgView = new ImageView(getContext());
                    LayoutParams params = new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT);
                    imgView.setLayoutParams(params);
                    imgView.setScaleType(ScaleType.CENTER_CROP);
                    imgView.setImageBitmap(imgList.get(i));
                    imgView.setTag(i);
                    imgView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (clickListenr != null)
                                clickListenr.onClick(v, (int) v.getTag());
                        }
                    });
                    views.add(imgView);
                    if (urls != null && !urls.isEmpty())
                        ImageCache.getInstance(getContext()).setImageUrl(
                                imgView, urls.get(i));
                }
            }
        }

        @Override
        public int getCount() {
            return pageSize < 2 ? pageSize : isRepate ? Integer.MAX_VALUE : pageSize;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0.equals(arg1);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//			if (views.isEmpty()) {
//				return;
//			}
//			container.removeView(views.get(position % views.size()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (views.isEmpty()) {
                return Color.WHITE;
            }
            int index = position % views.size();
            View view = views.get(index);
            container.removeView(view);
            container.addView(view);
            return view;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void notifyDataSetChanged() {
            init();
            super.notifyDataSetChanged();
        }

    }


    /**
     * 锟斤拷锟斤拷viewpager锟斤拷指示锟斤拷
     *
     * @author cpf
     */
    public class DotGroup extends View {

        private int size = 2;
        private Paint paint = new Paint();
        private int mH;
        private int position;

        public DotGroup(Context context) {
            super(context);
        }

        private void init() {
            if (dotRadius == 0) {
                dotRadius = 10;
            }
            mH = size * (dotRadius * 4) - dotRadius * 2;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            init();
            for (int i = 0; i < size; i++) {
                paint.reset();
                paint.setAntiAlias(true);
                switch (dotStyle) {
                    case 0:
                        if (position == i) {
                            paint.setColor(Color.WHITE);
                        } else {
                            paint.setColor(Color.parseColor("#80ffffff"));
                        }
                        canvas.drawCircle(dotRadius * 4 * i + (getWidth() - mH) / 2 + dotRadius,
                                getHeight() - getHeight() / 10, dotRadius, paint);
                        break;
                    case 1:
                        if (position == i) {
                            paint.setColor(Color.parseColor("#ff3358"));
                        } else {
                            paint.setColor(Color.WHITE);
//                            paint.setStrokeWidth(3);
//                            paint.setStyle(Paint.Style.STROKE);
                        }
                        canvas.drawCircle(dotRadius * 4 * i + (getWidth() - mH) / 2 + dotRadius,
                                getHeight() - getHeight() / 10, dotRadius, paint);
                        break;
                    case 2:
                        paint.setStrokeWidth(5);
                        if (position == i) {
                            paint.setColor(Color.WHITE);
                        } else {
                            paint.setColor(Color.parseColor("#80ffffff"));
                        }
                        canvas.drawLine(mH / size * i + (getWidth() - mH) / 2,
                                getHeight() - getHeight() / 20, mH / size * i + (getWidth() - mH) / 2 + dotRadius * 2, getHeight() - getHeight() / 20, paint);
                        break;
                }
            }
        }

        public void setDotNum(int size) {
            if (!isShow) {
                size = 0;
            }

            this.size = size;
            this.position = 0;
            this.invalidate();
        }

        public void setCurrentItem(int position) {
            this.position = position;
            this.invalidate();
        }

    }

    public void setDotStyle(int style) {
        dotStyle = style;
    }

}
