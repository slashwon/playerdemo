package demo.slash.customplayer.utils;

import android.os.Environment;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.data.database.DbOperator;
import demo.slash.customplayer.view.MainActivity;

/**
 * Created by Administrator on 2016/12/11 0011.
 */

public class CommonUtils {

    public static final String SDCARD_UNMOUNTED = "sdcard_unmounted";

    public static String convertTimeLong(long time){
        long secTime = time/1000;
        int SEC_MIN= 60;
        int MIN_HOU = 60;

        int sec = (int) (secTime%SEC_MIN);
        int min = (int) (secTime / SEC_MIN);
        if(min<=0){
            return "0:"+sec;
        } else {
            int hou = min / MIN_HOU;
            if(hou<=0){
                return "0:"+min%MIN_HOU+":"+sec;
            } else {
                return hou+":"+min%MIN_HOU+":"+sec;
            }
        }
    }

    public static String displayName(String path){
        return path.substring(path.lastIndexOf("/")+1);
    }

    public static boolean isVideo(String path) {
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

    public static VideoItem fromPath2Bean(String path){
        File f = new File(path);
        if(f.exists()){
            long date = f.lastModified();
            long size = f.length();
            String name = displayName(path);
            return new VideoItem(name,path,date,0,size,0,false);
        }
        return null;
    }

    public static boolean checkSdcard(){
        String state = Environment.getExternalStorageState();
        if(TextUtils.equals(state,Environment.MEDIA_MOUNTED)){
            return true;
        }
        EventBus.getDefault().post(SDCARD_UNMOUNTED);
        return false;
    }

    public static String getExternalStorage(){
        return null;
    }

    public static void deleteFiles(List<VideoItem> videoList) {
        if(videoList==null || videoList.isEmpty()){
            return;
        }
        Iterator<VideoItem> iterator = videoList.iterator();
        while (iterator.hasNext()){
            VideoItem next = iterator.next();
            if(next.isSelected()){
                String path = next.getPath();
                File file = new File(path);
                if(file.exists()){
                    boolean delete = file.delete();
                    Logger.D(MainActivity.TAG,"file delete local ? "+delete);
                }
                boolean d = DbOperator.delete(path);
                Logger.D(MainActivity.TAG,"delete from database ? "+d);
                iterator.remove();
            }
        }
    }
}
