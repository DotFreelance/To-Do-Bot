package uofm.software_engineering.group7.to_do_bot.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.db.TaskListContract;
import uofm.software_engineering.group7.to_do_bot.db.TaskModel;
import uofm.software_engineering.group7.to_do_bot.model.Task;

/**
 * Created by thuongle on 4/4/16.
 * Version
 */
public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<Task> tasks;
    private final TaskModel taskModel;
    private OnItemClickedListener mOnItemClickListener;
    private OnItemRemoved onItemRemoved;

    public TaskListAdapter(Context context, List<Task> tasks, TaskModel taskModel) {
        this.context = context;
        this.tasks = tasks;
        this.taskModel = taskModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TaskViewHolder) holder).bindView(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks != null ? tasks.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickedListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemRemoved(OnItemRemoved onItemRemoved) {
        this.onItemRemoved = onItemRemoved;
    }

    public void update(Task task) {
        //update the existing task
        int index = 0;
        for (int i = 0, size = tasks.size(); i < size; i++) {
            if (task.getId() == tasks.get(i).getId()) {
                index = i;
                break;
            }
        }
        tasks.remove(index);
        tasks.add(index, task);

        Collections.sort(tasks);
        notifyDataSetChanged();
    }

    public void add(Task task) {
        tasks.add(task);
        Collections.sort(tasks);
        notifyDataSetChanged();
    }

    public void remove(Task task) {
        tasks.remove(task);
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final CheckBox itemChecked;
        private final TextView textName;
        private final TextView textAlarm;
        private final ImageButton buttonDelete;
        private final ImageView imagePriority;
        private final ImageView imageDescription;

        public TaskViewHolder(View view) {
            super(view);
            // Get the elements of the list item
            this.view = view;
            itemChecked = (CheckBox) view.findViewById(R.id.itemChecked);
            textName = (TextView) view.findViewById(R.id.textTaskName);
            textAlarm = (TextView) view.findViewById(R.id.textTaskAlarm);
            buttonDelete = (ImageButton) view.findViewById(R.id.deleteButton);
            imagePriority = (ImageView) view.findViewById(R.id.imagePriority);
            imageDescription = (ImageView) view.findViewById(R.id.image_description);
        }

        public void bindView(final Task task) {
            // Populate the elements
            itemChecked.setChecked(task.isChecked());
            textName.setText(task.getName());
            // Set alarm time
            if (task.getAlarmTime() != null) {
                String[] alarmTime = task.getAlarmTime().split(";");
                String formattedDate = alarmTime[1] + " on " + alarmTime[0];
                textAlarm.setText(formattedDate);
            } else {
                textAlarm.setText(R.string.no_alarm_set);
            }

            switch (task.getPriority()) {
                case TaskListContract.TaskListItemSchema.PRIORITY_NONE:
                    imagePriority.setImageResource(R.mipmap.none);
                    break;
                case TaskListContract.TaskListItemSchema.PRIORITY_MEDIUM:
                    imagePriority.setImageResource(R.mipmap.medium);
                    break;
                case TaskListContract.TaskListItemSchema.PRIORITY_HIGH:
                    imagePriority.setImageResource(R.mipmap.high);
                    break;
            }

            if (task.getImageDescription() != null) {
                imageDescription.setImageBitmap(task.getImageDescription());
                imageDescription.setVisibility(View.VISIBLE);
            } else {
                imageDescription.setVisibility(View.GONE);
            }
            itemChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    task.setChecked(true);
                    taskModel.check(task);
                    notifyDataSetChanged();
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskModel.remove(task);
                    remove(task);
                    if (onItemRemoved != null) {
                        onItemRemoved.onItemRemove();
                    }
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(task);
                    }
                }
            });
        }
    }

    public interface OnItemClickedListener {
        void onItemClick(Task task);
    }

    public interface OnItemRemoved {
        void onItemRemove();
    }
}
