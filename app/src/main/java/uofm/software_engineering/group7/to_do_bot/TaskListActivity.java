package uofm.software_engineering.group7.to_do_bot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class TaskListActivity extends AppCompatActivity
{
    private ComponentsCreator creator;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        creator = new ComponentsCreator();
        creator.run(getApplicationContext());

        initList(findViewById(R.id.listView));
    }

    public void initList(View v){
        ListView listView = (ListView)v;
        ArrayAdapter adapter = creator.listManager.getAdapter();
        // Initialize the TaskList from the database
        creator.listManager.initFromDB(v.getContext());
        // Apply an adapter for the TaskList -> ListView
        listView.setAdapter(adapter);
        // Add the footer that contains the '+' button
        listView.addFooterView(LayoutInflater.from(v.getContext()).inflate(R.layout.task_list_footer, null, false));
    }

    public void onAddClick(View v){
        creator.listManager.getAdapter().setAddMode();
        creator.listManager.addTask(v.getContext(), "");
    }
}
