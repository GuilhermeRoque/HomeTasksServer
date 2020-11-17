package db;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBManagerFactory {
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("HomeTasksServer");

    public static EntityManager getDbManager() {
        return entityManagerFactory.createEntityManager();
    }


}
