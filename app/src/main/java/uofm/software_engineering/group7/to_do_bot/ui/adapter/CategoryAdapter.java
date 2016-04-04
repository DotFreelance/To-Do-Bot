package uofm.software_engineering.group7.to_do_bot.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import uofm.software_engineering.group7.to_do_bot.R;
import uofm.software_engineering.group7.to_do_bot.model.Category;
import uofm.software_engineering.group7.to_do_bot.model.Task;
import uofm.software_engineering.group7.to_do_bot.ui.TaskListActivity;

/**
 * Created by thuongle on 4/3/16.
 * Version
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindView(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public void update(Task task) {
        for (Category category : categories) {
            if (task.getId() == category.getId()) {
                category.setNumberOfTasks(category.getNumberOfTasks() + 1);
                notifyDataSetChanged();
            }
        }
    }

    public void refresh(List categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public void add(Category newCategory) {
        this.categories.add(newCategory);
        notifyItemInserted(this.categories.size() - 1);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textCategoryName;
        private final TextView textCategoryDescription;
        private final TextView textNumberOfTasks;

        public ViewHolder(View view) {
            super(view);
            textCategoryName = (TextView) view.findViewById(R.id.text_name);
            textCategoryDescription = (TextView) view.findViewById(R.id.text_description);
            textNumberOfTasks = (TextView) view.findViewById(R.id.text_number_item);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TaskListActivity.class);
                    intent.putExtra(TaskListActivity.EXTRA_CATEGORY, categories.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }

        void bindView(Category category) {
            textCategoryName.setText(category.getName());
            if (!TextUtils.isEmpty(category.getDescription())) {
                textCategoryDescription.setText(category.getDescription());
                textCategoryDescription.setVisibility(View.VISIBLE);
            } else {
                textCategoryDescription.setVisibility(View.GONE);
            }

            textNumberOfTasks.setText(category.getNumberOfTasks() + " tasks");
        }
    }
}
