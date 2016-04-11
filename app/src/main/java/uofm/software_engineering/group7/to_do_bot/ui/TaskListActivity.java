package uofm.software_engineering.group7.to_do_bot.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.db.DbHelper;
import uofm.software_engineering.group7.to_do_bot.db.TaskModel;
import uofm.software_engineering.group7.to_do_bot.db.TaskModelImpl;
import uofm.software_engineering.group7.to_do_bot.model.Category;
import uofm.software_engineering.group7.to_do_bot.model.Task;
import uofm.software_engineering.group7.to_do_bot.receiver.AlarmManagerBroadcastReceiver;
import uofm.software_engineering.group7.to_do_bot.ui.adapter.TaskListAdapter;
import uofm.software_engineering.group7.to_do_bot.ui.widget.DividerItemDecoration;
import uofm.software_engineering.group7.to_do_bot.utils.AppUtils;

/*
Created by Lam Doan
3/26/2016
 */
public class TaskListActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY = "category";
    private static final int REQUEST_ADD_OR_EDIT_TASK = 1;

    private Context context;
    private AlarmManagerBroadcastReceiver alarm;
    private Category mCategory;

    private RecyclerView recyclerView;
    private TaskModel taskModel;
    private TaskListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            mCategory = getIntent().getParcelableExtra(EXTRA_CATEGORY);
        } else {
            mCategory = savedInstanceState.getParcelable(EXTRA_CATEGORY);
        }
        setTitle(mCategory.getName());
        alarm = new AlarmManagerBroadcastReceiver();
        taskModel = new TaskModelImpl(new DbHelper(this));
        setupViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_CATEGORY, mCategory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  REQUEST_ADD_OR_EDIT_TASK) {
            if (resultCode == RESULT_OK) {
                Task task = data.getParcelableExtra(AddOrEditTaskActivity.EXTRA_TASK);
                byte[] imageByte = data.getByteArrayExtra(AddOrEditTaskActivity.EXTRA_IMAGE_AS_BYTE);
                if (imageByte != null) {
                    Bitmap icon = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                    task.setImageDescription(icon);
                }
                //if task is an update from the original task
                if (task.getAlarmTime() != null) {
                    AppUtils.setAlarmTime(this, task.getAlarmTime(),task.getName());
                }
                if (task.getId() != 0) {
                    taskModel.update(task);
                    adapter.update(task);
                } else {//if it is new task
                    taskModel.add(task);
                    if (adapter.getItemCount() == 0) {
                        findViewById(R.id.view_empty).setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    adapter.add(task);
                }
                if (task.getCategoryId() != mCategory.getId()) {//it is not in this category
                    adapter.remove(task);
                    mCategory.setNumberOfTasks(mCategory.getNumberOfTasks() - 1);
                    if (adapter.getItemCount() == 0) {
                        findViewById(R.id.view_empty).setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    mCategory.setNumberOfTasks(mCategory.getNumberOfTasks() + 1);
                }
            }
        }
    }

    private void setupViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);

        List<Task> tasks = taskModel.getTasksByCategory(mCategory.getId());

        if (tasks.isEmpty()) {
            findViewById(R.id.view_empty).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        setupRecyclerView(tasks);
    }

    private void setupRecyclerView(List<Task> tasks) {
        adapter = new TaskListAdapter(this, tasks, taskModel);
        adapter.setOnItemClickListener(new TaskListAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(context, AddOrEditTaskActivity.class);
                intent.putExtra(AddOrEditTaskActivity.EXTRA_TASK, task);
                if (task.getImageDescription() != null) {
                    intent.putExtra(AddOrEditTaskActivity.EXTRA_IMAGE_AS_BYTE, AppUtils.getBitmapAsByteArray(task.getImageDescription()));
                    task.setImageDescription(null);
                }
                startActivityForResult(intent, REQUEST_ADD_OR_EDIT_TASK);
            }
        });
        adapter.setOnItemRemoved(new TaskListAdapter.OnItemRemoved() {
            @Override
            public void onItemRemove() {
                mCategory.setNumberOfTasks(mCategory.getNumberOfTasks() - 1);
                setTitle(mCategory.getName());
                if (adapter.getItemCount() == 0) {
                    findViewById(R.id.view_empty).setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void addNewTask(View view) {
        Intent intent = new Intent(context, AddOrEditTaskActivity.class);
        intent.putExtra(AddOrEditTaskActivity.EXTRA_ORIGIN_CATEGORY, mCategory.getId());
        startActivityForResult(intent, REQUEST_ADD_OR_EDIT_TASK);
    }
}
