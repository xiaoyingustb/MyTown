package cn.ifingers.mytown.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.ifingers.mytown.R;
import cn.ifingers.mytown.utils.LibsChecker;
import cn.ifingers.mytown.widget.MediaController;
import cn.ifingers.mytown.widget.VideoView;
import io.vov.vitamio.MediaPlayer;

/**
 * Created by syfing on 2016/5/16.
 */
public class MonitorDetailActivity extends Activity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener {

    private static final String TAG = "VideoPlayerActivity";

    private String mPath;
    private String mTitle;
    private VideoView mVideoView;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    /** 最大声音 */
    private int mMaxVolume;
    /** 当前声音 */
    private int mVolume = -1;
    /** 当前亮度 */
    private float mBrightness = -1f;
    /** 当前缩放模式 */
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;
    private MediaController mMediaController;
    private View mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");

        boolean isInitialized = LibsChecker.checkVitamioLibs(this);
        if(!isInitialized){
            Log.i(TAG, String.valueOf(isInitialized));
            return;
        }
        // ~~~ 获取播放地址和标题
        Intent intent = getIntent();
        mPath = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
        mTitle = "星空卫视";

        // ~~~ 绑定控件
        setContentView(R.layout.activity_rt_monitor);
        mVideoView = (VideoView) findViewById(R.id.monitor_vedioview);

        // 音量示意总布局
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);

        // 声音示意图
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);

        // 声音大小条形图
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);

        // 缓冲示意组合(ProgressBar + "正在缓冲...")
        mLoadingView = findViewById(R.id.video_loading);

        // ~~~ 绑定事件
        // 注册回调接口, 在视频播放完成时调用
        mVideoView.setOnCompletionListener(this);

        // 注册回调接口, 在有警告或错误信息时调用, 比如: 开始缓冲, 缓冲结束, 下载速度变化等
        mVideoView.setOnInfoListener(this);

        // ~~~ 绑定数据
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (mPath.startsWith("http:"))
            mVideoView.setVideoURI(Uri.parse(mPath));
        else
            mVideoView.setVideoPath(mPath);

        // 设置显示名称
        mMediaController = new MediaController(this);
        mMediaController.setAnchorView(mVideoView);
        mMediaController.setFileName(mTitle);
        mVideoView.setMediaController(mMediaController);
        mVideoView.requestFocus();

        mGestureDetector = new GestureDetector(this, new MyGestureListener());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null)
            mVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null)
            mVideoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null)
            mVideoView.stopPlayback();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 双击return true 滑动return false
        if (mGestureDetector.onTouchEvent(event))
            return true;

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }

        return super.onTouchEvent(event);
    }

    /** 手势结束 */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 移除消息队列中所有what标识为0的消息
        mDismissHandler.removeMessages(0);

        // 延时隐藏布局
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        /**
         * 双击屏幕事件(缩放视频)
         * @param e
         * @return
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            else
                mLayout++;
            if (mVideoView != null)
                mVideoView.setVideoLayout(mLayout, 0);
            return true;
        }

        /**
         * 滑动
         * @param e1    触发滑动的首次MotionEvent (按下事件 对应于ActionDown)
         * @param e2    当前被触发的MotionEvent (滚动事件 对应于ActionMove)
         * @param distanceX e1和e2事件在X坐标方向上的距离
         * @param distanceY e1和e2事件在Y坐标方向上的距离
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            Log.i("onScroll", String.valueOf(mOldY - y));

            if (mOldX > windowWidth * 4.0 / 5)// 右边1/5屏幕宽度范围内上下滑动触发音量设置事件
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)// 左边滑动1/5屏幕宽度范围内上下滑动触发屏幕亮度设置事件
                onBrightnessSlide((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /** 定时隐藏 */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 显示
            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mVideoView != null)
            mVideoView.setVideoLayout(mLayout, 0);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        finish();
    }

    private void stopPlayer() {
        if (mVideoView != null)
            mVideoView.pause();
    }

    private void startPlayer() {
        if (mVideoView != null)
            mVideoView.start();
    }

    private boolean isPlaying() {
        return mVideoView != null && mVideoView.isPlaying();
    }

    /** 是否需要自动恢复播放，用于自动暂停，恢复播放 */
    private boolean needResume;

    @Override
    public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
        switch (arg1) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                //开始缓存，暂停播放
                if (isPlaying()) {
                    stopPlayer();
                    needResume = true;
                }
                mLoadingView.setVisibility(View.VISIBLE);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                //缓存完成，继续播放
                if (needResume)
                    startPlayer();
                mLoadingView.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                //显示 下载速度
                Log.i(TAG, "download rate:" + arg2);
                //mListener.onDownloadRateChanged(arg2);
                break;
        }
        return true;
    }
}