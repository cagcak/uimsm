package iaau.mas.uimsm.fragment.home;

import iaau.mas.uimsm.LoginActivity;
import iaau.mas.uimsm.R;
import iaau.mas.uimsm.fragment.home.myinfo.Fragment_MyInformation_Current;
import iaau.mas.uimsm.pdf.GenerateDiploma;
import iaau.mas.uimsm.pdf.GenerateSuccessReport;
import iaau.mas.uimsm.util.DiplomaInformationModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.itextpdf.text.DocumentException;


/**
 * Created by Administrator on 13.02.2014.
 */
public class Fragment_Diploma extends SherlockFragment 
{
	private final String servlet = LoginActivity.local_address + "/UIMS/GetDiplomaInformationRequest";
	private static String pdfLocation = GenerateDiploma.FILE_ADDRESS;
	
	public static JSONObject jsonDiplomInfo;
	
	private static Map<String, String> map = new HashMap<String, String>();
	
	DiplomaInformationModel model;
	
	
	EditText middlename_edit;
	EditText fullname_ru_edit;
	EditText current_address_edit;
	EditText passport_no_edit;
	EditText birthday_edit;
	EditText phone_number_edit;
	EditText year_graduation_edit;
	EditText thesis_project_en_edit;
	EditText thesis_project_ru_edit;
	EditText thesis_project_kg_edit;
	
