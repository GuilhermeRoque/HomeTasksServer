package hometasks.pojo;

import java.sql.Blob;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Comment {

	private int idComment;
	private String text;
	private Blob midia;
	private String date;
	private int idTask;
	private String idUser;

	public Comment(){}

	public Comment(int idComment, String text, Blob midia, String date, int idTask, String idUser) {
		this.idComment = idComment;
		this.text = text;
		this.midia = midia;
		this.date = date;
		this.idTask = idTask;
		this.idUser = idUser;
	}

	public Comment(String text, Blob midia, String date, int idTask, String idUser) {
		this.text = text;
		this.midia = midia;
		this.date = date;
		this.idTask = idTask;
		this.idUser = idUser;
	}

	public int getIdComment() {
		return idComment;
	}

	public void setIdComment(int idComment) {
		this.idComment = idComment;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Blob getMidia() {
		return midia;
	}

	public void setMidia(Blob midia) {
		this.midia = midia;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getIdTask() {
		return idTask;
	}

	public void setIdTask(int idTask) {
		this.idTask = idTask;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	@Override
	public String toString() {
		return "Comment{" +
				"text='" + text + '\'' +
				", midia=" + midia +
				", date='" + date + '\'' +
				", idTask=" + idTask +
				", idUser='" + idUser + '\'' +
				'}';
	}
}
