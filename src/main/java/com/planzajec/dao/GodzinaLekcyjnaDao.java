package com.planzajec.dao;

import com.planzajec.entity.GodzinaLekcyjna;

import java.util.List;

public interface GodzinaLekcyjnaDao extends GenericDao<GodzinaLekcyjna> {
    List<GodzinaLekcyjna> findAll(int id, Class k);
    GodzinaLekcyjna findCorresponding(int idCzasTrwania, int idDzien, int callerId, Class callerClass);
    Long size();
}
