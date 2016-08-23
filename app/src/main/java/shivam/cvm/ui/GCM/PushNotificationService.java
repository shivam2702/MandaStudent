package shivam.cvm.ui.GCM;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.List;
import java.util.Random;

import shivam.cvm.MyTest.R;
import shivam.cvm.ui.activity.Login;
import shivam.cvm.ui.activity.MainActivity;

/**
 * Created by Shivam on 10/22/2015.
 */
public class PushNotificationService extends GcmListenerService {
    public static String message;
    public static final int MESSAGE_NOTIFICATION_ID = 1;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        message = data.getString("message");
        createNotification("Manda Student Portal", message);
    }
    // Creates notification based on title and body received

    private void createNotification(String title, String body) {
        if(isActivityRunning(MainActivity.class)) {
            Toast.makeText(getApplicationContext(),body,Toast.LENGTH_SHORT).show();
        }
        else {
            Random random = new Random();
            int m = random.nextInt(9999-1000)+1000;
            Intent intent = new Intent(this,Login.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Context context = getBaseContext();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.noti).setContentTitle(title)
                    .setContentText(body)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);


            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(m, mBuilder.build());
        }

    }

    protected Boolean isActivityRunning(Class activityClass)
    {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }
}
