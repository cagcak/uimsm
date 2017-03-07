package iaau.mas.uimsm.fragment.home;

import iaau.mas.uimsm.LoginActivity;
import iaau.mas.uimsm.R;
import iaau.mas.uimsm.resultant.Activity_Registration_Confirm;
import iaau.mas.uimsm.util.Registration;

import java.io.BufferedReader;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

/**
 * Created by Administrator on 13.02.2014.
 */
public class Fragment_Registration extends SherlockListFragment
{
	private final String servlet = LoginActivity.local_address + "/UIMS/PostRegistrationServletResponse";
    
	private static Registration model;
	
	private JsonReader reader;
	
	private static Intent intent;
		
	private static String subject_code;
    private static String subject_name;
    private static String semester;
    private static String year;
    private static String hours;
    private static String credits;
    private static String registration_status;
	
	private static Map<String, HashMap<String, String>> registration_outer_map = new HashMap<String, HashMap<String, String>>();
    private static Map<String, String> registration_inner_map = new HashMap<String, String>();
    
    private static ArrayList<String> unregistered_list_subject_name = new ArrayList<String>();
    private static ArrayList<String> unregistered_list_subject_code = new ArrayList<String>();
    private static ArrayList<String> unregistered_list_semester = new ArrayList<String>();
    private static ArrayList<String> unregistered_list_year = new ArrayList<String>();
    private static ArrayList<String> unregistered_list_hours = new ArrayList<String>();
    private static ArrayList<String> unregistered_list_credits = new ArrayList<String>();
    private static ArrayList<String> unregistered_list_registration_status = new ArrayList<String>();
    
    EditText yearFilter;
	EditText subjectCodeFilter;
	ListView unRegisteredSubjectList;
	Button takeSubject;

	ArrayAdapter<String> registrationListAdapter;
	
	private static String[] subject_name_StringArray;
//	private static String[] subject_code_StringArray;
//	private static String[] subject_semester_StringArray;
//	private static String[] subject_year_StringArray;
//	private static String[] subject_hours_StringArray;
//	private static String[] subject_credits_StringArray;
//	private static String[] subject_registration_status_StringArray;
	
//	private static ArrayList<String> subject_name_StringArrayList; 
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
//        Bundle bundle = new Bundle();
//        bundle.putStringArray("selectedSubjects", outputStringArray);
//        HomeActivity activity = (HomeActivity) getActivity();
//        activity.saveData(BUNDLE_ID, bundle);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.layout_fragment_registration_not_taken, container, false);
		
		yearFilter = (EditText) view.findViewById(R.id.filter_by_year_nottaken);
        subjectCodeFilter = (EditText) view.findViewById(R.id.filter_by_code_edit_nottaken);
        unRegisteredSubjectList = (ListView) view.findViewById(android.R.id.list);
        unRegisteredSubjectList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        takeSubject = (Button) view.findViewById(R.id.take_subject_button);
        
        clearActionBar();
        
        RetrieveUnRegisteredSubjectsAsyncTask task = new RetrieveUnRegisteredSubjectsAsyncTask();
        task.execute(servlet);
        
        updateRegistrationList();
        
