package demo.slash.customplayer.player;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import demo.slash.customplayer.R;
import demo.slash.customplayer.view.VideoSurfaceView;

/**
 * Created by PICO-USER on 2016/12/13.
 */
public class Controller implements GestureDetector.OnGestureListener, View.OnClickListener {

    private final VideoSurfaceView mVideoView;
    private final View mControllView;

    private static final float LIMIT_MOVE_X = 0;
    private static final float LIMIT_MOVE_Y= 50;

    public Controller(VideoSurfaceView view, View controll){
        mVideoView = view;
        mControllView = controll;
        initComponent(controll);
    }

    private void initComponent(View view) {
        view.findViewById(R.id.ib_controll_play_stop).setOnClickListener(this);
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
        float dx = e2.getX() - e1.getX();
        float dy = e2.getY() - e1.getY();
        if(Math.abs(dx) >= LIMIT_MOVE_X && Math.abs(dx)>Math.abs(dy)){
            mVideoView.fastMove(dx);
            return true;
        }
        return false;
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
}
