package iaau.mas.uimsm.factory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 4/13/2014.
 */
public class ConnectionDetector {

    private Context _context;

    public ConnectionDetector(Context _context) {
        this._context = _context;
    }

    public boolean isConnectedToInternet()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null)
        {
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();

            if (infos != null)
            {
                for (int i = 0; i<infos.length; i++)
                {
                    if (infos[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

//    public boolean isNetworkAvailable(String url, int timeout)
//    {
//        try{
//            URL myUrl = new URL(url);
//            URLConnection connection = myUrl.openConnection();
//            connection.setConnectTimeout(timeout);
//            connection.connect();
//            return true;
//        } catch (Exception e) {
//            // Handle your exceptions
//            return false;
//        }
//    }
}
