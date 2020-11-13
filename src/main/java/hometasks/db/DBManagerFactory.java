package hometasks.db;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TransactionFactory {
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("HomeTasksServer");

    public static EntityManager getDbManager() {
        return entityManagerFactory.createEntityManager();
    }


}
