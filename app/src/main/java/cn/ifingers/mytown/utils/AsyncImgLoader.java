package cn.ifingers.mytown.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by syfing on 2016/5/13.
 */
public class AsyncImgLoader {
    private Handler uiHandler;
    private LinkedList<Runnable> taskQueue;
    private Handler taskHandler;
    private ExecutorService taskExecutors;
    private Thread taskThread;

    private static AsyncImgLoader loader;
    private Semaphore taskSemaphor;
    private Semaphore uiSemaphor;
    private LruCache<String, Bitmap> lruCache;

    private Context context;

    private AsyncImgLoader(Context context) {
        init(context);
    }

    public static AsyncImgLoader getInstance(Context context) {
        synchronized (AsyncImgLoader.class) {
            if (loader == null) {
                synchronized (AsyncImgLoader.class) {
                    if (loader == null) {
                        loader = new AsyncImgLoader(context);
                    }
                }
            }
        }
        return loader;
    }

    private void init(Context context) {
        this.context = context;
        uiHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0x110) {
                    HolderForMsg holder = (HolderForMsg) msg.obj;
                    String url = holder.url;
                    ImageView img = holder.img;
                    if (img.getTag().equals(url) && holder.bitmap != null) {
                        lruCache.put(url, holder.bitmap);
                        writeToFile(url, holder.bitmap);
                        img.setImageBitmap(holder.bitmap);
                    }
                }
                return true;
            }
        });

        taskQueue = new LinkedList<Runnable>();
        taskExecutors = Executors.newFixedThreadPool(5);
        taskSemaphor = new Semaphore(5);
        uiSemaphor = new Semaphore(0);

        long totalSize = Runtime.getRuntime().maxMemory();
        int size = (int) (totalSize / 8);
        lruCache = new LruCache<String, Bitmap>(size) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

        taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                taskHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if (msg.what == 0x111) {
                            try {
                                taskSemaphor.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            executeTask();
                        }
                        return true;
                    }
                });
                uiSemaphor.release();
                Looper.loop();
            }
        });

        taskThread.start();
    }

    private void executeTask() {
        taskExecutors.execute(taskQueue.removeLast());
    }

    public void loadImage(final String imgurl, final ImageView imageView) {
        Bitmap bp1 = lruCache.get(imgurl);
        if (bp1 != null) {
            Log.i("LoadingType", "从内存加载");
            imageView.setImageBitmap(bp1);
            return;
        }

        Bitmap bp2 = readFromFile(imgurl);
        if (bp2 != null) {
            Log.i("LoadingType", "从文件加载");
            imageView.setImageBitmap(bp2);
            lruCache.put(imgurl, bp2);
            return;
        }

        imageView.setTag(imgurl);

        addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("LoadingType", "从网络加载");
                    URL url = new URL(imgurl);
                    URLConnection connection = url.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    InputStream is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    HolderForMsg holder = new HolderForMsg();
                    holder.bitmap = bitmap;
                    holder.img = imageView;
                    holder.url = imgurl;
                    Message msg = Message.obtain();
                    msg.what = 0x110;
                    msg.obj = holder;
                    uiHandler.sendMessage(msg);
                    taskSemaphor.release();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void addTask(Runnable runnable) {
        taskQueue.addFirst(runnable);
        if (taskHandler == null) {
            try {
                uiSemaphor.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        taskHandler.sendEmptyMessage(0x111);
    }

    class HolderForMsg {
        ImageView img;
        String url;
        Bitmap bitmap;
    }

    private void writeToFile(String url, Bitmap bmp) {
        String fileName = MD5Utils.getMD5(url).substring(0, 20) + ".jpg";
        File targetFile = new File(context.getExternalCacheDir(), fileName);
        try {
            OutputStream os = new FileOutputStream(targetFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Bitmap readFromFile(String url) {
        String fileName = MD5Utils.getMD5(url).substring(0, 20) + ".jpg";
        File sourceFile = new File(context.getExternalCacheDir(), fileName);
        Bitmap bmp = BitmapFactory.decodeFile(sourceFile.getPath());
        if (bmp != null) {
            lruCache.put(url, bmp);
        }
        return bmp;
    }
}