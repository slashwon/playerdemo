package demo.slash.customplayer.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

import demo.slash.customplayer.controller.DeviceController;
import demo.slash.customplayer.listener.PlayerListener;
import demo.slash.customplayer.player.MediaPlayerWrapper;
import demo.slash.customplayer.utils.Logger;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoSurfaceView extends SurfaceView implements IjkMediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    private static final int MSG_UPDATE_SB = 100;
    private AutoSeekBar mSeekBar;
    private MediaPlayerWrapper mPlayer;
    private SurfaceHolder mSurfaceHolder;
    private String mPath;
    private PlayerActivity mActivity;
    private PlayerListener playerListener;
    private IMediaPlayer.OnVideoSizeChangedListener vscListener;
    private IMediaPlayer.OnPreparedListener opListener;
    private IMediaPlayer.OnCompletionListener ocListener;
    private int mWidth = 640;
    private int mHeight = 1020;

    public VideoSurfaceView(Context context) {
        super(context);
        initListener();
        initVideoView(context);
    }

    private void initListener() {
        vscListener = new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                System.out.println( "video size changed: width = " + width + "; height = " + height);
                mWidth = width;
                mHeight = height;
                invalidate();
            }
        };

        opListener = new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                System.out.println("onPrepared");
                mSeekBar.bindPlayer(mp);
                mp.start();
                getPlayer().setState(MediaPlayerWrapper.State.START);
                if (null != mSeekBar) {
                    mSeekBar.startTracking();
                    System.out.println(LocalVideos.TAG+ " seek bar start tracking");
                }
            }
        };

        ocListener = new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                System.out.println(LocalVideos.TAG+"on completion");
                if(null!=mSeekBar){
                    mSeekBar.setProgress(0);
                }
            }
        };

        playerListener = new PlayerListener();
        playerListener.mVideoSizeChangedListener = vscListener;
        playerListener.mOnPreparedListener = opListener;
        playerListener.mOnCompletionListener = ocListener;

    }

    public MediaPlayerWrapper getPlayer(){
        return mPlayer;
    }

    private void initVideoView(Context context) {
        mActivity=(PlayerActivity)context;
//        mWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth();
//        mHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        getHolder().addCallback(this);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initListener();
        initVideoView(context);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initListener();
        initVideoView(context);
    }

    public void initPlayer(){
        if(null==mPlayer) {
            mPlayer = new MediaPlayerWrapper(playerListener);
        }
    }

    public void setPath(String path){
        mPath = path;
    }

    public void playVideo(String path){
        if(mPlayer==null){
            Logger.D(LocalVideos.TAG,"please call initPlayer() first");
            return ;
        }
        if(mSurfaceHolder==null){
            Logger.D(LocalVideos.TAG,"surface holder is not prepared yet");
            return ;
        }
        mPlayer.setDisplay(mSurfaceHolder);
        if(!TextUtils.isEmpty(path)){
            mPlayer.openFile(path);
        }
    }

    public void startVideo(){
        mPlayer.start();
        mSeekBar.startTracking();
        updateSeekbar();
    }

    private void updateSeekbar() {
        MediaPlayerWrapper.State state = mPlayer.getState();
        switch (state){
            case STOP:
                if(null!=mSeekBar){
                    mSeekBar.setProgress(0);
                }
                break;
        }
    }

    public void stopVideo(){
        mPlayer.stop();

        updateSeekbar();
    }

    public void pauseVideo(){
       mPlayer.pause();
        updateSeekbar();
    }

    public void release(){
        mPlayer.release();
    }

    public void fastMove(float d){
        if(mPlayer!=null){
            mPlayer.fastMove(d);
        }
    }

    public void fastRateMove(float rate){
        mPlayer.fastRateMove(rate);
    }

    public void onPrepared(IMediaPlayer mp) {
        mp.start();
        Logger.D(LocalVideos.TAG,"player is prepared");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Logger.D(LocalVideos.TAG,"holder surface created");
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Logger.D(LocalVideos.TAG,"holder surface changed");
        mSurfaceHolder = holder;
        mHandler.sendEmptyMessage(MSG_SURFACE_READY);
        System.out.println("surface holder changed ,width = "+width+";height = "+height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceHolder = null;
        Logger.D(LocalVideos.TAG,"holder surface destroyed");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        System.out.println("on measure   mWidth = "+mWidth+";mHeight = "+mHeight);

        int wmode = MeasureSpec.getMode(widthMeasureSpec);
        int hmode = MeasureSpec.getMode(heightMeasureSpec);
        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        int hsize = MeasureSpec.getSize(heightMeasureSpec);

        boolean wMost = wmode == MeasureSpec.AT_MOST;
        boolean hMost = hmode == MeasureSpec.AT_MOST;

        if (wMost && hMost) {
            setMeasuredDimension(mWidth,mHeight);
        } else if (wMost){
            setMeasuredDimension(mWidth,hsize);
        } else if (hMost) {
            setMeasuredDimension(wsize,mHeight);
        }

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MSG_SURFACE_READY:
                    playVideo(mPath);
                    break;
                case MSG_UPDATE_SB:
                    switch (mPlayer.getState()){
                        case STOP:
                            break;
                    }
                    break;
            }
        }
    };

    private static final int MSG_SURFACE_READY=10;

    public void adjustLight(float touchMove,float maxMove) {
        DeviceController.instance().adjustLight(mActivity,touchMove,maxMove);
    }

    public void adjustVolume(float touchMove,float maxMove) {
        DeviceController.instance().adjustVolume(mActivity,touchMove,maxMove);
    }

    public void bindSeekBar(SeekBar seekBar) {
        mSeekBar = (AutoSeekBar) seekBar;
    }

//    public void setActivity(PlayerActivity playerActivity) {
//        mActivity = playerActivity;
//    }
}
