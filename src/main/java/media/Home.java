package media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Home implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idHome;

    String name;
    String description;
    int rent;
    String address;

    @JsonIgnore
    String picture;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idHome")
    Set<Task> tasks = new HashSet<>();

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "homes", fetch = FetchType.LAZY, targetEntity = User.class)
    Set<User> users = new HashSet<>();


    public Home(int idHome, String name, String description, int rent, String address, String picture) {
        this.idHome = idHome;
        this.name = name;
        this.description = description;
        this.rent = rent;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }


    @Override
    public String toString() {
        return "Home{" +
                "idHome=" + idHome +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rent=" + rent +
                ", address='" + address + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
