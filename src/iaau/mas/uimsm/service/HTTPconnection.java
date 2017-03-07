package iaau.mas.uimsm.service;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 4/16/2014.
 */
public class HTTPconnection
{
    private String url;
    private HttpURLConnection con;
    private OutputStream os;

    private String delimiter = "--";
    private String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";

    public HTTPconnection(String url) {
        this.url = url;
    }

    public byte[] downloadJSONfile(String... params) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Log.i("params.length", String.valueOf(params.length));

            for ( int i = 0; i<=params.length - 1 ; i++)
            {
                Log.i("File successfully received: ", String.valueOf(params[i]));

                HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                InputStream stream = con.getInputStream();
                byte[] newByteArray = new byte[1024];

                while ( stream.read(newByteArray) != -1)
                {
                    byteArrayOutputStream.write(newByteArray);
                }

                byteArrayOutputStream.close();
                con.disconnect();
            }

            Log.i("Received File RESPONSE CODE: ", String.valueOf(con.getResponseCode()));
//            Log.i(url, String.valueOf(params[0]));
//            Log.i(url, String.valueOf(params[1]));
//            Log.i(url, String.valueOf(params[2]));
//            Log.i(url, String.valueOf(params[3]));
//            Log.i(url, String.valueOf(params[4]));
//            Log.i(url, String.valueOf(params[5]));
//            Log.i(url, String.valueOf(params[6]));
//            Log.i(url, String.valueOf(params[7]));

//            con.getOutputStream().write( ("name=" + params[0]).getBytes());
//            con.getOutputStream().write( ("name=" + params[1]).getBytes());
//            con.getOutputStream().write( ("name=" + params[2]).getBytes());
//            con.getOutputStream().write( ("name=" + params[3]).getBytes());
//            con.getOutputStream().write( ("name=" + params[4]).getBytes());
//            con.getOutputStream().write( ("name=" + params[5]).getBytes());
//            con.getOutputStream().write( ("name=" + params[6]).getBytes());
//            con.getOutputStream().write( ("name=" + params[7]).getBytes());


//            InputStream is = con.getErrorStream();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SecurityException se) {
            se.getLocalizedMessage();
            se.printStackTrace();
        } catch(Throwable t) {
            t.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    public void connectForMultipart() throws Exception {
        con = (HttpURLConnection) ( new URL(url)).openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.connect();
        os = con.getOutputStream();
    }

    public void addFormPart(String paramName, String value) throws Exception {
        writeParamData(paramName, value);
    }

    public void addFilePart(String paramName, String fileName, byte[] data) throws Exception {
        os.write( (delimiter + boundary + "\r\n").getBytes());
        os.write( ("Content-Disposition: form-data; name=\"" + paramName +  "\"; filename=\"" + fileName + "\"\r\n"  ).getBytes());
        os.write( ("Content-Type: application/octet-stream\r\n"  ).getBytes());
        os.write( ("Content-Transfer-Encoding: binary\r\n"  ).getBytes());
        os.write("\r\n".getBytes());

        os.write(data);

        os.write("\r\n".getBytes());
    }

    public void finishMultipart() throws Exception {
        os.write( (delimiter + boundary + delimiter + "\r\n").getBytes());
    }


    public String getResponse() throws Exception {
        InputStream is = con.getInputStream();
        byte[] b1 = new byte[1024];
        StringBuffer buffer = new StringBuffer();

        while ( is.read(b1) != -1)
            buffer.append(new String(b1));

        con.disconnect();

        return buffer.toString();
    }



    private void writeParamData(String paramName, String value) throws Exception {


        os.write( (delimiter + boundary + "\r\n").getBytes());
        os.write( "Content-Type: text/plain\r\n".getBytes());
        os.write( ("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());;
        os.write( ("\r\n" + value + "\r\n").getBytes());


    }


}
