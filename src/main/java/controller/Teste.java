package controller;


import media.DAO;
import media.Home;
import media.User;

import java.util.Scanner;

public class Teste {
    public static void main(String[] args) {
        User roque1 = DAO.find(User.class, "Roque1");
        System.out.println(roque1.getHomes());
        Integer i = 1;
        Object o = i;
        Home home = DAO.find(Home.class, o);
        System.out.println(home);

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter username");
        String userName = myObj.nextLine();  // Read user input
    }
}
