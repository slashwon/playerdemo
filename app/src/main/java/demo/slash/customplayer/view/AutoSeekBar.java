package demo.slash.customplayer.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.SeekBar;

import demo.slash.customplayer.utils.Logger;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by whs on 16-12-24.
 */

public class AutoSeekBar extends SeekBar {

    private IMediaPlayer mPlayer;
    private long mCurrPosition  = 0;
    private long mDuration = 1;

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

    }

    public void bindPlayer(IMediaPlayer player){
        mPlayer = player;
    }

    public void startTracking(){
        if(null!=mPlayer){
            new Thread(new Runnable() {
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
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
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
