package com.mycompany.recipeaggregator.repository;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {

    List<T> list();

    T insert(T entity);

    T update(T entity);

    void delete(int id) ;
}
