package hometasks.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Blob;

@XmlRootElement
public class User {

	private String idUser;
	private String name;
	private String date;
	private String gender;
	private int points;
	private String telephone;
	private String password;
	private String email;
	private String profile;
	private int idHome;
	private Blob picture;
	private String token;

	public User(){}

	public User(String idUser, String name, String date, String gender, int points, String telephone, String password, String email, String profile, int idHome, Blob picture, String token) {
		this.idUser = idUser;
		this.name = name;
		this.date = date;
		this.gender = gender;
		this.points = points;
		this.telephone = telephone;
		this.password = password;
		this.email = email;
		this.profile = profile;
		this.idHome = idHome;
		this.picture = picture;
		this.token = token;
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

	public int getIdHome() {
		return idHome;
	}

	public void setIdHome(int idHome) {
		this.idHome = idHome;
	}

	public Blob getPicture() {
		return picture;
	}

	public void setPicture(Blob picture) {
		this.picture = picture;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}

