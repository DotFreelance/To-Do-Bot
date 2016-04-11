package test.model;

import android.graphics.Bitmap;
import android.os.Parcel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uofm.software_engineering.group7.to_do_bot.model.Task;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Paul Jarrow on 2016-04-09.
 */
public class TaskTest {
    Task testTask;
    Bitmap testBitmap;

    @Before
    public void setup(){
        // Classes need to be mocked
        testBitmap = Mockito.mock(Bitmap.class);
        // A default Parcel is needed to be mocked. How? I don't know.
        Parcel defaultParcel = Parcel.obtain();

        // Create the values for the Parcel. May want better ones.
//        defaultParcel.writeLong(80000000);
//        defaultParcel.writeString("");
//        defaultParcel.writeString("TaskName");
//        defaultParcel.writeParcelable(testBitmap, 0);
//        defaultParcel.writeLong(2);
//        defaultParcel.writeByte((byte) 1);
//        defaultParcel.writeString("");
//        defaultParcel.writeInt(1);

        // Finally, create the task
//         testTask = Task.CREATOR.createFromParcel(defaultParcel);
    }

    @Test
    public void firstTest(){
//         assertNotNull(testTask);
    }

    @After
    public void teardown(){
        testTask = null;
        testBitmap = null;
    }
}