	Button submit_button;
	Button generate_pdf_button;

	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.layout_fragment_diploma, container, false);
        
        clearActionBar();
        
        middlename_edit = (EditText) rootView.findViewById(R.id.middle_name_diploma);
    	fullname_ru_edit = (EditText) rootView.findViewById(R.id.fullname_diploma_edit);
    	current_address_edit = (EditText) rootView.findViewById(R.id.current_address_diploma);
    	passport_no_edit = (EditText) rootView.findViewById(R.id.passport_diploma);
    	birthday_edit = (EditText) rootView.findViewById(R.id.birthday_diploma);
    	phone_number_edit = (EditText) rootView.findViewById(R.id.phone_no_diploma);
    	year_graduation_edit = (EditText) rootView.findViewById(R.id.year_graduate);
    	thesis_project_en_edit = (EditText) rootView.findViewById(R.id.diploma_topic_en);
    	thesis_project_ru_edit = (EditText) rootView.findViewById(R.id.diploma_topic_ru);
    	thesis_project_kg_edit = (EditText) rootView.findViewById(R.id.diploma_topic_kg);
    	
    	submit_button = (Button) rootView.findViewById(R.id.submit_button_form);
    	generate_pdf_button = (Button) rootView.findViewById(R.id.generate_pdfdoc_button);
        
    	retrievePreferences();
        Log.v("retrievePreferences: ", String.valueOf(getSherlockActivity().getSharedPreferences("UserDiplom", 0).getAll()));
    	
        submit_button.setOnClickListener(new View.OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				if( !validateFields() )
	    		{
	    			Toast.makeText(getSherlockActivity().getBaseContext(), "You must fill all fields", Toast.LENGTH_LONG).show();
	    		} else {
	    			savePrefernces();
	    			new HttpAsyncTask().execute(servlet);
	    		}
			}
		});

        generate_pdf_button.setOnClickListener(new View.OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				if( !validateFields() )
	    		{
	    			Toast.makeText(getSherlockActivity().getBaseContext(), "You must fill all fields", Toast.LENGTH_LONG).show();
	    		} else {
	    			savePrefernces();
	    			retrievePreferences();
	    			
	    			GenerateDiploma makePDF = new GenerateDiploma();
	    			try {
						makePDF.generatePDF();
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			
	    			viewGeneratedPDF();
	    			
	    		}
			}
		});
    	
        return rootView;
    }

    public Bundle sendUserBundle()
    {  
//        map.put("idnumber", "0000");
        
        Bundle extras = new Bundle();
        extras.putSerializable("bundle_map", (Serializable) map);
        
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
    
    @Override
    public void onResume()
    {
        super.onResume();
        // Set title
        ((SherlockFragmentActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_diploma);
    }
    
    private boolean validateFields() 
    {
    	if((middlename_edit.getText().toString().trim().equals("")))
         {
             return false;
         } else if((middlename_edit.getText().toString().trim().equals(""))) {
        	 return false;
         } else if((fullname_ru_edit.getText().toString().trim().equals(""))) {
        	 return false;
         }else if((current_address_edit.getText().toString().trim().equals(""))) {
        	 return false;
         }else if((passport_no_edit.getText().toString().trim().equals(""))) {
        	 return false;
         }else if((birthday_edit.getText().toString().trim().equals(""))) {
        	 return false;
         }else if((phone_number_edit.getText().toString().trim().equals(""))) {
        	 return false;
         }else if((year_graduation_edit.getText().toString().trim().equals(""))) {
        	 return false;
         }else if((thesis_project_en_edit.getText().toString().trim().equals(""))) {
        	 return false;
         }else if((thesis_project_ru_edit.getText().toString().trim().equals(""))) {
        	 return false;
         }else if((thesis_project_kg_edit.getText().toString().trim().equals(""))) {
        	 return false;
         }else {
        	 return true;
         }
	}

	public String POST(String url, DiplomaInformationModel model)
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
            jsonObject.accumulate("middlename", model.getMiddlename());
            jsonObject.accumulate("fullname_ru", model.getFullname_ru());
            jsonObject.accumulate("current_address", model.getCurrent_address());
            jsonObject.accumulate("passport_no", model.getPassport_no());
            jsonObject.accumulate("birthday", model.getBirthday());
            jsonObject.accumulate("phone_number", model.getPhone_number());
            jsonObject.accumulate("year_graduation", model.getYear_graduation());
            jsonObject.accumulate("thesis_project_en", model.getThesis_project_en());
            jsonObject.accumulate("thesis_project_ru", model.getThesis_project_ru());
            jsonObject.accumulate("thesis_project_kg", model.getThesis_project_kg());

            jsonDiplomInfo = jsonObject;
            
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

            Log.v("result", result);
//            Log.v("responseData", responseData.getAsString());
            Log.v("servlet", servlet);

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
        	
        	model = new DiplomaInformationModel();
            
        	model.setIdnumber(idNumber);
            model.setMiddlename(middlename_edit.getText().toString());
            model.setFullname_ru(fullname_ru_edit.getText().toString());
            model.setCurrent_address(current_address_edit.getText().toString());
            model.setPassport_no(passport_no_edit.getText().toString());
            model.setBirthday(birthday_edit.getText().toString());
            model.setPhone_number(phone_number_edit.getText().toString());
            model.setYear_graduation(year_graduation_edit.getText().toString());
            model.setThesis_project_en(thesis_project_en_edit.getText().toString());
            model.setThesis_project_ru(thesis_project_ru_edit.getText().toString());
            model.setThesis_project_kg(thesis_project_kg_edit.getText().toString());
            
            Log.v("urls[0]", urls[0]);

            return POST(urls[0],model);
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            if ( result.equals(false) || ( result.equals("false") ) )
            {
                Log.e("-----------", "DATA NOT RECEIVED");
            } else if ( ( result.equals(true) ) || ( result.equals("true") ) ) {
            	Log.d("sonuç", "Data received");
    			Toast.makeText(getSherlockActivity().getBaseContext(), "Your informations are saved", Toast.LENGTH_LONG).show();
            }

            mProgressDialog.dismiss();
        }
    }
    
    public void savePrefernces()
    {
        SharedPreferences sharedDiplom = getSherlockActivity().getSharedPreferences("UserDiplom", 0);
        SharedPreferences.Editor editor = sharedDiplom.edit();
        if(middlename_edit.getText().equals("") || middlename_edit.getText() == null){
        	
        }
        editor.putString("middlename", middlename_edit.getText().toString());
        editor.putString("fullname_ru", fullname_ru_edit.getText().toString());
        editor.putString("currentaddress", current_address_edit.getText().toString());
        editor.putString("passport_no", passport_no_edit.getText().toString());
        editor.putString("birthday", birthday_edit.getText().toString());
        editor.putString("phone_no", phone_number_edit.getText().toString());
        editor.putString("year_graduate", year_graduation_edit.getText().toString());
        editor.putString("thesis_en", thesis_project_en_edit.getText().toString());
        editor.putString("thesis_ru", thesis_project_ru_edit.getText().toString());
        editor.putString("thesis_kg", thesis_project_kg_edit.getText().toString());
        editor.commit();
    }
    
    public void retrievePreferences()
    {
        SharedPreferences sharedDiplom = getSherlockActivity().getSharedPreferences("UserDiplom", 0);
        String backup_middlename = sharedDiplom.getString("middlename", "");
        String backup_fullname_ru = sharedDiplom.getString("fullname_ru", "");
        String backup_currentaddress = sharedDiplom.getString("currentaddress", "");
        String backup_passport_no = sharedDiplom.getString("passport_no", "");
        String backup_birthday = sharedDiplom.getString("birthday", "");
        String backup_phone_no = sharedDiplom.getString("phone_no", "");
        String backup_year_graduate = sharedDiplom.getString("year_graduate", "");
        String backup_thesis_en = sharedDiplom.getString("thesis_en", "");
        String backup_thesis_ru = sharedDiplom.getString("thesis_ru", "");
        String backup_thesis_kg = sharedDiplom.getString("thesis_kg", "");

        if (sharedDiplom.contains("middlename")) {
        	middlename_edit.setText(backup_middlename);
        	map.put("middlename", backup_middlename);
        } if (sharedDiplom.contains("fullname_ru")) {
        	fullname_ru_edit.setText(backup_fullname_ru);
        	map.put("fullname_ru", backup_fullname_ru);
        } if (sharedDiplom.contains("currentaddress")) {
        	current_address_edit.setText(backup_currentaddress);
        	map.put("current_address", backup_currentaddress);
        } if (sharedDiplom.contains("passport_no")) {
        	passport_no_edit.setText(backup_passport_no);
        	map.put("passport_no", backup_passport_no);
        } if (sharedDiplom.contains("birthday")) {
        	birthday_edit.setText(backup_birthday);
        	map.put("birthday", backup_birthday);
        } if (sharedDiplom.contains("phone_no")) {
        	phone_number_edit.setText(backup_phone_no);
        	map.put("phone_no", backup_phone_no);
        } if (sharedDiplom.contains("year_graduate")) {
        	year_graduation_edit.setText(backup_year_graduate);
        	map.put("year_graduate", backup_year_graduate);
        } if (sharedDiplom.contains("thesis_en")) {
        	thesis_project_en_edit.setText(backup_thesis_en);
        	map.put("thesis_en", backup_thesis_en);
        } if (sharedDiplom.contains("thesis_ru")) {
        	thesis_project_ru_edit.setText(backup_thesis_ru);
        	map.put("thesis_ru", backup_thesis_ru);
        } if (sharedDiplom.contains("thesis_kg")) {
        	thesis_project_kg_edit.setText(backup_thesis_kg);
        	map.put("thesis_kg", backup_thesis_kg);
        }
    }

    public void clearAllPreferences()
    {
        SharedPreferences sharedDiplom = getSherlockActivity().getSharedPreferences("UserDiplom", 0);
        SharedPreferences.Editor editor = sharedDiplom.edit();
        editor.clear();
        editor.commit();
    }
    
    public void clearActionBar()
    {
    	getSherlockActivity().getSupportActionBar().getNavigationMode();
        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }
}