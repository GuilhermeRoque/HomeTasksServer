package pojo;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Blob;
import java.util.List;

@XmlRootElement
@Entity
@Table
public class Home implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHome;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "rent",nullable = false)
    private int rent;

    @Column(name = "adress",nullable = false)
    private String adress;

    @Column(name = "picture",nullable = true)
    private Blob picture;

    @OneToMany(targetEntity = Task.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "idHome")
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(name = "User_has_Home",
            joinColumns = @JoinColumn(name = "Home_idHome"),
            inverseJoinColumns = @JoinColumn(name = "User_idUser"))
    private List<User> users;


    public Home(int idHome, String name, String description, int rent, String adress, Blob picture) {
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

    public Blob getPicture() {
        return picture;
    }

    public void setPicture(Blob picture) {
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
