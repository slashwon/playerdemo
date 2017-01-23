package demo.slash.customplayer.data;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.data.database.DbOperator;
import demo.slash.customplayer.utils.CommonUtils;
import demo.slash.customplayer.utils.Logger;
import demo.slash.customplayer.view.LocalVideos;

public class MediaQueryer {

    private static MediaQueryer mQuery;
    private final ExecutorService mThread;
    private boolean mIsLoading = false;

    private MediaQueryer(){
        mThread = Executors.newFixedThreadPool(1);
    }

    public static MediaQueryer instance()
    {
        if(null==mQuery)
        {
                mQuery = new MediaQueryer();
                Log.d(LocalVideos.TAG,"MediaQuerier has been created.");

        }
        return mQuery;
    }

    public void syncLoadVideos(final Context c,final boolean reload, final List<VideoItem> list, final IOnLoadingDoneListener listener){
        if(!CommonUtils.checkSdcard()){
            return;
        }
        if(mIsLoading){
            return;
        }
        mThread.execute(new Runnable() {
            @Override
            public void run() {
                mIsLoading = true;
                if(reload){
                    DbOperator.clear();
                }
                List<VideoItem> l = DbOperator.query();
                if(null==l || l.size()==0){
                    l = eachAll(c);
                }
                list.clear();
                list.addAll(l);
                mIsLoading = false;
                if(null!=listener){
                    listener.onLoadingDone();
                }
            }
        });
    }

    private String getCursorString(Cursor c, String col){
        if(null!=c){
            return c.getString(c.getColumnIndex(col));
        }
        return null;
    }

    private Long getCursorLong(Cursor c,String col){
        if(null!=c){
            return c.getLong(c.getColumnIndex(col));
        }
        return null;
    }

    public List<VideoItem> eachAll(Context context){
        List<VideoItem> videoItems = new ArrayList<>();
        String externalStorage = CommonUtils.getExternalStorage(context);
        if(!TextUtils.isEmpty(externalStorage)){
            File externRoot = new File(externalStorage);
            each(videoItems,externRoot);
        }
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        each(videoItems,root);
        Logger.D(LocalVideos.TAG,"list size = "+videoItems.size());
        return videoItems;
    }

    private void each(List<VideoItem> list,File root) {
        File[] subFiles = root.listFiles();
        if(subFiles!=null) {
            for (File f :
                    subFiles) {
                if (f.isDirectory()) {
                    each(list, f);
                } else {
                    String path = f.getAbsolutePath();
                    Logger.D(LocalVideos.TAG, "path = " + path);
                    if (CommonUtils.isVideo(path)) {
                        VideoItem videoItem = CommonUtils.fromPath2Bean(path);
                        if (videoItem != null) {
                            list.add(videoItem);
                            if (!DbOperator.exist(path)) {
                                DbOperator.insert(videoItem);
                            }
                        }
                    }
                }
            }
        }
    }

    public interface IOnLoadingDoneListener{
        void onLoadingDone();
    }
}
