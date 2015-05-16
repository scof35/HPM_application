package com.example.adrien.hpm_application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.net.wifi.WifiInfo;

import com.example.adrien.librairies.Capteur;
import com.example.adrien.librairies.Constante;
import com.example.adrien.librairies.DatabaseHandler;
import com.example.adrien.librairies.GlobalClass;
import com.example.adrien.librairies.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends Activity implements View.OnClickListener {

    Button btnAppareils, btnReglages, btnQuitter;
    JSONArray appareils;

    private static String appsURL;

    private ProgressDialog pDialog;
    private static final String TAG_APPAREILS = "apps";
    private static final String TAG_IDm = "id_maison";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        appsURL = "http://"+ Constante.IP_SERVEUR+"/hpm/get_all_sensors.php";

        btnAppareils = (Button) findViewById(R.id.btnAppareils);
        btnReglages = (Button) findViewById(R.id.btnReglages);
        btnQuitter = (Button) findViewById(R.id.btnQuitter);

        // assign listeners to buttons
        btnAppareils.setOnClickListener(this);
        btnReglages.setOnClickListener(this);
        btnQuitter.setOnClickListener(this);

            new ChargerApps().execute();

    }

    @Override
    public void onClick(View v) {
        // define the button that invoked the listener by id
        switch (v.getId()) {
            case R.id.btnAppareils:
                // Launch ListeAppareilsActivity Screen
                Intent listAppsActivity = new Intent(getApplicationContext(), ListeAppareilsActivity.class);
                listAppsActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(listAppsActivity);
                finish();
                break;
            case R.id.btnReglages:

                break;
            case R.id.btnQuitter:
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
                break;
        }
    }


    /**
     * AsyncTask  en arrière plan pour charger tous les appareils par requete HTTP
     */
    class ChargerApps extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            Log.v(Constante.TAG, "Exécution AsyncThread - ChargerApps");
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Chargement des appareils. Attendez SVP...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            if(!isFinishing()) {pDialog.show();}
        }

        /**
         * getting All appareils from url
         */
        protected String doInBackground(String... args) {

            Boolean captExiste = false;
            String etatCapt;
            JSONParser jParserAppareils = new JSONParser();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // Calling Application class (see application tag in AndroidManifest.xml)
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            // Get id_maison from global/application context
            final String id_maison = globalVariable.getIDm();
            params.add(new BasicNameValuePair("id_maison", id_maison));

            Log.v(Constante.TAG, "Requête HTTP - GET");

            // getting JSON string from URL
            JSONObject json_appareils = jParserAppareils.makeHttpRequest(appsURL, "GET", params);

            // Check your log cat for JSON reponse
            Log.v(Constante.TAG, "Résultat requête "+json_appareils.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json_appareils.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // appareils found
                    // Getting Array of appareils

                    appareils = json_appareils.getJSONArray(TAG_APPAREILS);
                    //Log.d("JSONarray ", appareils.toString());

                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                    // looping through All Appareils
                    for (int i = 0; i < appareils.length(); i++) {
                        JSONObject obj = appareils.getJSONObject(i);
                        if (obj.getString(TAG_IDm).equals(id_maison)) {
                            Capteur capteur = new Capteur();
                            capteur.setNom(obj.getString("description"));
                            capteur.setConso(obj.getString("puissance_actuelle"));
                            capteur.setIdc(obj.getString("id_capteur"));
                            //capteur.setIdm(obj.getString("id_maison"));
                            etatCapt = obj.getString("etat_demande");
                            Log.v(Constante.TAG, "Etat capteur "+capteur.getNom()+"->"+ etatCapt);
                            if (etatCapt.equals("1")) {
                                capteur.setEtat(true);
                            } else {
                                capteur.setEtat(false);
                            }

                            captExiste = db.capteurExiste(capteur);
                            Log.v(Constante.TAG, "Capteur existant ? "+Boolean.toString(captExiste));
                            if (captExiste) {
                                db.updateEtatCapteur(capteur);
                            } else {
                                // ajoute capteur à la liste des capteurs
                                db.addAppareil(capteur);
                                //Log.v("CapteurListe", );
                            }
                        }
                    }

                } else {
                    // no appareils found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all appareils
            pDialog.dismiss();
        }

    }


}

