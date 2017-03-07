package iaau.mas.uimsm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
//import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import iaau.mas.uimsm.factory.ConnectionDetector;
import iaau.mas.uimsm.util.User;

public class LoginActivity extends Activity implements OnClickListener
{
    // flag for Internet connection status
    Boolean isInternetConnected;
    //Boolean isNetworkAvailable = false;

    ConnectionDetector connectionDetector;

    final static String aCUSTOM_TOAST_MESSAGE = "NoSuchUser_toast";
    final static String bCUSTOM_TOAST_MESSAGE = "NoEnteredData_toast";
    final static String cCUSTOM_TOAST_MESSAGE = "NoInternetConnection_toast";
    final static String dCUSTOM_TOAST_MESSAGE = "NoServerConnection_toast";

    EditText txt_id;
    EditText txt_password;
    Button buttonPost;
    Button buttonExit;
    Button loginFacebook;

    public static AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    User user;

//    public final static String local_address = "http://192.168.137.1:8080";  // host: Local machine address
    public final static String local_address = "http://10.0.2.2:8080";  // host: emulator address
    final static String webApp_URL_overIP = local_address + "/UIMS/GetAndroidRequest";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // getting reference to the views
        txt_id = (EditText) findViewById(R.id.edittext_idnumber);
        txt_password = (EditText) findViewById(R.id.edittext_password);
        buttonPost = (Button) findViewById(R.id.button_Post);
        buttonExit = (Button) findViewById(R.id.cancel_button);
        loginFacebook = (Button) findViewById(R.id.sign_with_facebook);

        // Taking references from SharedPreferences if exists any typed before
        retrievePreferences();
        Log.v("retrievePreferences: ", String.valueOf(getSharedPreferences("UserCredentials", MODE_PRIVATE).getAll()));
//        savePrefernces();

        // checking connectivity to network
        connectionDetector = new ConnectionDetector(getApplicationContext());
        isInternetConnected = connectionDetector.isConnectedToInternet();
        Log.v("Internet durumu-: ", String.valueOf(isInternetConnected));

