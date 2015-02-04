package com.example.adrien.hpm_application;

//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import java.lang.String;
//import org.apache.http.*;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ByteArrayEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.json.*;
//
//
//public class LoginActivity extends ActionBarActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public boolean seConnecter(){
//        EditText edTlogin = null;
//        EditText edTMDP = null;
//        String sLogin = null;
//        String sMDP = null;
//        JSONObject json = new JSONObject();
//
//        edTlogin = (EditText)findViewById(R.id.editTextLogin);
//        edTMDP = (EditText)findViewById(R.id.editTextMDP);
//        sLogin = edTlogin.getText().toString();
//        sMDP = edTMDP.getText().toString();
//        try{
//            json.put("login", sLogin);
//            json.put("password", sMDP);
//            HttpParams httpParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, 0);
//            HttpConnectionParams.setSoTimeout(httpParams, 0);
//            HttpClient client = new DefaultHttpClient(httpParams);
//            //
//            //String url = "http://10.0.2.2:8080/sample1/webservice2.php?" +
//            //             "json={\"UserName\":1,\"FullName\":2}";
//            String url = "http://192.168.43.177/hpm/webservice.php";
//
//            HttpPost request = new HttpPost(url);
//            request.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF8")));
//            request.setHeader("json", json.toString());
//            HttpResponse response = client.execute(request);
//            HttpEntity entity = response.getEntity();
//        }
//        catch(Throwable t){
//            Toast.makeText(this, "Requete impossible : " + t.toString(),
//                    Toast.LENGTH_LONG).show();
//        }
//
//
//
//
//
//        return true;
//    }
//}

import org.json.JSONException;
        import org.json.JSONObject;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import com.example.adrien.librairies.DatabaseHandler;
        import com.example.adrien.librairies.FonctionsUtilisateur;

public class LoginActivity extends Activity {
    Button btnLogin;
    Button btnLinkToRegister;
    EditText inputLog;
    EditText inputPassword;
    TextView loginErrorMsg;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Importing all assets like buttons, text fields
        inputLog = (EditText) findViewById(R.id.editTextLogin);
        inputPassword = (EditText) findViewById(R.id.editTextMDP);
        btnLogin = (Button) findViewById(R.id.buttonConnecter);
        //btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        loginErrorMsg = (TextView) findViewById(R.id.textViewErreur);

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String login = inputLog.getText().toString();
                String mdp = inputPassword.getText().toString();
                FonctionsUtilisateur userFunction = new FonctionsUtilisateur();
                JSONObject json = userFunction.loginUser(login, mdp);

                // check for login response
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        loginErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if(Integer.parseInt(res) == 1){
                            // user successfully logged in
                            // Store user details in SQLite Database
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");

                            // Clear all previous data in database
                            userFunction.logoutUser(getApplicationContext());
                            db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));

                            // Launch Dashboard Screen
                            Intent dashboard = new Intent(getApplicationContext(), ListeAppareilsActivity.class);

                            // Close all views before launching Dashboard
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);

                            // Close Login Screen
                            finish();
                        }else{
                            // Error in login
                            loginErrorMsg.setText("Login ou mot de passe incorrect");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

//        // Link to Register Screen
//        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),
//                        RegisterActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });
    }
}