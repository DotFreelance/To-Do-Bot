package uofm.software_engineering.group7.to_do_bot.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import uofm.software_engineering.group7.to_do_bot.services.TaskListContract;
import uofm.software_engineering.group7.to_do_bot.services.TaskListDBHelper;

/**
 * Created by Faye on 1/22/2016.
 *
 * This is what goes on in the list.
 */
public class TaskListItem implements ListItem {
    // References to containing objects required for category and DB integration
    private TaskListDBHelper taskListDB;
    private TaskListManager taskListManager;

    // User values for this TaskListItem
    private long id;
    private String dateCreated;
    private String taskDescription;
    private boolean checked = false;
    private String alarmTime = null;
    private int priority = 0;
    private boolean spinAdaptorSet = false;

    public TaskListItem(TaskListManager listManager, TaskListDBHelper dbHelper,
                        long itemID,
                        String dateCreated,
                        String newTaskDescription,
                        boolean isChecked,
                        String alarmTime,
                        int priorityLevel
    ) {
        // Set the reference values
        taskListManager = listManager;
        taskListDB = dbHelper;
        // Set the user values
        this.id = itemID;
        this.dateCreated = dateCreated;
        this.taskDescription = newTaskDescription;
        this.checked = isChecked;
        this.alarmTime = alarmTime;
        this.priority = priorityLevel;
        this.spinAdaptorSet = false;
    }

    // Getters
    public long getId() {
        return id;
    }

    public boolean getSpinAdaptorSet() { return spinAdaptorSet; }

    public String getTaskDescription() { return taskDescription; }

    public boolean getChecked(){ return checked; }

    public String getCategory(){ return taskListManager.getCategory(); }

    public int getPriority() { return priority; }

    // Setters
    public void setTaskDescription(String newTaskDescription) {
        taskDescription = newTaskDescription;

        // Apply the changes to the database
        SQLiteDatabase db = taskListDB.getWritableDatabase();

        ContentValues dbValues = new ContentValues();
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_DESCRIPTION, newTaskDescription);

        db.update(TaskListContract.TABLE_NAME, dbValues, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{Long.toString(this.getId())});

        taskListDB.close();
    }

    public void setPriority(int priorityLevel) {

        // Apply the changes to the database
        this.priority = priorityLevel;
        SQLiteDatabase db = taskListDB.getWritableDatabase();
        ContentValues dbValues = new ContentValues();

        if(priorityLevel == 0) {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, TaskListContract.TaskListItemSchema.PRIORITY_NONE);
        }else if(priorityLevel == 1) {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, TaskListContract.TaskListItemSchema.PRIORITY_MEDIUM);
        }else if(priorityLevel == 2) {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, TaskListContract.TaskListItemSchema.PRIORITY_HIGH);
        }

        db.update(TaskListContract.TABLE_NAME, dbValues, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{Long.toString(this.getId())});

        this.taskListManager.getAdapter().notifyDataSetChanged();

        db.close();
    }

    public void setSpinAdaptor(boolean spinAdaptorSet) {
        this.spinAdaptorSet = spinAdaptorSet;
    }

    // Utility methods
    public void clearTaskDescription() {
        taskDescription = "";
    }

    public void check(boolean checked) {
        this.checked = checked;
        SQLiteDatabase db = taskListDB.getWritableDatabase();
        ContentValues dbValues = new ContentValues();
        // Set the checked value
        if(checked) {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_TRUE);
        } else {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_FALSE);
        }
        // Perform the database insert
        db.update(TaskListContract.TABLE_NAME, dbValues, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{ Long.toString(this.getId()) });

        this.taskListManager.getAdapter().notifyDataSetChanged();

        db.close();
    }


}
