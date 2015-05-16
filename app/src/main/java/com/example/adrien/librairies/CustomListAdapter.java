package com.example.adrien.librairies;

import android.content.DialogInterface;
import android.os.StrictMode;
import android.util.Log;
import android.widget.BaseAdapter;

import com.example.adrien.hpm_application.ListeAppareilsActivity;
import com.example.adrien.hpm_application.R;
import com.example.adrien.librairies.Capteur;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Adrien on 04/03/2015.
 */
public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Capteur> capteurItems;
    private static String updateURL = "http://192.168.43.109/hpm/update_sensor.php";
    //private static String updateURL = "http://10.0.2.2/hpm/update_sensor.php";



    public CustomListAdapter(Activity activity, List<Capteur> capteurItems) {
        this.activity = activity;
        this.capteurItems = capteurItems;
    }

    @Override
    public int getCount() {
        return capteurItems.size();
    }

    @Override
    public Object getItem(int location) {
        return capteurItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("---------FONCTION------", "GET VIEW");

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.custom_row, null);

        TextView nom = (TextView) convertView.findViewById(R.id.nom);
        TextView conso1 = (TextView) convertView.findViewById(R.id.conso1);
        TextView conso2 = (TextView) convertView.findViewById(R.id.conso2);
        TextView prix1 = (TextView) convertView.findViewById(R.id.prix1);
        TextView prix2 = (TextView) convertView.findViewById(R.id.prix2);
        final ToggleButton onoff = (ToggleButton) convertView.findViewById(R.id.onoff);
//
        // getting capteur data for the row
        final Capteur m = capteurItems.get(position);

        // nom
        nom.setText(m.getNom());

        // conso
        conso1.setText("Consommation : ");
        conso2.setText(String.valueOf(m.getConso()) + " kWh");

        //Prix
        prix1.setText("Prix : ");
        prix2.setText(String.valueOf(m.getPrix()) + " €");

        Log.v("Etat", Boolean.toString(m.getEtat()));

        // boutton ON/OFF
        final DatabaseHandler db = new DatabaseHandler(parent.getContext());
        onoff.setChecked(m.getEtat());


        onoff.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Boolean isChecked = ((ToggleButton) view).isChecked();
                        m.setEtat(isChecked);

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        JSONParser json = new JSONParser();

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("id_maison", db.getUserIdMaison()));
                        Log.v("ID_C", m.getIdc());
                        params.add(new BasicNameValuePair("id_capteur", m.getIdc()));
                        if (isChecked) {
                            Log.v("OLOL", "1");
                            //m.setEtat(true);
                            params.add(new BasicNameValuePair("etat_demande", "1"));
                        } else {
                            Log.v("OLOL", "0");
                            //m.setEtat(false);
                            params.add(new BasicNameValuePair("etat_demande", "0"));
                        }

                        db.updateEtatCapteur(m);
                        json.makeHttpRequest(updateURL, "POST", params);
                    }
                });

        return convertView;
    }


}
