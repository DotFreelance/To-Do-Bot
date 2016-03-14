package uofm.software_engineering.group7.to_do_bot;

import uofm.software_engineering.group7.to_do_bot.models.TaskListManager;

/**
 * Created by Matt on 2016-02-22.
 */
class RemoveItemTest {
    void removeItemTest(TaskListManager taskListManager) {
        int amountOfTasks = taskListManager.getList().size();
        taskListManager.removeTask(0);
        assert(taskListManager.getList().size() == (amountOfTasks - 1));
    }

    void checkEmptyCase(TaskListManager taskListManager) {
        try {
            taskListManager.removeTask(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
