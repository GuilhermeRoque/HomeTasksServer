package db;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class DAO {
    public static boolean persist(Object obj) {
        EntityManager dbManager = DBManagerFactory.getDbManager();
        try {
            EntityTransaction transaction = dbManager.getTransaction();
            transaction.begin();
            dbManager.persist(obj);
            transaction.commit();
        }catch (Exception e){
            System.err.println(e.toString());
            return false;
        }
        return true;
    }

    public static <T> T find(Class<T> entityClass, Object primaryKey) {
        EntityManager dbManager = DBManagerFactory.getDbManager();
        T t = null;
        try {
            EntityTransaction transaction = dbManager.getTransaction();
            transaction.begin();
            t = dbManager.find(entityClass, primaryKey);
        }catch (Exception e){
            System.err.println(e.toString());
        }
        return t;
    }

}
