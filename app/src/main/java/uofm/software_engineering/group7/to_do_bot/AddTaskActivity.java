package uofm.software_engineering.group7.to_do_bot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
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
import java.util.Collections;
import java.util.Locale;

import uofm.software_engineering.group7.to_do_bot.models.TaskListItem;
import uofm.software_engineering.group7.to_do_bot.models.TaskListManager;

/**
 * Created by thuongle on 3/27/16.
 * Version
 */
public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TIME_PATTERN = "HH:mm";
    public static final String EXTRA_TASK_NAME = "EXTRA_TASK_NAME";
    public static final String EXTRA_TASK_DESCRIPTION = "EXTRA_TASK_DESCRIPTION";
    public static final String EXTRA_TASK_PRIORITY = "EXTRA_TASK_PRIORITY";
    public static final String EXTRA_TASK_ALARM_TIME = "EXTRA_TASK_ALARM_TIME";
    public static final String EXTRA_TASK_ID = "EXTRA_TASK_ID";

    private EditText inputTaskName;
    private EditText inputTaskDescription;
    private RadioGroup radioTaskPriority;
    private TextView textYourDate;
    private TextView textYourTime;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private long mId;
    private TaskListManager taskListManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputTaskName = (EditText) findViewById(R.id.taskName);
        inputTaskDescription = (EditText) findViewById(R.id.taskDescription);
        radioTaskPriority = (RadioGroup) findViewById(R.id.radioTaskPriority);
        textYourDate = (TextView) findViewById(R.id.textYourDate);
        textYourTime = (TextView) findViewById(R.id.textYourTime);

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

            String alarmTime = getIntent().getStringExtra(EXTRA_TASK_ALARM_TIME);
            if (alarmTime != null) {
                String[] alarmTimeSplit = alarmTime.split(";");
                textYourDate.setText(alarmTimeSplit[0]);
                if (alarmTime.length() > 0) {
                    textYourTime.setText(alarmTimeSplit[1]);
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
        textYourDate.setText("" + "Your Date");
        textYourTime.setText("" + "Your Time");

        String alarmDate = textYourDate.getText().toString();
        String alarmTime = textYourTime.getText().toString();

        Intent intent = getIntent();
        intent.putExtra(EXTRA_TASK_ALARM_TIME, alarmDate + ";" + alarmTime);

        setResult(RESULT_OK, intent);
        //finish();
    }

    public void save(View view) {
        String taskName = inputTaskName.getText().toString();
        if (TextUtils.isEmpty(taskName)) {
            inputTaskName.setError(getString(R.string.error_field_empty));
            inputTaskName.requestFocus();
        } else {
            inputTaskName.setError(null);

            String taskDescription = inputTaskDescription.getText().toString();
            int checkedRadioButtonId = radioTaskPriority.getCheckedRadioButtonId();
            int priority;
            if (checkedRadioButtonId == R.id.radioTaskHigh) {
                priority = 2;
            } else if (checkedRadioButtonId == R.id.radioTaskLow) {
                priority = 1;
            } else {
                priority = 0;
            }

            String alarmDate = textYourDate.getText().toString();
            String alarmTime = textYourTime.getText().toString();

            Intent intent = getIntent();

            intent.putExtra(EXTRA_TASK_NAME, taskName);
            intent.putExtra(EXTRA_TASK_DESCRIPTION, taskDescription);
            intent.putExtra(EXTRA_TASK_PRIORITY, priority);
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
        textYourDate.setText(dateFormat.format(calendar.getTime()));
        textYourTime.setText(timeFormat.format(calendar.getTime()));
    }
}
