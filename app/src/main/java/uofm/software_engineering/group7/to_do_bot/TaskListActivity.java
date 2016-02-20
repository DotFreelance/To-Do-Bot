package uofm.software_engineering.group7.to_do_bot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


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
    }

    public void onAddClick(View v){
        creator.listManager.addTask("");
    }
}
