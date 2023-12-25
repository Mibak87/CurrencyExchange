package main.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    List<T> getAll() throws SQLException;
    void save(T t) throws SQLException;
    void update(T t) throws SQLException;
    T getById(int id) throws SQLException;
}
