package hometasks.db;
import hometasks.pojo.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class UserDAO {

	public boolean createUser(User user) {
		EntityManager dbManager = DBManagerFactory.getDbManager();
		try {
			EntityTransaction transaction = dbManager.getTransaction();
			transaction.begin();
			dbManager.persist(user);
			transaction.commit();
		}catch (Exception e){
			System.err.println(e.toString());
			return false;
		}
		return true;
	}


	public User getUser(String idUser) {
		EntityManager dbManager = DBManagerFactory.getDbManager();
		User user = null;
		try {
			EntityTransaction transaction = dbManager.getTransaction();
			transaction.begin();
			user = dbManager.find(User.class, idUser);
		}catch (Exception e){
			System.err.println(e.toString());
		}
		return user;
	}
}
