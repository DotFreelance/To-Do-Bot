package uofm.software_engineering.group7.to_do_bot.db;

import java.util.List;

import uofm.software_engineering.group7.to_do_bot.model.Task;


public interface TaskModel extends BaseModel<Task> {

    List<Task> getTasksByCategory(long categoryId);

    boolean updateTaskDescription(Task task);

    boolean updatePriority(Task task);

    boolean check(Task task);
}
