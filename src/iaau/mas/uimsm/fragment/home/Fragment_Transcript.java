package iaau.mas.uimsm.fragment.home;

import iaau.mas.uimsm.LoginActivity;
import iaau.mas.uimsm.R;
import iaau.mas.uimsm.pdf.GenerateTranscript;
import iaau.mas.uimsm.resultant.Activity_Transcript_Detail;
import iaau.mas.uimsm.util.Transcript;

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
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

/**
 * Created by Administrator on 13.02.2014.
 */
public class Fragment_Transcript extends SherlockListFragment
{
	private final String servlet = LoginActivity.local_address + "/UIMS/PostTranscriptResponse";
	private static final String pdfLocation = GenerateTranscript.FILE_ADDRESS;
	
	private static Transcript model;

	public static Context contextOfApplication;
	
	private JsonReader reader;
	
	private static Map<String, HashMap<String, String>> outer_map = new HashMap<String, HashMap<String, String>>();
    private static Map<String, String> inner_map = new HashMap<String, String>();
	
    private static String subject_code;
    private static String subject_name;
    private static String semester;
    private static String year;
    private static String credits;
    private static String average;
    
    ArrayAdapter<String> listAdapter;
    ListView transcript_list_view;
    Button makePDF;
   
    private static ArrayList<String> list_subject_name = new ArrayList<String>(); 

    public static AlphaAnimation buttonClickAnimation = new AlphaAnimation(1F, 0.8F);
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);  
        contextOfApplication = getSherlockActivity().getApplicationContext();
    }
    
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.layout_fragment_transcript, container, false);

        clearActionBar();
        
        transcript_list_view = (ListView) rootView.findViewById(android.R.id.list);
        makePDF = (Button) rootView.findViewById(R.id.button_genpdf);
        
        HttpAsyncTask task = new HttpAsyncTask();
        task.execute(servlet);
        
   
        list_subject_name.clear();
    	listAdapter = new ArrayAdapter<String>(getSherlockActivity(), R.layout.transcript_list_item_layout, R.id.transcript_list_item, list_subject_name);
        transcript_list_view.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();        
        
//        final Handler handler = new Handler();
        makePDF.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				buttonClickAnimation.setDuration(5000);
	            v.startAnimation(buttonClickAnimation);
//				ProgressDialog mDialog = new ProgressDialog(getSherlockActivity());
//	            mDialog.setTitle("Please wait!");
//	            mDialog.setMessage("PDF document is being generated");
//	            mDialog.setIndeterminate(false);
//	            mDialog.show();
	            
				GenerateTranscript pdf = new GenerateTranscript();
				pdf.generatePDF();
				
//				handler.postDelayed(new Runnable() 
//		        {
//		        	@Override
//		        	public void run() 
//		        	{
//		        		//Execute after 500ms = 0,5sec
		        		viewGeneratedPDF();
//		        	}
//		        }, 500);
				
//				mDialog.dismiss();
			}
		});

        return rootView;
    }
        
    @Override
	public void onListItemClick(ListView l, View v, int position, long id) 
    {
        String selected_subject_item_name = outer_map.get(String.valueOf(position+1)).get("subject_name");
    	Log.i("","Subject name = " + outer_map.get(String.valueOf(position+1)).get("subject_name") + "---Subject list position = " +String.valueOf(position));
        showDetails(selected_subject_item_name);
    }
    
    private void showDetails(String subjectParameter)
    {
    	Intent intent = new Intent(getSherlockActivity().getApplicationContext(), Activity_Transcript_Detail.class);
    	
    	for( int i=1; i<=outer_map.size(); i++ )
    	{
    		if( (outer_map.get(String.valueOf(i)).containsValue(subjectParameter) ) )
    		{
    			intent.putExtra("extra_subject_code", outer_map.get(String.valueOf(i)).get("subject_code"));
    			intent.putExtra("extra_subject_name", outer_map.get(String.valueOf(i)).get("subject_name"));
    			intent.putExtra("extra_subject_semester", outer_map.get(String.valueOf(i)).get("semester"));
    			intent.putExtra("extra_subject_year", outer_map.get(String.valueOf(i)).get("year"));
    			intent.putExtra("extra_subject_credits", outer_map.get(String.valueOf(i)).get("credits"));
    			intent.putExtra("extra_subject_average", outer_map.get(String.valueOf(i)).get("average"));
    		} else {
//            	Log.e(subjectParameter, "Empty!");
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

    @Override
    public void onResume()
    {
        super.onResume();
        // Set title
        ((SherlockFragmentActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_transcript);
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
	            inner_map.put("subject_code", subject_code);
	            
	            reader.nextName(); // "subject_name":
	            subject_name = reader.nextString().toString(); // value
	            inner_map.put("subject_name", subject_name);
	            
	            reader.nextName(); // "semester":
	            semester = reader.nextString().toString(); // value
	            inner_map.put("semester", semester);
	            
	            reader.nextName(); // "year":
	            year = reader.nextString().toString(); // value
	            inner_map.put("year", year);
	            
	            reader.nextName(); // "credits":
	            credits = reader.nextString().toString(); // value
	            inner_map.put("credits", credits);
	            
	            reader.nextName(); // "average":
	            average = reader.nextString().toString(); // value
	            inner_map.put("average", average);
            
            reader.endObject(); // }
            
            outer_map.put(String.valueOf(nodeCounter), (HashMap<String, String>) inner_map);
            inner_map = new HashMap<String, String>();
            nodeCounter++;
        }
        
        reader.endObject();  // }
        reader.endObject(); // }
    }


    public String POST(String url, Transcript model)
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
        	
            model = new Transcript();
            
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
                
            	/*
            	//Searching
                Set<String> searchResults = new HashSet<String>(); // Using set to avoid duplicate

                // Iterate over the outer map
                for(String key : outer_map.keySet())
                {
                    // Iterate through each inner_map value of outer map
                    for(Entry<String, String> innerEntry : outer_map.get(key).entrySet())
                    {
                    	if(innerEntry.getKey().equals("subject_name"))
                    	{
                    		searchResults.add(innerEntry.getValue());
                    		Log.i("****",String.valueOf(innerEntry.getValue()));
                    	}
                    }
                }
                
                list_subject_name.addAll(searchResults);
                */
            	
            	for( int i=1; i<=outer_map.size(); i++ )
            	{
            		list_subject_name.add(outer_map.get(String.valueOf(i)).get("subject_name"));
            	}
            	
            	listAdapter.notifyDataSetChanged();
                	mProgressDialog.dismiss();  

            }
        }
    }
    
    private void viewGeneratedPDF()
    {
    	File file = new File(pdfLocation);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/pdf";
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent); 
    }
    
    public void clearActionBar()
    {
    	getSherlockActivity().getSupportActionBar().getNavigationMode();
        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

}	