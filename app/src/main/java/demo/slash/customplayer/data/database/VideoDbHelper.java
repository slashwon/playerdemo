package demo.slash.customplayer.data.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PICO-USER on 2016/12/13.
 */
public class VideoDbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "cus_player_db";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_PATH = "path";
    public static final String COL_DATE = "date";
    public static final String COL_SIZE = "size";
    public static final String COL_DURATION = "duration";

    private static final String SQL_CREATE = "create table "+DB_NAME+" ( "+COL_ID+" int auto increment unique, "+
            COL_NAME+" varchar(100) ,"+
            COL_PATH+" varchar(200), " +
            COL_DATE+" long," +
            COL_SIZE+" long," +
            COL_DURATION+" long )";

    public VideoDbHelper(Context ctx){
        super(ctx,DB_NAME,null,DB_VERSION);
    }

    public VideoDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public VideoDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.print(SQL_CREATE);
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
