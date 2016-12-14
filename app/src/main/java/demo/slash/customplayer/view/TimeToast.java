package demo.slash.customplayer.view;

import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by PICO-USER on 2016/12/14.
 */
public class TimeToast {

    private static Toast mToast;

    public static void showTimeToast(Context ctx,String time){
        Context context = new WeakReference<>(ctx).get();
        if(mToast==null){
            mToast = Toast.makeText(context,time,Toast.LENGTH_SHORT);
        } else {
            mToast.setText(time);
        }
        mToast.show();
    }
}
