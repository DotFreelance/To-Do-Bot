package uofm.software_engineering.group7.to_do_bot.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import uofm.software_engineering.group7.to_do_bot.model.Category;
import uofm.software_engineering.group7.to_do_bot.model.Task;

/**
 * Created by thuongle on 4/3/16.
 * Version
 */
public class CategoryModelImpl implements CategoryModel {

    private final DbHelper mDbHelper;

    public CategoryModelImpl(DbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    @Override
    public ArrayList<Category> getAll() {
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + CategoryContract.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(CategoryContract.CategoryItemSchema._ID));
                    String name = cursor.getString(cursor.getColumnIndex(CategoryContract.CategoryItemSchema.COL_CATEGORY_NAME));
                    String description = cursor.getString(cursor.getColumnIndex(CategoryContract.CategoryItemSchema.COL_CATEGORY_DESCRIPTION));
                    Category category = new Category(name, description);
                    category.setId(id);
                    category.setNumberOfTasks(getNumberOfTasks(db, id));
                    categories.add(category);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        db.close();
        return categories;
    }

    @Override
    public Category add(Category object) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues dbValues = new ContentValues();
        long newItemID;
        // Set the values we need for this entry
        dbValues.put(CategoryContract.CategoryItemSchema.COL_CATEGORY_NAME, object.getName());
        dbValues.put(CategoryContract.CategoryItemSchema.COL_CATEGORY_DESCRIPTION, object.getDescription());
        // Perform the database insert, returning the _ID primary key value
        newItemID = db.insert(CategoryContract.TABLE_NAME, null, dbValues);
        db.close();

        // Instantiate a new TaskListItem using the new ID we just received from the DB, if we were successful
        if (newItemID != -1) {
            object.setId(newItemID);
        } else {
            return null;
        }

        return object;
    }

    @Override
    public boolean remove(Category object) {
        //delete tasks first
        TaskModelImpl taskModel = new TaskModelImpl(mDbHelper);
        List<Task> tasksByCategory = taskModel.getTasksByCategory(object.getId());
        for (Task task : tasksByCategory) {
            taskModel.remove(task);
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowAffect = db.delete(CategoryContract.TABLE_NAME, CategoryContract.CategoryItemSchema._ID + "=?", new String[]{Long.toString(object.getId())});
        db.close();
        //0 mean remove item false
        return (rowAffect > 0);
    }

    @Override
    public boolean update(Category object) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues dbValues = new ContentValues();
        // Set the values we need for this entry
        dbValues.put(CategoryContract.CategoryItemSchema.COL_CATEGORY_NAME, object.getName());
        dbValues.put(CategoryContract.CategoryItemSchema.COL_CATEGORY_DESCRIPTION, object.getDescription());
        // Perform the database insert, returning the _ID primary key value
        int row = db.update(CategoryContract.TABLE_NAME, dbValues, CategoryContract.CategoryItemSchema._ID + "=?", new String[]{Long.toString(object.getId())});
        db.close();
        return (row > 0);
    }

    private int getNumberOfTasks(SQLiteDatabase db, long id) {
        // Build the query
        String[] PROJECTION = {"*"};
        String SELECTION = TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY + "=?";
        String[] SELECTION_ARGS = {String.valueOf(id)};

        // Send the query
        Cursor readCursor = db.query(TaskListContract.TABLE_NAME, PROJECTION, SELECTION, SELECTION_ARGS, null, null, null);

        if (readCursor != null) {
            return readCursor.getCount();
        }
        return 0;
    }
}
