package com.planzajec.dao;

import com.planzajec.entity.Przedmiot;

import java.util.List;

public interface PrzedmiotDao extends GenericDao<Przedmiot> {
    List<Przedmiot> findAllOrderBy(String orderBy, boolean asc);
    List<Przedmiot> findAll();
}