        // add click listener to Button "POST"
        buttonPost.setOnClickListener(this);
        buttonExit.setOnClickListener(this);
        loginFacebook.setOnClickListener(this);
    }


    public String POST(String url, User user)
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
            jsonObject.accumulate("idnumber", user.getIdnumber());
            jsonObject.accumulate("password", user.getPassword());

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

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

            Log.v("result", result);

        } catch (ClientProtocolException e) {
            Log.w("HTTP2:", e);
        }  catch (IOException e) {
            Log.w("HTTP3:", e);
        } catch (Exception e) {
            Log.d("Unknown Exception", e.getLocalizedMessage());
        }

        return result;
    }

    @Override
    public void onClick(View view)
    {
        buttonClick.setDuration(1000);
        view.startAnimation(buttonClick);
        switch(view.getId()){
            case R.id.button_Post:
            {
                // If any idnumber or password NOT entered
                if( !validate() ) {
                    makeToast(bCUSTOM_TOAST_MESSAGE);
                } else if (!isInternetConnected) {
                    makeToast(cCUSTOM_TOAST_MESSAGE);
                } /*else if ( (!isNetworkAvailable) ) {
                    makeToast(dCUSTOM_TOAST_MESSAGE);
                }*/ else {
                    // call AsynTask to perform network operation on separate thread
                    //String webApp_URL_overIP = "http://192.168.1.100:8080/UIMS/GetAndroidRequest";
                    new HttpAsyncTask().execute(webApp_URL_overIP);
                }

                break;
            }

            case R.id.cancel_button:
            {
            	onBackPressed();
//                finish();
//                System.exit(0);
                break;
            }
//            case R.id.sign_with_facebook:
//            {
//                //
//                break;
//            }
        }

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setTitle("Please wait!");
            mProgressDialog.setMessage("Logging into the system..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }


        @Override
        protected String doInBackground(String... urls)
        {
            user = new User();
            user.setIdnumber(txt_id.getText().toString());
            user.setPassword(txt_password.getText().toString());

            savePrefernces();
            Log.v("savePrefernces: ", String.valueOf(getSharedPreferences("UserCredentials", MODE_PRIVATE).getAll()));
            Log.v("urls[0]", urls[0]);

            return POST(urls[0],user);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            if ( result.equals(false) || ( result.equals("false") ) )
            {
                makeToast(aCUSTOM_TOAST_MESSAGE);

                Log.d("-----------", "!!!NO SUCH USER ACTION!!!!");
            } else if ( ( result.equals(true) ) || ( result.equals("true") ) ) {
                Toast.makeText(getBaseContext(), "Login success!", Toast.LENGTH_SHORT).show();
                Log.d(doInBackground(result), "Success!!!!!");
                Intent intent = new Intent(LoginActivity.this, HelpActivity.class);
                startActivity(intent);
                finish();
            } else {
//                makeToast(dCUSTOM_TOAST_MESSAGE);
            }

            mProgressDialog.dismiss();
        }
    }


    private boolean validate()
    {
        if(txt_id.getText().toString().trim().equals(""))
        {
            return false;
        }else if(txt_password.getText().toString().trim().equals(""))
        {
            return false;
        }else
            return !((txt_password.getText().toString().equals("")) && (txt_id.getText().toString().trim().equals("")));
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

    public void savePrefernces()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("UserCredentials", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idnumber_key", txt_id.getText().toString());
        editor.putString("password_key", txt_password.getText().toString());
        editor.commit();
    }

    public void retrievePreferences()
    {
        SharedPreferences sharedPreferences1 = getSharedPreferences("UserCredentials", 0);
        String backup_idnumber = sharedPreferences1.getString("idnumber_key", "");
        String backup_password = sharedPreferences1.getString("password_key", "");

        if (sharedPreferences1.contains("idnumber_key")) {
            txt_id.setText(backup_idnumber);
        } if (sharedPreferences1.contains("password_key")) {
            txt_password.setText(backup_password);
        }
    }

    public void clearAllPreferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("UserCredentials", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void makeToast(String type)
    {
        if ( type == "NoSuchUser_toast" )
        {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            layout.findViewById(R.id.id_nointernet).setVisibility(View.GONE);
            layout.findViewById(R.id.id_nodata).setVisibility(View.GONE);
            layout.findViewById(R.id.id_nonetwork).setVisibility(View.GONE);
            ((TextView) layout.findViewById(R.id.toast_text_1)).setText("Unknown! No such user registered in UIMS");
            Toast toast = new Toast(getBaseContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else if ( type == "NoEnteredData_toast" ) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            layout.findViewById(R.id.id_nointernet).setVisibility(View.GONE);
            layout.findViewById(R.id.id_unknown).setVisibility(View.GONE);
            layout.findViewById(R.id.id_nonetwork).setVisibility(View.GONE);
            ((TextView) layout.findViewById(R.id.toast_text_2)).setText("You must enter your login information first!");
            Toast toast = new Toast(getBaseContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else if ( type == "NoInternetConnection_toast" ) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            layout.findViewById(R.id.id_unknown).setVisibility(View.GONE);
            layout.findViewById(R.id.id_nodata).setVisibility(View.GONE);
            layout.findViewById(R.id.id_nonetwork).setVisibility(View.GONE);
            ( (TextView) layout.findViewById(R.id.toast_text_3)).setText("No connection! You must have internet connection and UIMS server connection");
            Toast toast = new Toast(getBaseContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else if ( type == "NoServerConnection_toast" ) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            layout.findViewById(R.id.id_nointernet).setVisibility(View.GONE);
            layout.findViewById(R.id.id_unknown).setVisibility(View.GONE);
            layout.findViewById(R.id.id_nodata).setVisibility(View.GONE);
            ((TextView) layout.findViewById(R.id.toast_text_4)).setText("Cannot connect to UIMS server. Contact with IAAU admininstrators");
            Toast toast = new Toast(getBaseContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {

        android.os.Process.killProcess(android.os.Process.myPid());
        // This above line close correctly
    }

}