package cn.ifingers.mytown.utils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

/**
 * Created by syfing on 2016/5/18.
 */
public class ApplicationUtil extends Application{
    public static final String BASE_PATH = "http://www.ejob.org.cn/dianping";
    public static final String GOODS_PATH = BASE_PATH + "/api/goods";

    private static ApplicationUtil mApplication;

    /** OPlayer SD卡缓存路径 */
    public static final String OPLAYER_CACHE_BASE = Environment.getExternalStorageDirectory() + "/oplayer";
    /** 视频截图缓冲路径 */
    public static final String OPLAYER_VIDEO_THUMB = OPLAYER_CACHE_BASE + "/thumb/";
    /** 首次扫描 */
    public static final String PREF_KEY_FIRST = "application_first";

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        init();
    }

    private void init() {
        //创建缓存目录
        FileUtils.createIfNoExists(OPLAYER_CACHE_BASE);
        FileUtils.createIfNoExists(OPLAYER_VIDEO_THUMB);
    }

    public static ApplicationUtil getApplication() {
        return mApplication;
    }

    public static Context getContext() {
        return mApplication;
    }

    /** 销毁 */
    public void destory() {
        mApplication = null;
    }
}
