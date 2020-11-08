package hometasks.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Payment {



	public Payment() {
	}

    private int idPayment;
	private String idDebtor;
	private String idCreditor;
	private int interest;
	private float value;
	private String date;
	private String description;
	private boolean done;

	public Payment(int idPayment, String idDebtor, String idCreditor, int interest, float value, String date, String description, boolean done) {
		this.idPayment = idPayment;
		this.idDebtor = idDebtor;
		this.idCreditor = idCreditor;
		this.interest = interest;
		this.value = value;
		this.date = date;
		this.description = description;
		this.done = done;
	}

	public Payment(String idDebtor, String idCreditor, int interest, float value, String date, String description) {
		this.idDebtor = idDebtor;
		this.idCreditor = idCreditor;
		this.interest = interest;
		this.value = value;
		this.date = date;
		this.description = description;
	}

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getIdDebtor() {
		return idDebtor;
	}

	public void setIdDebtor(String idDebtor) {
		this.idDebtor = idDebtor;
	}

	public String getIdCreditor() {
		return idCreditor;
	}

	public void setIdCreditor(String idCreditor) {
		this.idCreditor = idCreditor;
	}

	public int getInterest() {
		return interest;
	}

	public void setInterest(int interest) {
		this.interest = interest;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIdPayment() {
		return idPayment;
	}

	public void setIdPayment(int idPayment) {
		this.idPayment = idPayment;
	}

	@Override
	public String toString() {
		return "Payment{" +
				"idPayment=" + idPayment +
				", idDebtor='" + idDebtor + '\'' +
				", idCreditor='" + idCreditor + '\'' +
				", interest=" + interest +
				", value=" + value +
				", date='" + date + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
