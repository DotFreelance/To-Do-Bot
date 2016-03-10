package uofm.software_engineering.group7.to_do_bot.services;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
    private int currentFocus;

    // Add mode prevents the Adapter from setting focus during initialization
    private boolean addMode = false;
    // TODO: Create a task list manager

    public TaskListItemAdapter(TaskListManager listManager, Context context, TaskList<TaskListItem> taskList){
        super(context, 0, taskList);
        this.currentFocus = -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final TaskListItem item = getItem(position);
        final int currentPosition = position;
        // This allows us to re-use views
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Get the elements of the list item
        final CheckBox itemChecked = (CheckBox)convertView.findViewById(R.id.itemChecked);
        final TextView itemDescription = (TextView)convertView.findViewById(R.id.list_item_String);
        final ImageButton itemDelete = (ImageButton)convertView.findViewById(R.id.deleteButton);

        // Populate the elements
        itemChecked.setChecked(item.getChecked());
        itemDescription.setText(item.getTaskDescription());

        // Add a listener for the checkbox
        itemChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.check(itemChecked.isChecked());
            }
        });


        itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewParent parent = v.getParent();
                ListView listView = (ListView) parent.getParent();

                int index = -1;

                if (parent instanceof View) {
                    index = listView.getPositionForView((View) parent);
                } else {
                    System.out.println("Not a List View");
                }

                if (index != -1) {
                    // TODO: call removeTask()
                }
            }
        });

        // Add a listener for the soft keyboard's done button to be able to apply the changes
        itemDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    updateTaskItemDescription(v, currentPosition);
                    // Clear focus
                    v.clearFocus();
                    // Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(itemDescription.getWindowToken(), 0);
                    // Remove the current focus
                    setCurrentFocus(-1);
                }
                return false;
            }
        });

        // Update the item when focus is lost
        itemDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateTaskItemDescription((TextView) v, currentPosition);
                    // Remove the current focus
                    setCurrentFocus(-1);
                } else {
                    // Set the current focus
                    setCurrentFocus(currentPosition);
                }
            }
        });

        // Provide focus to the item that was added or the appropriate item
        if(this.getCount()-1 == position && addMode) {
            itemDescription.post(new Runnable() {
                public void run() {
                    // Request focus
                    itemDescription.requestFocus();
                    // Show keyboard
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(itemDescription, InputMethodManager.SHOW_IMPLICIT);
                    // Set the current focus
                    setCurrentFocus(currentPosition);
                }
            });
            addMode = false;
        } else if(this.getCurrentFocus() == -1){
            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(itemDescription.getWindowToken(), 0);
        }

        // Return the completed view
        return convertView;
    }

    private void updateTaskItemDescription(TextView v, int position) {
        // Update the item at that position if it has been changed
        String enteredText = v.getText().toString();
        TaskListItem item = getItem(position);
        if(!enteredText.equals(item.getTaskDescription())) {
            item.setTaskDescription(enteredText);
            // Notify the user that the update has been processed.
            Toast.makeText(getContext(), getContext().getString(R.string.update_task_success), Toast.LENGTH_SHORT).show();
        }
    }

    // Public availability for setting the "Add Mode" which prevents applying focus on app start
    public void setAddMode(){
        this.addMode = true;
    }

    public void setCurrentFocus(int index){
        this.currentFocus = index;
    }

    public int getCurrentFocus(){
        return this.currentFocus;
    }

}
