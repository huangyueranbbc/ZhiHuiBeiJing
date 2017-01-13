package beijing.zhihui.huangyueran.cm.zhihuibeijing.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by huangyueran on 2016/11/3.
 * 菜单详情页父类
 */
public abstract class BaseMenuDetailPager {

    public Activity mActivity;
    private View mRootView; // 菜单详情页根布局

    public BaseMenuDetailPager(Activity Activity) {
        this.mActivity = Activity;
        mRootView = initView();
    }

    // 必须子类实现
    public abstract View initView();

    // 初始化数据
    public void initData() {

    }

    public View getmRootView() {
        return mRootView;
    }

    public void setmRootView(View mRootView) {
        this.mRootView = mRootView;
    }

    public Activity getmActivity() {
        return mActivity;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }
}
