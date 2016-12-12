package demo.slash.customplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import demo.slash.customplayer.adapter.VideoAdapter;
import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.utils.MediaQueryer;

public class MainActivity extends AppCompatActivity implements Runnable {

    public static final String TAG = "VideoPlayer";
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mAdapter.notifyDataSetChanged();
        }
    };
    private ListView lvVideos;

    List<VideoItem> videoList = new ArrayList<>();
    private VideoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPermissions();

        setContentView(R.layout.activity_main);
        new Thread(this).start();

        findViews();
    }

    private void getPermissions() {
        if(Build.VERSION.SDK_INT>=23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            }
        }
    }

    private void findViews() {
        lvVideos = (ListView) findViewById(R.id.lv_list);
        mAdapter = new VideoAdapter(MainActivity.this, videoList);
        lvVideos.setAdapter(mAdapter);
        lvVideos.setOnItemClickListener(mAdapter);
    }

    @Override
    public void run() {
        videoList.addAll(MediaQueryer.instance().getVideos(this));
        mHandler.sendEmptyMessage(0);
    }
}
