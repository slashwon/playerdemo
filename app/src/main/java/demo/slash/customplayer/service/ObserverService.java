package demo.slash.customplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import demo.slash.customplayer.bean.ObserverEvent;
import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.data.database.DbOperator;
import demo.slash.customplayer.utils.CommonUtils;
import demo.slash.customplayer.utils.Logger;
import demo.slash.customplayer.view.MainActivity;

public class ObserverService extends Service {

    private static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static Set<FileObserver> sObservers = new HashSet<>();
    private Thread mThread;
    private boolean mRunning = false;

    public ObserverService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static boolean isRunning = false;

    public static boolean isRunning() {
        return isRunning;
    }

    public static void setIsRunning(boolean isRunning) {
        ObserverService.isRunning = isRunning;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.D(MainActivity.TAG,"Observer service is running");
        setIsRunning(true);
        registerObservers();

        return super.onStartCommand(intent, flags, startId);
    }

    private void registerObservers() {
        final File root = new File(ROOT);
        mThread = new Thread() {
            public void run() {
                mRunning = true;
                eachObserver(root);
                if(null!=sObservers) {
                    Iterator<FileObserver> iterator = sObservers.iterator();
                    while (iterator.hasNext()) {
                        FileObserver o = iterator.next();
                        o.startWatching();
                    }
                }
                mRunning = false;
            }
        };
        mThread.start();
    }

    private void eachObserver(File root) {
        if(root.isDirectory()){
            VideoDirObserver observer = new VideoDirObserver(root.getAbsolutePath());
            sObservers.add(observer);

            File[] files = root.listFiles();
            if(files!=null && files.length!=0){
                for (File f :
                        files) {
                    eachObserver(f);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setIsRunning(false);

        unregisterObservers();

        Logger.D(MainActivity.TAG,"Observer service is destroy");
    }

    private void unregisterObservers() {
        if(mRunning){
            mThread.interrupt();
        }
        Logger.D(MainActivity.TAG,"interrupted ? "+mThread.isInterrupted());
        if(null!=sObservers) {
            Iterator<FileObserver> iterator = sObservers.iterator();
            while (iterator.hasNext()) {
                FileObserver fo = iterator.next();
                fo.stopWatching();
            }
            sObservers.clear();
        }
        sObservers = null;
    }

    static class VideoDirObserver extends FileObserver{

        private String mRoot;

        public VideoDirObserver(String path) {
            super(path);
            mRoot = path;
        }

        public VideoDirObserver(String path, int mask) {
            super(path, mask);
        }

        @Override
        public void onEvent(int event, String path) {
            path = mRoot+"/"+path;
            File changedFile = new File(path);

            if(!CommonUtils.isVideo(path)){
                return;
            }
            switch (event){
                case FileObserver.CREATE:
                    Logger.D(MainActivity.TAG,"file observer: create "+ path);
                    VideoItem videoItem = CommonUtils.fromPath2Bean(path);
                    if(videoItem!=null) {
                        DbOperator.insert(videoItem);
                        EventBus.getDefault().post(new ObserverEvent(ObserverEvent.TYPE.CREATE,path));
                    }
                    break;
                case FileObserver.DELETE:
                    Logger.D(MainActivity.TAG,"file observer: delete "+ path);
                    DbOperator.delete(path);
                    EventBus.getDefault().post(new ObserverEvent(ObserverEvent.TYPE.DELETE,path));
                    break;
                case FileObserver.MODIFY:
                    Logger.D(MainActivity.TAG,"file observer: modify "+ path);

                    break;
            }
        }
    }
}
