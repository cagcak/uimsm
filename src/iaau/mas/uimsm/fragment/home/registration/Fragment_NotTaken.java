/*package iaau.mas.uimsm.fragment.home.registration;

import iaau.mas.uimsm.HomeActivity;
import iaau.mas.uimsm.LoginActivity;
import iaau.mas.uimsm.R;
import iaau.mas.uimsm.factory.FragmentLifecycle;
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
import android.app.Fragment;
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

import com.actionbarsherlock.app.SherlockListFragment;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;


public class Fragment_NotTaken extends SherlockListFragment implements FragmentLifecycle
{
	private final String servlet = LoginActivity.local_address + "/UIMS/PostRegistrationServletResponse";
	
    public static int BUNDLE_ID = 1;
    
	private static Registration model;
	
	private JsonReader reader;
	
	private static Intent intent;
	
	Bus bus;
	
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
    
    EditText yearFilter;
	EditText subjectCodeFilter;
	ListView unRegisteredSubjectList;
	Button takeSubject;

	ArrayAdapter<String> registrationListAdapter;
	
	private static String[] outputStringArray;
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        Bundle bundle = new Bundle();
        bundle.putStringArray("selectedSubjects", outputStringArray);
        HomeActivity activity = (HomeActivity) getActivity();
        activity.saveData(BUNDLE_ID, bundle);
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
        
        RetrieveUnRegisteredSubjectsAsyncTask task = new RetrieveUnRegisteredSubjectsAsyncTask();
        task.execute(servlet);
        
        updateRegistrationList();
        
        takeSubject.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				registerSubjects();
			}
		});
        
		
		return view;
	}
	
	public void takeSelectedSubject()
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
    	
    	outputStringArray = new String[selectedItems.size()];
    	
    	for( int i=0; i<selectedItems.size(); i++ )
    	{
    		outputStringArray[i] = selectedItems.get(i);
    		Log.d("Selected subject: ", outputStringArray[i]);
    	}
    }

	public void registerSubjects()
    {
    	takeSelectedSubject();

    	Log.d("Selected subject list length: ", String.valueOf(outputStringArray.length));
    	Fragment_NotTaken fragment = new Fragment_NotTaken();
    	// Creating Bundle object to transfer selected subjects
    	Bundle bundle = new Bundle();
    	bundle.putStringArray("selectedItems", outputStringArray);
    	fragment.setArguments(bundle);
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
//        Log.e("result", result);
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

    @Produce
    @Override
	public void onPauseFragment() {
		Log.i(getTag(), "PAGE 1 PAUSED");
		Log.d("Selected subject list length: ", String.valueOf(outputStringArray.length));
    	Fragment_NotTaken fragment = new Fragment_NotTaken();
    	// Creating Bundle object to transfer selected subjects
    	Bundle bundle = new Bundle();
    	bundle.putStringArray("selectedItems", outputStringArray);
    	fragment.setArguments(bundle);
    	Log.d("posted bundle", String.valueOf(bundle));
    	
    	bus = new Bus();
    	bus.post(bundle);
    	
    	
	}

	@Override
	public void onResumeFragment() {
		Log.i(getTag(), "PAGE 1 RESUMED");
	}
	
	
	@Override
    public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
	}

	@Override
	public void onButtonPressed(String[] listSubject) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
*/