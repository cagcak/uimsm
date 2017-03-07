package iaau.mas.uimsm.resultant;

import iaau.mas.uimsm.R;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class Activity_Transcript_Detail extends SherlockActivity {

	TextView subject_code;
	TextView subject_name;
	TextView semester;
	TextView year;
	TextView credits;
	TextView average;
	
	Bundle extras;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.transcript_detail_layout);
	    
	    subject_code = (TextView) findViewById(R.id.transcript_subject_code_edited);
		subject_name = (TextView) findViewById(R.id.transcript_subject_name_edited);
		semester = (TextView) findViewById(R.id.transcript_semester_edited);
		year = (TextView) findViewById(R.id.transcript_year_edited);
		credits = (TextView) findViewById(R.id.transcript_credits_edited);
		average = (TextView) findViewById(R.id.transcript_average_edited);
		
		extras = getIntent().getExtras();
		
		if (extras != null)
		{
			subject_code.setText(extras.getString("extra_subject_code"));
			subject_name.setText(extras.getString("extra_subject_name"));
			semester.setText(extras.getString("extra_subject_semester"));
			year.setText(extras.getString("extra_subject_year"));
			credits.setText(extras.getString("extra_subject_credits"));
//			average.setText(extras.getString("extra_subject_average"));
			
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
			
		}
	}
	
}
