package demo.slash.customplayer.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.utils.Logger;
import demo.slash.customplayer.utils.StringUtils;
import demo.slash.customplayer.view.MainActivity;

/**
 * Created by PICO-USER on 2016/12/13.
 */
public class DbOperator {

    private static VideoDbHelper sDbHelper;
    private static SQLiteDatabase sDatabase;
//    private static int _id = 0;

    public static void initDatabase(Context ctx){
        sDbHelper = new VideoDbHelper(new SoftReference<>(ctx).get());
        sDatabase = sDbHelper.getWritableDatabase();
    }

    public static void insert(VideoItem item){
        ContentValues cv = new ContentValues();
        cv.put(VideoDbHelper.COL_NAME, StringUtils.displayName(item.getPath()));
        cv.put(VideoDbHelper.COL_PATH,item.getPath());
        cv.put(VideoDbHelper.COL_DATE,item.getDateAdded());
        cv.put(VideoDbHelper.COL_DURATION,item.getDuration());
        cv.put(VideoDbHelper.COL_SIZE,item.getSize());
        sDatabase.insertWithOnConflict(VideoDbHelper.DB_NAME,null,cv,SQLiteDatabase.CONFLICT_IGNORE);
    }

    public static boolean delete(VideoItem item){
        String path = item.getPath();
        int delete = sDatabase.delete(VideoDbHelper.DB_NAME, VideoDbHelper.COL_PATH + " = ? ", new String[]{path});
        return delete!=0;
    }

    public static boolean exist(String path){
        String sqlExist = " select * from "+VideoDbHelper.DB_NAME+" where "+VideoDbHelper.COL_PATH+" = ?";
        Cursor cursor = sDatabase.rawQuery(sqlExist, new String[]{path});
        return cursor!=null && cursor.getCount()!=0;
    }

    public static List<VideoItem> query(){
        String sql_query = "select * from "+VideoDbHelper.DB_NAME;
        Cursor cursor = sDatabase.rawQuery(sql_query, null);
        if(cursor==null || cursor.getCount()==0){
            return null;
        }
        List<VideoItem> list = new ArrayList<>();
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(VideoDbHelper.COL_NAME));
            String path = cursor.getString(cursor.getColumnIndex(VideoDbHelper.COL_PATH));
            long date = cursor.getLong(cursor.getColumnIndex(VideoDbHelper.COL_DATE));
            long size = cursor.getLong(cursor.getColumnIndex(VideoDbHelper.COL_SIZE));
            long duration = cursor.getLong(cursor.getColumnIndex(VideoDbHelper.COL_DURATION));
            VideoItem item = new VideoItem(name, path, date, duration, size);
            list.add(item);
        }
        cursor.close();
        return list;
    }

    public static void clear(){
        try {
            String sql_clear = "delete from "+VideoDbHelper.DB_NAME;
            sDatabase.execSQL(sql_clear);
        } catch (Exception e){
            Logger.E(MainActivity.TAG,"database clear fail");
        }
    }

    public static void close(){
        if(null!=sDatabase){
            sDatabase.close();
        }
    }
}
