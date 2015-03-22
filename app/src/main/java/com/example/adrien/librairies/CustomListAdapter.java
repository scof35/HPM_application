package com.example.adrien.librairies;

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
        TextView conso = (TextView) convertView.findViewById(R.id.conso);
        ToggleButton onoff = (ToggleButton) convertView.findViewById(R.id.onoff);

        // getting movie data for the row
        Capteur m = capteurItems.get(position);

        // nom
        nom.setText(m.getNom());

        // conso
        conso.setText("Consommation : " + String.valueOf(m.getConso()));

        // boutton ON/OFF
        onoff.setChecked(m.getEtat());

        return convertView;
    }


}
