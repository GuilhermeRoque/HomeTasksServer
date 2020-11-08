package hometasks.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Blob;

@XmlRootElement
public class Home {

	private int idHome;
	private String name;
	private String address;
	private int rent;
	private String description;
	private Blob picture;

	public Home() {}

	public Home(int idHome, String name, String address, int rent, String description, Blob picture) {
		this.idHome = idHome;
		this.name = name;
		this.address = address;
		this.rent = rent;
		this.description = description;
		this.picture = picture;
	}

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getRent() {
		return rent;
	}

	public void setRent(int rent) {
		this.rent = rent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Blob getPicture() {
		return picture;
	}

	public void setPicture(Blob picture) {
		this.picture = picture;
	}

	@Override
	public String toString() {
		return "Casa{" +
				"idHome=" + idHome +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", rent=" + rent +
				", description='" + description + '\'' +
				", picture=" + picture +
				'}';
	}
}
