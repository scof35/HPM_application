package com.example.adrien.librairies;

        import java.util.HashMap;

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
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PWD = "password";
    private static final String KEY_IDc = "id_capteur";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_IDm + " INTEGER PRIMARY KEY,"
                + KEY_LOGIN + " TEXT UNIQUE,"
                + KEY_PWD + " TEXT," + ")";
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

    /**
     * Storing user details in database
     * */
    public void addUser(String id_maison, String login, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDm, id_maison);
        values.put(KEY_LOGIN, login); // Login
        values.put(KEY_PWD, password); // Mot de passe

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("id_maison", cursor.getString(1));
            user.put("login", cursor.getString(2));
            user.put("password", cursor.getString(3));
            user.put("connected", cursor.getString(4));
        }
        cursor.close();
        return user;
    }

    public String getUserIdMaison(){
        String id_maison = "FAUX";
        int nbColonnes;
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        nbColonnes = cursor.getCount();
        Log.v("ID_MAISON", Integer.toString(nbColonnes));

        if( nbColonnes> 0){
            id_maison = cursor.getString(0);
            Log.v("ID_MAISON", id_maison);
        }
            cursor.close();
            return id_maison;
    }


    // Updating état capteur
    public int updateEtatCapteur(Capteur capteur, boolean onOff) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDc, capteur.getNom());

        // updating row
        return db.update(TABLE_LOGIN, values, KEY_IDm + " = ?",
                new String[] { String.valueOf(capteur.getNom()) });
    }

    // Deleting capteur
    public void deleteCapteur(Capteur capteur) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, KEY_IDm + " = ?",
                new String[] { String.valueOf(capteur.getNom()) });
        //db.close();
    }

    /**
     * Getting nombre de capteurs dans la maison
     * */
    public int getCapteurCount(){
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }


    /**
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

}
