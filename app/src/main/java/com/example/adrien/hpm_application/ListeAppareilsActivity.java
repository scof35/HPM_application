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
    //JSON Node names
    private static final String TAG_APPAREILS = "apps";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_IDm = "id_maison";


    private static String appsURL = "http://192.168.43.109/hpm/get_all_sensors.php";

    private ProgressDialog pDialog;
    private static List<Capteur> capteurListe = new ArrayList<Capteur>();
    private static ListView listView;
    private static CustomListAdapter adapt;

    JSONArray appareils;
    Button btnLogout;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            /**
             * Dashboard Screen for the application
             * */
              DatabaseHandler db = new DatabaseHandler(this);
             capteurListe.clear();

            Log.v("TEST", "ListeAppareilsActivity");
            setContentView(R.layout.activity_appareils);
            btnLogout = (Button) findViewById(R.id.btnLogout);
            listView = (ListView) findViewById(R.id.list);

            capteurListe = db.getAppareils();
            adapt = new CustomListAdapter(this, capteurListe);
            listView.setAdapter(adapt);

               // new ChargerApps().execute();

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




//    /**
//     * AsyncTask  en arrière plan pour charger tous les appareils par requete HTTP
//     * */
//    class ChargerApps extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(ListeAppareilsActivity.this);
//            pDialog.setMessage("Chargement des appareils. Attendez SVP...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        /**
//         * getting All appareils from url
//         * */
//        protected String doInBackground(String... args) {
//
//            String etatCapt;
//            JSONParser jParser = new JSONParser();
//
//            // Building Parameters
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("id_maison", id_maison));
//
//            // getting JSON string from URL
//            JSONObject json2 = jParser.makeHttpRequest(appsURL, "GET", params);
//
//            // Check your log cat for JSON reponse
//            Log.d("Tous les appareils: ", json2.toString());
//
//            try {
//                // Checking for SUCCESS TAG
//                int success = json2.getInt(TAG_SUCCESS);
//
//                Log.d("SUCCEEDED: ", Integer.toString(json2.getInt(TAG_SUCCESS)));
//
//                if (success == 1) {
//                    // appareils found
//                    // Getting Array of appareils
//
//                    appareils = json2.getJSONArray(TAG_APPAREILS);
//                    Log.d("JSONarray: ", appareils.toString());
//                    Log.d("AppsLength: ", Integer.toString(appareils.length()));
//
//                    // looping through All Appareils
//                    for (int i = 0; i < appareils.length(); i++) {
//
//                        JSONObject obj = appareils.getJSONObject(i);
//                        Log.v("CapteurListe", "HERE");
//
//                        if(obj.getString(TAG_IDm).equals(id_maison)) {
//
//                            Capteur capteur = new Capteur();
//                            capteur.setNom(obj.getString("description"));
//                            capteur.setConso(obj.getString("puissance_actuelle"));
//                            capteur.setId(obj.getString("id_capteur"));
//                            etatCapt = obj.getString("etat_demande");
//                            Log.v("Etatcapt", obj.getString("etat_demande"));
//                            if(etatCapt.equals("1")) {
//                                capteur.setEtat(true);
//                            }else{
//                                capteur.setEtat(false);
//                            }
//                            // ajoute capteur à la liste des capteurs
//                            capteurListe.add(capteur);
//                        }
//                    }
//
//
//                } else {
//                    // no appareils found
//                    // Launch Add New product Activity
//                    Intent i = new Intent(getApplicationContext(),
//                            LoginActivity.class);
//                    // Closing all previous activities
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog after getting all appareils
//
//            pDialog.dismiss();
////            //adapter.notifyDataSetChanged();
////            // updating UI from Background Thread
////            runOnUiThread(new Runnable() {
////                public void run() {
////                    /**
////                     * Updating parsed JSON data into ListView
////                     * */
////                    // updating listview
////                    //setListAdapter(adapt);
////                    listView.setAdapter(adapt);
////                }
////            });
//
//        }
//
//    }
}