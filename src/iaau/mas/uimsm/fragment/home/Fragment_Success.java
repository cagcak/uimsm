package iaau.mas.uimsm.fragment.home;

import iaau.mas.uimsm.LoginActivity;
import iaau.mas.uimsm.R;
import iaau.mas.uimsm.pdf.GenerateSuccessReport;
import iaau.mas.uimsm.resultant.Activity_Subject_Detail;
import iaau.mas.uimsm.util.SuccessReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.google.gson.stream.JsonReader;

/**
 * Created by Administrator on 13.02.2014.
 */
public class Fragment_Success extends SherlockListFragment implements ActionBar.OnNavigationListener
{
    private final String servlet = LoginActivity.local_address + "/UIMS/PostSuccessReportResponse";
    private static String pdfLocation = GenerateSuccessReport.FILE_ADDRESS;
    
    private static SuccessReport model;
    
    private JsonReader reader;
    
    private static Map<String, HashMap<String, String>> outer_map = new HashMap<String, HashMap<String, String>>();
    private static Map<String, String> inner_map = new HashMap<String, String>();
    
    private static String subject_name;
    private static String hours;
    private static String midterm;
    private static String final1;
    private static String average;
    private static String attandance;
    private static String semester;
    private static String aca_year;
    
    ArrayAdapter<String> listAdapter;
    ListView success_listView;
    ToggleButton fall_switch;
    ToggleButton spring_switch;
    Button generatePDF;
    Button retrieveData;
    
    private static ArrayList<String> list_subject_name = new ArrayList<String>();
    @SuppressWarnings("unused")
	private static String[] academic_year_array;
    private static ArrayAdapter<CharSequence> academic_year_list;
    
