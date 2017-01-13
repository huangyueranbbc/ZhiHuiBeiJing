package beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.MainActivity;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.BaseMenuDetailPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.BasePager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.menu.InteractMenuDetailPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.menu.NewsMenuDetailPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.menu.PhotosMenuDetailPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.menu.TopicMenuDetailPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.domain.NewsMenu;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.fragment.LeftMenuFragment;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.global.GlobalConstants;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl.CacheUtils;

/**
 * Created by huangyueran on 2016/10/29.
 *
 * @category 新闻中心
 */
public class NewsCenterPager extends BasePager {

    private static final String TAG = "ZGBJLog";

    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers; //菜单详情页集合
    private NewsMenu newsMenuData; //分类信息

    public NewsCenterPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        super.initData();
        // 要给帧布局填充布局对象

        //修改页面标题
        tvTitle.setText("新闻中心");

        //显示菜单按钮
        btnMenu.setVisibility(View.VISIBLE);

        //先判断有没有缓存，如果有则加载缓存
        String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            Log.i(TAG, "加载到缓存: " + cache);
            processData(cache);
        }

        //请求服务器,获取数据
        getDataFromServer();

    }

    /**
     * 从服务器获取数据
     * 需要添加权限
     */
    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // 请求成功
                String result = responseInfo.result;
                Log.i(TAG, "服务器返回结果: " + result);

                // JsonObject,Gsob 解析数据
                processData(result);

                //写入缓存
                CacheUtils.setCache(GlobalConstants.CATEGORY_URL, result, mActivity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                // 请求失败
                Log.i(TAG, "onFailure: " + GlobalConstants.CATEGORY_URL);
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 解析json数据
     */
    protected void processData(String json) {
        Gson gson = new Gson();
        newsMenuData = gson.fromJson(json, NewsMenu.class);
        Log.i(TAG, "processData: " + newsMenuData.toString());

        // 获取侧边栏对象
        MainActivity mainActivity = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();

        // 给侧边栏设置数据
        leftMenuFragment.setMenuData(newsMenuData.getData());

        //初始化4个菜单详情页面
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mainActivity, newsMenuData.getData().get(0).getChildren()));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mainActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mainActivity,btnPhoto));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mainActivity));

        // 将新闻详情页设置为新闻中心默认页面
        setCurrentDetailPager(0);
    }

    /**
     * 设置菜单详情页
     *
     * @param position
     */
    public void setCurrentDetailPager(int position) {
        // 重新给fragment添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position); //获取当前应该显示的页面
        View view = pager.getmRootView(); //当前页面根布局

        // 清楚之前旧的布局
        flContent.removeAllViews();

        flContent.addView(view); //添加布局

        //初始化页面数据
        pager.initData();

        //更新标题
        tvTitle.setText(newsMenuData.getData().get(position).getTitle());

        //如果是组图页面 需要显示切换按钮
        if (pager instanceof PhotosMenuDetailPager) {
            btnPhoto.setVisibility(View.VISIBLE);
        } else {
            // 隐藏切换按钮
            btnPhoto.setVisibility(View.GONE);
        }
    }
}
