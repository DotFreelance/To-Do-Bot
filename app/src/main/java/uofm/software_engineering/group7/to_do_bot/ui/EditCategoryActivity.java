package uofm.software_engineering.group7.to_do_bot.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.db.CategoryModel;
import uofm.software_engineering.group7.to_do_bot.db.CategoryModelImpl;
import uofm.software_engineering.group7.to_do_bot.db.DbHelper;
import uofm.software_engineering.group7.to_do_bot.model.Category;

public class EditCategoryActivity extends KeyboardActivity {
    private static final int REQUEST_ADD_NEW_CATEGORY = 1;

    public static final String EXTRA_CATEGORIES = "categories";
    private ArrayList<Category> categories;
    private Spinner spinnerCategory;
    private EditText inputCategoryName;
    private EditText inputCategoryDescription;
    private CategoryModel categoryModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.your_categories_title));
        categoryModel = new CategoryModelImpl(new DbHelper(this));

        if (savedInstanceState == null) {
            categories = getIntent().getParcelableArrayListExtra(EXTRA_CATEGORIES);
        }

        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories));
        inputCategoryName = (EditText) findViewById(R.id.input_category_name);
        inputCategoryDescription = (EditText) findViewById(R.id.input_category_description);

        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        refreshViewAfterChanged(selectedCategory);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = categories.get(position);
                refreshViewAfterChanged(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_NEW_CATEGORY) {
            if (resultCode == RESULT_OK) {
                Category category = data.getParcelableExtra(AddNewCategoryActivity.EXTRA_CATEGORY);
                Category newCategory = categoryModel.add(category);
                if (newCategory != null) {
                    categories.add(newCategory);
                    ((ArrayAdapter) spinnerCategory.getAdapter()).notifyDataSetChanged();
                } else {
                    Toast.makeText(EditCategoryActivity.this, "Cannot add this category", Toast.LENGTH_SHORT).show();
                }
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == R.id.action_add) {
            Intent intent = new Intent(this, AddNewCategoryActivity.class);
            startActivityForResult(intent, REQUEST_ADD_NEW_CATEGORY);
            return true;
        }
        else if (itemID == R.id.action_delete) {
            final Category currentCategory = (Category) spinnerCategory.getSelectedItem();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to delete " + currentCategory.getName());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (categoryModel.remove(currentCategory)) {
                        categories.remove(spinnerCategory.getSelectedItemPosition());
                        ((ArrayAdapter) spinnerCategory.getAdapter()).notifyDataSetChanged();
                        //Category currentCategory = (Category) spinnerCategory.getSelectedItem();
                        refreshViewAfterChanged(currentCategory);
                    } else {
                        Toast.makeText(EditCategoryActivity.this, "Cannot delete this category", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void save(View view) {
        String categoryName = inputCategoryName.getText().toString();
        if (TextUtils.isEmpty(categoryName)) {
            inputCategoryName.setError(getString(R.string.error_field_empty));
            inputCategoryName.requestFocus();
        } else {
            Category selectedItem = (Category) spinnerCategory.getSelectedItem();
            String categoryDescription = inputCategoryDescription.getText().toString();
            inputCategoryName.setError(null);
            selectedItem.setName(categoryName);
            selectedItem.setDescription(categoryDescription);
            categoryModel.update(selectedItem);
            setResult(RESULT_OK);
            finish();
        }
    }


    private void refreshViewAfterChanged(Category selectedCategory) {
        if (selectedCategory != null) {
            inputCategoryName.setText(selectedCategory.getName());
            inputCategoryDescription.setText(selectedCategory.getDescription());
        }
    }

}
