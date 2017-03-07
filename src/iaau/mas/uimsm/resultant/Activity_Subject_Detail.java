package iaau.mas.uimsm.resultant;

import iaau.mas.uimsm.R;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class Activity_Subject_Detail extends SherlockActivity
{
//	private String var_subject_name;
//	private String var_hours;
//	private String var_midterm;
//	private String var_final1;
//	private String var_average;	
//	private String var_attandance;
//	private String var_semester;
//	private String var_year;
	
	Bundle extras;
	
	TextView subject_name;
	TextView hours;
	TextView midterm;
	TextView final1;
	TextView average;	
	TextView attandance;
	TextView semester;
	TextView year;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.subject_detail_layout);
	     
	    subject_name = (TextView) findViewById(R.id.subject_name_edited);
	 	hours = (TextView) findViewById(R.id.hours_edited);
	 	midterm = (TextView) findViewById(R.id.midterm_edited);
	 	final1 = (TextView) findViewById(R.id.final_edited);
	 	average = (TextView) findViewById(R.id.average_edited);
	 	attandance = (TextView) findViewById(R.id.attandance_edited);
	 	semester = (TextView) findViewById(R.id.semester_edited);
	 	year = (TextView) findViewById(R.id.academic_year_edited);
	 	
	 	
	 	extras = getIntent().getExtras();
	 	
	 	if (extras != null) 
	 	{
	 		subject_name.setText(extras.getString("extra_subject_name"));
	 		hours.setText(extras.getString("extra_subject_hours"));
	 		midterm.setText(extras.getString("extra_subject_midterm"));
	 		final1.setText(extras.getString("extra_subject_final"));
	 		
//	 		int grade = Integer.parseInt(extras.getString("extra_subject_average"));
//	 		if (grade >= 50) 
//	 		{
//	 			final1.setTextColor(getResources().getColor(R.color.average_passed));
//	 			
//	 			final1.setText(extras.getString("extra_subject_average"));
//	 		} else {
//	 			final1.setTextColor(getResources().getColor(R.color.average_failed));
//	 			final1.setText(extras.getString("extra_subject_average"));

//	 		}
	 		
//	 		average.setText(extras.getString("extra_subject_average"));
	 		if( !(extras.getString("extra_subject_average").equals("IP")) )
			{
				int grade = Integer.parseInt(extras.getString("extra_subject_average"));
				if (grade >= 50) 
		 		{
		 			average.setTextColor(getResources().getColor(R.color.average_passed));
		 			average.setText(extras.getString("extra_subject_average"));
		 		} else {
		 			average.setTextColor(getResources().getColor(R.color.average_failed));
		 			average.setText(extras.getString("extra_subject_average"));
		 		}
			} else {
				average.setText(extras.getString("extra_subject_average"));
				average.setTextColor(getResources().getColor(R.color.average_inprogress));
			}
	 		attandance.setText(extras.getString("extra_subject_attandance"));
	 		semester.setText(extras.getString("extra_subject_semester"));
	 		year.setText(extras.getString("extra_subject_year"));
	 	}
	}
	
	
}
