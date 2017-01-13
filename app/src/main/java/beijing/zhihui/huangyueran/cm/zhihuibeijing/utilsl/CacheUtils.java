package beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl;

import android.content.Context;

/**
 * Created by huangyueran on 2016/11/2.
 */
public class CacheUtils {

    /**
     * 保存在本地
     * 缓存
     *
     * @param url  key
     * @param json value
     */
    public static void setCache(String url, String json, Context context) {
        // 也可以用文件进行缓存,用url为文件名,json为文件内容。可以把url进行加密压缩运算
        PrefUtils.setString(context, url, json);
    }

    /**
     * 读取缓存
     *
     * @param url
     * @param context
     */
    public static String getCache(String url, Context context) {
        // 文件缓存,查找是否有一个叫url的文件，有说明有缓存。
        return PrefUtils.getString(context, url, null);
    }

//    /**
//     * 清理缓存
//     * @param url
//     * @param context
//     */
//    public static void clearCache(String url, Context context) {
//
//    }
}
