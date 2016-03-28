package uofm.software_engineering.group7.to_do_bot;

import android.content.Context;

import uofm.software_engineering.group7.to_do_bot.models.TaskListManager;

/**
 * Created by Faye on 2/8/2016.
 */
class ComponentsCreator {
    public TaskListManager listManager;

    public void run(Context context) {
        listManager = new TaskListManager(context, "DefaultList");
        // TODO: this is the view for the list panel. Check Asana for "InitialDesignLayout"
        // TaskListPanel listPanel = new TaskListPanel(); 

        // TODO: Do you guys want a controller to change the model and the view?
        // TaskListManagerController = new TaskListManagerController(listManager, listPanel);
    }
}
