package uofm.software_engineering.group7.to_do_bot.db;

import android.provider.BaseColumns;

/**
 * Created by Paul J on 2016-02-19.
 */
public final class CategoryContract {
    private CategoryContract() {
    }

    public static final String TABLE_NAME = "categories";

    // Inner class defines the actual schema
    public static abstract class CategoryItemSchema implements BaseColumns {
        public static final String COL_CATEGORY_NAME = "categoryName";
        public static final String COL_CATEGORY_DESCRIPTION = "categoryDescription";
    }
}
