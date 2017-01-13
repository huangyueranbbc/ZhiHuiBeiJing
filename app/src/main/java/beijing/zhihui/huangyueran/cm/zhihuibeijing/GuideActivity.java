package beijing.zhihui.huangyueran.cm.zhihuibeijing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl.DensityUtils;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl.PrefUtils;

/**
 * 新手页面
 */
public class GuideActivity extends Activity {
    private static final String TAG = "GuideActivity";

    private ViewPager mViewPager;
    private LinearLayout llContainer;
    private int[] mImageIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3}; //图片id数组
    private List<ImageView> mImageViewList; //imageView集合
    private ImageView ivRedPoint;
    private Button btnStart; //开始按钮
    //小红点移动距离
    private int mPointDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);

        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
        btnStart = (Button) findViewById(R.id.btn_start);

        initData();
        mViewPager.setAdapter(new GuideAdapter());

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当页面滑动过程中的回调
                // 更新小红点距离
                int leftMargin = (int) (mPointDis * positionOffset) + position * mPointDis; //计算小红点当前的左边距
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                layoutParams.leftMargin = leftMargin;   // 修改左边距

                ivRedPoint.setLayoutParams(layoutParams);   //重新设置布局参数
            }

            @Override
            public void onPageSelected(int position) {
                //某个页面被选中
                if (position == mImageViewList.size() - 1) {
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //页面状态发生变化
            }
        });

        //监听layout方法结束事件
        // 视图树
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 移除监听 避免重复调用
                ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // layout方法执行结束的回调
                // 计算两个圆点的距离
                // 移动距离=第二圆点left值-第一个圆点left值
                mPointDis = llContainer.getChildAt(2).getLeft() - llContainer.getChildAt(1).getLeft();
                Log.i(TAG, "onGlobalLayout: " + mPointDis);
            }
        });

        //给按钮设置点击监听事件
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp
                PrefUtils.setBoolean(getApplicationContext(), "is_first_enter", false);

                //跳到主页面
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    // 初始化数据
    private void initData() {
        mImageViewList = new ArrayList<ImageView>();

        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]); //通过设置背景，可以让宽高填充布局
            mImageViewList.add(view);

            //初始化小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_gray);

            // 布局参数，宽高包裹内容
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                // 设置左边距
                params.leftMargin = DensityUtils.dip2px(10, this); // 尺寸适配
            }
            point.setLayoutParams(params); //设置布局参数

            llContainer.addView(point); //给容器添加圆点


        }
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 初始化item布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        //销毁item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
