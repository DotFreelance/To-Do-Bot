package uofm.software_engineering.group7.to_do_bot.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uofm.software_engineering.group7.to_do_bot.model.Task;
import uofm.software_engineering.group7.to_do_bot.utils.AppUtils;

public class TaskModelImpl implements TaskModel {

    private final DbHelper mDbHelper;

    public TaskModelImpl(DbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    @Override
    public ArrayList<Task> getAll() {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TaskListContract.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(TaskListContract.TaskListItemSchema._ID));
                    String dateCreated = cursor.getString(cursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_CREATED));
                    String taskName = cursor.getString(cursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_TASK_NAME));
                    long categoryId = cursor.getLong(cursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY));
                    boolean checked = cursor.getInt(cursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED)) == TaskListContract.TaskListItemSchema.CHECKED_TRUE;
                    int priority = cursor.getInt(cursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY));
                    String alarmTime = cursor.getString(cursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_ALARM));
                    byte[] blobIcon = cursor.getBlob(cursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_IMAGE_DESCRIPTION));
                    Bitmap bitmapDescription = null;
                    if (blobIcon != null) {
                        bitmapDescription = BitmapFactory.decodeByteArray(blobIcon, 0, blobIcon.length);
                    }

                    Task task = new Task();
                    task.setId(id);
                    task.setName(taskName);
                    task.setDateCreated(dateCreated);
                    task.setCategoryId(categoryId);
                    task.setImageDescription(bitmapDescription);
                    task.setChecked(checked);
                    task.setPriority(priority);
                    task.setAlarmTime(alarmTime);
                    tasks.add(task);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        db.close();
        return tasks;
    }


    @Override
    public Task add(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues dbValues = new ContentValues();
        long newItemID;
        String createdAt = AppUtils.generateTimeStamp();
        // Set the values we need for this entry
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CREATED, createdAt);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_TASK_NAME, task.getName());
        if (task.getImageDescription() != null) {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_IMAGE_DESCRIPTION, AppUtils.getBitmapAsByteArray(task.getImageDescription()));
        }
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY, task.getCategoryId());
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_FALSE);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, task.getPriority());
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_ALARM, task.getAlarmTime());
        // Perform the database insert, returning the _ID primary key value
        newItemID = db.insert(TaskListContract.TABLE_NAME, null, dbValues);
        db.close();

        // Instantiate a new TaskListItem using the new ID we just received from the DB, if we were successful
        if (newItemID != -1) {
            task.setId(newItemID);
        } else {
            return null;
        }

        return task;
    }

    // Retrieves all tasks from the DB for the category
    @Override
    public List<Task> getTasksByCategory(long categoryId) {
        ArrayList<Task> result = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Build the query
        String[] PROJECTION = {"*"};
        String SELECTION = TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY + "=?";
        String[] SELECTION_ARGS = {String.valueOf(categoryId)};
        String SORT_ORDER = TaskListContract.TaskListItemSchema._ID + " ASC";//Changed SORT_ORDER from ID to PRIORITY

        // Send the query
        Cursor readCursor = db.query(TaskListContract.TABLE_NAME, PROJECTION, SELECTION, SELECTION_ARGS, null, null, SORT_ORDER);

        // Go through the cursor to fill the task list
        if (readCursor != null) {
            if (readCursor.moveToFirst()) {
                do {
                    long id = readCursor.getLong(readCursor.getColumnIndex(TaskListContract.TaskListItemSchema._ID));
                    String dateCreated = readCursor.getString(readCursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_CREATED));
                    String taskName = readCursor.getString(readCursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_TASK_NAME));
                    boolean checked = readCursor.getInt(readCursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED)) == TaskListContract.TaskListItemSchema.CHECKED_TRUE;
                    int priority = readCursor.getInt(readCursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY));
                    String alarmTime = readCursor.getString(readCursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_ALARM));
                    byte[] blobIcon = readCursor.getBlob(readCursor.getColumnIndex(TaskListContract.TaskListItemSchema.COL_NAME_IMAGE_DESCRIPTION));
                    Bitmap bitmapDescription = null;
                    if (blobIcon != null) {
                        bitmapDescription = BitmapFactory.decodeByteArray(blobIcon, 0, blobIcon.length);
                    }

                    Task task = new Task();
                    task.setId(id);
                    task.setName(taskName);
                    task.setDateCreated(dateCreated);
                    task.setCategoryId(categoryId);
                    task.setImageDescription(bitmapDescription);
                    task.setChecked(checked);
                    task.setPriority(priority);
                    task.setAlarmTime(alarmTime);
                    result.add(task);
                } while (readCursor.moveToNext());
            }
            readCursor.close();
        }
        //Now that everything is added to the list, we want to sort
        Collections.sort(result);
        db.close();
        return result;
    }

    @Override
    public boolean remove(Task object) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowAffect = db.delete(TaskListContract.TABLE_NAME, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{Long.toString(object.getId())});
        db.close();
        //0 mean remove item false
        return (rowAffect > 0);
    }

    @Override
    public boolean update(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues dbValues = new ContentValues();
        // Set the values we need for this entry
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CREATED, task.getDateCreated());
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_TASK_NAME, task.getName());
        if (task.getImageDescription() != null) {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_IMAGE_DESCRIPTION, AppUtils.getBitmapAsByteArray(task.getImageDescription()));
        } else {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_IMAGE_DESCRIPTION, new byte[0]);
        }
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY, task.getCategoryId());
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_FALSE);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, task.getPriority());
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_ALARM, task.getAlarmTime());
        // Perform the database insert, returning the _ID primary key value
        int row = db.update(TaskListContract.TABLE_NAME, dbValues, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{Long.toString(task.getId())});
        db.close();
        return (row > 0);
    }

    @Override
    public boolean updateTaskDescription(Task task) {
        // Apply the changes to the database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues dbValues = new ContentValues();
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_IMAGE_DESCRIPTION, AppUtils.getBitmapAsByteArray(task.getImageDescription()));

        int row = db.update(TaskListContract.TABLE_NAME, dbValues, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{Long.toString(task.getId())});
        db.close();

        return row > 0;
    }

    @Override
    public boolean updatePriority(Task task) {
        // Apply the changes to the database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues dbValues = new ContentValues();
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, task.getPriority());
        int row = db.update(TaskListContract.TABLE_NAME, dbValues, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{Long.toString(task.getId())});
        db.close();

        return row > 0;
    }

    @Override
    public boolean check(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues dbValues = new ContentValues();
        // Set the checked value
        boolean checked = task.isChecked();
        if (checked) {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_TRUE);
        } else {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_FALSE);
        }
        // Perform the database insert
        int row = db.update(TaskListContract.TABLE_NAME, dbValues, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{Long.toString(task.getId())});
        db.close();

        return (row > 0);
    }
}
