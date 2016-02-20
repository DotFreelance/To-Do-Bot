package uofm.software_engineering.group7.to_do_bot.models;


import uofm.software_engineering.group7.to_do_bot.services.TaskListDBHelper;
import android.content.Context;
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

        ListItem item = new TaskListItem(this.taskListDB, taskDescription);
        list.put(counter, item);



        counter++;
    }

    public void removeTask(String id) {
        list.remove(id);
        // TODO: DB Integration
        if(counter > 0) counter--;
    }
}
