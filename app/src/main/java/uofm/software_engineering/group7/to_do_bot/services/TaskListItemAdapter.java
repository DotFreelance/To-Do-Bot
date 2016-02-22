package uofm.software_engineering.group7.to_do_bot.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.models.TaskList;
import uofm.software_engineering.group7.to_do_bot.models.TaskListItem;

/**
 * Created by Paul J on 2016-02-21.
 *
 * This is where the TaskList is converted to a view element in the list.
 *
 */
public class TaskListItemAdapter extends ArrayAdapter<TaskListItem>{
    public TaskListItemAdapter(Context context, TaskList<TaskListItem> taskList){
        super(context, 0, taskList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TaskListItem item = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Get the elements of the list item
        CheckBox itemChecked = (CheckBox)convertView.findViewById(R.id.itemChecked);
        TextView itemDescription = (TextView) convertView.findViewById(R.id.list_item_String);
        // Populate the elements
        itemChecked.setChecked(item.getChecked());
        itemDescription.setText(item.getTaskDescription());
        // Return the completed view
        return convertView;
    }
}
