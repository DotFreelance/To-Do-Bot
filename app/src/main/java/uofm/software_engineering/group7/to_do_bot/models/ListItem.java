package uofm.software_engineering.group7.to_do_bot.models;

/**
 * Created by Faye on 2/2/2016.
 */
public interface ListItem {
    public void editValue(String newValue);
    public String getValue();
    public void clear();
}
