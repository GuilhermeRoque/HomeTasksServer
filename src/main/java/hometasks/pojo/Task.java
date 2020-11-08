package hometasks.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Task {
	private  int idTask;
	private String name;
	private String description;
	private String idOwner;
	private String idReporter;
	private String state;
	private String date;
	private float value;
	private boolean alternate;
	private boolean renew;

	public Task(){}

	public Task(int idTask, String name, String description, String idOwner, String idReporter, String state, String date, float value, boolean alternate, boolean renew) {
		this.idTask = idTask;
		this.name = name;
		this.description = description;
		this.idOwner = idOwner;
		this.idReporter = idReporter;
		this.state = state;
		this.date = date;
		this.value = value;
		this.alternate = alternate;
		this.renew = renew;
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
}
