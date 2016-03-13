package uofm.software_engineering.group7.to_do_bot.models;


import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.services.TaskListContract;
import uofm.software_engineering.group7.to_do_bot.services.TaskListDBHelper;
import uofm.software_engineering.group7.to_do_bot.services.TaskListItemAdapter;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.widget.Toast;

/**
 * Created by Faye on 1/22/2016.
 */

public class TaskListManager  {
    private String category;
    private TaskList<TaskListItem> list;
    private TaskListDBHelper taskListDB;
    private TaskListItemAdapter adapter;

    public TaskListManager(Context context, String newName) {
        category = newName;
        list = new TaskList<>();
        taskListDB = new TaskListDBHelper(context);
        adapter = new TaskListItemAdapter(this, context, list);
    }

    public String getCategory() {
        return this.category;
    }

    public TaskListItemAdapter getAdapter() {
        return this.adapter;
    }

    public void editCategoryName(String newName) {
        category = newName;
        // TODO: DB Integration
    }

    public void addTask(Context context, String taskDescription) {
        SQLiteDatabase db = taskListDB.getWritableDatabase();
        ContentValues dbValues = new ContentValues();
        TaskListItem item;
        long newItemID;
        String currentDate = "";
        int priority;
        // Set the values we need for this entry
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CREATED, currentDate);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_DESCRIPTION, taskDescription);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY, this.category);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_FALSE);
        //dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, 0); ADD LATER
        // Perform the database insert, returning the _ID primary key value
        newItemID = db.insert(TaskListContract.TABLE_NAME, null, dbValues);
        // Instantiate a new TaskListItem using the new ID we just received from the DB, if we were successful
        if (newItemID >= 0) {
            item = new TaskListItem(this, this.taskListDB,
                    newItemID,
                    currentDate,
                    taskDescription,
                    false,
                    null//add priority default 0
                    );
            list.add(item);
        } else {
            Toast.makeText(context, context.getString(R.string.add_task_failed), Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
        db.close();
    }

    // Retrieves all tasks from the DB for the category
    public void initFromDB(Context context) {
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
        String SORT_ORDER = TaskListContract.TaskListItemSchema._ID + " ASC";

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
            //Add priority later
            //int priority = readCursor.getInt(
                    //readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY));
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
                    alarmTime//Add priority later
            );
            list.add(item);
        }

        adapter.notifyDataSetChanged();
        readCursor.close();
        db.close();
    }

    public void removeTask(int index) {
        SQLiteDatabase db = taskListDB.getWritableDatabase();
        ContentValues dbValues = new ContentValues();
        TaskListItem item = list.get(index);
        long deleteId = item.getId();


        db.delete(TaskListContract.TABLE_NAME, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{Long.toString(item.getId())});

        list.remove(index);

        adapter.notifyDataSetChanged();
        db.close();
    }

    //Remove task from local memory only (not db)
    public void removeCheckedTask(int index) {
        TaskListItem item = list.get(index);
        list.remove(index);
    }

    public TaskListDBHelper getTaskListDB() {
        return taskListDB;
    }
}
