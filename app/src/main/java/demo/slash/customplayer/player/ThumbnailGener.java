package demo.slash.customplayer.player;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import demo.slash.customplayer.view.MainActivity;
import demo.slash.customplayer.utils.Logger;

/**
 * Created by PICO-USER on 2016/12/12.
 */
public class ThumbnailGener{

    private static final int DST_WIDTH = 300;
    private static final int DST_HEIGHT = DST_WIDTH*9/16;
    private final MediaMetadataRetriever mRetriever;
    private final IOnThumbnailCreated mCallback;
    private ExecutorService mExecutors;
    private ThreadLocal<String> mThreadLocal = new ThreadLocal<>();
    private String mCurrPath;
    private ImageView mIv;

    public ThumbnailGener(IOnThumbnailCreated callback){
        if(null == mExecutors) {
            mExecutors = Executors.newFixedThreadPool(3);
        }
        mRetriever = new MediaMetadataRetriever();
        mCallback = callback;
    }

    public void showThumbnail(ImageView iv,String videoPath){
        mIv = iv;
        mCurrPath = videoPath;
        mExecutors.execute(new Runnable() {
            @Override
            public void run() {
                taskRun();
            }
        });
    }

    private void taskRun() {
        mThreadLocal.set(mCurrPath);
        mCurrPath = mThreadLocal.get();
        Logger.D("VideoPlayer","curr path  = "+mCurrPath);
        if(TextUtils.isEmpty(mCurrPath)){
            return;
        }
        mRetriever.setDataSource(mCurrPath);
        Bitmap bitmap = mRetriever.getFrameAtTime(100, MediaMetadataRetriever.OPTION_CLOSEST);
        if(null==bitmap){
            Logger.D(MainActivity.TAG,"create thumbnail failed");
            return ;
        } else {
            Bitmap thumb = Bitmap.createScaledBitmap(bitmap, DST_WIDTH, DST_HEIGHT, false);
            Logger.D(MainActivity.TAG,"video thumbnail created");
            Message msg = mHandler.obtainMessage(0);
            msg.obj = thumb;
            mHandler.sendMessage(msg);
            bitmap.recycle();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bm = (Bitmap) msg.obj;
            mIv.setImageBitmap(bm);
        }
    };

    public interface IOnThumbnailCreated{
        void onThumbnailCreated(ImageView i,Bitmap t);
    }
}
