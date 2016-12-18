package demo.slash.customplayer.player;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import demo.slash.customplayer.R;
import demo.slash.customplayer.utils.Logger;
import demo.slash.customplayer.view.MainActivity;
import demo.slash.customplayer.view.PlayerActivity;
import demo.slash.customplayer.view.VideoSurfaceView;

/**
 * Created by Administrator on 2016/12/17 0017.
 */

public class GestureController extends GestureDetector {

    public GestureController(Context context, OnGestureListener listener) {
        super(context, listener);
    }

    public GestureController(Context context,VideoSurfaceView vsv,View cv){
        this(context,new Controller(context,vsv,cv));
    }

    public static class Controller implements OnGestureListener,View.OnClickListener,SeekBar.OnSeekBarChangeListener{
        private final VideoSurfaceView mVideoView;
        private final View mControllView;

        private static final float LIMIT_MOVE_X = 300;
        private static final float LIMIT_MOVE_Y= 300;
        private final Context mContext;
        private final int mScreenW;
        private final int mScreenH;
        private AppCompatSeekBar mSeekBar;

        public Controller(Context context,VideoSurfaceView view, View controll){
            mContext = context;
            Display dd = ((PlayerActivity) context).getWindowManager().getDefaultDisplay();
            mScreenW = dd.getWidth();
            mScreenH = dd.getHeight();
            Logger.D(MainActivity.TAG,"screen width = "+mScreenW+";screen height = "+mScreenH);
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
            boolean visible = mControllView.getVisibility() == View.VISIBLE;
            mControllView.setVisibility(visible ? View.GONE : View.VISIBLE);
            if(!visible){
//                updateSeekbarProgress(0);
            }

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float dx = e2.getX()-e1.getX();
            float dy = e2.getY() - e1.getY();
            if(Math.abs(dx)>=LIMIT_MOVE_X && Math.abs(dx)>Math.abs(dy)){
                mVideoView.fastMove(dx);
                return true;
            } else if(Math.abs(dy)>=LIMIT_MOVE_Y && Math.abs(dx)<Math.abs(dy)){
                if(e1.getX()<mScreenW/2 && e2.getX()<mScreenW/2) {
                    mVideoView.adjustLight();
                } else if(e1.getX()>mScreenW && e2.getY()>mScreenW/2){
                    mVideoView.adjustVolume();
                }
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

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int sbWidth = seekBar.getMax();
            float rate = (float) progress/sbWidth;
            Logger.D(MainActivity.TAG,"rate = "+rate);
            mVideoView.fastRateMove(rate);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        public void updateSeekbarProgress(int progress){
            long total = mVideoView.getPlayer().getDuration();
            long currPos = mVideoView.getPlayer().getPlayer().getCurrentPosition();
            Logger.D(MainActivity.TAG,"total = "+total+";curr = "+currPos);
            long l = currPos * mSeekBar.getMax() / total;
            Logger.D(MainActivity.TAG,"progress = "+l);
            mSeekBar.setProgress((int) (currPos*100/total));
        }

    }

}
