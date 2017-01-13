package beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.BaseMenuDetailPager;

/**
 * Created by huangyueran on 2016/11/3.
 * 新闻-专题详情页
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager {

    public TopicMenuDetailPager(Activity Activity) {
        super(Activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("新闻-专题详情页");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
}
