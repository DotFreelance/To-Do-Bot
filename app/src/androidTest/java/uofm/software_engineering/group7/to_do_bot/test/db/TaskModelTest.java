package uofm.software_engineering.group7.to_do_bot.test.db;

import android.graphics.BitmapFactory;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.db.DbHelper;
import uofm.software_engineering.group7.to_do_bot.db.TaskListContract;
import uofm.software_engineering.group7.to_do_bot.db.TaskModel;
import uofm.software_engineering.group7.to_do_bot.db.TaskModelImpl;
import uofm.software_engineering.group7.to_do_bot.model.Task;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class TaskModelTest {

    private DbHelper db;
    private TaskModel model;

    @Before
    public void setUp() {
        getTargetContext().deleteDatabase(DbHelper.DATABASE_NAME);
        db = new DbHelper(getTargetContext());
        model = new TaskModelImpl(db);
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void testGetAll() {
        ArrayList<Task> tasks = model.getAll();
        assertEquals(0, tasks.size());

        //add new task
        Task task = new Task();
        task.setName("Task");
        task.setCategoryId(1);

        model.add(task);
        ArrayList<Task> newTasks = model.getAll();
        assertEquals(1, newTasks.size());
        Task dbTask = newTasks.get(0);
        assertEquals(1, dbTask.getId());
        assertNotNull(dbTask.getDateCreated());
        assertEquals("Task", dbTask.getName());
        assertEquals(1, dbTask.getCategoryId());
        assertFalse(dbTask.isChecked());
        assertEquals(TaskListContract.TaskListItemSchema.PRIORITY_NONE, dbTask.getPriority());
        assertNull(dbTask.getAlarmTime());
        assertNull(dbTask.getImageDescription());
    }

    @Test
    public void testAddSuccess() {
        Task task = new Task();
        task.setName("Task");
        task.setCategoryId(1);
        task.setPriority(TaskListContract.TaskListItemSchema.PRIORITY_HIGH);

        Task newTask = model.add(task);
        assertEquals(1, newTask.getId());

        ArrayList<Task> newTasks = model.getAll();
        assertEquals(1, newTasks.size());
        Task dbTask = newTasks.get(0);
        assertEquals(1, dbTask.getId());
        assertNotNull(dbTask.getDateCreated());
        assertEquals("Task", dbTask.getName());
        assertEquals(1, dbTask.getCategoryId());
        assertFalse(dbTask.isChecked());
        assertEquals(TaskListContract.TaskListItemSchema.PRIORITY_HIGH, dbTask.getPriority());
        assertNull(dbTask.getAlarmTime());
        assertNull(dbTask.getImageDescription());
    }

    @Test
    public void testAddTaskFailed() {
        Task task = new Task();
        task.setName(null);
        task.setCategoryId(1);
        task.setPriority(TaskListContract.TaskListItemSchema.PRIORITY_HIGH);

        Task newTask = model.add(task);
        assertNull(newTask);

        ArrayList<Task> newTasks = model.getAll();
        assertEquals(0, newTasks.size());
    }

    @Test
    public void testGetTasksByCategory() {
        Task task1 = new Task();
        task1.setName("Task1");
        task1.setCategoryId(1);
        task1.setPriority(TaskListContract.TaskListItemSchema.PRIORITY_MEDIUM);
        model.add(task1);

        Task task2 = new Task();
        task2.setName("Task2");
        task2.setCategoryId(1);
        task2.setPriority(TaskListContract.TaskListItemSchema.PRIORITY_HIGH);
        model.add(task2);

        Task task3 = new Task();
        task3.setName("Task3");
        task3.setCategoryId(1);
        task3.setPriority(TaskListContract.TaskListItemSchema.PRIORITY_NONE);
        model.add(task3);

        //test
        List<Task> tasksByCategoryNone = model.getTasksByCategory(2);
        assertEquals(0, tasksByCategoryNone.size());

        List<Task> tasksByCategory1 = model.getTasksByCategory(1);
        assertEquals(3, tasksByCategory1.size());

        //test sort out task
        Task testTask1 = tasksByCategory1.get(0);
        assertEquals(2, testTask1.getId());
        assertNotNull(testTask1.getDateCreated());
        assertEquals("Task2", testTask1.getName());
        assertEquals(1, testTask1.getCategoryId());
        assertFalse(testTask1.isChecked());
        assertEquals(TaskListContract.TaskListItemSchema.PRIORITY_HIGH, testTask1.getPriority());
        assertNull(testTask1.getAlarmTime());
        assertNull(testTask1.getImageDescription());

        Task testTask2 = tasksByCategory1.get(1);
        assertEquals(1, testTask2.getId());
        assertEquals("Task1", testTask2.getName());
        assertEquals(1, testTask2.getCategoryId());
        assertFalse(testTask2.isChecked());
        assertEquals(TaskListContract.TaskListItemSchema.PRIORITY_MEDIUM, testTask2.getPriority());
        assertNull(testTask2.getAlarmTime());
        assertNull(testTask2.getImageDescription());

        Task testTask3 = tasksByCategory1.get(2);
        assertEquals(3, testTask3.getId());
        assertNotNull(testTask3.getDateCreated());
        assertEquals("Task3", testTask3.getName());
        assertEquals(1, testTask3.getCategoryId());
        assertFalse(testTask3.isChecked());
        assertEquals(TaskListContract.TaskListItemSchema.PRIORITY_NONE, testTask3.getPriority());
        assertNull(testTask3.getAlarmTime());
        assertNull(testTask3.getImageDescription());
    }

    @Test
    public void testRemoveSuccess() {
        Task task = new Task();
        task.setName("Task");
        task.setCategoryId(1);

        Task dbTask = model.add(task);

        assertEquals(1, model.getAll().size());

        boolean remove = model.remove(dbTask);
        assertTrue(remove);
        assertEquals(0, model.getAll().size());
    }

    @Test
    public void testRemoveFailed() {
        Task task = new Task();
        task.setName("Task");
        task.setCategoryId(1);
        model.add(task);

        assertEquals(1, model.getAll().size());
        Task newTask = new Task();
        newTask.setId(10);
        boolean remove = model.remove(newTask);
        assertFalse(remove);
        assertEquals(1, model.getAll().size());
    }

    @Test
    public void testUpdateSuccess() {
        Task task = new Task();
        task.setName("Task");
        task.setCategoryId(1);

        model.add(task);
        assertEquals(1, model.getAll().size());

        Task newTask = new Task();
        newTask.setId(1);
        newTask.setCategoryId(2);
        newTask.setName("EditTask");
        newTask.setAlarmTime("EditAlarm");
        newTask.setChecked(true);
        newTask.setPriority(TaskListContract.TaskListItemSchema.PRIORITY_HIGH);

        boolean update = model.update(newTask);
        assertTrue(update);
        ArrayList<Task> dbTasks = model.getAll();
        assertEquals(1, dbTasks.size());

        Task dbTask = dbTasks.get(0);
        assertEquals(1, dbTask.getId());
        assertEquals(2, dbTask.getCategoryId());
        assertEquals("EditTask", dbTask.getName());
        assertEquals("EditAlarm", dbTask.getAlarmTime());
        assertEquals(TaskListContract.TaskListItemSchema.PRIORITY_HIGH, dbTask.getPriority());
        assertFalse(dbTask.isChecked());
        assertNull(dbTask.getImageDescription());
    }

    @Test
    public void testUpdateFailed() {
        assertEquals(0, model.getAll().size());

        Task task = new Task();
        task.setId(1);
        task.setCategoryId(1);
        task.setName("Task");

        assertFalse(model.update(task));
        assertEquals(0, model.getAll().size());
    }

    @Test
    public void testUpdateTaskDescriptionSuccess() {
        Task task = new Task();
        task.setCategoryId(1);
        task.setName("Task");

        model.add(task);
        assertEquals(1, model.getAll().size());

        task.setId(1);
        task.setImageDescription(BitmapFactory.decodeResource(getTargetContext().getResources(), R.mipmap.delete));

        boolean update = model.updateTaskDescription(task);
        assertTrue(update);
        assertNotNull(model.getAll().get(0).getImageDescription());
    }

    @Test
    public void testUpdateTaskDescriptionFailed() {
        Task task = new Task();
        task.setCategoryId(1);
        task.setName("Task");
        task.setId(10);
        model.add(task);

        assertEquals(1, model.getAll().size());

        task.setId(2);
        task.setImageDescription(BitmapFactory.decodeResource(getTargetContext().getResources(), R.mipmap.delete));
        boolean update = model.updateTaskDescription(task);
        assertFalse(update);
        assertNull(model.getAll().get(0).getImageDescription());
    }

    @Test
    public void testUpdatePrioritySuccess() {
        Task task = new Task();
        task.setCategoryId(1);
        task.setName("Task");
        task.setPriority(TaskListContract.TaskListItemSchema.PRIORITY_MEDIUM);

        model.add(task);
        assertEquals(1, model.getAll().size());

        task.setId(1);
        task.setPriority(TaskListContract.TaskListItemSchema.PRIORITY_HIGH);

        boolean update = model.updatePriority(task);
        assertTrue(update);
        assertEquals(TaskListContract.TaskListItemSchema.PRIORITY_HIGH, model.getAll().get(0).getPriority());
    }

    @Test
    public void testUpdatePriorityFailed() {
        Task task = new Task();
        task.setCategoryId(1);
        task.setName("Task");
        task.setPriority(TaskListContract.TaskListItemSchema.PRIORITY_HIGH);
        task.setId(10);
        model.add(task);

        assertEquals(1, model.getAll().size());

        task.setId(2);
        task.setPriority(TaskListContract.TaskListItemSchema.PRIORITY_MEDIUM);
        boolean update = model.updatePriority(task);
        assertFalse(update);
        assertEquals(TaskListContract.TaskListItemSchema.PRIORITY_HIGH, model.getAll().get(0).getPriority());
    }

    @Test
    public void testCheckSuccess() {
        Task task = new Task();
        task.setCategoryId(1);
        task.setName("Task");

        model.add(task);
        assertEquals(1, model.getAll().size());

        task.setId(1);
        task.setChecked(true);

        boolean update = model.check(task);
        assertTrue(update);
        assertTrue(model.getAll().get(0).isChecked());
    }

    @Test
    public void testCheckFailed() {
        Task task = new Task();
        task.setCategoryId(1);
        task.setName("Task");
        task.setId(10);
        model.add(task);

        assertEquals(1, model.getAll().size());

        task.setId(2);
        task.setChecked(true);
        boolean update = model.check(task);
        assertFalse(update);
        assertFalse(model.getAll().get(0).isChecked());
    }
}
