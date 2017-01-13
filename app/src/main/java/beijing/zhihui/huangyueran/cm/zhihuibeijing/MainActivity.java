package beijing.zhihui.huangyueran.cm.zhihuibeijing;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.fragment.ContentFragment;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.fragment.LeftMenuFragment;

/**
 * 主页面
 */
public class MainActivity extends SlidingFragmentActivity {
    private static final String TAG = "ActivityLOG";

    private String TAG_LEFTMENU = "TAG_LEFTMENU";
    private String TAG_CONTENT = "TAG_CONTENT";


    public static int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu); // 设置侧滑布局文件

        // 初始化侧边栏
        initSlideMenu(this);

        initFragment();


        //加入权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<String>();
            permissions.add(Manifest.permission.INTERNET);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
            permissions.add(Manifest.permission.READ_PHONE_STATE);

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            }
        }

    }

    private void initSlideMenu(Activity activity) {
        // 获取屏幕的宽度
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        SlidingMenu menu = getSlidingMenu();
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //        menu.setShadowWidthRes(R.dimen.shadow_width);
        //        menu.setShadowDrawable(R.drawable.shadow);

        WindowManager windowManager = getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();

        // 设置滑动菜单视图的宽度
        menu.setBehindOffset(width * 200 / 320);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_leftmenu, new LeftMenuFragment(), TAG_LEFTMENU); // 用Fragment替换帧布局
        transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);

        transaction.commit(); //提交事务

//        Fragment fragment = fragmentManager.findFragmentByTag(TAG_CONTENT); // 根据TAG找到Fragment对象
    }

    /**
     * 获取侧边栏fragment对象
     *
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fragmentManager.findFragmentByTag(TAG_LEFTMENU);
        return leftMenuFragment;
    }

    /**
     * 获取主页fragment对象
     *
     * @return
     */
    public ContentFragment getContentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ContentFragment contentFragment = (ContentFragment) fragmentManager.findFragmentByTag(TAG_CONTENT);
        return contentFragment;
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
