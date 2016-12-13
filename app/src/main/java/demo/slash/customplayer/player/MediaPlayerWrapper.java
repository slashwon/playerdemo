package demo.slash.customplayer.player;

import android.view.SurfaceHolder;

import java.io.IOException;

import demo.slash.customplayer.utils.Logger;
import demo.slash.customplayer.view.MainActivity;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by PICO-USER on 2016/12/12.
 */
public class MediaPlayerWrapper implements IMediaPlayer.OnPreparedListener {

    private IjkMediaPlayer mPlayer;

    public void setSurface(SurfaceHolder surfaceHolder) {
        if(mPlayer!=null){
            mPlayer.setDisplay(surfaceHolder);
        }
    }

    public enum State{
        START,STOP,PAUSE,IDLE,PREPARED
    };

    private State mState = State.IDLE;

    public MediaPlayerWrapper(){
        if(mPlayer ==null){
            mPlayer = new IjkMediaPlayer();
        }
        mPlayer.setOnPreparedListener(this);
    }

    public State getState(){
        return mState;
    }

    public void openFile(String path){
        if(mPlayer!=null){
            try {
                mPlayer.setDataSource(path);
                mPlayer.prepareAsync();
            } catch (IOException e) {
                Logger.E(MainActivity.TAG,"fail to set data source");
                e.printStackTrace();
            }
        }
    }

    public void start(){
        if(mPlayer!=null && (mState==State.STOP||mState==State.PAUSE||mState==State.PREPARED)){
            mPlayer.start();
            mState = State.START;
        }
    }

    public void stop(){
        if(mPlayer!=null && mState==State.START){
            mPlayer.stop();
            mState = State.STOP;
        }
    }

    public void pause(){
        if(mPlayer!=null && mState==State.START){
            mPlayer.pause();
            mState = State.PAUSE;
        }
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        mState = State.PREPARED;
        start();
    }

    public void fastMove(float d){
        long currPos = mPlayer.getCurrentPosition();
        currPos += d;
        currPos = currPos<=0 ? 0 : currPos;
        mPlayer.seekTo(currPos*5000);
    }
}
