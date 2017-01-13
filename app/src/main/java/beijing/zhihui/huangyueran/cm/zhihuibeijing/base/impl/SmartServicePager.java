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
 * @category 智慧服务
 */
public class SmartServicePager extends BasePager {

    public SmartServicePager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        super.initData();
        // 要给帧布局填充布局对象
        TextView textView = new TextView(mActivity);
        textView.setText("智慧服务");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        flContent.addView(textView);

        //修改页面标题
        tvTitle.setText("生活");

        //显示菜单按钮
        btnMenu.setVisibility(View.VISIBLE);
    }
}
