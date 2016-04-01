package uofm.software_engineering.group7.to_do_bot.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Comparator;

import uofm.software_engineering.group7.to_do_bot.services.TaskListContract;
import uofm.software_engineering.group7.to_do_bot.services.TaskListDBHelper;

/**
 * Created by Faye on 1/22/2016.
 * <p>
 * This is what goes on in the list.
 */
public class TaskListItem implements ListItem {
    // References to containing objects required for category and DB integration
    private final TaskListDBHelper taskListDB;
    private final TaskListManager taskListManager;

    // User values for this TaskListItem
    private final long id;
    private final String dateCreated;
    private  String taskName;
    private String taskDescription;
    private int category;
    private boolean checked = false;
    private String alarmTime = null;
    private int priority = 0;

    public TaskListItem(TaskListManager listManager, TaskListDBHelper dbHelper,
                        long itemID,
                        String dateCreated,
                        String taskName,
                        String newTaskDescription,
                        boolean isChecked,
                        String alarmTime,
                        int priorityLevel,
                        int category
    ) {
        // Set the reference values
        taskListManager = listManager;
        taskListDB = dbHelper;
        // Set the user values
        this.id = itemID;
        this.dateCreated = dateCreated;
        this.taskName = taskName;
        this.taskDescription = newTaskDescription;
        this.checked = isChecked;
        this.alarmTime = alarmTime;
        this.priority = priorityLevel;
        this.category = category;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public boolean getChecked() {
        return checked;
    }

    public int getPriority() {
        return priority;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

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

        if (priorityLevel == 0) {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, TaskListContract.TaskListItemSchema.PRIORITY_NONE);
        } else if (priorityLevel == 1) {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, TaskListContract.TaskListItemSchema.PRIORITY_MEDIUM);
        } else if (priorityLevel == 2) {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY, TaskListContract.TaskListItemSchema.PRIORITY_HIGH);
        }

        db.update(TaskListContract.TABLE_NAME, dbValues, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{Long.toString(this.getId())});

        this.taskListManager.getAdapter().notifyDataSetChanged();

        db.close();
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
        if (checked) {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_TRUE);
        } else {
            dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_FALSE);
        }
        // Perform the database insert
        db.update(TaskListContract.TABLE_NAME, dbValues, TaskListContract.TaskListItemSchema._ID + "=?", new String[]{Long.toString(this.getId())});

        this.taskListManager.getAdapter().notifyDataSetChanged();

        db.close();
    }

    //Comparator for sorting purposes
    public static final Comparator<TaskListItem> PriorityComparator = new Comparator<TaskListItem>() {
        public int compare(TaskListItem item1, TaskListItem item2) {
            int item1Priority = item1.getPriority();
            int item2Priority = item2.getPriority();

            //descending order
            return item2Priority - item1Priority;
        }
    };


}
