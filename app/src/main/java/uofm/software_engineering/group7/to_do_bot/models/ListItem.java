package uofm.software_engineering.group7.to_do_bot.models;

/**
 * Created by Faye on 2/2/2016.
 */
interface ListItem {
    void setTaskDescription(String newTaskDescription);

    String getTaskDescription();

    void clearTaskDescription();
}
