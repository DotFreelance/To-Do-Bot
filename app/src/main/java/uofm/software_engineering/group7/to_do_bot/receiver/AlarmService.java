package uofm.software_engineering.group7.to_do_bot.receiver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.ui.MainActivity;

/**
 * Created by linxi_000 on 2016-04-04.
 */
public class AlarmService extends Service {
    MediaPlayer rings;

    public IBinder onBind(Intent arg0){
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int onStartCommand(Intent intent,int flags,int startId){
        String taskName=intent.getExtras().getString("TaskName");
        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent1=new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent1, 0);

        Notification notification=new Notification.Builder(this)
                .setContentTitle("Are you ready for:")
                .setContentText(taskName)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_business)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0,notification);

        rings=MediaPlayer.create(this,R.raw.q);
        rings.start();

        return START_NOT_STICKY;
    }
}
