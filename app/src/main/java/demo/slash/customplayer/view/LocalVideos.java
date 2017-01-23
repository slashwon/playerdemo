package demo.slash.customplayer.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class LocalVideos extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
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
//    private View mRLbrand;
//    private View mRLoperate;
//    private CheckBox mCBAll;
    private FragmentActivity mActivity;
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.pager_local, null);
        return mRootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        mRLbrand = mRootView.findViewById(R.id.rl_main_brand);
//        mRLoperate = mRootView.findViewById(R.id.rl_main_operate);
//        mRootView.findViewById(R.id.ib_main_header_del).setOnClickListener(this);
//        mCBAll = (CheckBox) mRootView.findViewById(R.id.cb_main_header_all);
//        mCBAll.setOnCheckedChangeListener(this);
        mLvVideos = (ListView) mRootView.findViewById(R.id.lv_list);

        mPbLoading = mRootView.findViewById(R.id.pb_loading);

//        mRootView.findViewById(R.id.ib_main_refresh).setOnClickListener(this);
//        updateHeaderView(mRLoperate);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();

        EventBus.getDefault().register(this);

        mAdapter = new VideoAdapter(mActivity, videoList);
        mLvVideos.setAdapter(mAdapter);
        mLvVideos.setOnItemClickListener(mAdapter);
        mLvVideos.setOnItemLongClickListener(mAdapter);

        DbOperator.initDatabase(mActivity);
        getPermissions();

        mHandler.sendEmptyMessage(MSG_QUERY_START);
        MediaQueryer.instance().syncLoadVideos(mActivity,false,videoList,mLoadedListener);
        serviceStart();
    }

    private void serviceStart() {
        if(!ObserverService.isRunning()) {
            mService = new Intent(mActivity, ObserverService.class);
            mActivity.startService(mService);
        }
    }

    private void getPermissions() {
        if(Build.VERSION.SDK_INT>=23) {
            if (mActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
//        serviceStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMAIN(Object event){
        Logger.D(TAG,"onEventMAIN");

        if(event instanceof String){
            switch ((String)event){
                case VideoAdapter.EB_MSG_UPDATE_HEADER:
                    Logger.D(TAG,"receive eb event: "+event);
//                    updateHeaderView(mRLoperate,mRLbrand);
                    break;
                case CommonUtils.SDCARD_UNMOUNTED:
                    Toast.makeText(mActivity,"未检测到SD卡!",Toast.LENGTH_LONG).show();
                    mHandler.sendEmptyMessage(MSG_QUERY_END);
                    break;
                default:
                    // do nothing
                    break;
            }
        }
        if(event instanceof EBEvent.MainEvent){
            switch (((EBEvent.MainEvent) event).event){
                case EBEvent.MainEvent.EVENT_MAIN_DEL_DONE:
                    mAdapter.updateCheckState(false);
//                    updateHeaderView(mRLbrand,mRLoperate);
                    break;
                case EBEvent.MainEvent.EVENT_MAIN_DEL_START:
                    EventBus.getDefault().post(new EBEvent.BackgroundEvent(EBEvent.BackgroundEvent.EVENT_SYNC_DEL));
                    break;
                default:
                    // do nothing
                    break;
            }
            Logger.D(TAG,"receive main event");
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

    /*public void updateHeaderView(View...views){
        if(views==null || views.length==0){
            return;
        }
        for (View v :
                views) {
            int visibility = v.getVisibility();
            v.setVisibility((visibility == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE);
        }
        mLvVideos.setItemsCanFocus(mRLbrand.getVisibility()==View.VISIBLE);
    }*/

   /* public void onBackPressed() {
        if(mRLbrand.getVisibility()==View.VISIBLE) {
//            super.onBackPressed();
        } else {
            updateHeaderView(mRLbrand,mRLoperate);
        }
    }*/

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_main_header_del){
            EventBus.getDefault().post(new EBEvent.MainEvent(EBEvent.MainEvent.EVENT_MAIN_DEL_START));
        }
        if(v.getId() == R.id.ib_main_refresh){
            mHandler.sendEmptyMessage(MSG_QUERY_START);
            MediaQueryer.instance().syncLoadVideos(mActivity,true,videoList,mLoadedListener);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId()==R.id.cb_main_header_all){
            Iterator<VideoItem> iterator = videoList.iterator();
            while(iterator.hasNext()){
                VideoItem next = iterator.next();
//                next.setSelected(mCBAll.isChecked());
                Logger.D(TAG,"item checked ? "+next.isSelected());
            }
//            mAdapter.updateCheckState(mCBAll.isChecked());
            mAdapter.notifyDataSetChanged();
        }
    }
}
