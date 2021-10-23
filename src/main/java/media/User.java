package media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User implements Serializable{

	@Id
	@Column(unique = true)

	private String idUser;
	private String name;
	private LocalDate date;
	private String telephone;
	private String password;
	private String email;
	@Enumerated(EnumType.STRING)
	private UserCategory category;
	@JsonIgnore
	private String picture;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, targetEntity = Home.class)
	@JoinTable(name = "User_has_Home",
			joinColumns = @JoinColumn(name = "User_idUser"),
			inverseJoinColumns = @JoinColumn(name = "Home_idHome"))
	Set<Home> homes = new HashSet<>();


	public User(){}

	public Set<Home> getHomes() {
		return homes;
	}

	public void setHomes(Set<Home> homes) {
		this.homes = homes;
	}

	public User(String idUser, String name, LocalDate date, String telephone, String password, String email, UserCategory category, String picture) {
		this.idUser = idUser;
		this.name = name;
		this.date = date;
		this.telephone = telephone;
		this.password = password;
		this.email = email;
		this.category = category;
		this.picture = picture;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserCategory getCategory() {
		return category;
	}

	public void setCategory(UserCategory profile) {
		this.category = profile;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}



	@Override
	public String toString() {
		return "User{" +
				"idUser='" + idUser + '\'' +
				", name='" + name + '\'' +
				", date='" + date + '\'' +
				", telephone='" + telephone + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", category='" + category + '\'' +
				'}';
	}
}

