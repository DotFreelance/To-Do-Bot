package uofm.software_engineering.group7.to_do_bot.test.db;

import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.db.CategoryModel;
import uofm.software_engineering.group7.to_do_bot.db.CategoryModelImpl;
import uofm.software_engineering.group7.to_do_bot.db.DbHelper;
import uofm.software_engineering.group7.to_do_bot.db.TaskListContract;
import uofm.software_engineering.group7.to_do_bot.db.TaskModel;
import uofm.software_engineering.group7.to_do_bot.db.TaskModelImpl;
import uofm.software_engineering.group7.to_do_bot.model.Category;
import uofm.software_engineering.group7.to_do_bot.model.Task;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CategoryModelTest {

    private DbHelper db;
    private CategoryModel model;

    @Before
    public void setUp() {
        getTargetContext().deleteDatabase(DbHelper.DATABASE_NAME);
        db = new DbHelper(getTargetContext());
        model = new CategoryModelImpl(db);
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void testGetAll() {
        String[] categorieArray = getTargetContext().getResources().getStringArray(R.array.taskCategories);
        ArrayList<Category> categories = model.getAll();
        assertNotNull(categories);
        assertEquals(5, categories.size());

        int index = 0;
        for (Category category : categories) {
            assertEquals(index + 1, category.getId());
            assertEquals(categorieArray[index], category.getName());
            assertEquals(null, category.getDescription());
            assertEquals(0, category.getNumberOfTasks());
            index++;
        }
    }

    @Test
    public void testAddSuccess() {
        //test add pass
        String name = "Test Category";
        String description = "Dummy Description";
        Category category = new Category(name, description);
        Category dbCategory = model.add(category);
        assertEquals(6, dbCategory.getId());

        ArrayList<Category> categories = model.getAll();
        assertEquals(6, categories.size());
        //get the item has just added to db
        Category expectedCategory = categories.get(5);
        assertEquals(name, expectedCategory.getName());
        assertEquals(description, expectedCategory.getDescription());
        assertEquals(0, expectedCategory.getNumberOfTasks());
    }

    @Test
    public void testAddFailed() {
        String name = null;
        String description = "Dummy Description";
        Category category = new Category(name, description);

        Category dbCategory = model.add(category);
        assertNull(dbCategory);

        ArrayList<Category> categories = model.getAll();
        assertEquals(5, categories.size());
    }

    @Test
    public void testRemoveSuccess() {
        Category category = new Category();
        category.setId(1);

        boolean remove = model.remove(category);
        assertTrue(remove);
        ArrayList<Category> categories = model.getAll();
        assertEquals(4, categories.size());
    }

    @Test
    public void testRemoveSuccessWithTaskInside() {
        TaskModel taskModel = new TaskModelImpl(db);
        Task task = new Task();
        task.setName("Dummy Task Name");
        task.setCategoryId(1);

        taskModel.add(task);

        Category category = new Category();
        category.setId(1);

        boolean remove = model.remove(category);
        assertTrue(remove);
        assertEquals(4, model.getAll().size());

        Cursor cursor = db.getWritableDatabase().rawQuery("SELECT * FROM " + TaskListContract.TABLE_NAME + " WHERE " + TaskListContract.TaskListItemSchema._ID + "= \"1\"", null);
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    @Test
    public void testRemoveFailed() {
        Category category = new Category();
        category.setId(10);

        boolean remove = model.remove(category);
        assertFalse(remove);
        assertEquals(5, model.getAll().size());
    }

    @Test
    public void testUpdateSuccess() {
        String updatedName = "Dummy updated name";
        String updatedDescription = "Dummy updated description";
        ArrayList<Category> categories = model.getAll();
        Category updatedCategory = categories.get(0);

        //update Category
        updatedCategory.setName(updatedName);
        updatedCategory.setDescription(updatedDescription);
        boolean update = model.update(updatedCategory);
        assertTrue(update);
        ArrayList<Category> categoriesNew = model.getAll();
        assertEquals(5, categoriesNew.size());
        assertEquals(1, categoriesNew.get(0).getId());
        assertEquals(updatedName, categoriesNew.get(0).getName());
        assertEquals(updatedDescription, categoriesNew.get(0).getDescription());
        assertEquals(0, categoriesNew.get(0).getNumberOfTasks());
    }

    @Test
    public void testUpdateFailed() {
        String updatedName = "Dummy updated name";
        String updatedDescription = "Dummy updated description";
        ArrayList<Category> categories = model.getAll();
        Category updatedCategory = categories.get(0);

        //update Category
        updatedCategory.setName(updatedName);
        updatedCategory.setDescription(updatedDescription);
        updatedCategory.setId(10);

        boolean update = model.update(updatedCategory);
        assertFalse(update);
        ArrayList<Category> categoriesNew = model.getAll();
        assertEquals(5, categoriesNew.size());
        assertEquals(1, categoriesNew.get(0).getId());
        assertEquals(getTargetContext().getResources().getStringArray(R.array.taskCategories)[0], categoriesNew.get(0).getName());
        assertEquals(null, categoriesNew.get(0).getDescription());
        assertEquals(0, categoriesNew.get(0).getNumberOfTasks());
    }
}
