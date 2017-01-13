package beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.R;

/**
 * 自定义图片加载工具
 * Created by huangyueran on 2016/12/16.
 */
public class MyBitMapUtils {
    private static final String TAG = "ZGBJLog";

    private NetCacheUtils mNetCacheUtils;       //网络缓存工具
    private LocalCacheUtils mLocalCacheUtils;   //本地缓存工具
    private MemoryCacheUtils mMemoryCacheUtils; //内存缓存工具

    public MyBitMapUtils() {
        mMemoryCacheUtils = new MemoryCacheUtils();
        mLocalCacheUtils = new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
    }

    /**
     * 展示图片
     *
     * @param ivPic
     * @param imageUrl
     */
    public void display(ImageView ivPic, String imageUrl) {
        //设置默认图片
        ivPic.setImageResource(R.drawable.pic_item_list_default);

        // 优先从内存中加载图片, 速度最快, 不浪费流量
        Bitmap cache = mMemoryCacheUtils.getMemoryCache(imageUrl);
        if (cache != null) {
            Log.i(TAG, "内存加载缓存: " + cache + ":===:" + imageUrl);
            ivPic.setImageBitmap(cache);
            return;
        }

        // 其次从本地(sdcard)加载图片, 速度快, 不浪费流量
        cache = mLocalCacheUtils.getLocalCache(imageUrl);
        if (cache != null) {
            Log.i(TAG, "本地加载缓存: " + cache + ":===:" + imageUrl);
            ivPic.setImageBitmap(cache);

            // 写内存缓存
            mMemoryCacheUtils.setMemoryCache(imageUrl, cache);

            return;
        }


        //  网络下载
        mNetCacheUtils.getBitMapFromNet(ivPic, imageUrl);
    }
}
