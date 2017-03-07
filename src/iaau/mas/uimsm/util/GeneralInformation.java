package iaau.mas.uimsm.util;

public class GeneralInformation {

	private String idnumber;
	

	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	
	@Override
    public String toString() {
        return "ModelGeneralInfo [" + " idNumber=" + idnumber + "]";
    }
	
}
