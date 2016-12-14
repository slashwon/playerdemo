package demo.slash.customplayer.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import demo.slash.customplayer.R;

public class GlActivity extends AppCompatActivity {

    private VideoGlView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl);

        String path = getIntent().getData().toString();
        mVideoView = (VideoGlView) findViewById(R.id.video_gl_view);
        mVideoView.initPlayer();
        mVideoView.setPath(path);
        mVideoView.playVideo(path);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.onPause();
    }
}
