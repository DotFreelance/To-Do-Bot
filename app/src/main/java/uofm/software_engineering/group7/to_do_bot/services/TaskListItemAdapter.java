package uofm.software_engineering.group7.to_do_bot.services;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.models.TaskList;
import uofm.software_engineering.group7.to_do_bot.models.TaskListItem;
import uofm.software_engineering.group7.to_do_bot.models.TaskListManager;

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

        // Add a listener for the soft keyboard's done button to be able to apply the changes
        itemDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // Get the position of the parent
                    View parentRow = (View)v.getParent();
                    ListView listView = (ListView)parentRow.getParent();
                    int position = listView.getPositionForView(parentRow);
                    // Update the item at that position
                    TaskListItem item = getItem(position);
                    item.setTaskDescription(v.getText().toString());
                }
                return false;
            }
        });

        // Populate the elements
        itemChecked.setChecked(item.getChecked());
        itemDescription.setText(item.getTaskDescription());
        // Return the completed view
        return convertView;
    }
}
