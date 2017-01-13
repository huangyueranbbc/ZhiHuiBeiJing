package beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.NewsDetailActivity;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.R;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.BaseMenuDetailPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.domain.NewsMenu;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.domain.NewsTabBean;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.global.GlobalConstants;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl.CacheUtils;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl.PrefUtils;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.view.PullToRefreshListView;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.view.TopNewsViewPager;

/**
 * Created by huangyueran on 2016/11/13.
 * 页签页面对象
 */
public class TabDetailPager extends BaseMenuDetailPager {
    private static final String TAG = "ZGBJLog";
    private ArrayList<NewsTabBean.TopNews> mTopnews;
    private ArrayList<NewsTabBean.NewsData> mNewsList;
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl; // 下一页数据链接

    private Handler mHandler;

    @Override
    public void setmRootView(View mRootView) {
        super.setmRootView(mRootView);
    }

    public TabDetailPager(Activity Activity) {
        super(Activity);
    }

    private NewsMenu.NewsTabData mTabData; // 单个页签的网络数据

    @ViewInject(R.id.vp_top_news)
    private TopNewsViewPager mViewPager;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mCirclePageIndicator;

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.lv_list)
    private PullToRefreshListView lvList;

    private String mUrl; //网络链接

    public TabDetailPager(Activity Activity, NewsMenu.NewsTabData mTabData) {
        super(Activity);
        this.mTabData = mTabData;

        mUrl = GlobalConstants.SERVER_URL + mTabData.getUrl();
    }

    @Override
    public View initView() {
        // 给帧布局填充对象
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);

        //给ListView添加头布局
        View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
        ViewUtils.inject(this, mHeaderView); // 此处必须将头布局也注入
        lvList.addHeaderView(mHeaderView);

        /**
         * 1\2\3\4. PullToRefreshListView.java
         * 5. 前端界面设置回调
         */
        lvList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void OnRefresh() {
                // 刷新数据
                getDataFromServer();
            }

            // 加载下一页数据
            @Override
            public void onLoadMore() {
                // 判断是否有下一页数据
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_LONG).show();
                    lvList.onRefreshComplete(true);
                }
            }
        });


        // ListView点击事件
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerViewsCount = lvList.getHeaderViewsCount();//获取头布局数量
                position = position - headerViewsCount;
                NewsTabBean.NewsData news = mNewsList.get(position);

                // read_ids:1101,1102,1105 已读标记
                String readIds = PrefUtils.getString(mActivity, "read_ids", "");

                if (!readIds.contains(news.id + "")) {
                    readIds = readIds + news.id + ",";
                    PrefUtils.setString(mActivity, "read_ids", readIds);
                }
                // 将被点击的item文字颜色改为灰色
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);

                //跳到新闻详情页面
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", news.url);
                mActivity.startActivity(intent);
            }
        });

        return view;
    }

    /**
     * 加载下一页数据
     */
    private void getMoreDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processDate(result, true); // 解析数据

                // 收起下拉刷新控件
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                // 请求失败
                Log.i(TAG, "onFailure: " + GlobalConstants.CATEGORY_URL);
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();

                // 收起下拉刷新控件
                lvList.onRefreshComplete(false);
            }
        });
    }

    @Override
    public void initData() {
        // 缓存
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processDate(cache, false);
        }
        getDataFromServer(); //请求网络数据
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processDate(result, false); // 解析数据
                // 设置缓存
                CacheUtils.setCache(mUrl, result, mActivity);

                // 收起下拉刷新控件
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                // 请求失败
                Log.i(TAG, "onFailure: " + GlobalConstants.CATEGORY_URL);
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();

                // 收起下拉刷新控件
                lvList.onRefreshComplete(false);
            }
        });
    }

    private void processDate(String result, boolean isMore) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);

        String moreUrl = newsTabBean.data.more;
        if (!TextUtils.isEmpty(moreUrl)) {
            mMoreUrl = GlobalConstants.SERVER_URL + moreUrl;
        } else {
            mMoreUrl = null;
        }

        if (!isMore) {
            // 头条新闻填充数据
            mTopnews = newsTabBean.data.topnews;
            if (mTopnews != null) {
                mViewPager.setAdapter(new TopNewsAdapter());
                mCirclePageIndicator.setViewPager(mViewPager);
                mCirclePageIndicator.setSnap(true); // 快照方式展示

                // 事件要设置给CirclePageIndicator
                mCirclePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        // 更新头条新闻标题
                        NewsTabBean.TopNews topNews = mTopnews.get(position);
                        tvTitle.setText(topNews.title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });


                // 更新第一个头条新闻标题栏
                tvTitle.setText(mTopnews.get(0).title);
                mCirclePageIndicator.onPageSelected(0); // 默认让第一个选择，解决页面销毁后，重新初始化时，仍然保留上次圆点完治的BUG
            }

            //  列表新闻填充数据
            //列表新闻
            mNewsList = newsTabBean.data.news;
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
            }

            // 头条新闻轮播 保证只在null时 执行一次
            if (mHandler == null) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = mViewPager.getCurrentItem();
                        currentItem++;
                        if (currentItem > mTopnews.size() - 1) {
                            currentItem = 0;
                        }
                        mViewPager.setCurrentItem(currentItem);

                        mHandler.sendEmptyMessageDelayed(0, 3000); //发送3秒的延时消息
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 3000); //发送3秒的延时消息

                mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                System.out.println("ACTION_DOWN");
                                // 停止广告自动轮播
                                // 删除handler的所有消息
                                mHandler.removeCallbacksAndMessages(null);
                                // mHandler.post(new Runnable() {
                                //
                                // @Override
                                // public void run() {
                                // //在主线程运行
                                // }
                                // });
                                break;
                            case MotionEvent.ACTION_CANCEL:// 取消事件,
                                // 当按下viewpager后,直接滑动listview,导致抬起事件无法响应,但会走此事件
                                // 启动广告
                                mHandler.sendEmptyMessageDelayed(0, 3000);
                                break;
                            case MotionEvent.ACTION_UP:
                                // 启动广告
                                mHandler.sendEmptyMessageDelayed(0, 3000);
                                break;

                            default:
                                break;
                        }
                        return false;
                    }
                });
            }

        } else {
            // 加载更多数据
            ArrayList<NewsTabBean.NewsData> moreNews = newsTabBean.data.news;
            mNewsList.addAll(moreNews); // 追加数据

            //刷新listview
            mNewsAdapter.notifyDataSetChanged();
        }

    }

    // 头条新闻数据适配器
    class TopNewsAdapter extends PagerAdapter {

        private BitmapUtils mBitmapUtils;

        public TopNewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default); //设置加载中默认的图片
        }

        @Override
        public int getCount() {
            return mTopnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
            // view.setImageResource(R.drawable.topnews_item_default); //无效
            view.setScaleType(ImageView.ScaleType.FIT_XY); //设置图片缩放方式 x和y方向都要和父控件匹配填充

            // 下载图片 将图片设置给ImageView 图片异步加载
            // BitMapUtils-XUtils
            String url = mTopnews.get(position).topimage; //图片下载链接
            String imageUrl = url.replace("10.0.2.2", "192.168.191.1");
            Log.i(TAG, "imageUrl: " + imageUrl);
            mBitmapUtils.display(view, imageUrl);

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    class NewsAdapter extends BaseAdapter {

        private BitmapUtils mBitmapUtils;

        public NewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default); //默认图片
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsTabBean.NewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
                viewHolder = new ViewHolder();
                viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            NewsTabBean.NewsData news = getItem(position);
            viewHolder.tvTitle.setText(news.title);
            viewHolder.tvDate.setText(news.pubdate);

            // 标记已读未读
            String readIds = PrefUtils.getString(mActivity, "read_ids", "");
            if (readIds.contains(news.id + "")) {
                viewHolder.tvTitle.setTextColor(Color.GRAY);
            } else { // 防止listview重用
                viewHolder.tvTitle.setTextColor(Color.BLACK);
            }

            String url = news.listimage; //图片下载链接
            String imageUrl = url.replace("10.0.2.2", "192.168.191.1");

            mBitmapUtils.display(viewHolder.ivIcon, imageUrl);

            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }
}
