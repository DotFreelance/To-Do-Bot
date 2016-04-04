package uofm.software_engineering.group7.to_do_bot.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by linxi_000 on 2016-03-16.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    private final static String ONE_TIME = "onetime";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("error", "arrive here");
        PowerManager pm=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock w1=pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"TODOBOT");

        w1.acquire();
        /* Setting the alarm here */
        String taskName=intent.getExtras().getString("TaskName");

        Intent service1=new Intent(context,AlarmService.class);

        service1.putExtra("TaskName",taskName);

        context.startService(service1);

        w1.release();

    }
}
