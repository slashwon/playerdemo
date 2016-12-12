package demo.slash.customplayer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class PlayerActivity extends Activity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_player);

        String path = getIntent().getData().toString();

        videoView = (VideoView) findViewById(R.id.videoView);

        if(!TextUtils.isEmpty(path)){
            videoView.setVideoURI(Uri.parse(path));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
