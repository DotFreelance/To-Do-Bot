package uofm.software_engineering.group7.to_do_bot.services;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

/**
 * Created by Paul J on 2016-02-18.
 */
public class TaskListDBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "to-do-bot";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + TaskListContract.TABLE_NAME + " (" +
            TaskListContract.TaskListItemSchema._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TaskListContract.TaskListItemSchema.COL_NAME_CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
            TaskListContract.TaskListItemSchema.COL_NAME_CATEGORY + " TEXT NOT NULL," +
            TaskListContract.TaskListItemSchema.COL_NAME_DESCRIPTION + " TEXT NOT NULL," +
            TaskListContract.TaskListItemSchema.COL_NAME_CHECKED + " BOOLEAN NOT NULL" +
            " CHECK (" + TaskListContract.TaskListItemSchema.COL_NAME_CHECKED + " IN (0,1))," +
            TaskListContract.TaskListItemSchema.COL_NAME_ALARM + " DATETIME DEFAULT NULL" +
            ");";

    public TaskListDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE_QUERY);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Required concrete method does not require implementation for Software Engineering 1
        // - Paul J
    }
}
