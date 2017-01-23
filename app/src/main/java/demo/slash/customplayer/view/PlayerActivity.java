package demo.slash.customplayer.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import demo.slash.customplayer.R;
import demo.slash.customplayer.controller.GestureController;

public class PlayerActivity extends Activity {

    private VideoSurfaceView mVideoView;
    private GestureController mGestureDetector;
    private View mControllView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_player);
        mVideoView = (VideoSurfaceView) findViewById(R.id.videoView);
        mControllView = findViewById(R.id.layout_controll);

//        mVideoView.setActivity(this);

        initGesture();

    }

    private void initGesture() {
//        mGestureDetector = new GestureDetector(this, new Controller(this,mVideoView,mControllView));
        mGestureDetector = new GestureController(this, mVideoView,mControllView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String path = getIntent().getData().toString();
        if(!TextUtils.isEmpty(path)){
            mVideoView.initPlayer();
//            mVideoView.setPath(path);

            // rtmp support
//            mVideoView.setPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");

            // http
//            mVideoView.setPath("http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8");
            // rtsp
            mVideoView.setPath("rtsp://116.199.127.68/huayu");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVideoView.stopVideo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mVideoView.stopVideo();
    }

}
