package uofm.software_engineering.group7.to_do_bot.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.db.BaseModel;
import uofm.software_engineering.group7.to_do_bot.db.CategoryModelImpl;
import uofm.software_engineering.group7.to_do_bot.db.DbHelper;
import uofm.software_engineering.group7.to_do_bot.model.Category;
import uofm.software_engineering.group7.to_do_bot.ui.adapter.CategoryAdapter;
import uofm.software_engineering.group7.to_do_bot.ui.widget.DividerItemDecoration;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ADD_CATEGORY = 1;
    private static final int REQUEST_ADD_OR_EDIT_CATEGORY = 2;

    private RecyclerView recyclerView;
    private Context context;
    private BaseModel categoryModel;
    private CategoryAdapter adapter;
    private ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        categoryModel = new CategoryModelImpl(new DbHelper(context));
        categories = categoryModel.getAll();
        initializeView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD_OR_EDIT_CATEGORY:
                if (resultCode == RESULT_OK) {
                    List categories = categoryModel.getAll();
                    adapter.refresh(categories);
                }
                break;
            case REQUEST_ADD_CATEGORY:
                if(resultCode == RESULT_OK){
                    Category category = data.getParcelableExtra(AddNewCategoryActivity.EXTRA_CATEGORY);
                    Category newCategory = (Category) categoryModel.add(category);
                    adapter.add(newCategory);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                if (!categories.isEmpty()) {
                    Intent intent = new Intent(this, EditCategoryActivity.class);
                    intent.putExtra(EditCategoryActivity.EXTRA_CATEGORIES, categories);
                    startActivityForResult(intent, REQUEST_ADD_OR_EDIT_CATEGORY);
                } else {
                    Intent intent = new Intent(this, AddNewCategoryActivity.class);
                    startActivityForResult(intent, REQUEST_ADD_CATEGORY);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        adapter = new CategoryAdapter(this, categories);
        recyclerView.setAdapter(adapter);
    }
}
