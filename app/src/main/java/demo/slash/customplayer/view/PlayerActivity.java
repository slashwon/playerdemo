package demo.slash.customplayer.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import demo.slash.customplayer.R;
import demo.slash.customplayer.player.Controller;

public class PlayerActivity extends Activity {

    private VideoSurfaceView videoView;
    private GestureDetector mGestureDetector;
    private View mControllView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_player);
        videoView = (VideoSurfaceView) findViewById(R.id.videoView);
        mControllView = findViewById(R.id.layout_controll);

        initGesture();
    }

    private void initGesture() {
        mGestureDetector = new GestureDetector(this, new Controller(videoView,mControllView));
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
            videoView.initPlayer();
            videoView.setPath(path);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
