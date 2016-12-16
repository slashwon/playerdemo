package demo.slash.customplayer.utils;

import java.io.File;

import demo.slash.customplayer.bean.VideoItem;

/**
 * Created by Administrator on 2016/12/11 0011.
 */

public class CommonUtils {

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
            return new VideoItem(name,path,date,0,size);
        }
        return null;
    }

}
