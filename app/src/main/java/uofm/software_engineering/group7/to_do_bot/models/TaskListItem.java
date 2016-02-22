package uofm.software_engineering.group7.to_do_bot.models;

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


    public TaskListItem(TaskListManager listManager, TaskListDBHelper dbHelper,
                        long itemID,
                        String dateCreated,
                        String newTaskDescription,
                        boolean isChecked,
                        String alarmTime) {
        // Set the reference values
        taskListManager = listManager;
        taskListDB = dbHelper;
        // Set the user values
        this.id = itemID;
        this.dateCreated = dateCreated;
        this.taskDescription = newTaskDescription;
        this.checked = isChecked;
        this.alarmTime = alarmTime;
    }

    public long getId() {
        return id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public boolean getChecked(){
        return checked;
    }

    public String getCategory(){
        return taskListManager.getCategory();
    }

    public void editTaskDescription(String newTaskDescription) {
        taskDescription = newTaskDescription;
        // TODO: DB Integration
    }

    public void clearTaskDescription() {
        taskDescription = "";
        // TODO: DB Integration
    }

    public void check() {
        checked = true;
        // TODO: DB Integration
    }


}
