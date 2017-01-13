package beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.R;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.BaseMenuDetailPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.domain.PhotosBean;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.global.GlobalConstants;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl.CacheUtils;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl.MyBitMapUtils;

/**
 * Created by huangyueran on 2016/11/3.
 * 新闻-组图详情页
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener {
    private static final String TAG = "ZGBJLog";

    @ViewInject(R.id.lv_photo)
    private ListView lvPhoto;
    @ViewInject(R.id.gv_photo)
    private GridView gvPhoto;

    private ArrayList<PhotosBean.PhotoNews> mNewsList;

    private ImageButton btnPhoto;

    public PhotosMenuDetailPager(Activity Activity, ImageButton btnPhoto) {
        super(Activity);
        btnPhoto.setOnClickListener(this); // 组图设置按钮 设置点击事件
        this.btnPhoto = btnPhoto;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
        ViewUtils.inject(this, view);
        return view;
    }

    public void initData() {
        String cache = CacheUtils.getCache(GlobalConstants.PHOTO_URL, mActivity); //读取缓存
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        Log.i(TAG, "url:====== " + GlobalConstants.PHOTO_URL);
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.PHOTO_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);

                CacheUtils.setCache(GlobalConstants.PHOTO_URL, result, mActivity); //设置缓存
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
     * 解析数据
     *
     * @param result
     */
    private void processData(String result) {
        Gson gson = new Gson();
        PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);

        mNewsList = photosBean.data.news;
        lvPhoto.setAdapter(new PhotoAdapter());
        gvPhoto.setAdapter(new PhotoAdapter());// gridview的布局结构和listview完全一致 所以可以共用一个adapter
    }

    private boolean isListView = true;// 标记当前是否是listview展示

    @Override
    public void onClick(View v) {
        if (isListView) {
            // 切成gridview
            lvPhoto.setVisibility(View.GONE);
            gvPhoto.setVisibility(View.VISIBLE);
            btnPhoto.setImageResource(R.drawable.icon_pic_list_type);

            isListView = false;
        } else {
            // 切成listview
            lvPhoto.setVisibility(View.VISIBLE);
            gvPhoto.setVisibility(View.GONE);
            btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);

            isListView = true;
        }
    }

        class PhotoAdapter extends BaseAdapter {
        // private BitmapUtils mBitmapUtils;
        private MyBitMapUtils mBitmapUtils;

        public PhotoAdapter() {
            mBitmapUtils = new MyBitMapUtils();
            //  mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);

        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public PhotosBean.PhotoNews getItem(int position) {
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
                convertView = View.inflate(mActivity, R.layout.list_item_photos, null);
                viewHolder = new ViewHolder();
                viewHolder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            PhotosBean.PhotoNews item = getItem(position);
            viewHolder.tvTitle.setText(item.title);

            String imageUrl = item.listimage.replace("10.0.2.2", "192.168.191.1");
            mBitmapUtils.display(viewHolder.ivPic, imageUrl);

            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvTitle;
    }
}
