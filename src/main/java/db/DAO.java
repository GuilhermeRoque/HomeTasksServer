package db;



import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DAO{
    private final EntityManager entityManager;
    private final EntityTransaction transaction;

    public DAO(){

        this.entityManager = entityManagerFactory.createEntityManager();
        this.transaction = entityManager.getTransaction();
        this.transaction.begin();
    }

    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("HomeTasksServer");

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public boolean persist(Object obj) {
        boolean ret = true;
        try {
            entityManager.persist(obj);
            transaction.commit();
        }catch (Exception e){
            System.err.println(e.toString());
            ret = false;
        }
        return ret;
    }

    public <T> T find(Class<T> entityClass, Object primaryKey) {
        T t = null;
        try {
            t = entityManager.find(entityClass, primaryKey);
        }catch (Exception e){
            System.err.println(e.toString());
        }
        return t;
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> from = query.from(entityClass);
        CriteriaQuery<T> select = query.select(from);
        TypedQuery<T> typedQuery = entityManager.createQuery(select);
        return typedQuery.getResultList();
    }

    public void close(){
        this.entityManager.close();
    }

}
