package demo.slash.customplayer;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

public class App extends Application {

    private HttpProxyCacheServer mCacheProxy = null;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return (app.mCacheProxy == null) ? (app.mCacheProxy = app.newProxy()) : app.mCacheProxy ;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this) ;
    }
}
