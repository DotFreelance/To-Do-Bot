package uofm.software_engineering.group7.to_do_bot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import uofm.software_engineering.group7.to_do_bot.services.TaskListItemAdapter;


public class TaskListActivity extends AppCompatActivity
{
    private ComponentsCreator creator;
    private SimpleCursorAdapter listAdaptor;
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

        creator.listManager.initFromDB(v.getContext());

        listView.setAdapter(creator.listManager.getAdapter());
        listView.addFooterView(LayoutInflater.from(v.getContext()).inflate(R.layout.task_list_footer, null, false));
    }

    public void onAddClick(View v){
        // TODO: Modify addTask to take actual user data
        creator.listManager.addTask(v.getContext(), "A default description for testing");
    }
}
