package uofm.software_engineering.group7.to_do_bot.test.ui;

import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.db.CategoryModel;
import uofm.software_engineering.group7.to_do_bot.db.CategoryModelImpl;
import uofm.software_engineering.group7.to_do_bot.db.DbHelper;
import uofm.software_engineering.group7.to_do_bot.ui.AddNewCategoryActivity;
import uofm.software_engineering.group7.to_do_bot.ui.EditCategoryActivity;
import uofm.software_engineering.group7.to_do_bot.ui.MainActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testAddNewCategoryShouldReturn() {
        // check that we have the right activity
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        //click action bar item will start new activity (EditCategoryActivity)
        solo.clickOnView(solo.getView(R.id.action_edit));
        solo.assertCurrentActivity("wrong activity", EditCategoryActivity.class);
        assertTrue(solo.waitForActivity(EditCategoryActivity.class, 2000));
        //in EditCategoryActivity, click add new activity
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.assertCurrentActivity("wrong activity", AddNewCategoryActivity.class);
        assertTrue(solo.waitForActivity(AddNewCategoryActivity.class, 5000));
        assertTrue(solo.searchText("Category Name"));
        assertTrue(solo.searchText("Category description"));
        assertTrue(solo.searchButton("Cancel"));
        assertTrue(solo.searchButton("Save"));
        solo.enterText((EditText) solo.getView(R.id.input_category_name), "Dummy Category");
        solo.enterText((EditText) solo.getView(R.id.input_category_description), "Dummy Description");
        solo.clickOnView(solo.getView(R.id.saveCategory));
        //should finish activity
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        assertTrue(solo.waitForActivity(MainActivity.class, 2000));
        //new category should be added in the view
        assertEquals(6, ((RecyclerView) solo.getView(R.id.recycler_view)).getAdapter().getItemCount());
        DbHelper db = new DbHelper(getActivity());
        CategoryModel model = new CategoryModelImpl(db);
        assertEquals(6, model.getAll().size());
        assertTrue(solo.searchText("Dummy Category"));
        assertTrue(solo.searchText("Dummy Description"));
        assertTrue(solo.searchText("0 tasks"));
    }

    public void testEditCategoryShouldReturn() {
        // check that we have the right activity
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        //click action bar item will start new activity (EditCategoryActivity)
        solo.clickOnView(solo.getView(R.id.action_edit));
        solo.assertCurrentActivity("wrong activity", EditCategoryActivity.class);
        assertTrue(solo.waitForActivity(EditCategoryActivity.class, 2000));
        assertTrue(solo.searchText("None"));
        assertTrue(solo.searchText("Category description"));
        assertTrue(solo.searchText("Category Name"));
        assertTrue(solo.searchButton("Cancel"));
        assertTrue(solo.searchButton("Save"));
        assertEquals("None", ((EditText) solo.getView(R.id.input_category_name)).getText().toString());
        solo.enterText((EditText) solo.getView(R.id.input_category_name), "");
        solo.enterText((EditText) solo.getView(R.id.input_category_name), "Edit Category");
        solo.enterText((EditText) solo.getView(R.id.input_category_description), "Edit Description");
        solo.clickOnView(solo.getView(R.id.saveCategory));
        //should finish activity
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        assertTrue(solo.waitForActivity(MainActivity.class, 2000));
    }

    public void testDeleteCategoryShouldReturn() {
        // check that we have the right activity
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        //click action bar item will start new activity (EditCategoryActivity)
        solo.clickOnView(solo.getView(R.id.action_edit));
        solo.assertCurrentActivity("wrong activity", EditCategoryActivity.class);
        assertTrue(solo.waitForActivity(EditCategoryActivity.class, 2000));
        assertTrue(solo.searchText("None"));
        assertTrue(solo.searchText("Category description"));
        assertTrue(solo.searchText("Category Name"));
        assertTrue(solo.searchButton("Cancel"));
        assertTrue(solo.searchButton("Save"));
        solo.clickOnView(solo.getView(R.id.action_delete));
        solo.waitForDialogToOpen(2000);
        solo.clickOnButton("OK");
        solo.waitForDialogToClose(2000);

        solo.clickOnView(solo.getView(R.id.saveCategory));
        //should finish activity
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
        assertTrue(solo.waitForActivity(MainActivity.class, 2000));
        //new category should be added in the view
        assertEquals(5, ((RecyclerView) solo.getView(R.id.recycler_view)).getAdapter().getItemCount());
    }
}
