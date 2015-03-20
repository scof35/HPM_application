package com.example.adrien.librairies;

/**
 * Created by Adrien on 04/03/2015.
 */
public class Capteur {

    private String nom, conso;
    private boolean etat;

    public Capteur(){
    }

    public Capteur(String v_nom, boolean v_etat, String v_conso) {
        this.nom = v_nom;
        this.etat = v_etat;
        this.conso = v_conso;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String v_nom) {
        this.nom = v_nom;
    }

    public boolean getEtat() {
        return etat;
    }

    public void setEtat(boolean v_etat) {
        this.etat = v_etat;
    }

    public String getConso() {
        return conso;
    }

    public void setConso(String v_conso) {
        this.conso = v_conso;
    }

}
