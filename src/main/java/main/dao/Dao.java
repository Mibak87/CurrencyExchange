package main.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    List<T> getAll() throws SQLException;
    void save(T t);
    void update(T t);
    T getById(int id);
}
