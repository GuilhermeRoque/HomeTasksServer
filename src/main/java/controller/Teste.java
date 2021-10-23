package controller;


import media.Home;
import media.JPAUtils;
import media.User;
import media.UserCategory;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Teste {
    public static void main(String[] args) {
        User user = new User("Roque", "Roque2", LocalDate.now(),"telephone", "password", "email", UserCategory.Adult,"");
        Home home = new Home();
        home.setAddress("vai reto toda vida");
        home.setDescription("casa legal");
        home.setRent(100);
        home.setName("casa dos roque");

        Set<Home> homeSet = new HashSet<>();
        homeSet.add(home);
        user.setHomes(homeSet);

        EntityManager entityManager = JPAUtils.entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(home);
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
//        JPAUtils.persist(home);
//        JPAUtils.update(user);
        System.out.println(user);
        System.out.println(user.getHomes());
        User user1 = JPAUtils.find(user.getClass(), user.getIdUser());
        System.out.println(user1);
        System.out.println(user1.getHomes());

//        User roque1 = DAO.find(User.class, "Roque1");
//        System.out.println(roque1.getHomes());
//        Integer i = 1;
//        Object o = i;
//        Home home = DAO.find(Home.class, o);
//        System.out.println(home);
//
//        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//        System.out.println("Enter username");
//        String userName = myObj.nextLine();  // Read user input
    }
}
