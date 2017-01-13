package beijing.zhihui.huangyueran.cm.zhihuibeijing.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.MainActivity;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.R;

/**
 * Created by huangyueran on 2016/10/29.
 * 五个标签页的基类
 */
public class BasePager {

    protected Activity mActivity;

    protected TextView tvTitle;
    protected ImageButton btnMenu;
    protected FrameLayout flContent; //空的帧布局对象，动态添加设置

    protected View mRootView; //当前页面的布局对象

    protected ImageButton btnPhoto; //组图切换按钮

    public BasePager(Activity mActivity) {
        this.mActivity = mActivity;
        mRootView = initView();
    }

    //初始化布局
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        btnPhoto = (ImageButton) view.findViewById(R.id.btn_photo);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(); // 开打或关闭侧边栏
            }
        });

        return view;
    }

    /**
     * 打开或关闭侧边栏
     */
    private void toggle() {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle(); //如果是开，则关。如果是关，则开。
    }

    //初始化数据
    public void initData() {

    }

    public Activity getmActivity() {
        return mActivity;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public ImageButton getBtnMenu() {
        return btnMenu;
    }

    public void setBtnMenu(ImageButton btnMenu) {
        this.btnMenu = btnMenu;
    }

    public FrameLayout getFlContent() {
        return flContent;
    }

    public void setFlContent(FrameLayout flContent) {
        this.flContent = flContent;
    }

    public View getmRootView() {
        return mRootView;
    }

    public void setmRootView(View mRootView) {
        this.mRootView = mRootView;
    }
}
