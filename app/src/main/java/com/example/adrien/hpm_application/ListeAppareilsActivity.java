package com.example.adrien.hpm_application;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;

        import com.example.adrien.librairies.FonctionsUtilisateur;
/**
 * Created by Adrien on 04/02/2015.
 */
public class ListeAppareilsActivity extends Activity {
    private static final String TAG = "myApp";


        FonctionsUtilisateur userFunctions;
        Button btnLogout;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.v(TAG, "did something");
            /**
             * Dashboard Screen for the application
             * */
            // Check login status in database
            userFunctions = new FonctionsUtilisateur();
            if(userFunctions.isUserLoggedIn(getApplicationContext())){
                // user already logged in show databoard
                setContentView(R.layout.activity_appareils);
                btnLogout = (Button) findViewById(R.id.btnLogout);

                btnLogout.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        userFunctions.logoutUser(getApplicationContext());
                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(login);
                        // Closing dashboard screen
                        finish();
                    }
                });

            }else{
                // user is not logged in show login screen
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                // Closing dashboard screen
                finish();
            }
        }
    }