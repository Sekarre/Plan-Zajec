package com.planzajec.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Logger;

public abstract class HibernateUtil {
    private static SessionFactory sessionFactory;

    private static void buildSessionFactory() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            Logger.getGlobal().info("----Połączenie udane----");
        } catch (Throwable ex) {
            Logger.getGlobal().info("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) buildSessionFactory();
        return sessionFactory;
    }

}
