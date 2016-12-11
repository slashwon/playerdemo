package demo.slash.customplayer.utils;

import android.util.Log;

public class Logger {

    private static final boolean DEBUG = true;

    public static void I(String tag,String msg){
        if(DEBUG){
            Log.i(tag,msg);
        }
    }

    public static void D(String tag,String msg){
        if(DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void W(String tag,String msg){
        if(DEBUG){
            Log.w(tag,msg);
        }
    }

    public static void E(String tag,String msg){
        if(DEBUG){
            Log.e(tag,msg);
        }
    }

}
