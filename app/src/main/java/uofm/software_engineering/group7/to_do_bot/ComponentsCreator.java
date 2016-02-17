package uofm.software_engineering.group7.to_do_bot;

import uofm.software_engineering.group7.to_do_bot.models.TaskListManager;

/**
 * Created by Faye on 2/8/2016.
 */
public class ComponentsCreator {
    public void run() {
        TaskListManager listManager = new TaskListManager("Default");
        // TODO: this is the view for the list panel. Check Asana for "InitialDesignLayout"
        // TaskListPanel listPanel = new TaskListPanel(); 

        // TODO: Do you guys want a controller to change the model and the view?
        // TaskListManagerController = new TaskListManagerController(listManager, listPanel);
    }
}
