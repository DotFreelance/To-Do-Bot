package uofm.software_engineering.group7.to_do_bot.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import uofm.software_engineering.group7.to_do_bot.receiver.AlarmManagerBroadcastReceiver;
import uofm.software_engineering.group7.to_do_bot.receiver.AlarmService;

/**
 * Created by thuongle on 4/3/16.
 * Version 1.0
 */
public class AppUtils {
    private AppUtils() {
    }

    public static Bitmap decodeUri(Context context, Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 220;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Generate a ISO-8601 format timestamp as required by Amazon.
     *
     * @return ISO-8601 format timestamp.
     */
    public static String generateTimeStamp() {
        String timestamp;
        Calendar cal = Calendar.getInstance();
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
        timestamp = dfm.format(cal.getTime());
        return timestamp;
    }

    public static void setAlarmTime(Context context, String alarmTime,String taskName) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy;HH:mm");
        try {
            Date alarm = simpleDateFormat.parse(alarmTime);

            Calendar current = Calendar.getInstance();

            Calendar cal = Calendar.getInstance();
            cal.setTime(alarm);

            if (cal.compareTo(current) <= 0) {
                //The set Date/Time already passed
                Toast.makeText(context, "Invalid Date/Time. Alarm in the past", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
                intent.putExtra("TaskName",taskName);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                Toast.makeText(context, "Alarm set", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
