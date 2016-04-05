package uofm.software_engineering.group7.to_do_bot.db;

import java.util.ArrayList;

public interface BaseModel<T> {

    ArrayList<T> getAll();

    T add(T object);

    boolean remove(T object);

    boolean update(T object);
}
