package com.planzajec.dao.Impl;

import com.planzajec.dao.PrzedmiotDao;
import com.planzajec.entity.Przedmiot;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PrzedmiotDaoImpl extends AbstractGenericDao<Przedmiot> implements PrzedmiotDao {

    @Override
    public Przedmiot findOne(int id) {
        Transaction transaction = getSession().beginTransaction();
        Query query = getSession().createQuery("select p from Przedmiot p join fetch p.nauczyciele where p.id = :id");
        query.setParameter("id", id);
        Przedmiot przedmiot = (Przedmiot) query.getResultList().get(0);

        transaction.commit();

        return przedmiot;
    }

    public List<Przedmiot> findAllOrderBy(String orderBy, boolean asc){
        String query = "select distinct p from Przedmiot p join fetch p.nauczyciele order by p.";
        query += orderBy;
        query += asc ? " asc" : " desc";

        Transaction transaction = getSession().beginTransaction();
        List<Przedmiot> przedmioty =  getSession().createQuery(query).list();

        transaction.commit();

        return przedmioty;
    }

    @Override
    public List<Przedmiot> findAll() {
        Transaction transaction = getSession().beginTransaction();
        List<Przedmiot> przedmioty  = getSession().createQuery("select p from Przedmiot p join fetch p.nauczyciele").list();
        transaction.commit();
        return przedmioty;
    }
}
