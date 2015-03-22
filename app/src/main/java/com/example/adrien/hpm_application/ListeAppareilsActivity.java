package com.example.adrien.hpm_application;

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;

        import com.example.adrien.librairies.Capteur;
        import com.example.adrien.librairies.CustomListAdapter;
        import com.example.adrien.librairies.FonctionsUtilisateur;
        import com.example.adrien.librairies.JSONParser;

        import org.apache.http.NameValuePair;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

public class ListeAppareilsActivity extends Activity {
    private static final String TAG = "myApp";
    private static final String TAG_SUCCESS = "success";

    //JSON Node names
    private static final String TAG_APPAREILS = "apps";
    private static final String TAG_ID_CAPTEUR = "id_capteur";
    private static final String TAG_ID_MAISON = "id_maison";
    private static final String TAG_CONSOMMATION = "consommation";
    private static final String TAG_CONNECTE = "connecte";
    private static final String TAG_NOM = "nom";

    private static String appsURL = "http://192.168.43.109/hpm/get_all_products.php";

    private ProgressDialog pDialog;
    private List<Capteur> capteurListe = new ArrayList<Capteur>();
    private ListView listView;
    private CustomListAdapter adapter;

    JSONArray appareils;
    ArrayList<HashMap<String, String>> appareilsList;
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
                listView = (ListView) findViewById(R.id.list);
                adapter = new CustomListAdapter(this, capteurListe);
                listView.setAdapter(adapter);

                new ChargerApps().execute();

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



    /**
     * AsyncTask  en arrière plan pour charger tous les appareils par requete HTTP
     * */
    class ChargerApps extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListeAppareilsActivity.this);
            pDialog.setMessage("Chargement des appareils. Attendez SVP...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All appareils from url
         * */
        protected String doInBackground(String... args) {


            JSONParser jParser = new JSONParser();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // getting JSON string from URL
            JSONObject json2 = jParser.makeHttpRequest(appsURL, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Tous les appareils: ", json2.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json2.getInt(TAG_SUCCESS);

                Log.d("SUCCEEDED: ", Integer.toString(json2.getInt(TAG_SUCCESS)));

                if (success == 1) {
                    // appareils found
                    // Getting Array of appareils
                    appareils = json2.getJSONArray(TAG_APPAREILS);
                    Log.d("JSONarray: ", json2.getJSONArray(TAG_APPAREILS).toString());

                    // looping through All Appareils
                    for (int i = 0; i < appareils.length(); i++) {
                        JSONObject c = appareils.getJSONObject(i);
//                        Log.d("JSONarray[i]: ", c.toString());
//
//                        // Storing each json item in variable
//                        String nom = c.getString(TAG_NOM);
//                        String consommation = c.getString(TAG_CONSOMMATION);
//
//                        // creating new HashMap
//                        HashMap<String, String> map = new HashMap<String, String>();
//
//                        // adding each child node to HashMap key => value
//                        map.put(TAG_NOM, nom);
//                        map.put(TAG_ID_CAPTEUR, consommation);
//                        Log.v("HashMap", map.toString());
//
//                        // adding HashList to ArrayList
//                        appareilsList.add(map);





                        JSONObject obj = appareils.getJSONObject(i);
                        Capteur capteur = new Capteur();
                        capteur.setNom(obj.getString("description"));
                        capteur.setConso(obj.getString("consommation"));



                        // adding movie to movies array
                        capteurListe.add(capteur);







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
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all appareils
            pDialog.dismiss();
            // updating UI from Background Thread
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    /**
//                     * Updating parsed JSON data into ListView
//                     * */
//                    ListAdapter adapter = new SimpleAdapter(
//                            ListeAppareilsActivity.this, appareilsList,
//                            R.layout.list_item, new String[] { TAG_ID_CAPTEUR,
//                            TAG_NAME},
//                            new int[] { R.id.id_capteur, R.id.name });
//                    // updating listview
//                    setListAdapter(adapter);
//                }
//            });

        }

    }
}