package uofm.software_engineering.group7.to_do_bot;

import junit.framework.TestCase;

import uofm.software_engineering.group7.to_do_bot.models.TaskListManager;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends TestCase {
    private static final int AMOUNT_OF_ITEMS = 100;
    private static final AddItemTest addItemTest = new AddItemTest();
    private static final RemoveItemTest removeItemTest = new RemoveItemTest();
    private static final TaskListManager taskListManager = new TaskListManager(null, "Test Category");

    public static void main(String[] args) {
        populate();

        addItemTest.addItemTest00(taskListManager);
        removeItemTest.removeItemTest(taskListManager);
    }

    private static void populate() {
        for(int i = 0; i < AMOUNT_OF_ITEMS; i++) {
            taskListManager.addTask("Description", 0, null);
        }
    }
}