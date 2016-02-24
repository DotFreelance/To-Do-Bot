package uofm.software_engineering.group7.to_do_bot.services;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    // Add mode prevents the Adapter from setting focus during initialization
    private boolean addMode = false;

    public TaskListItemAdapter(Context context, TaskList<TaskListItem> taskList){
        super(context, 0, taskList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final TaskListItem item = getItem(position);
        // This allows us to re-use views
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Get the elements of the list item
        final CheckBox itemChecked = (CheckBox)convertView.findViewById(R.id.itemChecked);
        final TextView itemDescription = (TextView)convertView.findViewById(R.id.list_item_String);

        // Populate the elements
        itemChecked.setChecked(item.getChecked());
        itemDescription.setText(item.getTaskDescription());

        // Add a listener for the checkbox
        itemChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewParent parentView = v.getParent();
                ListView listView = (ListView) parentView.getParent();
                int posn = -1;

                if(parentView instanceof View) {
                    posn = listView.getPositionForView((View) parentView);
                }

                if(posn != -1) {
                    item.check();
                }
            }
        });

        // Add a listener for the soft keyboard's done button to be able to apply the changes
        itemDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // Get the position of the parent
                    View parentRow = (View) v.getParent();
                    ListView listView = (ListView) parentRow.getParent();
                    int position = listView.getPositionForView(parentRow);
                    // Update the item at that position
                    TaskListItem item = getItem(position);
                    item.setTaskDescription(v.getText().toString());
                    // Notify the user that the update has been processed.
                    Toast.makeText(getContext(), getContext().getString(R.string.update_task_success), Toast.LENGTH_SHORT).show();
                    // Remove focus
                    v.clearFocus();
                    // Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(itemDescription.getWindowToken(), 0);
                }
                return false;
            }
        });

        // Provide focus to the item that was added
        if(addMode) {
            itemDescription.post(new Runnable() {
                public void run() {
                    // Request focus
                    itemDescription.requestFocus();
                    // Show keyboard
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(itemDescription, InputMethodManager.SHOW_IMPLICIT);
                }
            });
            addMode = false;
        }

        // Return the completed view
        return convertView;
    }

    // Public availability for setting the "Add Mode" which prevents applying focus on app start
    public void setAddMode(){
        this.addMode = true;
    }
}
