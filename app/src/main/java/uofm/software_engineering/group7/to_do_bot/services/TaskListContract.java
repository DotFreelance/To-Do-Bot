package uofm.software_engineering.group7.to_do_bot.services;

import android.provider.BaseColumns;

/**
 * Created by Paul J on 2016-02-19.
 */
public final class TaskListContract {
    public TaskListContract() {}

    public static final String TABLE_NAME = "tasks";

    // Inner class defines the actual schema
    public static abstract class TaskListItemSchema implements BaseColumns{
        public static final String COL_NAME_CREATED = "createdAt";
        public static final String COL_NAME_CATEGORY = "taskCategory";
        public static final String COL_NAME_DESCRIPTION = "taskDescription";
        public static final String COL_NAME_CHECKED = "checked";
        public static final String COL_NAME_ALARM = "alarmTime";
        public static final int CHECKED_TRUE = 1;
        public static final int CHECKED_FALSE = 0;
    }
}
