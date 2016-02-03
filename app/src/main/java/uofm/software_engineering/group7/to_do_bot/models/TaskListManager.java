package uofm.software_engineering.group7.to_do_bot.models;


import uofm.software_engineering.group7.to_do_bot.models.ListItem;
import uofm.software_engineering.group7.to_do_bot.models.TaskList;
import uofm.software_engineering.group7.to_do_bot.models.TaskListItem;

/**
 * Created by Faye on 1/22/2016.
 */

public class TaskListManager {
    private String categoryName;
    private TaskList list;
    private int counter; //TODO: keep or not? for ID

    public TaskListManager(String newName) {
        categoryName = newName;
        counter = 0;
    }

    public void editCategoryName(String newName) {
        categoryName = newName;
    }

    public void addTask(String value) {
        counter ++;

        ListItem item = new TaskListItem(value);
        list.put(counter, item);
    }

    public void removeTask(String id) {
        list.remove(id);
    }
}
