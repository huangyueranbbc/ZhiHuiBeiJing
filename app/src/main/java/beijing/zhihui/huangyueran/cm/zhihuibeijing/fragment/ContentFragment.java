package beijing.zhihui.huangyueran.cm.zhihuibeijing.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.MainActivity;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.R;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.BasePager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.GovAffairsPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.HomePager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.NewsCenterPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.SettingPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.SmartServicePager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.view.NoScrollViewPager;

/**
 * Created by huangyueran on 2016/10/29.
 */
public class ContentFragment extends BaseFragment {

    private NoScrollViewPager mViewPager;
    private ArrayList<BasePager> mPagers; //五个标签页的集合
    private RadioGroup rgGroup;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);


        return view;
    }

    @Override
    public void initDate() {
        mPagers = new ArrayList<BasePager>();

        //添加5个标签页
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContextAdapter());

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MobclickAgent.onEvent(getActivity(),"zhihuibeijing");
                MobclickAgent.onEvent(mActivity,"zhihuibeijing");
                Log.i("umeng", "onCheckedChanged=============purchase");

                switch (checkedId) {
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0, false);// 参数2:是否平滑滑动(是否有滑动动画)
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4, false);
                        break;
                    default:
                        break;
                }
            }

        });

        // 监听页面是否选中，加载数据
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager basePager = mPagers.get(position);
                basePager.initData();

                if (position == 0 || position == mPagers.size() - 1) {
                    //首页和设置页禁用侧边栏
                    setSlidingMenuEnable(false);
                } else {
                    // 否则开启侧边栏
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 手动加载第一页数据
        mPagers.get(0).initData();
        // 首页禁用侧边栏
        setSlidingMenuEnable(false);
    }


    /**
     * 开启/禁用侧边栏
     *
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable) {
        // 获取侧边栏对象
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class ContextAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = mPagers.get(position);
            View view = basePager.getmRootView(); //获取当前页面对象的布局

            // basePager.initData(); //初始化数据 viewpager会默认加载下一个页面，为了节省流量和性能，不要在此处调用初始化数据方法

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取新闻中心页面
     */
    public NewsCenterPager getNewsCenterPager() {
        NewsCenterPager newsCenterPager = (NewsCenterPager) mPagers.get(1);
        return newsCenterPager;
    }
}

