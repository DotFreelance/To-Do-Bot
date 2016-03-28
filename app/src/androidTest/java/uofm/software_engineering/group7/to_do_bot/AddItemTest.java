package uofm.software_engineering.group7.to_do_bot;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import junit.framework.TestCase;

import uofm.software_engineering.group7.to_do_bot.models.TaskListManager;
import uofm.software_engineering.group7.to_do_bot.services.TaskListContract;

/**
 * Created by Matt on 2016-02-22.
 */
public class AddItemTest extends TestCase {
    private final int AMOUNT_OF_ITEMS = 10000;

    public void addItemTest00(TaskListManager taskListManager) {
        SQLiteDatabase db = taskListManager.getTaskListDB().getReadableDatabase();
        taskListManager.addTask("Description", 0, null);
        String countQuery = "SELECT count(*) FROM " + TaskListContract.TaskListItemSchema.COL_NAME_DESCRIPTION;

        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        assert(count > 0);
    }

    public void addItemTest01(TaskListManager taskListManager) {
        SQLiteDatabase db = taskListManager.getTaskListDB().getReadableDatabase();

        for(int i = 0; i < AMOUNT_OF_ITEMS; i++) {
            taskListManager.addTask("Description", 0, null);
        }

        String countQuery = "SELECT count(*) FROM " + TaskListContract.TaskListItemSchema.COL_NAME_DESCRIPTION;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        assert(count > 0);
    }
}
