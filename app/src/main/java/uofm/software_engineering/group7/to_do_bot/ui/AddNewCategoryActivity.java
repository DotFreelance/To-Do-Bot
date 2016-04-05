package uofm.software_engineering.group7.to_do_bot.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.model.Category;


public class AddNewCategoryActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY = "category";
    private EditText inputCategoryName;
    private EditText inputCategoryDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        findViewById(R.id.spinnerCategory).setVisibility(View.GONE);
        inputCategoryName = (EditText) findViewById(R.id.input_category_name);
        inputCategoryDescription = (EditText) findViewById(R.id.input_category_description);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        cancel(null);
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
            inputCategoryName.setError(null);
            String categoryDescription = inputCategoryDescription.getText().toString();
            Category category = new Category(categoryName, categoryDescription);
            Intent intent = getIntent();
            intent.putExtra(EXTRA_CATEGORY, category);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
