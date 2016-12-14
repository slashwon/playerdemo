package demo.slash.customplayer.data;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.data.database.DbOperator;
import demo.slash.customplayer.view.MainActivity;

public class MediaQueryer {

    private static MediaQueryer mQuery;

    private MediaQueryer(){}

    public static MediaQueryer instance()
    {
        if(null==mQuery)
        {
                mQuery = new MediaQueryer();
                Log.d(MainActivity.TAG,"MediaQuerier has been created.");

        }
        return mQuery;
    }

    public List<VideoItem> getVideos(){
        List<VideoItem> l = DbOperator.query();
        if(null==l){
            l = eachAll();
        }
        Collections.sort(l, new Comparator<VideoItem>() {
            @Override
            public int compare(VideoItem lhs, VideoItem rhs) {
                return (int) (lhs.getDateAdded()-rhs.getDateAdded());
            }
        });
        return l;
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

    public List<VideoItem> eachAll(){
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        List<VideoItem> videoItems = new ArrayList<>();
        each(videoItems,root);
        return videoItems;
    }

    private void each(List<VideoItem> list,File root) {
        File[] subFiles = root.listFiles();
        for (File f :
                subFiles) {
            if (f.isDirectory()){
                each(list,f);
            } else {
                String path = f.getAbsolutePath();
                if(isVideo(path)){
                    long date = f.lastModified();
                    long size = f.length();
                    VideoItem videoItem = new VideoItem(path.substring(path.lastIndexOf("/") + 1), path, date, 0,size);

                    list.add(videoItem);
                    if(!DbOperator.exist(path)) {
                        DbOperator.insert(videoItem);
                    }
                }
            }
        }
    }

    private boolean isVideo(String path) {
        String ext = path.substring(path.lastIndexOf(".")+1);
        if(ext.equalsIgnoreCase("mp4") ||
                ext.equalsIgnoreCase("rm") ||
                ext.equalsIgnoreCase("avi") ||
                ext.equalsIgnoreCase("3gp") ||
                ext.equalsIgnoreCase("rmvb") ||
                ext.equalsIgnoreCase("mkv")){
            return true;
        }
        return false;
    }

}
