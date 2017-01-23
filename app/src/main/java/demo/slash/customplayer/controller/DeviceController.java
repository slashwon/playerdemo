package demo.slash.customplayer.controller;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.WindowManager;

import demo.slash.customplayer.utils.Logger;
import demo.slash.customplayer.view.LocalVideos;

/**
 * Created by whs on 16-12-23.
 */

public class DeviceController {

    private DeviceController(){}

    private static DeviceController mController;

    public static DeviceController instance(){
        if(mController==null){
            synchronized (""){
                if(mController==null){
                    mController = new DeviceController();
                }
            }
        }
        return mController;
    }

    public void adjustVolume(Context context,float touchMove,float maxMove){
        float rate = Math.round(touchMove/maxMove);
        AudioManager admngr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = admngr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currVolume = admngr.getStreamVolume(AudioManager.STREAM_MUSIC);
        int targetVolume = (int) (currVolume + maxVolume*rate);
        targetVolume = targetVolume>maxVolume ? maxVolume : targetVolume;
        admngr.setStreamVolume(AudioManager.STREAM_MUSIC,targetVolume,0);
        Logger.D(LocalVideos.TAG,"adjust volume : max =  "+maxVolume+";curr = "+currVolume+";target = "+targetVolume);
    }

    public void adjustLight(Activity activity,float touchMove,float maxMove){
        float mBrightness
            = activity.getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
        float percent = Math.round(touchMove/maxMove);
        WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        activity.getWindow().setAttributes(lpa);
    }
}
