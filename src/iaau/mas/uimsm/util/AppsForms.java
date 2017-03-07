package iaau.mas.uimsm.util;

public class AppsForms {
	
	private String idnumber;
    private String reference;
    private String language;
    private String fullname;
    private String requestTime;
	
    
    
	public String getIdnumber() {
		return idnumber;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	@Override
	public String toString() {
		return "AppsForms [idnumber=" + idnumber + ", reference=" + reference
				+ ", language=" + language + ", fullname=" + fullname
				+ ", requestTime=" + requestTime + "]";
	}
	
	


}
