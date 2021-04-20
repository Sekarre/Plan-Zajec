package com.planzajec.dao.Impl;

import com.planzajec.dao.GenericDao;
import com.planzajec.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractGenericDao<T> implements GenericDao<T> {

    private final Class<T> entityClass;
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public AbstractGenericDao() {
        this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public Session openSession() {
        return this.sessionFactory.openSession();
    }

    @Override
    public T findOne(int id) {
        Transaction transaction = getSession().beginTransaction();
        T t = (T) getSession().find(this.entityClass, id);
        transaction.commit();
        return t;
    }

    @Override
    public List<T> findAll() {
        Transaction transaction = getSession().beginTransaction();
        List t = getSession().createQuery("from " + this.entityClass.getName()).list();
        transaction.commit();
        return t;
    }


    @Override
    public void saveOrUpdate(T entity) {
        Transaction transaction = getSession().beginTransaction();
        getSession().saveOrUpdate(entity);
        transaction.commit();
    }

    @Override
    public void delete(T entity) {
        Transaction transaction = getSession().beginTransaction();
        getSession().delete(entity);
        transaction.commit();
    }

    @Override
    public List<T> findAllOrderBy(String orderBy, boolean asc) {
        Transaction transaction = getSession().beginTransaction();

        String query = "from " + this.entityClass.getName() + " order by " + orderBy;

        List t = null;

        query += asc ? " ASC" : " DESC";

        try {
            t = getSession().createQuery(query).list();
        } catch (Exception e){
            Logger.getGlobal().info("Niepoprawne zapytanie w com.planzajec.dao.Impl.findAllOrderBy");
            transaction.commit();
            return Collections.emptyList();
        }

        transaction.commit();
        return t;
    }

}
