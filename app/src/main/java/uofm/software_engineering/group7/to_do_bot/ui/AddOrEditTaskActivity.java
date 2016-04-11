package uofm.software_engineering.group7.to_do_bot.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.db.TaskListContract;
import uofm.software_engineering.group7.to_do_bot.model.Task;
import uofm.software_engineering.group7.to_do_bot.utils.AppUtils;

/**
 * Created by Lam Doan on 3/27/16.
 * Version
 */
public class AddOrEditTaskActivity extends KeyboardActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = AddOrEditTaskActivity.class.getSimpleName();

    private static final int REQUEST_TAKE_PICTURE = 1;
    private static final int REQUEST_PICK_PHOTO = 2;

    private static final String TIME_PATTERN = "HH:mm";
    public static final String EXTRA_TASK = "task";
    public static final String EXTRA_ORIGIN_CATEGORY = "originCategory";
    public static final String EXTRA_IMAGE_AS_BYTE = "bitmap";

    private EditText inputTaskName;
    private ImageView imageDescription;
    private Button buttonRemovePic;
    private RadioGroup radioTaskPriority;
    private Button yourDateButton;
    private Button yourTimeButton;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    private Task task;
    private Bitmap selectedBitmap;
    private long inCategoryId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        inputTaskName = (EditText) findViewById(R.id.input_task_name);
        imageDescription = (ImageView) findViewById(R.id.image_description);
        buttonRemovePic = (Button) findViewById(R.id.button_remove_picture);
        radioTaskPriority = (RadioGroup) findViewById(R.id.radioTaskPriority);
        yourDateButton = (Button) findViewById(R.id.button_pick_day);
        yourTimeButton = (Button) findViewById(R.id.button_pick_time);

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        byte[] imageByte;
        if (savedInstanceState == null) {
            task = getIntent().getParcelableExtra(EXTRA_TASK);
            inCategoryId = getIntent().getLongExtra(EXTRA_ORIGIN_CATEGORY, -1);
            imageByte = getIntent().getByteArrayExtra(AddOrEditTaskActivity.EXTRA_IMAGE_AS_BYTE);
        } else {
            task = savedInstanceState.getParcelable(EXTRA_TASK);
            imageByte = savedInstanceState.getByteArray(EXTRA_IMAGE_AS_BYTE);
            inCategoryId = savedInstanceState.getLong(EXTRA_ORIGIN_CATEGORY, -1);
        }
        if (imageByte != null) {
            Bitmap icon = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            selectedBitmap = icon;
            task.setImageDescription(icon);
        }
        if (task != null) {
            inputTaskName.setText(task.getName());
            if (task.getImageDescription() != null) {
                imageDescription.setImageBitmap(task.getImageDescription());
                imageDescription.setVisibility(View.VISIBLE);
                selectedBitmap = task.getImageDescription();
                buttonRemovePic.setVisibility(View.VISIBLE);
            }
            switch (task.getPriority()) {
                case TaskListContract.TaskListItemSchema.PRIORITY_NONE:
                    ((RadioButton) findViewById(R.id.radioTaskNone)).setChecked(true);
                    break;
                case TaskListContract.TaskListItemSchema.PRIORITY_MEDIUM:
                    ((RadioButton) findViewById(R.id.radioTaskMedium)).setChecked(true);
                    break;
                case TaskListContract.TaskListItemSchema.PRIORITY_HIGH:
                    ((RadioButton) findViewById(R.id.radioTaskHigh)).setChecked(true);
                    break;
            }

            String alarmTime = task.getAlarmTime();
            if (alarmTime != null) {
                String[] alarmTimeSplit = alarmTime.split(";");
                yourDateButton.setText(alarmTimeSplit[0]);
                if (alarmTime.length() > 0) {
                    yourTimeButton.setText(alarmTimeSplit[1]);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_TASK, task);
        if (selectedBitmap != null) {
            outState.putByteArray(EXTRA_IMAGE_AS_BYTE, AppUtils.getBitmapAsByteArray(selectedBitmap));
        }
        outState.putLong(EXTRA_ORIGIN_CATEGORY, inCategoryId);
    }

    @Override
    public boolean onNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
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

    public void removeAlarm(View view) {
        yourDateButton.setText(R.string.pick_a_date);
        yourTimeButton.setText(R.string.pick_a_time);
        if (task != null) {
            task.setAlarmTime(null);
        }
    }

    public void save(View view) {
        if (task == null) {
            task = new Task();
        }
        String taskName = inputTaskName.getText().toString();
        if (TextUtils.isEmpty(taskName)) {
            inputTaskName.setError(getString(R.string.error_field_empty));
            inputTaskName.requestFocus();
        } else {
            inputTaskName.setError(null);
            task.setName(taskName);

            saveCategory();
            savePriority();
            saveAlarm();
        }
    }

    private void saveCategory() {
        if (inCategoryId != -1) {
            task.setCategoryId(inCategoryId);
        }
    }

    private void savePriority() {
        int checkedRadioButtonId = radioTaskPriority.getCheckedRadioButtonId();
        int priority;
        if (checkedRadioButtonId == R.id.radioTaskHigh) {
            priority = TaskListContract.TaskListItemSchema.PRIORITY_HIGH;
        } else if (checkedRadioButtonId == R.id.radioTaskMedium) {
            priority = TaskListContract.TaskListItemSchema.PRIORITY_MEDIUM;
        } else {
            priority = TaskListContract.TaskListItemSchema.PRIORITY_NONE;
        }
        task.setPriority(priority);
        task.setImageDescription(selectedBitmap);
    }

    private void saveAlarm() {
        String alarmDate = yourDateButton.getText().toString();
        String alarmTime = yourTimeButton.getText().toString();
        boolean isAlarmValid = false;
        String textPicDate = getString(R.string.pick_a_date);
        String textPickTime = getString(R.string.pick_a_time);
        if (!alarmDate.equals(textPicDate) && !alarmTime.equals(textPickTime)) {
            isAlarmValid = true;
            task.setAlarmTime(alarmDate + ";" + alarmTime);
        } else if (alarmDate.equals(textPicDate) && !alarmTime.equals(textPickTime)) {
            Toast.makeText(this, "Your alarm date cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (!alarmDate.equals(textPicDate) && alarmTime.equals(textPickTime)) {
            Toast.makeText(this, "Your alarm time cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            //in case of alarmDate.equals(getString(R.string.pick_a_date)) && alarmTime.equals(getString(R.string.pick_a_time)
            isAlarmValid = true;
        }

        if (isAlarmValid) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_TASK, task);
            if (task.getImageDescription() != null) {
                intent.putExtra(EXTRA_IMAGE_AS_BYTE, AppUtils.getBitmapAsByteArray(task.getImageDescription()));
                task.setImageDescription(null);
            }
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

    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
        }
    }

    public void pickPhoto(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/jpeg");
        startActivityForResult(photoPickerIntent, REQUEST_PICK_PHOTO);
    }

    public void removePic(View view) {
        buttonRemovePic.setVisibility(View.GONE);
        selectedBitmap = null;
        if(task != null) {
            task.setImageDescription(null);
        }
        imageDescription.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = data.getData();
                        Bitmap bitmap = AppUtils.decodeUri(this, selectedImage);
                        selectedBitmap = bitmap;
                        if (imageDescription != null) {
                            imageDescription.setImageBitmap(bitmap);
                            imageDescription.setVisibility(View.VISIBLE);
                            buttonRemovePic.setVisibility(View.VISIBLE);
                        }
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "Error in choose avatar profile");
                        Toast.makeText(this, "Your file not found", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case REQUEST_TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if (imageBitmap != null) {
                        selectedBitmap = imageBitmap;
                        imageDescription.setImageBitmap(imageBitmap);
                        imageDescription.setVisibility(View.VISIBLE);
                        buttonRemovePic.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }

    }
}
