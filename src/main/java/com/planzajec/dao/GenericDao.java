package com.planzajec.dao;

import java.util.List;

public interface GenericDao <T> {

    T findOne(final int id);

    List<T> findAll();

    void saveOrUpdate(final T entity);

    void delete(final T entity);

    List<T> findAllOrderBy(String orderBy, boolean asc);

}
