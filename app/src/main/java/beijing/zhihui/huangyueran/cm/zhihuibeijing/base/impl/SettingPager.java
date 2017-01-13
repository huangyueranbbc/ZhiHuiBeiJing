package beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.BasePager;

/**
 * Created by huangyueran on 2016/10/29.
 *
 * @category 设置
 */
public class SettingPager extends BasePager {

    public SettingPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        super.initData();
        // 要给帧布局填充布局对象
        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        flContent.addView(textView);

        //修改页面标题
        tvTitle.setText("设置");

        //隐藏菜单按钮
        btnMenu.setVisibility(View.GONE);
    }
}