        takeSubject.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				registerSubjects();
			}
		});
        
		
		return view;
	}
	
	public void getSelectedSubject()
    {
    	SparseBooleanArray checked = unRegisteredSubjectList.getCheckedItemPositions();
    	ArrayList<String> selectedItems = new ArrayList<String>();
    	
    	for ( int i=0; i<checked.size(); i++ )
    	{
    		// Item position in adapter
    		int position = checked.keyAt(i);
			Log.d("checked subject position:", String.valueOf(checked.keyAt(i)));

    		// Adding element if it is checked 
    		if( checked.valueAt(i) )
    		{
    			selectedItems.add(registrationListAdapter.getItem(position));
    			Log.d("isChecked", registrationListAdapter.getItem(position));
    		}
    	}
    	
    	subject_name_StringArray = new String[selectedItems.size()];
//    	subject_name_StringArrayList = new ArrayList<String>();
    	
    	for( int i=0; i<selectedItems.size(); i++ )
    	{
    		subject_name_StringArray[i] = selectedItems.get(i);
    		Log.d("Selected subject: ", subject_name_StringArray[i]);
//    		subject_name_StringArrayList.add(selectedItems.get(i));
    	}
    }

	@SuppressWarnings("unused")
	private void getSelectedSubjectDetails() 
	{
		/*if( !( subject_name_StringArray.equals("") ) )
		{ 
			Log.d("Subject name list size",String.valueOf(subject_name_StringArray.length));
			
			while( registration_outer_map.keySet().iterator().hasNext() )
			{
//				if( outerCounter <= subject_name_StringArray.length )
//				{
				for(int i=0;i<=subject_name_StringArrayList.size();i++) {
//					if(  )
//							registration_outer_map.entrySet().contains(registration_inner_map.get("subject_name").equals(subject_name_StringArrayList.get(i))))
					{
						Log.i("value",String.valueOf(subject_name_StringArrayList.get(i)));
//					}else {
						Log.e("maalesef","");
//						Log.e("mapte",String.valueOf(registration_outer_map.get(String.valueOf(outerCounter)).get("subject_name")));
//						Log.e("listede",String.valueOf(subject_name_StringArray[outerCounter-1]));
					}}
					if(  subject_name_StringArray.equals(registration_outer_map.get(String.valueOf(outerCounter)).get("subject_name")) )
//					if(  registration_outer_map.get(String.valueOf(outerCounter)).get("subject_name").equals(subject_name_StringArray[outerCounter-1]) )
//					if(  registration_outer_map.get(String.valueOf(outerCounter)).get("subject_name").contains(subject_name_StringArray[outerCounter-1] ) )
//						if( ( registration_outer_map.get(String.valueOf(outerCounter)).get("subject_name") ) == ( subject_name_StringArray[outerCounter-1] ) )
						{Log.i("inside loop","");
							subject_name_StringArray[outerCounter-1] = registration_outer_map.get(String.valueOf(outerCounter)).get("subject_name");
							subject_code_StringArray[outerCounter-1] = registration_outer_map.get(String.valueOf(outerCounter)).get("subject_code");
							subject_semester_StringArray[outerCounter-1] = registration_outer_map.get(String.valueOf(outerCounter)).get("semester");
							subject_year_StringArray[outerCounter-1] = registration_outer_map.get(String.valueOf(outerCounter)).get("year");
							subject_average_StringArray[outerCounter-1] = registration_outer_map.get(String.valueOf(outerCounter)).get("average");
							subject_credits_StringArray[outerCounter-1] = registration_outer_map.get(String.valueOf(outerCounter)).get("credits");
							subject_registration_status_StringArray[outerCounter-1] = registration_outer_map.get(String.valueOf(outerCounter)).get("registration_status");
						} else {
							Log.e("item found in array",String.valueOf(subject_name_StringArray[outerCounter-1]));
							Log.e("item found in map",String.valueOf(registration_outer_map.get(String.valueOf(outerCounter)).get("subject_name")));
						}
//				} else {
					Log.i("size array",String.valueOf(subject_name_StringArray.length));
//					Log.i("counter",String.valueOf(outerCounter));
				}
			}
//		} else {
			Log.e("Subject name list size",String.valueOf(subject_name_StringArray.length));
		} */ 
		
	}
	
	@SuppressWarnings("unused")
	private void getSelectedSubjectDetails2()
	{
		/*if( !(subject_name_StringArrayList.isEmpty()) )
		{
			while( registration_outer_map.keySet().iterator().hasNext() )
			{
				
			}
		} else {
			Log.e("maalesef","2");
		}*/
	}
	
	public void registerSubjects()
    {
    	getSelectedSubject();

//    	getSelectedSubjectDetails();
//    	getSelectedSubjectDetails2();
    	
    	Log.d("Selected subject list length: ", String.valueOf(subject_name_StringArray.length));
    	
    	intent = new Intent(getSherlockActivity().getApplicationContext(), Activity_Registration_Confirm.class);
    	
    	// Creating Bundle object to transfer selected subjects
    	Bundle bundle = new Bundle();
    	bundle.putStringArray("selectedSubjectItemNames", subject_name_StringArray);
    	/*bundle.putStringArray("selectedSubjectItemCodes", subject_code_StringArray);
    	bundle.putStringArray("selectedSubjectItemSemesters", subject_semester_StringArray);
    	bundle.putStringArray("selectedSubjectItemYears", subject_year_StringArray);
    	bundle.putStringArray("selectedSubjectItemHours", subject_hours_StringArray);
    	bundle.putStringArray("selectedSubjectItemCredits", subject_credits_StringArray);
    	bundle.putStringArray("selectedSubjectItemRegStatus", subject_registration_status_StringArray);*/

    	bundle.putSerializable("subjectDetails", (Serializable) registration_inner_map);
    	bundle.putSerializable("subjects", (Serializable) registration_outer_map);
    	// Adding bundle to the intent
    	intent.putExtras(bundle);    	
    	
    	startActivity(intent);
    }

	public String POST(String url, Registration model)
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
            JsonPrimitive js = new JsonPrimitive(model.getIdnumber());

            json = js.toString();

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
	
	private class RetrieveUnRegisteredSubjectsAsyncTask extends AsyncTask<String, Void, String>
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
        	
            model = new Registration();
            
            model.setIdnumber(idNumber);            

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
            	Log.i("Server result",String.valueOf(result));
            	try {
					parse(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
                    
            	for( int i=1; i<=registration_outer_map.size(); i++ )
            	{
            		unregistered_list_subject_name.add(registration_outer_map.get(String.valueOf(i)).get("subject_name"));
            		unregistered_list_subject_code.add(registration_outer_map.get(String.valueOf(i)).get("subject_code"));
            		unregistered_list_semester.add(registration_outer_map.get(String.valueOf(i)).get("semester"));
            		unregistered_list_year.add(registration_outer_map.get(String.valueOf(i)).get("year"));
            		unregistered_list_hours.add(registration_outer_map.get(String.valueOf(i)).get("hours"));
            		unregistered_list_credits.add(registration_outer_map.get(String.valueOf(i)).get("credits"));
            		unregistered_list_registration_status.add(registration_outer_map.get(String.valueOf(i)).get("registration_status"));
            	}
            	registrationListAdapter.notifyDataSetChanged();
                	mProgressDialog.dismiss();  

            }
        }
    }
	
	private void parse(String in) throws IOException
    {
    	reader = new JsonReader(new StringReader(in));
    	
    	reader.beginObject(); // {
        reader.nextName(); // NodeCounter starting 1
        reader.beginObject(); // {
        
        int nodeCounter = 1;
        while(reader.hasNext())
        {    
            reader.nextName();
            reader.beginObject(); // {
            
	            reader.nextName(); // "subject_code"
	            subject_code = reader.nextString().toString(); // value
	            registration_inner_map.put("subject_code", subject_code);
	            
	            reader.nextName(); // "subject_name":
	            subject_name = reader.nextString().toString(); // value
	            registration_inner_map.put("subject_name", subject_name);
	            
	            reader.nextName(); // "semester":
	            semester = reader.nextString().toString(); // value
	            registration_inner_map.put("semester", semester);
	            
	            reader.nextName(); // "year":
	            year = reader.nextString().toString(); // value
	            registration_inner_map.put("year", year);
	            
	            reader.nextName(); // "hours":
	            hours = reader.nextString().toString(); // value
	            registration_inner_map.put("average", hours);
	            
	            reader.nextName(); // "credits":
	            credits = reader.nextString().toString(); // value
	            registration_inner_map.put("credits", credits);
	            
	            reader.nextName(); // "registration_status":
	            reader.nextNull(); // value
	            registration_status = "";
	            registration_inner_map.put("registration_status", registration_status);
	            
            
            reader.endObject(); // }
            
            registration_outer_map.put(String.valueOf(nodeCounter), (HashMap<String, String>) registration_inner_map);
            registration_inner_map = new HashMap<String, String>();
            nodeCounter++;
        }
        
        reader.endObject();  // }
        reader.endObject(); // }
    }

    private void updateRegistrationList()
    {
    	registration_outer_map.clear();
    	unregistered_list_subject_name.clear();
        registrationListAdapter = new ArrayAdapter<String>(getSherlockActivity(), R.layout.registration_list_item_layout, R.id.registration_list_item, unregistered_list_subject_name);
        unRegisteredSubjectList.setAdapter(registrationListAdapter);
        registrationListAdapter.notifyDataSetChanged();
    }
	
	@Override
    public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
	}
	
	public void clearActionBar()
    {
    	getSherlockActivity().getSupportActionBar().getNavigationMode();
        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

}