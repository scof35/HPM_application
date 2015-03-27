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
public class CustomListAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Capteur> capteurItems;
    private static String appsURL = "http://192.168.43.109/hpm/update_product.php";


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
        ToggleButton onoff = (ToggleButton) convertView.findViewById(R.id.onoff);
//
        // getting capteur data for the row
        final Capteur m = capteurItems.get(position);

        // nom
        nom.setText(m.getNom());

        // conso
        conso1.setText("Consommation : ");
        conso2.setText(String.valueOf(m.getConso()) + " Watts");

        //Prix
        prix1.setText("Prix : ");
        prix2.setText(String.valueOf(m.getPrix()) + " €");

        // boutton ON/OFF
        onoff.setChecked(m.getEtat());
        onoff.setOnCheckedChangeListener(new  CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                JSONParser json = new JSONParser();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id_maison", ListeAppareilsActivity.id_maison));
                params.add(new BasicNameValuePair("id_capteur", m.getId()));
                params.add(new BasicNameValuePair("description", m.getNom()));
                if(isChecked){
                    Log.v("OLOL","1");
                    params.add(new BasicNameValuePair("etat_demande",Integer.toString(1)));
                } else{
                    Log.v("OLOL","0");
                    params.add(new BasicNameValuePair("etat_demande", Integer.toString(0)));
                }
                json.makeHttpRequest(appsURL,"POST",params);
            }
        });
        return convertView;
    }


}
