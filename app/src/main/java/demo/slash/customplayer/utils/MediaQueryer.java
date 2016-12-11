package demo.slash.customplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import demo.slash.customplayer.MainActivity;
import demo.slash.customplayer.bean.VideoItem;

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

    public List<VideoItem> getVideos(Context ctx){
        Logger.D(MainActivity.TAG,"get videos");
        List<VideoItem> videoList = new ArrayList<>();

        String[] projections = new String[]{
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DATE_ADDED,
                MediaStore.Video.VideoColumns.DURATION
        };
        Cursor cursor = ctx.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projections, null, null, MediaStore.Video.VideoColumns.DATE_ADDED);

        if(null==cursor){
            Logger.D(MainActivity.TAG,"cursor is null");
            return videoList;
        }

        while(cursor.moveToNext()){
            String displayName = getCursorString(cursor, MediaStore.Video.VideoColumns.DISPLAY_NAME);
            String path = getCursorString(cursor, MediaStore.Video.VideoColumns.DATA);
            long dateAdded = getCursorLong(cursor, MediaStore.Video.VideoColumns.DATE_ADDED);
            Long duration = getCursorLong(cursor, MediaStore.Video.VideoColumns.DURATION);

            VideoItem videoItem = new VideoItem(displayName, path, dateAdded, duration);
            videoList.add(videoItem);

            Log.d(MainActivity.TAG,videoItem.getPath());
        }
        cursor.close();
        Logger.D(MainActivity.TAG,"Video list loaded.");
        return videoList;
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

}
