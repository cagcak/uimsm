package iaau.mas.uimsm.service;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 4/17/2014.
 */
public class RequestDownloadForm {

    private String url;

    public RequestDownloadForm(String url) {
        this.url = url;
    }

    public byte[] download(String current_parameter)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {

            HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            InputStream stream = con.getInputStream();
            byte[] byteArray = new byte[1024];

            while ( stream.read(byteArray) != -1)
            {
                byteArrayOutputStream.write(byteArray);
            }

            con.disconnect();
            Log.i("File successfully received: ", String.valueOf(current_parameter));
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("IOException---> ", String.valueOf(ex.getLocalizedMessage()));
        } catch (SecurityException se) {
            se.printStackTrace();
            Log.e("IOException---> ", String.valueOf(se.getLocalizedMessage()));
        } catch(Throwable t) {
            t.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
