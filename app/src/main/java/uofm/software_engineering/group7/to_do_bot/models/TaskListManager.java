package uofm.software_engineering.group7.to_do_bot.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.Collections;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.services.TaskListContract;
import uofm.software_engineering.group7.to_do_bot.services.TaskListDBHelper;
import uofm.software_engineering.group7.to_do_bot.services.TaskListItemAdapter;

/**
 * Created by Faye on 1/22/2016.
 */

public class TaskListManager {
    private final Context context;
    private String category;
    private final TaskList<TaskListItem> list;
    private final TaskListDBHelper taskListDB;
    private final TaskListItemAdapter adapter;

    public TaskListManager(Context context, String newName) {
        this.context = context;
        list = new TaskList<>();
        category = newName;
        taskListDB = new TaskListDBHelper(context);
        adapter = new TaskListItemAdapter(this, context, list);
    }

    public TaskList<TaskListItem> getList() {
        return list;
    }

    public TaskListDBHelper getTaskListDB() {
        return taskListDB;
    }

    public String getCategory() {
        return this.category;
    }

    public TaskListItemAdapter getAdapter() {
        return this.adapter;
    }

    public void editCategoryName(String newName) {
        category = newName;
    }

    // This writes to DB before taskDescription is even set
    public void addTask(String description, int priority, String alarmTime) {
        SQLiteDatabase db = taskListDB.getWritableDatabase();
        ContentValues dbValues = new ContentValues();
        TaskListItem item;
        long newItemID;
        String currentDate = "";
        // Set the values we need for this entry
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CREATED, currentDate);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_DESCRIPTION, description);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY, this.category);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_FALSE);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, priority);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_ALARM, alarmTime);
        // Perform the database insert, returning the _ID primary key value
        newItemID = db.insert(TaskListContract.TABLE_NAME, null, dbValues);
        // Instantiate a new TaskListItem using the new ID we just received from the DB, if we were successful
        if (newItemID >= 0) {
            item = new TaskListItem(this, this.taskListDB,
                    newItemID,
                    currentDate,
                    description,
                    false,
                    alarmTime,
                    priority
            );
            list.add(item);
        } else {
            Toast.makeText(context, context.getString(R.string.add_task_failed), Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
        db.close();
    }

    // Retrieves all tasks from the DB for the category
    public void initFromDB() {
        SQLiteDatabase db = taskListDB.getReadableDatabase();

        // Build the query
        String[] PROJECTION = {
                "*"
        };
        String SELECTION = TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY + "=?" +
                " AND " + TaskListContract.TaskListItemSchema.COL_NAME_CHECKED + "=?";
        String[] SELECTION_ARGS = {
                this.category,
                String.valueOf(TaskListContract.TaskListItemSchema.CHECKED_FALSE)
        };
        String SORT_ORDER = TaskListContract.TaskListItemSchema._ID + " ASC";//Changed SORT_ORDER from ID to PRIORITY

        // Send the query
        Cursor readCursor = db.query(TaskListContract.TABLE_NAME, PROJECTION, SELECTION, SELECTION_ARGS, null, null, SORT_ORDER);

        // Go through the cursor to fill the task list
        while (readCursor.moveToNext()) {
            long itemId = readCursor.getLong(
                    readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema._ID));
            String dateCreated = readCursor.getString(
                    readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema.COL_NAME_CREATED));
            String taskCategory = readCursor.getString(
                    readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY));
            String description = readCursor.getString(
                    readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema.COL_NAME_DESCRIPTION));
            boolean checked = readCursor.getInt(
                    readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED))
                    == TaskListContract.TaskListItemSchema.CHECKED_TRUE;
            int priority = readCursor.getInt(
                    readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY));
            String alarmTime = null;
            int alarmTimeIndex = readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema.COL_NAME_ALARM);
            if (!readCursor.isNull(alarmTimeIndex)) {
                alarmTime = readCursor.getString(alarmTimeIndex);
            }

            // Add this item to the TaskList
            TaskListItem item = new TaskListItem(this, this.taskListDB,
                    itemId,
                    dateCreated,
                    description,
                    checked,
                    alarmTime,
                    priority
            );
            list.add(item);
        }

        //Now that everything is added to the list, we want to sort
        Collections.sort(list, TaskListItem.PriorityComparator);

        adapter.notifyDataSetChanged();
        readCursor.close();
        db.close();
    }

    public void removeTask(int index) {
        SQLiteDatabase db = taskListDB.getWritableDatabase();
        TaskListItem item = list.get(index);

        db.delete(TaskListContract.TABLE_NAME, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{Long.toString(item.getId())});

        list.remove(index);

        adapter.notifyDataSetChanged();
        db.close();
    }
}
