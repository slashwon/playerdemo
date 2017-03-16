package demo.slash.customplayer.adapter;

import android.view.View;

import java.util.LinkedHashMap;

import demo.slash.customplayer.utils.Logger;

/**
 * Created by PICO-USER on 2017/3/15.
 */
public class GlobalHolder {

    private final View mRootView;

    private static final String TAG = "GlobalHolder";

    private LinkedHashMap<Integer,View> viewMap = null;

    public GlobalHolder(View rootView) {
        mRootView = rootView;
        viewMap = new LinkedHashMap<>();
    }

    public void save(int id) {
        View view = mRootView.findViewById(id);
        if(view!=null) {
            viewMap.put(id,view);
        } else {
            Logger.D(TAG,"找不到id");
        }
    }

    /*return the view object ,maybe null*/
    public View get(int id) {
        View view = viewMap.get(id);
        return view;
    }
}
