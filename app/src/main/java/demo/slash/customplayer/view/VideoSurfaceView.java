package demo.slash.customplayer.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import demo.slash.customplayer.player.MediaPlayerWrapper;
import demo.slash.customplayer.utils.Logger;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by PICO-USER on 2016/12/12.
 */
public class VideoSurfaceView extends SurfaceView implements IjkMediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    private static final int MSG_UPDATE_SB = 100;
    private MediaPlayerWrapper mPlayer;
    private SurfaceHolder mSurfaceHolder;
    private String mPath;
    private PlayerActivity mActivity;

    public VideoSurfaceView(Context context) {
        super(context);
        initVideoView();
    }

    public MediaPlayerWrapper getPlayer(){
        return mPlayer;
    }

    private void initVideoView() {
        getHolder().addCallback(this);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
    }

    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView();
    }

    public void initPlayer(){
        if(null==mPlayer) {
            mPlayer = new MediaPlayerWrapper();
        }
    }

    public void setPath(String path){
        mPath = path;
    }

    public void playVideo(String path){
        if(mPlayer==null){
            Logger.D(MainActivity.TAG,"please call initPlayer() first");
            return ;
        }
        if(mSurfaceHolder==null){
            Logger.D(MainActivity.TAG,"surface holder is not prepared yet");
            return ;
        }
        mPlayer.setDisplay(mSurfaceHolder);
        if(!TextUtils.isEmpty(path)){
            mPlayer.openFile(path);
        }
    }

    public void startVideo(){
        mPlayer.start();
        updateSeekbar();
    }

    private void updateSeekbar() {
        MediaPlayerWrapper.State state = mPlayer.getState();
        Message msg = mHandler.obtainMessage(MSG_UPDATE_SB);
        msg.obj = state;
        mHandler.sendMessage(msg);
    }

    public void stopVideo(){
        mPlayer.stop();
        updateSeekbar();
    }

    public void pauseVideo(){
       mPlayer.pause();
        updateSeekbar();
    }

    public void resumeVideo(){
        mPlayer.start();
        updateSeekbar();
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
        Logger.D(MainActivity.TAG,"player is prepared");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Logger.D(MainActivity.TAG,"holder surface created");
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Logger.D(MainActivity.TAG,"holder surface changed");
        mSurfaceHolder = holder;
        mHandler.sendEmptyMessage(MSG_SURFACE_READY);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceHolder = null;
        Logger.D(MainActivity.TAG,"holder surface destroyed");
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
                    MediaPlayerWrapper.State state = (MediaPlayerWrapper.State) msg.obj;
                    if(mActivity!=null){
                        mActivity.updateUIstate(state);
                    }
                    break;
            }
        }
    };

    private static final int MSG_SURFACE_READY=10;

    public void setActivity(PlayerActivity playerActivity) {
        mActivity = playerActivity;
    }
}
