package iaau.mas.uimsm.util;

import java.util.ArrayList;

public class SuccessReportModel {
	
	ArrayList<ResponseResult> SuccessReport_Response;

	
	public ArrayList<ResponseResult> getSuccessReport_Response() {
		return SuccessReport_Response;
	}

	public void setSuccessReport_Response(
			ArrayList<ResponseResult> successReport_Response) {
		SuccessReport_Response = successReport_Response;
	}


	public static class ResponseResult
	{
		private String subject_name;
	    private String hours;
	    private String midterm;
	    private String final1;
	    private String average;
	    private String attandance;
	    private String semester;
	    private String year;
	    
		public String getYear() {
			return year;
		}
		public void setYear(String year) {
			this.year = year;
		}
		public String getSubject_name() {
			return subject_name;
		}
		public void setSubject_name(String subject_name) {
			this.subject_name = subject_name;
		}
		public String getHours() {
			return hours;
		}
		public void setHours(String hours) {
			this.hours = hours;
		}
		public String getMidterm() {
			return midterm;
		}
		public void setMidterm(String midterm) {
			this.midterm = midterm;
		}
		public String getFinal1() {
			return final1;
		}
		public void setFinal1(String final1) {
			this.final1 = final1;
		}
		public String getAverage() {
			return average;
		}
		public void setAverage(String average) {
			this.average = average;
		}
		public String getAttandance() {
			return attandance;
		}
		public void setAttandance(String attandance) {
			this.attandance = attandance;
		}
		public String getSemester() {
			return semester;
		}
		public void setSemester(String semester) {
			this.semester = semester;
		}
	    
	    
	}
}
