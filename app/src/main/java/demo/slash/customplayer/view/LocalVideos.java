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
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import demo.slash.customplayer.R;
import demo.slash.customplayer.adapter.Video2Adapter;
import demo.slash.customplayer.bean.EBEvent;
import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.data.MediaQueryer;
import demo.slash.customplayer.data.database.DbOperator;
import demo.slash.customplayer.utils.CommonUtils;
import demo.slash.customplayer.utils.Logger;

public class LocalVideos extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "VideoPlayer";
    public static final int MSG_QUERY_START = 10;
    public static final int MSG_QUERY_END = 20;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_QUERY_START:
                    Logger.D(TAG, "query started");
                    videoAdapter.refresh(mHandler);
                    break;
                case MSG_QUERY_END:
                    Logger.D(TAG, "query end");
                    mRefresh.setRefreshing(false);
                    break;
            }
        }
    };
    private ListView mLvVideos;

    List<VideoItem> videoList = new ArrayList<>();

    private Video2Adapter videoAdapter;

    private Intent mService;
    private MediaQueryer.IOnLoadingDoneListener mLoadedListener = new MediaQueryer.IOnLoadingDoneListener() {
        @Override
        public void onLoadingDone(List<VideoItem> l) {
            mHandler.sendEmptyMessage(MSG_QUERY_END);
        }
    };
    private FragmentActivity mActivity;
    private View mRootView;
    private SwipeRefreshLayout mRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.pager_local, null);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mLvVideos = (ListView) mRootView.findViewById(R.id.lv_list);

        // swiperefresh
        mRefresh = (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh_layout_local);
        mRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();

        EventBus.getDefault().register(this);

        videoAdapter = new Video2Adapter(mActivity, R.layout.video_item_layout);
        mLvVideos.setAdapter(videoAdapter);
        mLvVideos.setOnItemClickListener(videoAdapter);
        mLvVideos.setOnItemLongClickListener(videoAdapter);

        DbOperator.initDatabase(mActivity);
        getPermissions();

        mHandler.sendEmptyMessage(MSG_QUERY_START);

    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (mActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMAIN(Object event) {
        Logger.D(TAG, "onEventMAIN");

        if (event instanceof String) {
            switch ((String) event) {
               /* case VideoAdapter.EB_MSG_UPDATE_HEADER:
                    Logger.D(TAG,"receive eb event: "+event);
                    break;*/
                case CommonUtils.SDCARD_UNMOUNTED:
                    Toast.makeText(mActivity, "未检测到SD卡!", Toast.LENGTH_LONG).show();
                    mHandler.sendEmptyMessage(MSG_QUERY_END);
                    break;
                default:
                    // do nothing
                    break;
            }
        }
        if (event instanceof EBEvent.MainEvent) {
            switch (((EBEvent.MainEvent) event).event) {
                case EBEvent.MainEvent.EVENT_MAIN_DEL_DONE:
//                    mAdapter.updateCheckState(false);
//                    updateHeaderView(mRLbrand,mRLoperate);
                    break;
                case EBEvent.MainEvent.EVENT_MAIN_DEL_START:
                    EventBus.getDefault().post(new EBEvent.BackgroundEvent(EBEvent.BackgroundEvent.EVENT_SYNC_DEL));
                    break;
                default:
                    // do nothing
                    break;
            }
            Logger.D(TAG, "receive main event");
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBACKGROUND(EBEvent.BackgroundEvent event) {
        if (TextUtils.equals(event.event, EBEvent.BackgroundEvent.EVENT_SYNC_DEL)) {
            CommonUtils.deleteFiles(videoList);
            EventBus.getDefault().post(new EBEvent.MainEvent(EBEvent.MainEvent.EVENT_MAIN_DEL_DONE));
        }
    }


    @Override
    public void onRefresh() {
        videoAdapter.refresh(mHandler);
    }

}
