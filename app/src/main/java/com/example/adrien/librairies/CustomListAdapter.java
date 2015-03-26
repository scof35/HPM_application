package com.example.adrien.librairies;

import android.content.DialogInterface;
import android.util.Log;
import android.widget.BaseAdapter;

import com.example.adrien.hpm_application.R;
import com.example.adrien.librairies.Capteur;

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

/**
* Created by Adrien on 04/03/2015.
*/
public class CustomListAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Capteur> capteurItems;

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

        // getting capteur data for the row
        Capteur m = capteurItems.get(position);

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
                if(isChecked){
                    Log.v("OLOL","1");

                } else{
                    Log.v("OLOL","0");
                }

            }
        });

        return convertView;
    }


}
