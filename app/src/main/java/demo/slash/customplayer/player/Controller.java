package demo.slash.customplayer.player;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import demo.slash.customplayer.R;
import demo.slash.customplayer.utils.Logger;
import demo.slash.customplayer.view.MainActivity;
import demo.slash.customplayer.view.VideoSurfaceView;

/**
 * Created by PICO-USER on 2016/12/13.
 */
public class Controller implements GestureDetector.OnGestureListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private final VideoSurfaceView mVideoView;
    private final View mControllView;

    private static final float LIMIT_MOVE_X = 0;
    private static final float LIMIT_MOVE_Y= 50;
    private final Context mContext;
    private AppCompatSeekBar mSeekBar;

    public Controller(Context context,VideoSurfaceView view, View controll){
        mContext = context;
        mVideoView =  view;
        mControllView = controll;
        initComponent(controll);
    }

    private void initComponent(View view) {
        view.findViewById(R.id.ib_controll_play_stop).setOnClickListener(this);
        mSeekBar = (AppCompatSeekBar) view.findViewById(R.id.controll_seek);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        int visibility = mControllView.getVisibility();
        mControllView.setVisibility((visibility==View.VISIBLE) ? View.GONE : View.VISIBLE);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float x1 = e1.getX();
        float x2 = e2.getX();

        mVideoView.fastMove(x2-x1);

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        return false;
    }

    @Override
    public void onClick(View v) {
        System.out.print("=====================on click======================\n");
        switch (v.getId()){
            case R.id.ib_controll_play_stop:
                switchPlayState(v);
                break;
        }
    }

    private void switchPlayState(View v) {
        MediaPlayerWrapper player = mVideoView.getPlayer();
        if(player!=null){
            MediaPlayerWrapper.State state = player.getState();
            if(state== MediaPlayerWrapper.State.START){
                mVideoView.pauseVideo();
                v.setActivated(true);
            } else if(state== MediaPlayerWrapper.State.PAUSE){
                mVideoView.startVideo();
                v.setActivated(false);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int sbWidth = seekBar.getMax();
        Logger.D(MainActivity.TAG,"seek bar width = "+sbWidth);
        float rate = (float) progress/sbWidth;
        mVideoView.fastRateMove(rate);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void updateSeekbarProgress(int progress){
        mSeekBar.setProgress(progress);
    }
}
