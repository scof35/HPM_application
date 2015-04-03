package com.example.adrien.hpm_application;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
        import org.json.JSONObject;

        import android.app.Activity;
        import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import com.example.adrien.librairies.FonctionsUtilisateur;
import com.example.adrien.librairies.GlobalClass;
import com.example.adrien.librairies.JSONParser;

import java.util.List;

public class LoginActivity extends Activity {
    Button btnLogin;
    EditText inputLog;
    EditText inputPassword;
    TextView textViewErreur ;

    // JSON Response node names
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_IDm = "id_maison";
    private static final String TAG = "myApp";
    private static List<NameValuePair> params;



    // Pour des tests,
    // Utiliser http://10.0.2.2/  pour se connecter au localhost avec WAMP i.e. http://localhost/
    private static final String loginURL = "http://192.168.43.109/hpm/index.php";
    private static final String login_tag = "login";
    private static final String register_tag = "register";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

      // Importing all assets like buttons, text fields
        btnLogin = (Button) findViewById(R.id.buttonConnecter);

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new IdentificationClient().execute();
            }
        });
    }







    /**
     * AsyncTask  en arrière plan pour récupérer et comparer les identifiants par requete HTTP
     * */

    class IdentificationClient extends AsyncTask<String, String, JSONObject> {

        private FonctionsUtilisateur userFunction;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Importing all assets like buttons, text fields
            inputLog = (EditText) findViewById(R.id.editTextLogin);
            inputPassword = (EditText) findViewById(R.id.editTextMDP);
            textViewErreur = (TextView)findViewById(R.id.textViewErreur);

            String login = inputLog.getText().toString();
            Log.v(TAG, login);
            String mdp = inputPassword.getText().toString();
            Log.v(TAG, mdp);

            userFunction = new FonctionsUtilisateur();
            params = userFunction.loginUser(login, mdp);

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParserMaison = new JSONParser();
            // Getting JSON from URL
            JSONObject jsonMaison = jParserMaison.makeHttpRequest(loginURL,"POST",params);
            return jsonMaison;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Log.v(TAG, "ACHIEVE SUCCESS");
            // check for login response
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    textViewErreur.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    Log.v(TAG, res);
                    if (Integer.parseInt(res) == 1) {
                        // user successfully logged in
                        // Store user details in SQLite Database
                        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");


                        // Clear all previous data in database
                        userFunction.logoutUser(getApplicationContext());
//                        db.addUser(json_user.getString(KEY_ID), json_user.getString(KEY_LOGIN),
//                                json_user.getString(KEY_PWD));
                        // Calling Application class (see application tag in AndroidManifest.xml)
                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                        globalVariable.setIDm(json_user.getString(KEY_IDm));

                        // Launch ListeAppareilsActivity Screen
                        Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);

                        // Close all views before launching Dashboard
                        homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeActivity);

                        // Close Login Screen
                        finish();
                    } else {
                        // Error in login
                        textViewErreur.setText("Login ou mot de passe incorrect");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}