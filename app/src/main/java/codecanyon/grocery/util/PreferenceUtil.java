package codecanyon.grocery.util;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by FAMILY on 14-12-2017.
 */

public class PreferenceUtil {
    public static int getCoupon(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("coupon", 0);
    }

    public static void setCoupon(Context context, int coupon) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("coupon", coupon).apply();
    }

    public static boolean getEnablePushNotification(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("push_notification", false);
    }

    public static void setEnablePushNotification(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("push_notification", true).apply();
    }
}
