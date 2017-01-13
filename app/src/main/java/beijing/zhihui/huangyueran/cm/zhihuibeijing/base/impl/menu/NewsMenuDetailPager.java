package beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.MainActivity;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.R;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.BaseMenuDetailPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.domain.NewsMenu;

/**
 * Created by huangyueran on 2016/11/13.
 * 菜单详情页
 * 1.从sample程序中引入ViewPageIndicator库
 * 2.解决v4冲突
 * 3.从sample程序中引用布局文件
 * 4.从sample程序中引入相关代码(指示器和viewpager绑定，重写getPageTitle来指定标题)
 * 5.在清单文件中指定activity布局样式
 * 6.背景修改为白色
 * 7.字体颜色修改为红/黑切换
 * 8.修改样式 在库文件中修改 背景样式和文字样式
 * 9.在库文件中dispatchTouchEvent 通知父控件和祖宗控件不要拦截事件
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {
    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager mViewPager;

    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;

    private ArrayList<NewsMenu.NewsTabData> mTabData;// 页签网络数据
    private ArrayList<TabDetailPager> mPagers;// 页签页面集合

    public NewsMenuDetailPager(Activity Activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(Activity);
        this.mTabData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail,
                null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        // 初始化页签
        mPagers = new ArrayList<TabDetailPager>();
        for (int i = 0; i < mTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity,
                    mTabData.get(i));
            mPagers.add(pager);
        }

        mViewPager.setAdapter(new NewsMenuDetailAdapter());

        // 注意:必须在viewpager设置完数据之后再绑定
        mIndicator.setViewPager(mViewPager); // 将viewpager和指示器Indicator绑定

        // 页面滑动监听 此处必须给指示器设置页面监听，不能给viewpager设置
        //mViewPager.setOnPageChangeListener(this);
        mIndicator.setOnPageChangeListener(this);

    }

    class NewsMenuDetailAdapter extends PagerAdapter {

        // 指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.NewsTabData newsTabData = mTabData.get(position);
            return newsTabData.getTitle();
        }

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
            TabDetailPager pager = mPagers.get(position);

            View view = pager.getmRootView();
            container.addView(view);

            pager.initData();

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


    //=========== OnPageChangeListener 监听事件处理
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 当前页面position
        if (position == 0) {
            // 启动侧边栏
            setSlidingMenuEnable(true);
        } else {
            // 禁用侧边栏
            setSlidingMenuEnable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    // 通过xutils绑定button的onclick事件
    @OnClick(R.id.btn_next)
    public void nextPage(View view) {
        // 跳到下一个页面
        int currentItem = mViewPager.getCurrentItem();
        currentItem++;
        mViewPager.setCurrentItem(currentItem);
    }
}
