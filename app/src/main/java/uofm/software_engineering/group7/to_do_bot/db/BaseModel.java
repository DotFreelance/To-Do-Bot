package uofm.software_engineering.group7.to_do_bot.db;

import java.util.ArrayList;

/**
 * Created by thuongle on 4/3/16.
 * Version
 */
public interface BaseModel<T> {

    ArrayList<T> getAll();

    T add(T object);

    boolean remove(T object);

    boolean update(T object);
}
