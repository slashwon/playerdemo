package demo.slash.customplayer.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import demo.slash.customplayer.MainActivity;
import demo.slash.customplayer.utils.Logger;

/**
 * Created by PICO-USER on 2016/12/12.
 */
public class VideoSurfaceView extends SurfaceView implements MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    private MediaPlayer mPlayer;
    private SurfaceHolder mSurfaceHolder;

    public VideoSurfaceView(Context context) {
        super(context);
        initVideoView();
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

    public VideoSurfaceView initPlayer(){
        if(null==mPlayer) {
            mPlayer = new MediaPlayer();
        }
        mPlayer.setOnPreparedListener(this);
        return this;
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
        if(!mPlayer.isPlaying()) {
            mPlayer.stop();
            if (!TextUtils.isEmpty(path)) {
                try {
                    mPlayer.setDataSource(path);
                    mPlayer.setDisplay(mSurfaceHolder);
                    mPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopVideo(){
        if(mPlayer!=null && mPlayer.isPlaying()){
            mPlayer.stop();
            mPlayer.release();
        }
    }

    public void pauseVideo(){
        if(mPlayer!=null && mPlayer.isPlaying()){
            mPlayer.pause();
        }
    }

    public void resumeVideo(){
        if(mPlayer!=null && !mPlayer.isPlaying()){
            mPlayer.start();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
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
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceHolder = null;
        Logger.D(MainActivity.TAG,"holder surface destroyed");
    }
}
