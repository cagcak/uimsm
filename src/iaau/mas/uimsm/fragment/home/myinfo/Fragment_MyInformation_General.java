package iaau.mas.uimsm.fragment.home.myinfo;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

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

import iaau.mas.uimsm.LoginActivity;
import iaau.mas.uimsm.R;
import iaau.mas.uimsm.util.GeneralInformation;


/**
 * Created by Administrator on 17.02.2014.
 */
public class Fragment_MyInformation_General extends SherlockFragment
{
	private final String servlet = LoginActivity.local_address + "/UIMS/PostGeneralInfoResponse";

    private static GeneralInformation model;
    
    private static String faculty;
    private static String department;
    public static String group;
    private static String supervisor;
    private static String education;
    private static String registration;

    TextView faculty_edit;
    TextView department_edit;
    TextView group_edit;
    TextView supervisor_edit;
    TextView education_edit;
    TextView registration_edit;
	private JsonReader reader;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {

    	View view = inflater.inflate(R.layout.layout_fragment_myinformation_general, container, false);

        faculty_edit = (TextView) view.findViewById(R.id.faculty_get);
        department_edit = (TextView) view.findViewById(R.id.department_get);
        group_edit = (TextView) view.findViewById(R.id.group_get);
        supervisor_edit = (TextView) view.findViewById(R.id.supervisor_get);
        education_edit = (TextView) view.findViewById(R.id.education_get);
        registration_edit = (TextView) view.findViewById(R.id.registration_get);
        
        HttpAsyncTask task = new HttpAsyncTask();
        task.execute(servlet);
        
        return view;
    }

    private void parse(String in) throws IOException
    {
    	reader = new JsonReader(new StringReader(in));
    	
    	reader.beginObject(); // {
    	reader.nextName(); //GeneralInfo_Response
    	reader.beginArray(); // [
    	reader.beginObject(); // {
    	reader.nextName(); // faculty
    	faculty = reader.nextString().toString();
        reader.nextName();
        department = reader.nextString().toString();
        reader.nextName();
        group = reader.nextString().toString();
        reader.nextName();
        supervisor = reader.nextString().toString();
        reader.nextName();
        education = reader.nextString().toString();
        reader.nextName();
        registration = reader.nextString().toString();
        reader.endObject(); // }
        reader.endArray(); // ]
        reader.endObject(); // }
    }
    
    public String POST(String url, GeneralInformation model)
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
        	
            model = new GeneralInformation();
            
            model.setIdnumber(idNumber);            

            return POST(urls[0],model);
        }

        // onPostExecute displays the results of the AsyncTask.
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                    
                    faculty_edit.setText(faculty);
                    department_edit.setText(department);
                    group_edit.setText(group);
                    supervisor_edit.setText(supervisor);
                    education_edit.setText(education);
                    registration_edit.setText(registration);
//            	}
                	mProgressDialog.dismiss();  

            }
        }
    }
    

}
