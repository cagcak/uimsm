package iaau.mas.uimsm.fragment.home;

import iaau.mas.uimsm.LoginActivity;
import iaau.mas.uimsm.R;
import iaau.mas.uimsm.service.RequestDownloadForm;
import iaau.mas.uimsm.util.AppsForms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Created by Administrator on 13.02.2014.
 */

public class Fragment_AppsForms extends SherlockFragment implements ActionBar.OnNavigationListener, View.OnClickListener
{
	private final String storedPATH = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/UIMSM/forms/";
	private final String server = LoginActivity.local_address;
	private final String servlet = server + "/UIMS/GetAppsFormsRequest";
	private final String EN_Form_servlet = server + "/UIMS/DownloadFormEN";
	private final String RU_Form_servlet = server + "/UIMS/DownloadFormRU";
	private final String KG_Form_servlet = server + "/UIMS/DownloadFormKG";
	private final String TR_Form_servlet = server + "/UIMS/DownloadFormTR";
	private final String name_EN_Form = "Petition(EN).doc";
	private final String name_RU_Form = "Petition(RU).doc";
	private final String name_KG_Form = "Petition(KG).doc";
	private final String name_TR_Form = "Petition(TR).doc";

	Context context;
	private static AppsForms model;
	
	private static RadioGroup radioGroup;
	private static RadioButton confirmation_form; 
	private static RadioButton transcript_form;
	private static RadioButton military_service_form;

	ImageButton en_form;
	ImageButton ru_form;
	ImageButton kg_form;
	ImageButton tr_form;
	
//	private static WebView webView;
	
	private static EditText fullname_military;
	private static int checkedRadioID;
	
	Button submit_form;
	
	private static String form_request = "";      // initialization of current form type
	private static String language_request = "";  // initialization of current language
	private static String military_request = "";  // initialization of fullname for military conf. letter 

	private static String requestTime = "";
	
	@SuppressWarnings("unused")
	private static String[] language_array;
	private static ArrayAdapter<CharSequence> language_adapter;

	
	@Override
    public void onAttach(Activity activity)
	{
		super.onAttach(activity);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.layout_fragment_appsforms, container, false);
        
        // Clearing other fragments' actionbar menu 
        clearActionBar();
        
        // initializing the language actionbar menu
        initializeActionBar();
        
