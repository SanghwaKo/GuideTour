package getyourguide.mobile.ksh.berlintour;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by KSH on 2017-05-22.
 */

public class AddNewReview {
    private URL url;
    private JSONObject json;

    public AddNewReview(String url, JSONObject json){
        // Connect to the server and send json to be added
        try{
            this.url = new URL(url);
        }catch (MalformedURLException ex){
            ex.printStackTrace();
        }

        this.json = json;
    }

    public void sendJSON(){
        //// Should be finished
        try{
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
