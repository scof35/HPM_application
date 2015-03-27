package com.example.adrien.librairies;

/**
 * Created by Adrien on 04/03/2015.
 */
public class Capteur {

    private String nom, conso, prix, id;
    private boolean etat;

    public Capteur(){
    }

    public Capteur(String v_nom, boolean v_etat, String v_conso, String v_prix, String v_id) {
        this.nom = v_nom;
        this.etat = v_etat;
        this.conso = v_conso;
        this.prix= v_prix;
        this.id= v_id;
    }

    //Accesseurs
    public String getNom() {
        return nom;
    }
    public boolean getEtat() {
        return etat;
    }
    public String getConso() {
        return conso;
    }
    public String getPrix() { return prix;}
    public String getId() { return id;}

    //Mutateurs
    public void setNom(String v_nom) {
        this.nom = v_nom;
    }
    public void setEtat(boolean v_etat) {
        this.etat = v_etat;
    }
    public void setConso(String v_conso) {
        this.conso = v_conso;
    }
    public void setPrix(String v_prix) {
        this.prix = v_prix;
    }
    public void setId(String v_id) {this.id = v_id;}

}