        // initializing checkbox window components
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup1);
        confirmation_form = (RadioButton) rootView.findViewById(R.id.form_confirmation_letter);
        transcript_form = (RadioButton) rootView.findViewById(R.id.form_transcript);
        military_service_form = (RadioButton) rootView.findViewById(R.id.form_military_service);
        
        // initializing form template imagebutton window components
        en_form = (ImageButton) rootView.findViewById(R.id.download_form_en);
        en_form.setOnClickListener(this);
        ru_form = (ImageButton) rootView.findViewById(R.id.download_form_ru);
        ru_form.setOnClickListener(this);
        kg_form = (ImageButton) rootView.findViewById(R.id.download_form_kg);
        kg_form.setOnClickListener(this);
        tr_form = (ImageButton) rootView.findViewById(R.id.download_form_tr);
        tr_form.setOnClickListener(this);
        
        // initializing military request form russian fullname editbox window component 
        fullname_military = (EditText) rootView.findViewById(R.id.fullname_military_form);
        
        // initializing submit button window component
        submit_form = (Button) rootView.findViewById(R.id.submit_button_form);
        submit_form.setOnClickListener(this);
        
        // initializing the language actionbar menu adapter and window component
        language_array = getResources().getStringArray(R.array.language);
        Context context = getSherlockActivity().getSupportActionBar().getThemedContext();
        language_adapter = ArrayAdapter.createFromResource(context, R.array.language, R.layout.sherlock_spinner_item);
        language_adapter.setDropDownViewResource(com.actionbarsherlock.R.layout.sherlock_spinner_dropdown_item);

        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSherlockActivity().getSupportActionBar().setListNavigationCallbacks(language_adapter, this);
        
        // other initializations
        language_request = getResources().getStringArray(R.array.language)[0]; // 0: En, 1: Ru, 2: kg, 3: Tr
        getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(0); // English
        radioGroup.clearCheck();
        fullname_military.setVisibility(View.GONE);
        
        
        checkBoxConditions();
        
        return rootView;
    }
    
    @Override
	public void onClick(View view) 
	{
    	switch(view.getId())
    	{
    		case R.id.download_form_en:
    		{
    			download_EN_form();
    			viewForm_EN();
    			break;
    		}
    		case R.id.download_form_ru:
    		{
    			download_RU_form();
    			viewForm_RU();
    			break;
    		}
    		case R.id.download_form_kg:
    		{
    			download_KG_form();
    			viewForm_KG();
    			break;
    		}
    		case R.id.download_form_tr:
    		{
    			download_TR_form();
    			viewForm_TR();
    			break;
    		}
    		case R.id.submit_button_form:
    		{
    			if( (requestTime.equals("")) || (requestTime == "") )
    			{
    				RequestTask submit = new RequestTask();
        			submit.execute(servlet);
        			
    			} else {
    				String dailyAttempt = requestTime;
    				getRequestTime();  // taking request time	
    				
    				if( !(validateRequestTime(convertDate(dailyAttempt), convertDate(requestTime))) )
    				{
                    	Toast.makeText(getSherlockActivity().getBaseContext(), "You can submit one type application only once a day", Toast.LENGTH_LONG).show();
    				} else {
    					RequestTask submit = new RequestTask();
            			submit.execute(servlet);
    				}
    			}
    			
    			break;
    		}
    	}
		
	}
    

	@Override
    public void onResume()
    {
        super.onResume();
        ((SherlockFragmentActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_appsforms);
    }
    
    public void clearActionBar()
    {
    	getSherlockActivity().getSupportActionBar().getNavigationMode();
        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }
    
    public void initializeActionBar()
    {
    	getSherlockActivity().getSupportActionBar().getNavigationMode();
    	getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    }

    public void checkBoxConditions()
    {
    	radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() 
    	{	
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				checkedRadioID = radioGroup.getCheckedRadioButtonId();
				
				if(checkedRadioID == R.id.form_confirmation_letter) 
		    	{
		    		form_request = "Confirmation letter";
		    		
		    		initializeActionBar();
		    		transcript_form.setChecked(false);
		    		military_service_form.setChecked(false);
		    		fullname_military.setVisibility(View.GONE);
		    		Log.i("form_request",form_request);
		    	} 
		    	
				else if(checkedRadioID == R.id.form_transcript) 
		    	{
		    		form_request = "Transcript";
		    		language_request = "";
		    		
		    		confirmation_form.setChecked(false);
		    		military_service_form.setChecked(false);
		    		fullname_military.setVisibility(View.GONE);
		    		clearActionBar();
		    		
		    		Log.i("form_request",form_request);
		    	} 
		    	
				else if(checkedRadioID == R.id.form_military_service) 
		    	{
		    		form_request = "Military Service";
		    		language_request = "";
		    		
		    		confirmation_form.setChecked(false);
		    		transcript_form.setChecked(false);
		    		
		    		clearActionBar();
		    		fullname_military.setVisibility(View.VISIBLE);
		    		fullname_military.setText(R.string.editbox_military_samplename);
		    		
		    		Log.i("form_request",form_request);
		    	}
				
			}
		});
    }
    
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) 
	{
		switch(itemPosition) 
    	{
    	    case 0:
                language_request = "EN";
                Log.i("language_request", language_request);
                return true;
            case 1:
            	language_request = "RU";
                Log.i("language_request", language_request);
                return true;
            case 2:
            	language_request = "KG";
                Log.i("language_request", language_request);
                return true;
            case 3:
            	language_request = "TR";
                Log.i("language_request", language_request);
                return true;
            default:
            	Log.e("default case", "no selection performed");
                break;
        }
    	
    	
        return true;
	}
	
	
