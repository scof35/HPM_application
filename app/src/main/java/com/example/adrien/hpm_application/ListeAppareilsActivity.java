package com.example.adrien.hpm_application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.adrien.librairies.Capteur;
import com.example.adrien.librairies.CustomListAdapter;
import com.example.adrien.librairies.DatabaseHandler;
import com.example.adrien.librairies.FonctionsUtilisateur;

import org.json.JSONArray;


import java.util.ArrayList;
import java.util.List;

public class ListeAppareilsActivity extends Activity {

    private static List<Capteur> capteurListe = new ArrayList<Capteur>();
    private static ListView listView;
    private static CustomListAdapter adapt;

    Button btnLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v("---------FONCTION------", "ListeAppareilsActivity");
        super.onCreate(savedInstanceState);

        DatabaseHandler db = new DatabaseHandler(this);
        capteurListe.clear();

        setContentView(R.layout.activity_appareils);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        listView = (ListView) findViewById(R.id.list);

        capteurListe = db.getAppareils();
        adapt = new CustomListAdapter(this, capteurListe);
        listView.setAdapter(adapt);


        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //userFunctions.logoutUser(getApplicationContext());
                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
                finish();
            }
        });

        //}else{
//                // user is not logged in show login screen
//                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
//                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(login);
//                finish();
//            }
    }
}