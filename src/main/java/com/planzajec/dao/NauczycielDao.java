package com.planzajec.dao;

import com.planzajec.entity.Nauczyciel;

import java.util.List;

public interface NauczycielDao extends GenericDao<Nauczyciel> {
    List<Nauczyciel> findAllOrderBy(String orderBy, boolean asc);
    List<Nauczyciel> findAll();

}
