package beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by huangyueran on 2016/10/26.
 *
 * @catagory SharePerference封装
 */
public class PrefUtils {

    public static void setBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, defValue).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defValue);
    }


    public static void setString(Context context, String key, String defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, defValue).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defValue);
    }

    public static void setInt(Context context, String key, int defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, defValue).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defValue);
    }


}
