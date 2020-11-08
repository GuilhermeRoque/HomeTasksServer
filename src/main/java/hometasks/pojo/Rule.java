package hometasks.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Rule {
	private int idRule;
	private String name;
	private String description;
	private boolean state;
	private String idUser;
	private int idHome;
	private String date;
	private int value;

	public Rule() {
	}

	public Rule(int idRule, String name, String description, boolean state, String idUser, int idHome, String date, int value) {
		this.idRule = idRule;
		this.name = name;
		this.description = description;
		this.state = state;
		this.idUser = idUser;
		this.idHome = idHome;
		this.date = date;
		this.value = value;
	}

	public Rule(String name, String description, boolean state, String idUser, int idHome, String date, int value) {
		this.name = name;
		this.description = description;
		this.state = state;
		this.idUser = idUser;
		this.idHome = idHome;
		this.date = date;
		this.value = value;
	}


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getIdRule() {
		return idRule;
	}

	public void setIdRule(int idRule) {
		this.idRule = idRule;
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

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public int getIdHome() {
		return idHome;
	}

	public void setIdHome(int idHome) {
		this.idHome = idHome;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Rule{" +
				"idRule=" + idRule +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", state=" + state +
				", idUser='" + idUser + '\'' +
				", idHome=" + idHome +
				", date='" + date + '\'' +
				", value=" + value +
				'}';
	}
}