    private static String year_request = "2012-2013"; // initialization of current year
    private static String semester_request = "spring"; // initialization of current semester

    

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onPause() 
    {
      Log.e("DEBUG", "OnPause of loginFragment");
      super.onPause();
//      academic_year_list.clear(); // Unsupported operation
      getSherlockActivity().getSupportActionBar().getNavigationMode();
      getSherlockActivity().closeOptionsMenu();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
    	View rootView = inflater.inflate(R.layout.layout_fragment_success, container, false);
    	
    	success_listView = (ListView) rootView.findViewById(android.R.id.list);
        fall_switch = (ToggleButton) rootView.findViewById(R.id.fall_toggle);
        spring_switch = (ToggleButton) rootView.findViewById(R.id.spring_toggle);
        generatePDF = (Button) rootView.findViewById(R.id.make_pdf_button);
        retrieveData = (Button) rootView.findViewById(R.id.retrieve_data_button);
        
        academic_year_array = getResources().getStringArray(R.array.academic_year);
        Context context = getSherlockActivity().getSupportActionBar().getThemedContext();
        academic_year_list = ArrayAdapter.createFromResource(context, R.array.academic_year, R.layout.sherlock_spinner_item);
        academic_year_list.setDropDownViewResource(com.actionbarsherlock.R.layout.sherlock_spinner_dropdown_item);

        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSherlockActivity().getSupportActionBar().setListNavigationCallbacks(academic_year_list, this);
        
        // Initially we take the current semester subjects
        fall_switch.setChecked(false);  // Fall OFF
        spring_switch.setChecked(true);  // Spring ON
        getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(4); // "2012-2013"
        
        HttpAsyncTask task1 = new HttpAsyncTask();
        task1.execute(servlet);
        
        updateListView();
        
        
        fall_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) 
                {
                	spring_switch.setChecked(false);
                	semester_request = "fall";
                	Log.d("1-Semester selection: ",semester_request);
                } else if (!isChecked) {
                	spring_switch.setChecked(true);
                	Log.d("2-Semester selection: ",semester_request);
                }
            }
        });

        spring_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                	fall_switch.setChecked(false);
                	semester_request = "spring";
                	Log.d("3-Semester selection: ",semester_request);
                } else if (!isChecked) {
                	fall_switch.setChecked(true);
                	Log.d("4-Semester selection: ",semester_request);
                }
            }
        });
        

        retrieveData.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				HttpAsyncTask task2 = new HttpAsyncTask();
		        task2.execute(servlet);
		        
		        updateListView();
			}
		});
        
        generatePDF.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
                GenerateSuccessReport makePDF = new GenerateSuccessReport();
                makePDF.generatePDF();
                viewGeneratedPDF();
            }
        });
        
        
        return rootView;
    }
    

    
    public String POST(String url, SuccessReport model)
    {
        InputStream inputStream;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json;

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("idnumber", model.getIdnumber());
            jsonObject.accumulate("year", model.getYear());
            jsonObject.accumulate("semester", model.getSemester());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            //httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert input stream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

//            Log.v("result", result);
//
//            Log.v("servlet", servlet);

        } catch (ClientProtocolException e) {
            Log.w("HTTP2:", e);
        }  catch (IOException e) {
            Log.w("HTTP3:", e);
        } catch (Exception e) {
            Log.e("Unknown Exception", e.getLocalizedMessage());
        }

        return result;
    }
    
    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line;
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
    
    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle("Please wait!");
            mProgressDialog.setMessage("Data is in progress");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {
        	SharedPreferences sharedPreferences1 = getSherlockActivity().getSharedPreferences("UserCredentials", 0);
            String idNumber = sharedPreferences1.getString("idnumber_key","").toString();
        	
            model = new SuccessReport();
            
            model.setIdnumber(idNumber);
            model.setYear(year_request);
            model.setSemester(semester_request);
//            Log.v("Exact Servlet address", urls[0]);

            return POST(urls[0],model);
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            if ( result.equals("") )
            {
            	Toast.makeText(getSherlockActivity().getBaseContext(), "DATA NOT RECEIVED", Toast.LENGTH_SHORT).show();
                Log.d("-----------", "DATA NOT RECEIVED");
            } else {
            	Toast.makeText(getSherlockActivity().getBaseContext(), "DATA RECEIVED", Toast.LENGTH_SHORT).show();
            	Log.d("result", result);    
            	
            	try {
					parse(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
            	
            	for( int i=1; i<=outer_map.size(); i++ )
            	{
            		list_subject_name.add(outer_map.get(String.valueOf(i)).get("subject_name"));
            	}
            	listAdapter.notifyDataSetChanged();
                	mProgressDialog.dismiss();  

            }
        }
    }
    
    private void parse(String in) throws IOException
    {
    	reader = new JsonReader(new StringReader(in));
//    	reader.setLenient(true);
    	
    	reader.beginObject(); // {
        reader.nextName(); // NodeCounter starting 1
        reader.beginObject(); // {
        
        int nodeCounter = 1;
        while(reader.hasNext())
        {    
            reader.nextName();
            reader.beginObject(); // {
            
	            reader.nextName(); // "subject_name":
	            subject_name = reader.nextString().toString(); // value
	            inner_map.put("subject_name", subject_name);
            
	            reader.nextName(); // "hours"
	            hours = reader.nextString().toString(); // value
	            inner_map.put("hours", hours);
	            
	            reader.nextName(); // "midterm":
	            midterm = reader.nextString().toString(); // value
	            inner_map.put("midterm", midterm);
	            
	            reader.nextName(); // "final":
	            final1 = reader.nextString().toString(); // value
	            inner_map.put("final", final1);
	            
	            reader.nextName(); // "average":
	            average = reader.nextString().toString(); // value
	            inner_map.put("average", average);
	            
	            reader.nextName(); // "attandance":
	            attandance = reader.nextString().toString(); // value
	            inner_map.put("attandance", attandance);
	            
	            reader.nextName(); // "semester":
	            semester = reader.nextString().toString(); // value
	            inner_map.put("semester", semester);
	            
	            reader.nextName(); // "academic_year":
	            aca_year = reader.nextString().toString(); // value
	            inner_map.put("year", aca_year);
	            
            
            reader.endObject(); // }
            
            outer_map.put(String.valueOf(nodeCounter), (HashMap<String, String>) inner_map);
            inner_map = new HashMap<String, String>();
            nodeCounter++;
        }
        
        reader.endObject();  // }
        reader.endObject(); // }
    }
    
    public void onListItemClick(ListView l, View v, int position, long id) 
    {
        String selected_subject_item_name = outer_map.get(String.valueOf(position+1)).get("subject_name");
    	Log.i("","Subject name = " + outer_map.get(String.valueOf(position+1)).get("subject_name") + "Subject list position = " +String.valueOf(position));
        showDetails(selected_subject_item_name);
    }
    
    private void showDetails(String subjectParameter)
    {
    	Intent intent = new Intent(getSherlockActivity().getApplicationContext(), Activity_Subject_Detail.class);
    	
    	for( int i=1; i<=outer_map.size(); i++ )
    	{
    		if( (outer_map.get(String.valueOf(i)).containsValue(subjectParameter) ) )
    		{
    			intent.putExtra("extra_subject_name", outer_map.get(String.valueOf(i)).get("subject_name"));
    			intent.putExtra("extra_subject_hours", outer_map.get(String.valueOf(i)).get("hours"));
    			intent.putExtra("extra_subject_midterm", outer_map.get(String.valueOf(i)).get("midterm"));
    			intent.putExtra("extra_subject_final", outer_map.get(String.valueOf(i)).get("final"));
    			intent.putExtra("extra_subject_average", outer_map.get(String.valueOf(i)).get("average"));
    			intent.putExtra("extra_subject_attandance", outer_map.get(String.valueOf(i)).get("attandance"));
    			intent.putExtra("extra_subject_semester", outer_map.get(String.valueOf(i)).get("semester"));
    			intent.putExtra("extra_subject_year", outer_map.get(String.valueOf(i)).get("year"));
    		} else {
            	Log.e(subjectParameter, "Empty!");
            }
    	}
    	startActivity(intent);
    }
    
    public Bundle sendUserBundle()
    {
    	Bundle extras = new Bundle();
    	extras.putSerializable("outer", (Serializable) outer_map);
    	extras.putSerializable("inner", (Serializable) inner_map);
    	
    	return extras;
    }
    
    private void viewGeneratedPDF()
    {
    	pdfLocation = GenerateSuccessReport.FILE_ADDRESS;
    	
    	File file = new File(pdfLocation);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/pdf";
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent); 
    }
    
    private void updateListView()
    {
    	outer_map.clear();
    	list_subject_name.clear();
        listAdapter = new ArrayAdapter<String>(getSherlockActivity(), R.layout.success_report_list_item_layout , R.id.success_report_list_item , list_subject_name );
        success_listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) 
    {	
    	switch(itemPosition) 
    	{
    	    case 0:
                year_request = "2008-2009";
                Log.i("year_request is ", year_request);
                return true;
            case 1:
            	year_request = "2009-2010";
                Log.i("year_request is ", year_request);
                return true;
            case 2:
                year_request = "2010-2011";
                Log.i("year_request is ", year_request);
                return true;
            case 3:
            	year_request = "2011-2012";
                Log.i("year_request is ", year_request);
                return true;
            case 4:
                year_request = "2012-2013";
                Log.i("year_request is ", year_request);
                return true;
            default:
            	Log.e("default case", "no selection performed");
                break;
        }
    	
    	
        return true;
    }
   

}