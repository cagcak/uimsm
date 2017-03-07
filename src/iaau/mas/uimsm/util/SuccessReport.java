package iaau.mas.uimsm.util;

public class SuccessReport {

	
	private String idnumber;
    private String year;
    private String semester;
	
    
    
    public String getIdnumber() {
		return idnumber;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
    
	@Override
    public String toString() 
    {
        return "SuccessReportRequest [idnumber=" + idnumber + ", year=" + year + ", semester=" + semester + "]";
    }
}
