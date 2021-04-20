package com.planzajec.dao.Impl;

import com.planzajec.dao.GodzinaLekcyjnaDao;
import com.planzajec.entity.GodzinaLekcyjna;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class GodzinaLekcyjnaDaoImpl extends AbstractGenericDao<GodzinaLekcyjna> implements GodzinaLekcyjnaDao {

    @Override
    public List<GodzinaLekcyjna> findAll(int id, Class klasaBazowa) {
        String from = ("g." + klasaBazowa.getSimpleName() + ".id").toLowerCase();
        Transaction transaction = getSession().beginTransaction();
        Query query = getSession().createQuery("select g from GodzinaLekcyjna g join fetch g.czasTrwaniaLekcji join fetch g.dzien join fetch g.klasa" +
                " join fetch g.nauczyciel n join fetch g.przedmiot join fetch g.sala join fetch n.przedmioty where " + from + " = :id order by g.dzien.id asc");
        query.setParameter("id", id);
        List result = query.getResultList();

        transaction.commit();

        return result;
    }

    @Override
    public GodzinaLekcyjna findCorresponding(int idCzasTrwania, int idDzien, int callerId, Class callerClass) {
        GodzinaLekcyjna result = null;
        String from = ("g." + callerClass.getSimpleName() + ".id").toLowerCase();
        Transaction transaction = null;
        try (Session sessionForSingleThread = openSession()) {
            transaction = sessionForSingleThread.beginTransaction();

            Query query = sessionForSingleThread.createQuery("select g from GodzinaLekcyjna g join fetch g.czasTrwaniaLekcji join fetch g.dzien join fetch g.klasa" +
                    " join fetch g.nauczyciel n join fetch g.przedmiot join fetch g.sala join fetch n.przedmioty where g.czasTrwaniaLekcji.id = :idCzasTrwania and g.dzien.id = :idDzien" +
                    " and " + from + " = :id");
            query.setParameter("idCzasTrwania", idCzasTrwania);
            query.setParameter("idDzien", idDzien);
            query.setParameter("id", callerId);


            result = (GodzinaLekcyjna) query.getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            transaction.commit();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return result;
    }

    @Override
    public Long size() {

        Transaction transaction = getSession().beginTransaction();
        Long size = (Long) getSession().createQuery("select count(g.id) from GodzinaLekcyjna g").getSingleResult();
        transaction.commit();

        return size;
    }
}
