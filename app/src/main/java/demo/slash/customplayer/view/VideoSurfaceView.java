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

    private MediaPlayerWrapper mPlayer;
    private SurfaceHolder mSurfaceHolder;
    private String mPath;

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
        mPlayer.setSurface(mSurfaceHolder);
        if(!TextUtils.isEmpty(path)){
            mPlayer.openFile(path);
        }
    }

    public void startVideo(){
        mPlayer.start();
    }

    public void stopVideo(){
        mPlayer.stop();
    }

    public void pauseVideo(){
       mPlayer.pause();
    }

    public void resumeVideo(){
        mPlayer.start();
    }

    public void fastMove(float d){
        if(mPlayer!=null){
            mPlayer.fastMove(d);
        }
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
            }
        }
    };

    private static final int MSG_SURFACE_READY=10;
}
