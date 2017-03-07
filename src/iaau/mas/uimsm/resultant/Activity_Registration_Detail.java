package iaau.mas.uimsm.resultant;

import iaau.mas.uimsm.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class Activity_Registration_Detail extends SherlockActivity
{
	
	TextView subject_code;
	TextView subject_name;
	TextView semester;
	TextView year;
	TextView hours;
	TextView credits;
	TextView registration_status;
	
	Bundle extras;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_requested_subject_detail_layout);
		
		subject_code = (TextView) findViewById(R.id.registration_subject_code_edited);
		subject_name = (TextView) findViewById(R.id.registration_subject_name_edited);
		semester = (TextView) findViewById(R.id.registration_semester_edited);
		year = (TextView) findViewById(R.id.registration_year_edited);
		hours = (TextView) findViewById(R.id.registration_hours_edited);
		credits = (TextView) findViewById(R.id.registration_credits_edited);
		registration_status = (TextView) findViewById(R.id.registration_status_edited);
		
		extras = getIntent().getExtras();
		
		if (extras != null)
		{
			subject_code.setText(extras.getString("subjectcode"));
			subject_name.setText(extras.getString("subjectname"));
			semester.setText(extras.getString("semester"));
			year.setText(extras.getString("year"));
			hours.setText(extras.getString("hours"));
			credits.setText(extras.getString("credits"));
			registration_status.setText(extras.getString("registrationstatus"));
		} else {
			Log.e("Bundle object is empty","");
		}
	}
	
	/*@Override
	public void onBackPressed()
	{
		extras.clear();
	}*/
	
}
