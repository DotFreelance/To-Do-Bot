package uofm.software_engineering.group7.to_do_bot;

import uofm.software_engineering.group7.to_do_bot.models.TaskList;
import uofm.software_engineering.group7.to_do_bot.models.TaskListItem;
import uofm.software_engineering.group7.to_do_bot.models.TaskListManager;
import uofm.software_engineering.group7.to_do_bot.services.TaskListContract;

/**
 * Created by Lam Doan && Faye Lim on 2016-03-14.
 */
public class PriorityTest {
    private TaskList taskList = null;

    public void priorityTest(TaskListManager taskListManager) {
        //checkEmptyCase(taskListManager);

        int priority = TaskListContract.TaskListItemSchema.PRIORITY_MEDIUM;
        taskList = taskListManager.getList();

        taskListManager.addTask("Description", 0, null);
        TaskListItem currItem = (TaskListItem) taskList.get(0);
        currItem.setPriority(priority);

        int tempPriority = currItem.getPriority();

        assert(tempPriority == priority);
    }

    void checkEmptyCase(TaskListManager taskListManager) {
        try {
            TaskListItem testItem = (TaskListItem) taskList.get(0);
            int testPriority = testItem.getPriority();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
