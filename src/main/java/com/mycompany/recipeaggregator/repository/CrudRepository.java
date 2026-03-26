package com.mycompany.recipeaggregator.repository;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {

    List<T> list() throws SQLException;

    T insert(T entity) throws SQLException;

    T update(T entity) throws SQLException;

    void delete(int id) throws SQLException;
}
