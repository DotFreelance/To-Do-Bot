package uofm.software_engineering.group7.to_do_bot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


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
        ListView list = (ListView)v;

        creator.listManager.initFromDB();

        /*
        TODO: Apply a ListView Adaptor that will tie the TaskList to the ListView.
        See: http://developer.android.com/guide/topics/ui/layout/listview.html
        */
    }

    public void onAddClick(View v){
        creator.listManager.addTask("A default description for testing");
    }
}
