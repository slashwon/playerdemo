package demo.slash.customplayer.player;

import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;

import demo.slash.customplayer.listener.PlayerListener;
import demo.slash.customplayer.utils.Logger;
import demo.slash.customplayer.view.LocalVideos;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by PICO-USER on 2016/12/12.
 */
public class MediaPlayerWrapper{

    private IjkMediaPlayer mPlayer;

    public void setDisplay(SurfaceHolder surfaceHolder) {
        if(mPlayer!=null){
            mPlayer.setDisplay(surfaceHolder);
        }
    }

    public IjkMediaPlayer getPlayer(){
        return mPlayer;
    }

    public void setSurface(Surface s){
        mPlayer.setSurface(s);
    }

    public enum State{
        START,STOP,PAUSE,IDLE,PREPARED
    };

    private State mState = State.IDLE;

    public MediaPlayerWrapper(PlayerListener listener){
        init(listener);
    }

    public State getState(){
        return mState;
    }

    public void setState(State mState) {
        this.mState = mState;
    }

    public void openFile(String path){
        if(mPlayer!=null){
            try {
                mPlayer.setDataSource(path);
                mPlayer.prepareAsync();
            } catch (IOException e) {
                Logger.E(LocalVideos.TAG,"fail to set data source");
                e.printStackTrace();
            }
        }
    }

    public long getDuration(){
        return mPlayer.getDuration();
    }

    public void init(PlayerListener listener){
        Logger.D(LocalVideos.TAG,"listener == null ? "+(listener==null));
        if(mPlayer ==null){
            mPlayer = new IjkMediaPlayer();
        }
        mPlayer.setOnPreparedListener(listener.mOnPreparedListener);
        mPlayer.setOnVideoSizeChangedListener(listener.mVideoSizeChangedListener);
        mPlayer.setOnCompletionListener(listener.mOnCompletionListener);
    }

    public void start(){
        if(mPlayer!=null && (mState==State.STOP||mState==State.PAUSE||mState==State.PREPARED)){
            mPlayer.start();
            mState = State.START;
            updateHandler();
        }
    }

    // do nothing
    private void updateHandler() {

    }

    public void stop(){
        if(mPlayer!=null && mState==State.START){
            mPlayer.stop();
            mState = State.STOP;
            updateHandler();
        }
    }

    public void release(){
        if(mPlayer!=null && mState!=State.IDLE){
            mPlayer.release();
            mState = State.IDLE;
            updateHandler();
        }
        mPlayer=null;
    }

    public void pause(){
        if(mPlayer!=null && mState==State.START){
            mPlayer.pause();
            mState = State.PAUSE;
            updateHandler();
        }
    }

    public void fastMove(float d){
        long total = mPlayer.getDuration();
        long curr = mPlayer.getCurrentPosition();
        long progress = total/100;
        float t = curr + progress * (d) / Math.abs(d);
        t = t<=0 ? 0 : t;
        t = t>=total ? total : t;

        mPlayer.seekTo((long) t);
    }

    public void fastRateMove(float rate){
        long duration = mPlayer.getDuration();
        mPlayer.seekTo((long) (duration*rate));
        Logger.D(LocalVideos.TAG,"seek to = "+duration*rate);
    }
}
