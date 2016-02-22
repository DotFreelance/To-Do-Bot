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
    private TaskListManager taskManager = new TaskListManager(null, "List");

    public void addItemTest() {
        SQLiteDatabase db = taskManager.getTaskListDB().getReadableDatabase();
        taskManager.addTask(null, "Test");
        String countQuery = "SELECT count(*) FROM " + TaskListContract.TaskListItemSchema.COL_NAME_DESCRIPTION;

        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        assert(count > 0);
    }
}
