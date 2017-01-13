package beijing.zhihui.huangyueran.cm.zhihuibeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by huangyueran on 2016/10/29.
 *
 * @category 不允许滑动的viewpager
 */
public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 重写此方法，触摸时什么都不做，实现滑动事件的禁用
        return true;
    }

    // 事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false; // 不拦截子控件的直接
    }
}
