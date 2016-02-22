package uofm.software_engineering.group7.to_do_bot;

import junit.framework.TestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends TestCase {
    private static AddItemTest addItemTest = new AddItemTest();
    private static RemoveItemTest removeItemTest = new RemoveItemTest();

    public static void main(String[] args) {
        addItemTest.addItemTest();
        removeItemTest.removeItemTest();
    }
}