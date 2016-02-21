package uofm.software_engineering.group7.to_do_bot.models;


import uofm.software_engineering.group7.to_do_bot.services.TaskListContract;
import uofm.software_engineering.group7.to_do_bot.services.TaskListDBHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

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
        taskListDB = new TaskListDBHelper(context);
    }

    public String getCategory(){
        return this.category;
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
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY, this.category);
        dbValues.put(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED, TaskListContract.TaskListItemSchema.CHECKED_FALSE);
        // Perform the database insert, returning the _ID primary key value
        newItemID = db.insert(TaskListContract.TABLE_NAME, null, dbValues);
        // Instantiate a new TaskListItem using the new ID we just received from the DB, if we were successful
        if(newItemID >= 0){
            item = new TaskListItem(this, this.taskListDB, newItemID, taskDescription);
            list.put(counter, item);
            counter++;

            // DEBUG
            System.out.println("[ADDED Item " + item.getId() +
                    "] Added. Description: [" +
                    item.getTaskDescription() + "] Category: [" +
                    item.getCategory() + "]");
        }

        db.close();
    }

    // Retrieves all tasks from the DB for the category
    public void initFromDB(){
        SQLiteDatabase db = taskListDB.getReadableDatabase();

        // Build the query
        String[] PROJECTION = {
                "*"
        };
        String SELECTION = TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY + "=?";
        String[] SELECTION_ARGS = {
                this.category
        };
        String SORT_ORDER = TaskListContract.TaskListItemSchema._ID + " ASC";

        // Send the query
        Cursor readCursor = db.query(TaskListContract.TABLE_NAME, PROJECTION, SELECTION, SELECTION_ARGS, null, null, SORT_ORDER);

        System.out.println("Initializing task list from database...");

        // Go through the query to fill the task list
        while(readCursor.moveToNext()){
            long itemId = readCursor.getLong(
                    readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema._ID));
            String dateCreated = readCursor.getString(
                    readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema.COL_NAME_CREATED));
            String taskCategory = readCursor.getString(
                    readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY));
            String description = readCursor.getString(
                    readCursor.getColumnIndexOrThrow(TaskListContract.TaskListItemSchema.COL_NAME_DESCRIPTION));

            // DEBUG
            System.out.println("[RETRIEVED Item " + itemId +
                    "] Added. Description: [" +
                    description + "] Category: [" +
                    taskCategory + "] Created: [" +
                    dateCreated + "]"
            );
        }

        readCursor.close();
    }

    public void removeTask(String id) {
        list.remove(id);
        // TODO: DB Integration
        if(counter > 0) counter--;
    }
}
