package com.example.adrien.librairies;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adrien.hpm_application.ListeAppareilsActivity;
import com.example.adrien.hpm_application.R;
import com.example.adrien.librairies.DatabaseHandler;

public class FonctionsUtilisateur {

    private static final String TAG = "myApp";
    private JSONParser jsonParser;
    private String login_tag = "login";
    private String register_tag = "register";

    // constructor
    public FonctionsUtilisateur() {
        jsonParser = new JSONParser();
    }

    /**
     * function make Login Request
     *
     * @param login
     * @param password
     */
    public List<NameValuePair> loginUser(String login, String password) {
        //Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params = new ArrayList<NameValuePair>();
        Log.v(TAG, login_tag);
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("login", login));
        params.add(new BasicNameValuePair("password", password));
        return params;
    }

    /**
     * function make Login Request
     *
     * @param name
     * @param login
     * @param password
     */
    public List<NameValuePair> registerUser(String name, String login, String password) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("login", login));
        params.add(new BasicNameValuePair("password", password));

        // getting JSON Object
        // JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return params;
    }

    /**
     * Function get Login status
     */
    public boolean isUserLoggedIn(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getCapteurCount();
        //Log.v("COUNT", Integer.toString(count));
        if (count > 0) {
            // user logged in
            return true;
        }
        return false;
    }

    /**
     * Function to logout user
     * Reset Database
     */
    public boolean logoutUser(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}


