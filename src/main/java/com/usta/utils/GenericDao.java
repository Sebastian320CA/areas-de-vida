package com.usta.utils;
import java.sql.SQLException;
import java.util.List;

public interface GenericDao <T>{
    public void add(T obj) throws SQLException;
    T getById(Integer id) throws SQLException;
    List<T> getAll() throws SQLException;
    public void update (T obj) throws SQLException;
    public void delete(Integer obj) throws SQLException;

}
