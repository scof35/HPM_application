package com.example.adrien.librairies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "hpm";

    // Login table name
    private static final String TABLE_LOGIN = "maisons";

    // Login Table Columns names
    private static final String KEY_IDm = "id_maison";
    private static final String KEY_IDc = "id_capteur";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ETATr = "etat_reel";
    private static final String KEY_ETATd = "etat_demande";
    private static final String KEY_DEMANDE_TRAITEE = "demande_traitee";
    private static final String KEY_PUISSc = "puissance_cumulee";
    private static final String KEY_PUISSa = "puissance_actuelle";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_IDm + " INTEGER PRIMARY KEY,"
                + KEY_IDc + " INTEGER,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_ETATr + " INTEGER,"
                + KEY_ETATd + " INTEGER,"
                + KEY_DEMANDE_TRAITEE + " INTEGER,"
                + KEY_PUISSc + " FLOAT,"
                + KEY_PUISSa + " FLOAT"
                + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

//    /**
//     * Storing user details in database
//     * */
//    public void addUser(String id_maison, String login, String password) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_IDm, id_maison);
//        values.put(KEY_LOGIN, login); // Login
//        values.put(KEY_PWD, password); // Mot de passe
//
//        // Inserting Row
//        db.insert(TABLE_LOGIN, null, values);
//        db.close(); // Closing database connection
//    }

//    /**
//     * Getting user data from database
//     * */
//    public HashMap<String, String> getUserDetails(){
//        HashMap<String,String> user = new HashMap<String,String>();
//        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        // Move to first row
//        cursor.moveToFirst();
//        if(cursor.getCount() > 0){
//            user.put("id_maison", cursor.getString(1));
//            user.put("login", cursor.getString(2));
//            user.put("password", cursor.getString(3));
//            user.put("connected", cursor.getString(4));
//        }
//        cursor.close();
//        return user;
//    }

    public String getUserIdMaison() {
        String id_maison = "FAUX";
        int nbColonnes;
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        nbColonnes = cursor.getCount();

        if (nbColonnes > 0) {
            id_maison = cursor.getString(0);
            Log.v("ID_MAISON", id_maison);
        }
        cursor.close();
        return id_maison;
    }


    // Updating état capteur
    public int updateEtatCapteur(Capteur capteur) {
        Log.v("---------FONCTION------", "UPDATE_ETAT_CAPTEUR");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean etatCapteur = capteur.getEtat();
        String conso = capteur.getConso();
        values.put(KEY_ETATd, etatCapteur);
        values.put(KEY_PUISSa,conso);

        // updating row
        return db.update(TABLE_LOGIN, values, KEY_IDc + " = ?",
                new String[]{String.valueOf(capteur.getIdc())});
    }

    // Deleting capteur
    public void deleteCapteur(Capteur capteur) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, KEY_IDc + " = ?",
                new String[]{String.valueOf(capteur.getNom())});
        //db.close();
    }

    public boolean capteurExiste(Capteur capteur) {
        Log.v("---------FONCTION------", "CAPTEUR_EXISTE");

        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_LOGIN + " WHERE " + KEY_IDc + " = " + capteur.getIdc();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    /**
     * Getting nombre de capteurs dans la maison
     */
    public int getCapteurCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();
        //db.close();
        return rowCount;
    }

    /**
     * Storing appareils details in database
     */
    public void addAppareil(Capteur capt) {
        Log.v("---------FONCTION------", "ADD_APPAREIL");

        SQLiteDatabase db = this.getWritableDatabase();
        //Log.v("TEST", "SQLITE");

        ContentValues values = new ContentValues();
        values.put(KEY_IDc, capt.getIdc());
        values.put(KEY_DESCRIPTION, capt.getNom());
        //values.put(KEY_ETATr, capt.getId());
        values.put(KEY_ETATd, capt.getEtat());
        //values.put(KEY_DEMANDE_TRAITEE, capt.getId());
        //values.put(KEY_PUISSc, capt.getId());
        values.put(KEY_PUISSa, capt.getConso());
        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        //db.close(); // Closing database connection
    }

    public List<Capteur> getAppareils() {
        Log.v("---------FONCTION------", "GET_APPAREILS");

        List<Capteur> captList = new ArrayList<Capteur>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
        String etatCapt;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Capteur capt = new Capteur();
                capt.setIdc(cursor.getString(1));
                capt.setNom(cursor.getString(2));
                etatCapt = cursor.getString(4);
                Log.v(cursor.getString(1), etatCapt);
                if (etatCapt.equals("1")) {
                    capt.setEtat(true);
                } else {
                    capt.setEtat(false);
                }

                capt.setConso(cursor.getString(7));
                // Adding contact to list
                captList.add(capt);
            } while (cursor.moveToNext());
        }
        //Log.v("Appareils SQLITE",captList.toString());
        cursor.close();
        // db.close();
        return captList;
    }

    public Object getDonneeStrCapteur(int id_c, String donnee) {
        Log.v("---------FONCTION------", "GET_DONNEE_STR_CAPT");

        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_LOGIN + " WHERE " + KEY_IDc + " = " + id_c;
        Cursor cursor = db.rawQuery(Query, null);
        int i = 0;
        switch (donnee) {
            case KEY_IDc:
                i = 1;
                break;
            case KEY_DESCRIPTION:
                i = 2;
                break;
            case KEY_ETATr:
                i = 3;
                break;
            case KEY_ETATd:
                i = 4;
                break;
            case KEY_DEMANDE_TRAITEE:
                i = 5;
                break;
            case KEY_PUISSc:
                i = 6;
                break;
            case KEY_PUISSa:
                i = 7;
                break;
            default:
                break;
        }
        return cursor.getString(i);
    }


    public boolean getDonneeBoolCapteur(int id_c) {
        Log.v("---------FONCTION------", "GET_DONNEE_BOOL_CAPTEUR");

        int etat;
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_LOGIN + " WHERE " + KEY_IDc + "=" + id_c;

        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        int i = cursor.getColumnIndex(KEY_ETATd);

        etat = cursor.getInt(i);
        //Log.v("Etat du capteur", Integer.toString(etat));
        if (etat == 0) {// || cursor.getInt(4)==0
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    /**
     * Delete all tables and create them again
     */
    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

}
