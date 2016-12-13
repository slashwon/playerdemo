package demo.slash.customplayer.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import demo.slash.customplayer.R;
import demo.slash.customplayer.adapter.VideoAdapter;
import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.data.MediaQueryer;
import demo.slash.customplayer.data.database.DbOperator;

public class MainActivity extends AppCompatActivity implements Runnable {

    public static final String TAG = "VideoPlayer";
    private static final int MSG_QUERY_START = 10;
    private static final int MSG_QUERY_END = 20;

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
        DbOperator.initDatabase(this);
        getPermissions();
        setContentView(R.layout.activity_main);
        findViews();
        new Thread(this).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        mHandler.sendEmptyMessage(MSG_QUERY_START);
        videoList.addAll(MediaQueryer.instance().getVideos());
        mHandler.sendEmptyMessage(MSG_QUERY_END);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        DbOperator.close();
    }

    public void refresh(View v){
        DbOperator.clear();
        new Thread(this).start();
    }
}
