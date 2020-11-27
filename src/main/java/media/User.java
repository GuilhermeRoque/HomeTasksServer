package media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table
public class User implements Serializable{

	@Id
	@Column(name="idUser",unique = true)
	private String idUser;

	@Column(name="name",nullable = false)
	private String name;

	@Column(name="date",nullable = false)
	private String date;

	@Column(name="gender",nullable = false)
	private String gender;

	@Column(name="points",nullable = false)
	private int points;

	@Column(name="telephone",nullable = false)
	private String telephone;

	@Column(name="password",nullable = false)
	private String password;

	@Column(name="email",nullable = false)
	private String email;

	@Column(name="profile",nullable = false)
	private String profile;

	@JsonIgnore
	@Column(name="picture")
	private byte[] picture;

	@JsonIgnore
	@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
	private List<Home> homes;
	public User(){}

	public User(String idUser, String name, String date, String gender, int points, String telephone, String password, String email, String profile, byte[] picture) {
		this.idUser = idUser;
		this.name = name;
		this.date = date;
		this.gender = gender;
		this.points = points;
		this.telephone = telephone;
		this.password = password;
		this.email = email;
		this.profile = profile;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
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

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public List<Home> getHomes() {
		return homes;
	}

	public void setHomes(List<Home> homes) {
		this.homes = homes;
	}

	@Override
	public String toString() {
		return "User{" +
				"idUser='" + idUser + '\'' +
				", name='" + name + '\'' +
				", date='" + date + '\'' +
				", gender='" + gender + '\'' +
				", points=" + points +
				", telephone='" + telephone + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", profile='" + profile + '\'' +
				", homes=" + homes +
				'}';
	}
}

