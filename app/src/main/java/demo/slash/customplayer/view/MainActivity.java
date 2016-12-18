package demo.slash.customplayer.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import demo.slash.customplayer.R;
import demo.slash.customplayer.adapter.VideoAdapter;
import demo.slash.customplayer.bean.EBEvent;
import demo.slash.customplayer.bean.ObserverEvent;
import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.data.MediaQueryer;
import demo.slash.customplayer.data.database.DbOperator;
import demo.slash.customplayer.service.ObserverService;
import demo.slash.customplayer.utils.CommonUtils;
import demo.slash.customplayer.utils.Logger;

public class MainActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "VideoPlayer";
    private static final int MSG_QUERY_START = 10;
    private static final int MSG_QUERY_END = 20;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_QUERY_START:
                    Logger.D(TAG,"query started");
                    mPbLoading.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                    break;
                case MSG_QUERY_END:
                    Logger.D(TAG,"query end");
                    mPbLoading.setVisibility(View.INVISIBLE);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private ListView mLvVideos;

    List<VideoItem> videoList = new ArrayList<>();
    private VideoAdapter mAdapter;
    private View mPbLoading;
    private Intent mService;
    private MediaQueryer.IOnLoadingDoneListener mLoadedListener = new MediaQueryer.IOnLoadingDoneListener() {
        @Override
        public void onLoadingDone() {
                mHandler.sendEmptyMessage(MSG_QUERY_END);
        }
    };
    private View mRLbrand;
    private View mRLoperate;
    private CheckBox mCBAll;

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
        serviceStart();

        mHandler.sendEmptyMessage(MSG_QUERY_START);
        MediaQueryer.instance().syncLoadVideos(false,videoList,mLoadedListener);
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
        mRLbrand = findViewById(R.id.rl_main_brand);
        mRLoperate = findViewById(R.id.rl_main_operate);
        findViewById(R.id.ib_main_header_del).setOnClickListener(this);
        mCBAll = (CheckBox) findViewById(R.id.cb_main_header_all);
        mCBAll.setOnCheckedChangeListener(this);
        mLvVideos = (ListView) findViewById(R.id.lv_list);
        mAdapter = new VideoAdapter(MainActivity.this, videoList);
        mLvVideos.setAdapter(mAdapter);
        mLvVideos.setOnItemClickListener(mAdapter);
        mLvVideos.setOnItemLongClickListener(mAdapter);

        mPbLoading = findViewById(R.id.pb_loading);

        findViewById(R.id.ib_main_refresh).setOnClickListener(this);

        updateHeaderView(mRLoperate);
    }


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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMAIN(Object event){
        Logger.D(TAG,"onEventMAIN");

        if(event instanceof String){
            if(TextUtils.equals(VideoAdapter.EB_MSG_UPDATE_HEADER,(String)event)){
                Logger.D(TAG,"receive eb event: "+event);
                updateHeaderView(mRLoperate,mRLbrand);
            }
            if(TextUtils.equals(CommonUtils.SDCARD_UNMOUNTED,(String)event)){
                Toast.makeText(this,"未检测到SD卡!",Toast.LENGTH_LONG).show();
                mHandler.sendEmptyMessage(MSG_QUERY_END);
            }
        }
        if(event instanceof EBEvent.MainEvent){
            Logger.D(TAG,"receive main event");
            if(TextUtils.equals(((EBEvent.MainEvent) event).event, EBEvent.MainEvent.EVENT_MAIN_DEL_DONE)){
                mAdapter.updateCheckState(false);
                updateHeaderView(mRLbrand,mRLoperate);
            }
            if(TextUtils.equals(((EBEvent.MainEvent) event).event, EBEvent.MainEvent.EVENT_MAIN_DEL_START)){
                EventBus.getDefault().post(new EBEvent.BackgroundEvent(EBEvent.BackgroundEvent.EVENT_SYNC_DEL));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBACKGROUND(EBEvent.BackgroundEvent event){
        if(TextUtils.equals(event.event, EBEvent.BackgroundEvent.EVENT_SYNC_DEL)){
            CommonUtils.deleteFiles(videoList);
            EventBus.getDefault().post(new EBEvent.MainEvent(EBEvent.MainEvent.EVENT_MAIN_DEL_DONE));
        }
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

    public void updateHeaderView(View...views){
        if(views==null || views.length==0){
            return;
        }
        for (View v :
                views) {
            if (v.getVisibility() == View.VISIBLE){
                v.setVisibility(View.INVISIBLE);
            } else if(v.getVisibility() == View.INVISIBLE){
                v.setVisibility(View.VISIBLE);
            }
        }
        mLvVideos.setItemsCanFocus(mRLbrand.getVisibility()==View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if(mRLbrand.getVisibility()==View.VISIBLE) {
            super.onBackPressed();
        } else {
            updateHeaderView(mRLbrand,mRLoperate);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_main_header_del){
            EventBus.getDefault().post(new EBEvent.MainEvent(EBEvent.MainEvent.EVENT_MAIN_DEL_START));
        }
        if(v.getId() == R.id.ib_main_refresh){
            mHandler.sendEmptyMessage(MSG_QUERY_START);
            MediaQueryer.instance().syncLoadVideos(true,videoList,mLoadedListener);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId()==R.id.cb_main_header_all){
            Iterator<VideoItem> iterator = videoList.iterator();
            while(iterator.hasNext()){
                VideoItem next = iterator.next();
                next.setSelected(mCBAll.isChecked());
                Logger.D(TAG,"item checked ? "+next.isSelected());
            }
            mAdapter.updateCheckState(mCBAll.isChecked());
            mAdapter.notifyDataSetChanged();
        }
    }
}
