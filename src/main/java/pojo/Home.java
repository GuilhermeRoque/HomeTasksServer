package pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table
public class Home implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idHome;

    @Column(name = "name",nullable = false)
    String name;

    @Column(name = "description",nullable = false)
    String description;

    @Column(name = "rent",nullable = false)
    int rent;

    @Column(name = "adress",nullable = false)
    String adress;

    @Column(name = "picture",nullable = true)
    byte[] picture;

    @JsonIgnore
    @OneToMany(targetEntity = Task.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "idHome")
    public List<Task> tasks;

    @JsonIgnore
    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinTable(name = "User_has_Home",
            joinColumns = @JoinColumn(name = "Home_idHome"),
            inverseJoinColumns = @JoinColumn(name = "User_idUser"))
    public List<User> users;


    public Home(int idHome, String name, String description, int rent, String adress, byte[] picture) {
        this.idHome = idHome;
        this.name = name;
        this.description = description;
        this.rent = rent;
        this.adress = adress;
        this.picture = picture;
    }

    public Home(){}

    public int getIdHome() {
        return idHome;
    }

    public void setIdHome(int idHome) {
        this.idHome = idHome;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
