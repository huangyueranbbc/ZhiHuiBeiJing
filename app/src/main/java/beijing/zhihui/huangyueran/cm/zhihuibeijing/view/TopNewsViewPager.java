package beijing.zhihui.huangyueran.cm.zhihuibeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by huangyueran on 2016/10/29.
 * 头条新闻自定义ViewPager
 *
 * @category 不允许滑动的viewpager
 */
public class TopNewsViewPager extends ViewPager {

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 1. 上下滑动需要拦截
     * 2. 向右滑动并且当前是第一个页面，需要拦截
     * 3.向左滑动并且是最后i一个页面，需要拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true); //通知父控件和祖宗控件不拦截此方法
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = (int) ev.getX();
                endY = (int) ev.getY();

                int dx = endX - startX;
                int dy = endY - startY;

                if (Math.abs(dy) < Math.abs(dx)) {
                    int currentItem = getCurrentItem(); //ViewPager当前页面
                    // 左右滑动
                    if (dx > 0) {
                        // 向右滑动
                        if (currentItem == 0) {
                            // 第一个页面 需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false); //通知父控件和祖宗控件拦截此方法
                        }
                    } else {
                        // 向左滑动
                        int count = getAdapter().getCount(); // item总数
                        if (currentItem == count - 1) {
                            // 最后一个页面 需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false); //通知父控件和祖宗控件拦截此方法
                        }
                    }
                } else {
                    // 上下滑动 拦截
                    getParent().requestDisallowInterceptTouchEvent(false); //通知父控件和祖宗控件拦截此方法
                }

                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
