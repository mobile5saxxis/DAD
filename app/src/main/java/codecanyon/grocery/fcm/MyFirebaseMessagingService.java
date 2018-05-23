package codecanyon.grocery.fcm;

/**
 * Created by subhashsanghani on 12/21/16.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import codecanyon.grocery.R;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.activities.ProductDetailsActivity;
import codecanyon.grocery.activities.SplashActivity;
import codecanyon.grocery.models.ProductDetailResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            JSONObject object = new JSONObject(remoteMessage.getData());
            try {
                String pid = null;

                try {
                    pid = object.getString("pid");
                } catch (Exception ignore) {
                }

                sendNotification(pid, object.getString("message"), object.getString("title"), object.getString("image"), object.getString("created_at"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    /*private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_user)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }
    */

    private void sendNotification(final String pid, final String message, final String title, final String imageUrl, final String created_at) {
        if (pid == null || pid.equals("0")) {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                showImageNotification(imageUrl, title, message, created_at, pendingIntent);
            } else {
                simpleteNotification(title, message, created_at, pendingIntent);
            }
        } else {
            RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
            service.getProductDetails(pid).enqueue(new Callback<ProductDetailResponse>() {
                @Override
                public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ProductDetailResponse productDetailResponse = response.body();

                        Intent backIntent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
                        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        String value = new Gson().toJson(productDetailResponse.getData());
                        Intent intent = new Intent(MyFirebaseMessagingService.this, ProductDetailsActivity.class);
                        intent.putExtra(ProductDetailsActivity.PRODUCT, value);
                        intent.putExtra(ProductDetailsActivity.CONTENT, "0");
                        final PendingIntent pendingIntent = PendingIntent.getActivities(MyFirebaseMessagingService.this, 0, new Intent[]{backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);

                        if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                            showImageNotification(imageUrl, title, message, created_at, pendingIntent);
                        } else {
                            simpleteNotification(title, message, created_at, pendingIntent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductDetailResponse> call, Throwable t) {

                }
            });


        }
    }

    private void simpleteNotification(String title, String message, String timeStamp, PendingIntent pendingIntent) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrate = {0, 100, 200, 300};

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setVibrate(vibrate);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            notificationBuilder.setSmallIcon(R.drawable.ic_new_notification);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_logonew);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showBigNotification(Bitmap bitmap, String title, String message, String timeStamp, PendingIntent resultPendingIntent) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrate = {0, 100, 200, 300};

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setContentTitle(message)
                .setContentText(title)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(bitmap))
                .setContentIntent(resultPendingIntent)
                .setSound(uri)
                .setVibrate(vibrate);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            notificationBuilder.setSmallIcon(R.drawable.ic_new_notification);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_logonew);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
    }

    /**
     * Downloading push notification iv_category before displaying it in
     * the notification tray
     */
    public void showImageNotification(final String imageUrl, final String title, final String message, final String created_at, final PendingIntent pendingIntent) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(this).asBitmap()
                    .load(imageUrl)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            showBigNotification(resource, title, message, created_at, pendingIntent);
                        }
                    });
        }
    }
}