package mydad.saxxis;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.orm.SugarContext;

import mydad.saxxis.util.ConnectivityReceiver;


/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class DADApp extends Application {

    public static final String TAG = DADApp.class.getSimpleName();
    private static DADApp mInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        mInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    public static synchronized DADApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