///////////////////////////////////////////////////////  Submitting Request /////////////////////	
	public String POST(String url, AppsForms model)
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
            jsonObject.accumulate("reference", model.getReference());
            jsonObject.accumulate("language", model.getLanguage());
            jsonObject.accumulate("fullname", model.getFullname());
            jsonObject.accumulate("requesttime", model.getRequestTime());

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
	
	private class RequestTask extends AsyncTask<String, Void, String>
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
        	
            military_request = fullname_military.getText().toString();
            
            model = new AppsForms();
            
            model.setIdnumber(idNumber);
            model.setReference(form_request);
            model.setLanguage(language_request);
            model.setFullname(military_request);
            model.setRequestTime(requestTime);

            return POST(urls[0],model);
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            if ( result.equals(false) || ( result.equals("false") ) )
            {
            	Toast.makeText(getSherlockActivity().getBaseContext(), "Request is not performed! Please, try again!", Toast.LENGTH_LONG).show();
            	Log.e("Request is not performed!", result);
            } else if ( ( result.equals(true) ) || ( result.equals("true") ) ) {
            	
            	Toast.makeText(getSherlockActivity().getBaseContext(), "Your application is successfully saved", Toast.LENGTH_LONG).show();
            	Log.d("Request is performed!", result);
            	getRequestTime();  // registering request time
            }
            	
            mProgressDialog.dismiss();  
        }

	}

	
	@SuppressLint("SimpleDateFormat")
	private void getRequestTime()
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss ");
		String currentTime = sdf.format(c.getTime());
		Log.i("Current request time: ", currentTime);
		requestTime = currentTime;
	}
	
	private boolean validateRequestTime(Date startDate, Date endDate)
	{
		//1 minute = 60 seconds
		//1 hour = 60 x 60 = 3600
		//1 day = 3600 x 24 = 86400
		
		
		//milliseconds
        long different = endDate.getTime() - startDate.getTime();
        
        long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
		
		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;
 
		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;
 
		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;
 
		long elapsedSeconds = different / secondsInMilli;
		
		Log.i("elapsedDays",String.valueOf(elapsedDays));
		Log.i("elapsedHours",String.valueOf(elapsedHours));
		Log.i("elapsedMinutes",String.valueOf(elapsedMinutes));
		Log.i("elapsedSeconds",String.valueOf(elapsedSeconds));
        
		if( elapsedDays >= 1 )
		{
			return true;
		}

		
		return false;
	}
	
	@SuppressLint("SimpleDateFormat")
	private Date convertDate(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss ");
		
		try{
			Date realDate = sdf.parse(date);
			return realDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////// Petition Request Form  /////////////////////////// 
	public void download_EN_form()
	{
		String[] form_params = new String[] {EN_Form_servlet, name_EN_Form};
		DownloadForm_EN_Task downloadtTask = new DownloadForm_EN_Task();
		downloadtTask.execute(form_params);
	}
	
	public void download_RU_form()
	{
		String[] form_params = new String[] {RU_Form_servlet, name_RU_Form};
		DownloadForm_RU_Task downloadtTask = new DownloadForm_RU_Task();
		downloadtTask.execute(form_params);
	}
	
	public void download_KG_form()
	{
		String[] form_params = new String[] {KG_Form_servlet, name_KG_Form};
		DownloadForm_KG_Task downloadtTask = new DownloadForm_KG_Task();
		downloadtTask.execute(form_params);
	}
	
	public void download_TR_form()
	{
		String[] form_params = new String[] {TR_Form_servlet, name_TR_Form};
		DownloadForm_TR_Task downloadtTask = new DownloadForm_TR_Task();
		downloadtTask.execute(form_params);
	}
	
	private class DownloadForm_EN_Task extends AsyncTask<String, Void, byte[]>
	{
		private String URL;
        private String FormFile;
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle("Please wait!");
            mProgressDialog.setMessage("Logging into the system..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        
        @Override
        protected byte[] doInBackground(String... params)
		{
        	URL = params[0];
            FormFile = params[1];
            
            RequestDownloadForm rdf = new RequestDownloadForm(URL);
            byte[] data = rdf.download(FormFile);
			
			return data;
		}
        
        protected void onPostExecute(byte[] result) 
        {
            super.onPostExecute(result);
            Log.i("Total Received byte: ", String.valueOf(result.length));

            WriteByteArrayToFile(result, FormFile);
            
            mProgressDialog.dismiss();
        }
	}
	
	private class DownloadForm_RU_Task extends AsyncTask<String, Void, byte[]>
	{
		private String URL;
        private String FormFile;
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle("Please wait!");
            mProgressDialog.setMessage("Logging into the system..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        
        @Override
        protected byte[] doInBackground(String... params)
		{
        	URL = params[0];
            FormFile = params[1];
            
            RequestDownloadForm rdf = new RequestDownloadForm(URL);
            byte[] data = rdf.download(FormFile);
			
			return data;
		}
        
        protected void onPostExecute(byte[] result) 
        {
            super.onPostExecute(result);
            Log.i("Total Received byte: ", String.valueOf(result.length));

            WriteByteArrayToFile(result, FormFile);
            
            mProgressDialog.dismiss();
        }
	}
	
	private class DownloadForm_KG_Task extends AsyncTask<String, Void, byte[]>
	{
		private String URL;
        private String FormFile;
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle("Please wait!");
            mProgressDialog.setMessage("Logging into the system..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        
        @Override
        protected byte[] doInBackground(String... params)
		{
        	URL = params[0];
            FormFile = params[1];
            
            RequestDownloadForm rdf = new RequestDownloadForm(URL);
            byte[] data = rdf.download(FormFile);
			
			return data;
		}
        
        protected void onPostExecute(byte[] result) 
        {
            super.onPostExecute(result);
            Log.i("Total Received byte: ", String.valueOf(result.length));

            WriteByteArrayToFile(result, FormFile);
            
            mProgressDialog.dismiss();
        }
	}
	
	private class DownloadForm_TR_Task extends AsyncTask<String, Void, byte[]>
	{
		private String URL;
        private String FormFile;
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle("Please wait!");
            mProgressDialog.setMessage("Logging into the system..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        
        @Override
        protected byte[] doInBackground(String... params)
		{
        	URL = params[0];
            FormFile = params[1];
            
            RequestDownloadForm rdf = new RequestDownloadForm(URL);
            byte[] data = rdf.download(FormFile);
			
			return data;
		}
        
        protected void onPostExecute(byte[] result) 
        {
            super.onPostExecute(result);
            Log.i("Total Received byte: ", String.valueOf(result.length));

            WriteByteArrayToFile(result, FormFile);
            
            mProgressDialog.dismiss();
        }
	}
	
	public void WriteByteArrayToFile(byte[] receivedFile, String currentParameter)
    {
		checkExternalMedia();
		
        File directory = new File(storedPATH);    
        directory.mkdirs();

        try {
               FileOutputStream fos = new FileOutputStream(directory + File.separator + currentParameter);
               fos.write(receivedFile);
               fos.close();
               
            } catch(FileNotFoundException ex) {
                 System.out.println("FileNotFoundException : " + ex);
            } catch(IOException ioe) {
                 System.out.println("IOException : " + ioe);
            }
    }
	
    private void checkExternalMedia()
    {
    	boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
 
        if (Environment.MEDIA_MOUNTED.equals(state)) 
        {
        	// Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        	// Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
        	// Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        
        Log.i("External Media: readable="+mExternalStorageAvailable+" writable="+mExternalStorageWriteable,"");
    }
	
	public void viewForm_EN()
	{
		File file = new File(Environment.getExternalStorageDirectory().getPath() + "/UIMSM/forms/" + name_EN_Form);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/msword";
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent);  
	}
	
	public void viewForm_RU()
	{
		File file = new File(Environment.getExternalStorageDirectory().getPath() + "/UIMSM/forms/" + name_RU_Form);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/msword";
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent);  
	}
	
	public void viewForm_KG()
	{
		File file = new File(Environment.getExternalStorageDirectory().getPath() + "/UIMSM/forms/" + name_KG_Form);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/msword";
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent);  
	}
	
	public void viewForm_TR()
	{
		File file = new File(Environment.getExternalStorageDirectory().getPath() + "/UIMSM/forms/" + name_TR_Form);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/msword";
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent);  
	}

///////////////////////////////////////////////////////////////////////////////////////////
	
}