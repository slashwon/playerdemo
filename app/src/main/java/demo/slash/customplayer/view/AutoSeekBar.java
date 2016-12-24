package demo.slash.customplayer.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.SeekBar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import demo.slash.customplayer.utils.Logger;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by whs on 16-12-24.
 */

public class AutoSeekBar extends SeekBar {

    private IMediaPlayer mPlayer;
    private long mCurrPosition  = 0;
    private long mDuration = 1;
    private ExecutorService mExecutors;

    public AutoSeekBar(Context context) {
        this(context,null);
    }

    public AutoSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mExecutors = Executors.newFixedThreadPool(1);
    }

    public void bindPlayer(IMediaPlayer player){
        mPlayer = player;
    }

    public void startTracking(){
        if(null!=mPlayer){
            mExecutors.execute(new Runnable() {
                @Override
                public void run() {
                    while(mPlayer.isPlaying()){
                        int max = getMax();
                        mCurrPosition = mPlayer.getCurrentPosition();
                        mDuration = mPlayer.getDuration();
                        int progress = (int) (max * ((float) mCurrPosition/mDuration));
                        Message msg = handler.obtainMessage();
                        msg.arg1 = progress;
                        handler.sendMessage(msg);
                    }
                }
            });
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = msg.arg1;
            setTag(false);
            setProgress(progress);
            setTag(true);
        }
    };

    public void pauseTracking(){

    }

    public void resumeTracking(){

    }
}
