package codecanyon.grocery;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import codecanyon.grocery.util.ConnectivityReceiver;


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
        mInstance = this;
    }

    public static synchronized DADApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
