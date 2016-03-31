package uofm.software_engineering.group7.to_do_bot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import uofm.software_engineering.group7.to_do_bot.services.AlarmManagerBroadcastReceiver;
import uofm.software_engineering.group7.to_do_bot.models.TaskListItem;
import uofm.software_engineering.group7.to_do_bot.services.TaskListItemAdapter;

/*
Created by Lam Doan
3/26/2016
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_ADD_NEW_TASK = 1;
    private static final int REQUEST_EDIT_TASK = 2;

    private Context context;
    private AlarmManagerBroadcastReceiver alarm;
    private ComponentsCreator creator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        alarm = new AlarmManagerBroadcastReceiver();
        creator = new ComponentsCreator();
        creator.run(getApplicationContext());

        initList(findViewById(R.id.listView));
    }

    private void initList(View list) {
        ListView listView = (ListView) list;
        TaskListItemAdapter adapter = creator.listManager.getAdapter();
        // Initialize the TaskList from the database
        creator.listManager.initFromDB();
        // Apply an adapter for the TaskList -> ListView
        adapter.setOnItemClickListener(new TaskListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TaskListItem item) {
                Intent intent = new Intent(context, AddTaskActivity.class);
                intent.putExtra(AddTaskActivity.EXTRA_TASK_ID, item.getId());
                intent.putExtra(AddTaskActivity.EXTRA_TASK_NAME, item.getTaskName());
                intent.putExtra(AddTaskActivity.EXTRA_TASK_DESCRIPTION, item.getTaskDescription());
                intent.putExtra(AddTaskActivity.EXTRA_TASK_PRIORITY, item.getPriority());
                intent.putExtra(AddTaskActivity.EXTRA_TASK_ALARM_TIME, item.getAlarmTime());
                startActivityForResult(intent, REQUEST_EDIT_TASK);
            }
        });
        listView.setAdapter(adapter);
        // Add the footer that contains the '+' button
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(context, AddTaskActivity.class);
                startActivityForResult(intent, REQUEST_ADD_NEW_TASK);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String taskName = data.getStringExtra(AddTaskActivity.EXTRA_TASK_NAME);
        String taskDescription = data.getStringExtra(AddTaskActivity.EXTRA_TASK_DESCRIPTION);
        int priority = data.getIntExtra(AddTaskActivity.EXTRA_TASK_PRIORITY, 0);
        String alarmTime = data.getStringExtra(AddTaskActivity.EXTRA_TASK_ALARM_TIME);
        long taskId = data.getLongExtra(AddTaskActivity.EXTRA_TASK_ID, -1);
        creator.listManager.getAdapter().setAddMode();

        if (requestCode == REQUEST_ADD_NEW_TASK) {

            if (resultCode == RESULT_OK) {
                creator.listManager.addTask(taskName, taskDescription, priority, alarmTime);
            }
        } else if (requestCode == REQUEST_EDIT_TASK) {
            if (resultCode == RESULT_OK) {
                creator.listManager.updateTask(taskId, taskName, taskDescription, priority, alarmTime);
            }
        }

        Collections.sort(creator.listManager.getList(),TaskListItem.PriorityComparator);
        setAlarmTime(alarmTime);
    }

    private void setAlarmTime(String alarmTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy;HH:mm");
        try {
            Date alarm = simpleDateFormat.parse(alarmTime);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, alarm.getMonth());
            cal.set(Calendar.YEAR, alarm.getYear());
            cal.set(Calendar.DAY_OF_MONTH, alarm.getDay());
            cal.set(Calendar.HOUR_OF_DAY, alarm.getHours());
            cal.set(Calendar.MINUTE, alarm.getMinutes());

            Intent intent = new Intent(this, AlarmManagerBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1253, intent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Alarm worked.", Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Alarm not set.", Toast.LENGTH_LONG).show();
        }
    }
}
