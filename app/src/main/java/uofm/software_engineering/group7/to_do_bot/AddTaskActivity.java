package uofm.software_engineering.group7.to_do_bot;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Lam Doan on 3/27/16.
 * Version
 */
public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TIME_PATTERN = "HH:mm";
    public static final String EXTRA_TASK_NAME = "EXTRA_TASK_NAME";
    public static final String EXTRA_TASK_DESCRIPTION = "EXTRA_TASK_DESCRIPTION";
    public static final String EXTRA_TASK_PRIORITY = "EXTRA_TASK_PRIORITY";
    public static final String EXTRA_TASK_CATEGORY = "EXTRA_TASK_CATEGORY";
    public static final String EXTRA_TASK_ALARM_TIME = "EXTRA_TASK_ALARM_TIME";
    public static final String EXTRA_TASK_ID = "EXTRA_TASK_ID";

    private EditText inputTaskName;
    private EditText inputTaskDescription;
    private Spinner spinnerCategory;
    private RadioGroup radioTaskPriority;
    private Button yourDateButton;
    private Button yourTimeButton;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private long mId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputTaskName = (EditText) findViewById(R.id.taskName);
        inputTaskDescription = (EditText) findViewById(R.id.taskDescription);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        radioTaskPriority = (RadioGroup) findViewById(R.id.radioTaskPriority);
        yourDateButton = (Button) findViewById(R.id.button_pick_day);
        yourTimeButton = (Button) findViewById(R.id.button_pick_time);

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());


        if (savedInstanceState == null) {
            mId = getIntent().getLongExtra(EXTRA_TASK_ID, -1);
            String taskName = getIntent().getStringExtra(EXTRA_TASK_NAME);
            inputTaskName.setText(taskName);
            String taskDescription = getIntent().getStringExtra(EXTRA_TASK_DESCRIPTION);
            inputTaskDescription.setText(taskDescription);
            int taskPriority = getIntent().getIntExtra(EXTRA_TASK_PRIORITY, 0);
            if (taskPriority == 0) {
                ((RadioButton) findViewById(R.id.radioTaskNone)).setChecked(true);
            } else if (taskPriority == 1) {
                ((RadioButton) findViewById(R.id.radioTaskLow)).setChecked(true);
            } else {
                ((RadioButton) findViewById(R.id.radioTaskHigh)).setChecked(true);
            }

            int taskCategory = getIntent().getIntExtra(EXTRA_TASK_CATEGORY, 0);
            spinnerCategory.setSelection(taskCategory);

            String alarmTime = getIntent().getStringExtra(EXTRA_TASK_ALARM_TIME);
            if (alarmTime != null) {
                String[] alarmTimeSplit = alarmTime.split(";");
                yourDateButton.setText(alarmTimeSplit[0]);
                if (alarmTime.length() > 0) {
                    yourTimeButton.setText(alarmTimeSplit[1]);
                }
            }
        } else {
            mId = savedInstanceState.getLong(EXTRA_TASK_ID);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_TASK_ID, mId);
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }

    public void pickTime(View view) {
        TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
    }

    public void pickDate(View view) {
        DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
    }

    public void removeAlarm(View view){
        yourDateButton.setText(R.string.pick_a_date);
        yourTimeButton.setText(R.string.pick_a_time);

        String alarmDate = yourDateButton.getText().toString();
        String alarmTime = yourTimeButton.getText().toString();

        Intent intent = getIntent();
        intent.putExtra(EXTRA_TASK_ALARM_TIME, alarmDate + ";" + alarmTime);

        setResult(RESULT_OK, intent);
    }

    public void save(View view) {
        String taskName = inputTaskName.getText().toString();
        if (TextUtils.isEmpty(taskName)) {
            inputTaskName.setError(getString(R.string.error_field_empty));
            inputTaskName.requestFocus();
        } else {
            inputTaskName.setError(null);

            String taskDescription = inputTaskDescription.getText().toString();
            long category = spinnerCategory.getSelectedItemId();
            int checkedRadioButtonId = radioTaskPriority.getCheckedRadioButtonId();
            int priority;
            if (checkedRadioButtonId == R.id.radioTaskHigh) {
                priority = 2;
            } else if (checkedRadioButtonId == R.id.radioTaskLow) {
                priority = 1;
            } else {
                priority = 0;
            }

            String alarmDate = yourDateButton.getText().toString();
            String alarmTime = yourTimeButton.getText().toString();

            Intent intent = getIntent();

            intent.putExtra(EXTRA_TASK_NAME, taskName);
            intent.putExtra(EXTRA_TASK_DESCRIPTION, taskDescription);
            intent.putExtra(EXTRA_TASK_PRIORITY, priority);
            intent.putExtra(EXTRA_TASK_CATEGORY, category);
            intent.putExtra(EXTRA_TASK_ALARM_TIME, alarmDate + ";" + alarmTime);

            setResult(RESULT_OK, intent);

            finish();
        }
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void update() {
        yourDateButton.setText(dateFormat.format(calendar.getTime()));
        yourTimeButton.setText(timeFormat.format(calendar.getTime()));
    }


    //Keyboard minimize when clicking outside of text box
    @Override
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
