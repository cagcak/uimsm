package iaau.mas.uimsm.resultant;

import iaau.mas.uimsm.LoginActivity;
import iaau.mas.uimsm.R;
import iaau.mas.uimsm.util.RegistrationConfirm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class Activity_Registration_Confirm extends SherlockListActivity
{
	@SuppressWarnings("unused")
	private static String servletToSave = LoginActivity.local_address + "/UIMS/PerformRegistration";
	private static String servletToGet = LoginActivity.local_address + "/UIMS/PostRegistrationDetails";

    private static String[] selectedItems;
	
    private static String subject_code;
    private static String subject_name;
    private static String semester;
    private static String year;
    private static String hours;
    private static String credits;
    private static String registration_status;
    
    private static String requestedSubject;
    
    private static RegistrationConfirm model;
    
    private JsonReader reader;
    private static Intent intent;
    
	ListView registration_confirm_list_view;
    Button saveSubjects;
	ArrayAdapter<String> selectedSubjectListAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registration_confirm);
        
        registration_confirm_list_view = (ListView) findViewById(android.R.id.list);
        saveSubjects = (Button) findViewById(R.id.button_save_reg);
        
        getRegistrationSubjects();
        
        setListView();
        
        saveSubjects.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
//				sendSavedSubjects();
				Toast.makeText(getBaseContext(), "Registration is successfull!", Toast.LENGTH_LONG).show();
			}
		});
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		requestedSubject = l.getItemAtPosition(position).toString();
//		Log.i("Requested subject",requestedSubject);
		
		retrieveData();
		
//      henüz geçerli veri sunucudan gelmeden bu metod bir önceki bundle nesnesini 
//		sonraki activitye gönderiyor, yada hemen gönderemiyor ki ilk olarak tüm alanlar
//		null veriyor
		showDetails(requestedSubject);
	}
	
	private void showDetails(String subjectParameter)
	{
		intent = new Intent(Activity_Registration_Confirm.this, Activity_Registration_Detail.class);
		
			intent.putExtra("subjectcode", subject_code);
			intent.putExtra("subjectname", subject_name);
			intent.putExtra("semester", semester);
			intent.putExtra("year", year);
			intent.putExtra("hours", hours);
			intent.putExtra("credits", credits);
			intent.putExtra("registrationstatus", registration_status);
		
		startActivity(intent);
	}
	
	private void getRegistrationSubjects()
	{
		Bundle bundle = getIntent().getExtras();
        selectedItems = bundle.getStringArray("selectedSubjectItemNames");
	}

	private void setListView()
	{
		selectedSubjectListAdapter = new ArrayAdapter<String>(this, R.layout.registraation_confirm_list_item_layout, R.id.regs_list_item, selectedItems);
        registration_confirm_list_view.setAdapter(selectedSubjectListAdapter);
	}
	
	public String POST(String url, RegistrationConfirm model)
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
            JsonObject js_object = new JsonObject();
            js_object.addProperty("idnumber", model.getIdnumber());
            js_object.addProperty("requestedSubject", model.getRequestedSubject());

            json = js_object.toString();

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
	
    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Activity_Registration_Confirm.this);
            mProgressDialog.setTitle("Please wait!");
            mProgressDialog.setMessage("Data is in progress");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {
        	SharedPreferences sharedPreferences1 = getSharedPreferences("UserCredentials", 0);
            String idNumber = sharedPreferences1.getString("idnumber_key","").toString();
        	
            model = new RegistrationConfirm();
            
            model.setIdnumber(idNumber);
            model.setRequestedSubject(requestedSubject);

            return POST(urls[0],model);
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            if ( result.equals("") )
            {
                Log.e("DATA NOT RECEIVED",String.valueOf(result));
            } else {
            	Log.i("Server result",String.valueOf(result));
            	try {
					parse(result);
				} catch (IOException e) {
					e.printStackTrace();
				}            	
            }
        }
    }
    
    private void parse(String in) throws IOException
    {
    	reader = new JsonReader(new StringReader(in));
    	
    	//  Sample generated json form
    	/*{
    		  "RegistrationDetail": {
    		    "RequestSubject": {
    		      "subject_code": "COM 514",
    		      "subject_name": "Diploma Project",
    		      "semester": "spring",
    		      "year": "5",
    		      "hours": "0",
    		      "credits": "0",
    		      "registration_status": null
    		    }
    		  }
    		}*/
    	
    	
    	reader.beginObject(); // {
        reader.nextName(); // RegistrationDetail
        reader.beginObject(); // {
        
        reader.nextName(); // RequestSubject
            reader.beginObject(); // {
            
	            reader.nextName(); // "subject_code"
	            subject_code = reader.nextString().toString(); // value
            
	            reader.nextName(); // "subject_name":
	            subject_name = reader.nextString().toString(); // value
            
	            reader.nextName(); // "semester":
	            semester = reader.nextString().toString(); // value
            
	            reader.nextName(); // "year":
	            year = reader.nextString().toString(); // value
	            
	            reader.nextName(); // "hours":
	            hours = reader.nextString().toString(); // value
            
	            reader.nextName(); // "credits":
	            credits = reader.nextString().toString(); // value
            
	            reader.nextName(); // "registration_status":
	            reader.nextNull(); // value
	            registration_status = "";
         
            reader.endObject(); // }        
        
        reader.endObject();  // }
        reader.endObject(); // }
    }

	@SuppressLint("NewApi")
	private void retrieveData()
	{
		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
		{
			new HttpAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).execute(servletToGet);
		} else {
			new HttpAsyncTask().execute(servletToGet);
		}	
	}
	
	/*@SuppressLint("NewApi")
	private void sendSavedSubjects()
	{
		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
		{
			new SendJsonArray().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).execute(servletToSave);
		} else {
			new SendJsonArray().execute(servletToSave);
		}
	}*/

	/*private class SendJsonArray extends AsyncTask<String, Void, String>
	{
		private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Activity_Registration_Confirm.this);
            mProgressDialog.setTitle("Please wait!");
            mProgressDialog.setMessage("Data is in progress");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

		@Override
		protected String doInBackground(String... params) 
		{
//			ArrayList<String> list = new ArrayList<String>();
//			
//			for(int i=1; i<=selectedItems.length; i++)
//			{
//				list.add(selectedItems[i]);
//			}
			
			Gson gson = new Gson();
			String convertedArray = gson.toJson(selectedItems);
			
			return sendJson(params[0], convertedArray);
		}
		
		@Override
        protected void onPostExecute(String result)
		{
			
		}
	}
	
	protected String sendJson(String url, String array)
	{
		  HttpClient client = new DefaultHttpClient();
	      HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
	      HttpResponse response;
	      try
	      {
	        HttpPost post = new HttpPost(url);
	        StringEntity se = new StringEntity(array);  
	        Log.i("JSON before sending", array.toString());

	        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        post.setEntity(se);
	        response = client.execute(post);

	        Checking response 
	        if(response!=null)
	        {
	          InputStream in = response.getEntity().getContent();   
	          //Get the data in the entity
	        }
	    }
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }
		return array;
	}*/
}
