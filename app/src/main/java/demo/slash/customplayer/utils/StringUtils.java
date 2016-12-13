package demo.slash.customplayer.utils;

/**
 * Created by Administrator on 2016/12/11 0011.
 */

public class StringUtils {

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
}
