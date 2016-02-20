package uofm.software_engineering.group7.to_do_bot.models;


import uofm.software_engineering.group7.to_do_bot.services.TaskListContract;
import uofm.software_engineering.group7.to_do_bot.services.TaskListDBHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Faye on 1/22/2016.
 */

public class TaskListManager {
    private String category;
    private TaskList list;
    private TaskListDBHelper taskListDB;
    private int counter; //TODO: keep or not? for ID

    public TaskListManager(Context context, String newName) {
        // TODO: Critical Section: This value, category, must be checked for unique-ness and fail on non-unique.
        category = newName;
        counter = 0;
        list = new TaskList();
        taskListDB = new TaskListDBHelper(context, this.category);
    }

    public void editCategoryName(String newName) {
        category = newName;
        // TODO: DB Integration
    }

    public void addTask(String taskDescription) {
        SQLiteDatabase db = taskListDB.getWritableDatabase();
        ContentValues dbValues = new ContentValues();
        TaskListItem item;
        long newItemID = 0;
        // Set the values we need for this entry
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_DESCRIPTION, taskDescription);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_FALSE);
        // Perform the database insert, returning the _ID primary key value
        newItemID = db.insert(this.category, null, dbValues);
        // Instantiate a new TaskListItem using the new ID we just received from the DB
        item = new TaskListItem(this.taskListDB, newItemID, taskDescription);
        list.put(counter, item);

        // DEBUG
        System.out.println("[Item " + item.getId() +
                "] Added. Description: [" +
                item.getTaskDescription() + "]");

        db.close();
        counter++;
    }

    public void removeTask(String id) {
        list.remove(id);
        // TODO: DB Integration
        if(counter > 0) counter--;
    }
}
