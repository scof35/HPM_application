package com.example.adrien.librairies;

/**
 * Created by Adrien on 04/03/2015.
 */
public class Capteur {

    private String nom, conso, prix, id_m, id_c;
    //private int id_c;
    boolean etat;

    public Capteur(){
    }

    public Capteur(String v_nom, boolean v_etat, String v_conso, String v_prix,
                   String v_id_m, String v_id_c) {
        this.nom = v_nom;
        this.etat = v_etat;
        this.conso = v_conso;
        this.prix= v_prix;
        this.id_m= v_id_m;
        this.id_c= v_id_c;
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
    public String getIdc() { return id_c;}
    public String getIdm() { return id_m;}


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
    public void setIdc(String v_id_c) {this.id_c = v_id_c;}
    public void setIdm(String v_id_m) {this.id_m = v_id_m;}

}
