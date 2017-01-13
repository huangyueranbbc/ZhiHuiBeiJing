package beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 内存缓存
 * 因为从 Android 2.3 (API Level 9)开始，垃圾回收器会更倾向于回收持有软引用或弱引用的对象，这让软引用和弱引用变得不再可靠。Google建议使用LruCache
 * Created by huangyueran on 2016/12/16.
 */
public class MemoryCacheUtils {
    private static final String TAG = "ZGBJLog";

    //    private HashMap<String, Bitmap> mMemoryCache = new HashMap<String, Bitmap>();
    private LruCache<String, Bitmap> mMemoryCache; //lru 最近最少被使用

    public MemoryCacheUtils() {
        long maxMemory = Runtime.getRuntime().maxMemory(); // app分配内存大小
        Log.i(TAG, "maxMemory: " + maxMemory);
        mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {
            // 返回每个对象的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getByteCount();// 返回字节总大小
                return byteCount;
            }
        };
    }

    /**
     * 写缓存
     */
    public void setMemoryCache(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }

    /**
     * 读缓存
     */
    public Bitmap getMemoryCache(String url) {
        return mMemoryCache.get(url);
    }
}
