package iaau.mas.uimsm.util;

public class RegistrationConfirm {
	
	private String idnumber;
	private String requestedSubject;
	public String getIdnumber() {
		return idnumber;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	public String getRequestedSubject() {
		return requestedSubject;
	}
	public void setRequestedSubject(String requestedSubject) {
		this.requestedSubject = requestedSubject;
	}
	@Override
	public String toString() {
		return "RegistrationConfirm [idnumber=" + idnumber
				+ ", requestedSubject=" + requestedSubject + "]";
	}
	
	

}
