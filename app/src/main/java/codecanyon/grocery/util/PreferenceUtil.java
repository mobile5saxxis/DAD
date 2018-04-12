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
}
