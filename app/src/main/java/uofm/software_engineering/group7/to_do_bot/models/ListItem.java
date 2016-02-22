package uofm.software_engineering.group7.to_do_bot.models;

/**
 * Created by Faye on 2/2/2016.
 *
 */
interface ListItem {
    public void setTaskDescription(String newTaskDescription);
    public String getTaskDescription();
    public void clearTaskDescription();
}
