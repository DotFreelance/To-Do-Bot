package uofm.software_engineering.group7.to_do_bot.test.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.db.CategoryContract;
import uofm.software_engineering.group7.to_do_bot.db.DbHelper;
import uofm.software_engineering.group7.to_do_bot.db.TaskListContract;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DbHelperTest {
    private DbHelper db;
    private SQLiteDatabase sqlite;

    @Before
    public void setUp() {
        getTargetContext().deleteDatabase(DbHelper.DATABASE_NAME);
        db = new DbHelper(getTargetContext());
        sqlite = db.getWritableDatabase();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void testDbVersion() {
        assertEquals(4, sqlite.getVersion());
    }

    @Test
    public void testDbName() {
        assertEquals(DbHelper.DATABASE_NAME, db.getDatabaseName());
    }

    @Test
    public void testDbSchema() {
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + CategoryContract.TABLE_NAME, null);
        assertEquals(3, cursor.getColumnCount());
        assertEquals(CategoryContract.CategoryItemSchema._ID, cursor.getColumnName(0));
        assertEquals(CategoryContract.CategoryItemSchema.COL_CATEGORY_NAME, cursor.getColumnName(1));
        assertEquals(CategoryContract.CategoryItemSchema.COL_CATEGORY_DESCRIPTION, cursor.getColumnName(2));
        cursor.close();

        Cursor cursor1 = sqlite.rawQuery("SELECT * FROM " + TaskListContract.TABLE_NAME, null);
        assertEquals(8, cursor1.getColumnCount());
        assertEquals(TaskListContract.TaskListItemSchema._ID, cursor1.getColumnName(0));
        assertEquals(TaskListContract.TaskListItemSchema.COL_NAME_CREATED, cursor1.getColumnName(1));
        assertEquals(TaskListContract.TaskListItemSchema.COL_NAME_TASK_NAME, cursor1.getColumnName(2));
        assertEquals(TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY, cursor1.getColumnName(3));
        assertEquals(TaskListContract.TaskListItemSchema.COL_NAME_IMAGE_DESCRIPTION, cursor1.getColumnName(4));
        assertEquals(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, cursor1.getColumnName(5));
        assertEquals(TaskListContract.TaskListItemSchema.COL_NAME_ALARM, cursor1.getColumnName(6));
        assertEquals(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, cursor1.getColumnName(7));
        cursor1.close();
    }

    @Test
    public void testDbInitialization() {
        String[] categories = getTargetContext().getResources().getStringArray(R.array.taskCategories);
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + CategoryContract.TABLE_NAME, null);
        assertEquals(categories.length, cursor.getCount());
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(CategoryContract.CategoryItemSchema._ID));
                String name = cursor.getString(cursor.getColumnIndex(CategoryContract.CategoryItemSchema.COL_CATEGORY_NAME));
                String description = cursor.getString(cursor.getColumnIndex(CategoryContract.CategoryItemSchema.COL_CATEGORY_DESCRIPTION));
                assertEquals(count + 1, id);
                assertEquals(categories[count], name);
                assertEquals(null, description);
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        Cursor cursor1 = sqlite.rawQuery("SELECT * FROM " + TaskListContract.TABLE_NAME, null);
        assertEquals(0, cursor1.getCount());
    }
}
