package com.planzajec.dao.Impl;

import com.planzajec.dao.NauczycielDao;
import com.planzajec.entity.Nauczyciel;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class NauczycielDaoImpl extends AbstractGenericDao<Nauczyciel> implements NauczycielDao {

    @Override
    public Nauczyciel findOne(int id) {
        Transaction transaction = getSession().beginTransaction();
        Query query = getSession().createQuery("select n from Nauczyciel n join fetch n.przedmioty where n.id = :id");
        query.setParameter("id", id);
        Nauczyciel nauczyciel = (Nauczyciel) query.getResultList().get(0);

        transaction.commit();

        return nauczyciel;
    }

    public List<Nauczyciel> findAllOrderBy(String orderBy, boolean asc){
        String query = "select distinct n from Nauczyciel n join fetch n.przedmioty order by n.";
        query += orderBy;
        query += asc ? " asc" : " desc";

        Transaction transaction = getSession().beginTransaction();
        List<Nauczyciel> nauczyciele = getSession().createQuery(query).list();

        transaction.commit();

        return nauczyciele;
    }

    @Override
    public List<Nauczyciel> findAll() {
        Transaction transaction = getSession().beginTransaction();
        List<Nauczyciel> nauczyciele  = getSession().createQuery("select distinct n from Nauczyciel n join fetch n.przedmioty").list();
        transaction.commit();
        return nauczyciele;
    }
}
