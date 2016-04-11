package uofm.software_engineering.group7.to_do_bot.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import uofm.software_engineering.group7.to_do_bot.R;

/**
 * Created by Paul J on 2016-02-18.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "to-do-bot";
    private static final int DATABASE_VERSION = 4;//for update category
    private static final StringBuffer TASK_TABLE_CREATE_QUERY = new StringBuffer()
            .append("CREATE TABLE IF NOT EXISTS ").append(TaskListContract.TABLE_NAME).append(" (")
            .append(TaskListContract.TaskListItemSchema._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
            .append(TaskListContract.TaskListItemSchema.COL_NAME_CREATED).append(" DATETIME DEFAULT CURRENT_TIMESTAMP,")
            .append(TaskListContract.TaskListItemSchema.COL_NAME_TASK_NAME).append(" TEXT NOT NULL,")
            .append(TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY).append(" INTEGER NOT NULL,")
            .append(TaskListContract.TaskListItemSchema.COL_NAME_IMAGE_DESCRIPTION).append(" BLOB,")
            .append(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED).append(" BOOLEAN NOT NULL")
            .append(" CHECK (").append(TaskListContract.TaskListItemSchema.COL_NAME_CHECKED).append(" IN (0,1)),")
            .append(TaskListContract.TaskListItemSchema.COL_NAME_ALARM).append(" DATETIME DEFAULT NULL,")
            .append(TaskListContract.TaskListItemSchema.COL_NAME_PRIORITY).append(" INTEGER NOT NULL DEFAULT ").append(TaskListContract.TaskListItemSchema.PRIORITY_NONE).append(");");

    private static final StringBuffer CATEGORY_TABLE_CREATE_QUERY = new StringBuffer()
            .append("CREATE TABLE IF NOT EXISTS ").append(CategoryContract.TABLE_NAME + " (")
            .append(CategoryContract.CategoryItemSchema._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
            .append(CategoryContract.CategoryItemSchema.COL_CATEGORY_NAME).append(" TEXT NOT NULL,")
            .append(CategoryContract.CategoryItemSchema.COL_CATEGORY_DESCRIPTION).append(" TEXT DEFAULT NULL);");
    private final Context mContext;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CATEGORY_TABLE_CREATE_QUERY.toString());
        db.execSQL(TASK_TABLE_CREATE_QUERY.toString());

        initializeCategories(db);
    }

    private void initializeCategories(SQLiteDatabase db) {
        String[] categories = mContext.getResources().getStringArray(R.array.taskCategories);

        for (String category : categories) {
            ContentValues values = new ContentValues();
            values.put(CategoryContract.CategoryItemSchema.COL_CATEGORY_NAME, category);

            db.insert(CategoryContract.TABLE_NAME, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Required concrete method does not require implementation for Software Engineering 1
        // - Paul J
        db.execSQL("DROP TABLE IF EXISTS " + TaskListContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryContract.TABLE_NAME);
        onCreate(db);
    }
}
