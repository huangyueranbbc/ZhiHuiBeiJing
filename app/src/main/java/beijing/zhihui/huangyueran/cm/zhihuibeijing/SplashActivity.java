package beijing.zhihui.huangyueran.cm.zhihuibeijing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl.PrefUtils;

/**
 * 闪屏页面
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

        // 旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(1000); //动画时间
        rotateAnimation.setFillAfter(true); //保持动画结束状态

        // 缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);

        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        //动画集合
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        //启动动画
        rlRoot.startAnimation(animationSet);

        // 监听动画播放事件
        animationSet.setAnimationListener(new Animation.AnimationListener() {

            private Intent intent;

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束，跳转页面
                // 如果是第一次进入，跳转到新手引导
                // 否则跳转主页面

                // SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                // boolean is_first_enter = sp.getBoolean("is_first_enter", true);
                boolean is_first_enter = PrefUtils.getBoolean(SplashActivity.this, "is_first_enter", true);
                if (is_first_enter) {
                    //新手引导
                    intent = new Intent(getApplicationContext(), GuideActivity.class);

                } else {
                    //主页面
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }

                startActivity(intent);
                finish(); //结束当前页面
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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

