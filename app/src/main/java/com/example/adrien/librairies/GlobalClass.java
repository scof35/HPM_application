package com.example.adrien.librairies;

import android.app.Application;

/**
 * Created by Adrien on 01/04/2015.
 */
public class GlobalClass extends Application {

    private String id_maison;

    public String getIDm() {

        return id_maison;
    }

    public void setIDm(String v_id_maison) {

        id_maison = v_id_maison;
    }
}
