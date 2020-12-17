package media;

import javax.persistence.*;

@Entity
@Table
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idTask;

	@Column(name="name",nullable = false)
	private String name;

	@Column(name="description",nullable = false)
	private String description;

	@Column(name="state",nullable = false)
	private String state;

	@Column(name="date",nullable = false)
	private String date;

	@Column(name="value",nullable = false)
	private float value;

	@Column(name="alternate",nullable = false)
	private boolean alternate;

	@Column(name="renew",nullable = false)
	private boolean renew;

	@Column(name="idOwner",nullable = false)
	private String idOwner;

	@Column(name="idReporter",nullable = false)
	private String idReporter;

	@Column(name="idHome",nullable = false)
	private int idHome;

	public Task(){}

	public Task(int idTask, String name, String description, String state, String date, float value, boolean alternate, boolean renew, String idOwner, String idReporter, int idHome) {
		this.idTask = idTask;
		this.name = name;
		this.description = description;
		this.state = state;
		this.date = date;
		this.value = value;
		this.alternate = alternate;
		this.renew = renew;
		this.idOwner = idOwner;
		this.idReporter = idReporter;
		this.idHome = idHome;
	}

	public int getIdTask() {
		return idTask;
	}

	public void setIdTask(int idTask) {
		this.idTask = idTask;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public boolean isAlternate() {
		return alternate;
	}

	public void setAlternate(boolean alternate) {
		this.alternate = alternate;
	}

	public boolean isRenew() {
		return renew;
	}

	public void setRenew(boolean renew) {
		this.renew = renew;
	}

	public String getIdOwner() {
		return idOwner;
	}

	public void setIdOwner(String idOwner) {
		this.idOwner = idOwner;
	}

	public String getIdReporter() {
		return idReporter;
	}

	public void setIdReporter(String idReporter) {
		this.idReporter = idReporter;
	}

	public int getIdHome() {
		return idHome;
	}

	public void setIdHome(int idHome) {
		this.idHome = idHome;
	}
}
