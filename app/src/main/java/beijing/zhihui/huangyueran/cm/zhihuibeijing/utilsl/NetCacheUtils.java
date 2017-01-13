package beijing.zhihui.huangyueran.cm.zhihuibeijing.utilsl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by huangyueran on 2016/12/16.
 */
public class NetCacheUtils {
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.mLocalCacheUtils = localCacheUtils;
        mMemoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 从网络获取图片
     *
     * @param ivPic
     * @param imageUrl
     */
    public void getBitMapFromNet(ImageView ivPic, String imageUrl) {
        new BitmapTask().execute(ivPic, imageUrl); // 启动asyncTask
    }

    /**
     * 三个泛型意义: 第一个泛型:doInBackground里的参数类型 第二个泛型: onProgressUpdate里的参数类型 第三个泛型:
     * onPostExecute里的参数类型及doInBackground的返回类型
     */
    class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {

        private ImageView imageView;
        private String mUrl;

        // 1.预加载, 运行在主线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // 2.正在加载, 运行在子线程(核心方法), 可以直接异步请求
        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];
            mUrl = (String) params[1];

            imageView.setTag(mUrl);// 打标记, 将当前imageview和url绑定在了一起

            //开始下载图片
            Bitmap bitmap = download(mUrl);

            // publishProgress(values) 调用此方法实现进度更新(会回调onProgressUpdate)
            return bitmap;
        }

        // 3.更新进度的方法, 运行在主线程
        @Override
        protected void onProgressUpdate(Integer... values) {
            // 更新进度条
            super.onProgressUpdate(values);
        }

        // 4.加载结束, 运行在主线程(核心方法), 可以直接更新UI
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                // 由于listview的重用机制导致imageview对象可能被多个item共用,
                // 从而可能将错误的图片设置给了imageView对象
                // 所以需要在此处校验, 判断是否是正确的图片

                String url = (String) imageView.getTag();

                if (url.equals(this.mUrl)) {// 判断图片绑定的url是否就是当前bitmap的url
                    imageView.setImageBitmap(bitmap); // 设置ImageView图片

                    // 写本地缓存
                    mLocalCacheUtils.setLocalCache(url, bitmap);

                    // 写内存缓存
                    mMemoryCacheUtils.setMemoryCache(url, bitmap);

                }
            }
            super.onPostExecute(bitmap);
        }
    }

    /**
     * 下载图片
     *
     * @param url
     * @return
     */
    private Bitmap download(String url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000); //连接超时时间
            urlConnection.setReadTimeout(5000); //读取超时

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) { //连接成功
                InputStream inputStream = urlConnection.getInputStream();

                // 根据输入流生成bitmap对象
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }
}
