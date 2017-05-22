package getyourguide.mobile.ksh.berlintour;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by KSH on 2017-05-21.
 */

public class UrlConnection {
    private String TAG = "";

    private String urlStr = "";

    private StringBuffer response;

    public UrlConnection(String urlStr){
        this.urlStr = urlStr;
        TAG = getClass().getName();
        response = new StringBuffer();
        sendingGetRequest();
    }

    private void sendingGetRequest(){
        try{
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Add request header
            if(Debug.DEBUG){
                int responseCode = conn.getResponseCode();
                Log.d(TAG, "URL " + url);
                Log.d(TAG, "Response Code : " + responseCode);
            }

            // Read response from input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output = "";

            while((output = in.readLine()) != null){
                response.append(output);
            }
            in.close();
            if(Debug.DEBUG){
                Log.d(TAG, "string from url " + response);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String getResponse(){
        return response.toString();
    }
}
