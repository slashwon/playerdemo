package demo.slash.customplayer.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import demo.slash.customplayer.R;
import demo.slash.customplayer.adapter.VideoAdapter;
import demo.slash.customplayer.bean.ObserverEvent;
import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.data.MediaQueryer;
import demo.slash.customplayer.data.database.DbOperator;
import demo.slash.customplayer.service.ObserverService;
import demo.slash.customplayer.utils.Logger;

public class MainActivity extends Activity implements Runnable {

    public static final String TAG = "VideoPlayer";
    private static final int MSG_QUERY_START = 10;
    private static final int MSG_QUERY_END = 20;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mAdapter.notifyDataSetChanged();
            mPbLoading.setVisibility(View.INVISIBLE);
        }
    };
    private ListView lvVideos;

    List<VideoItem> videoList = new ArrayList<>();
    private VideoAdapter mAdapter;
    private View mPbLoading;
    private Intent mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DbOperator.initDatabase(this);
        getPermissions();
        setContentView(R.layout.activity_main);
        findViews();
        new Thread(this).start();

        serviceStart();

    }

    private void serviceStart() {
        if(!ObserverService.isRunning()) {
            mService = new Intent(this, ObserverService.class);
            startService(mService);
        }
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
        lvVideos.setOnItemLongClickListener(mAdapter);

        mPbLoading = findViewById(R.id.pb_loading);
    }


    @Override
    public void run() {
        videoList.addAll( MediaQueryer.instance().getVideos());
        Logger.D(TAG,"size = "+videoList.size());
        mHandler.sendEmptyMessage(MSG_QUERY_END);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        serviceStop();
    }

    private void serviceStop() {
        if(null!=mService){
            stopService(mService);
        }
    }

    public void refresh(View v){
        DbOperator.clear();
        new Thread(this).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMAIN(ObserverEvent event){
        Logger.D(TAG,"onEventMAIN");

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBACKGROUND(){

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventASYNC(ObserverEvent event){
//        ObserverEvent.TYPE type = event.getTYPE();
//        switch (type){
//            case CREATE:
//                CollectionUtils.addToList(videoList,event.getPath());
//                break;
//            case DELETE:
//                CollectionUtils.removeFromList(videoList,event.getPath());
//                break;
//        }
//        mHandler.sendEmptyMessage(MSG_QUERY_END);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventPOSTING(){

    }
}
