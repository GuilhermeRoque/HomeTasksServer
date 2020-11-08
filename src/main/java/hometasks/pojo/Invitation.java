package hometasks.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Invitation {
	private String idGuest;
	private String idInviting;
	private int idHome;
	private boolean invitingIsInitiator;
	private boolean state;


	public Invitation(String idGuest, String idInviting, int idHome, boolean invitingIsInitiator, boolean state) {
		this.idGuest = idGuest;
		this.idInviting = idInviting;
		this.idHome = idHome;
		this.invitingIsInitiator = invitingIsInitiator;
		this.state = state;
	}

	public String getIdGuest() {
		return idGuest;
	}

	public void setIdGuest(String idGuest) {
		this.idGuest = idGuest;
	}

	public String getIdInviting() {
		return idInviting;
	}

	public void setIdInviting(String idInviting) {
		this.idInviting = idInviting;
	}

	public int getIdHome() {
		return idHome;
	}

	public void setIdHome(int idHome) {
		this.idHome = idHome;
	}

	public boolean isInvitingIsInitiator() {
		return invitingIsInitiator;
	}

	public void setInvitingIsInitiator(boolean invitingIsInitiator) {
		this.invitingIsInitiator = invitingIsInitiator;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Invitation{" +
				"idGuest='" + idGuest + '\'' +
				", idInviting='" + idInviting + '\'' +
				", idCasa=" + idHome +
				", invitingIsInitiator=" + invitingIsInitiator +
				", state=" + state +
				'}';
	}
}
