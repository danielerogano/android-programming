package com.dani.digitalbusride;
 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DatabaseHelper extends SQLiteOpenHelper {
 
    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "BusDB";
 
    // Table Names
    private static final String TABLE_FERMATA = "Fermate";
    private static final String TABLE_TRACKPOINT = "Trackpoints";
 
    // FERMATA Table - column names
    private static final String KEY_ID = "id";
    private static final String KEY_NOME = "nome";
    private static final String KEY_LAT_A = "latitude";
    private static final String KEY_LON_A = "longitude";
    private static final String KEY_LINEA = "linea";

    // TRACKPOINT Table - column names
    private static final String KEY_IDTRACK = "idtrack";
    private static final String KEY_ISTANTE = "istante";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_SPEED = "speed";
    private static final String KEY_LINEA_TRACK = "lineatrack";

    // Table Create Statements
    // FERMATA table create statement
    private static final String CREATE_TABLE_FERMATA = "CREATE TABLE "
            + TABLE_FERMATA + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOME
            + " TEXT," + KEY_LAT_A + " REAL," + KEY_LON_A
            + " REAL," + KEY_LINEA + " TEXT" + ")";
 
    // TRACKPOINT table create statement
    private static final String CREATE_TABLE_TRACKPOINT = "CREATE TABLE "
            + TABLE_TRACKPOINT + "(" + KEY_IDTRACK + " INTEGER," + KEY_ISTANTE
            + " INTEGER PRIMARY KEY," + KEY_LATITUDE + " REAL," + KEY_LONGITUDE
            + " REAL," + KEY_SPEED + " REAL," + KEY_LINEA_TRACK + " TEXT" + ")";
 
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // creating required tables
        db.execSQL(CREATE_TABLE_FERMATA);
        db.execSQL(CREATE_TABLE_TRACKPOINT);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FERMATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKPOINT);
 
        // create new tables
        onCreate(db);
    }
 
    // ------------------------ "fermate" table methods ----------------//
 
     /**
     * Creazione fermata
     */
    public long createFermata(Fermata fermata) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, fermata.getId());
        values.put(KEY_NOME, fermata.getNome());
        values.put(KEY_LAT_A, fermata.getLatitude());
        values.put(KEY_LON_A, fermata.getLongitude());
        values.put(KEY_LINEA, fermata.getLinea());

        long fermata_id = db.insert(TABLE_FERMATA, null, values);
        
        return fermata_id;
    }
    /**
     * getting all todos
     * */
    public List<Fermata> getAllToDos() {
        List<Fermata> fermate = new ArrayList<Fermata>();
        String selectQuery = "SELECT  * FROM " + TABLE_FERMATA;
 
        Log.e(LOG, selectQuery);
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Fermata td = new Fermata();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setNome((c.getString(c.getColumnIndex(KEY_NOME))));
                td.setLatitude(c.getDouble(c.getColumnIndex(KEY_LAT_A)));
                td.setLongitude(c.getDouble(c.getColumnIndex(KEY_LON_A)));
                td.setLinea((c.getString(c.getColumnIndex(KEY_LINEA))));
 
                // adding to fermata list
                fermate.add(td);
            } while (c.moveToNext());
        }
 
        return fermate;
    }
 
    /**
     * Retrieve delle fermate di una stessa linea
     * */
    public List<Fermata> getAllFermateByLinea(String linea) {
        List<Fermata> fermate = new ArrayList<Fermata>();
 
        String selectQuery = "SELECT  * FROM " + TABLE_FERMATA + " td WHERE td."
                + KEY_LINEA + " = '" + linea+"'";
 
        Log.e(LOG, selectQuery);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
            	Fermata td = new Fermata();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setNome((c.getString(c.getColumnIndex(KEY_NOME))));
                td.setLatitude(c.getDouble(c.getColumnIndex(KEY_LAT_A)));
                td.setLongitude(c.getDouble(c.getColumnIndex(KEY_LON_A)));
                td.setLinea((c.getString(c.getColumnIndex(KEY_LINEA))));
 
                // adding to fermata list
                fermate.add(td);
            } while (c.moveToNext());
        }
 
        return fermate;
    }
    
    public List<String> getAllLinee() {
        List<String> linee = new ArrayList<String>();
 
        String selectQuery = "SELECT DISTINCT "+KEY_LINEA+" FROM '" + TABLE_FERMATA+"'";
 
        Log.e(LOG, selectQuery);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
            	linee.add((c.getString(c.getColumnIndex(KEY_LINEA))));
            } while (c.moveToNext());
        }
        return linee;
    }
 
    /**
     * Retrieve numero fermate
     */
    public int getFermataCount(String linea) {
        String countQuery = "SELECT  * FROM '" + TABLE_FERMATA + "' td WHERE td."
                + KEY_LINEA + " = '" + linea+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
 
        int count = cursor.getCount();
        cursor.close();
 
        // return count
        return count;
    }
 
    /**
     * Eliminazione fermata
     */
    public void deleteFermata(long fermata_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FERMATA, KEY_ID + " = ?",
                new String[] { String.valueOf(fermata_id) });
    }
    
    /**
     * Eliminazione fermate per linea
     */
    public void deleteFermate(String linea) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FERMATA, KEY_LINEA + " = ?",
                new String[] { String.valueOf(linea) });
    }
 
    // ------------------------ "trackpoint" table methods ----------------//
 
    /**
     * Creating trackpoint
     */
    public long createTrackpoint(Trackpoint trackpoint) {
        SQLiteDatabase db = this.getWritableDatabase();
         
        ContentValues values = new ContentValues();
        values.put(KEY_IDTRACK, trackpoint.getIdtrack());
        values.put(KEY_ISTANTE, trackpoint.getIstante());
        values.put(KEY_LAT_A, trackpoint.getLatitude());
        values.put(KEY_LON_A, trackpoint.getLongitude());
        values.put(KEY_SPEED, trackpoint.getSpeed());
        values.put(KEY_LINEA_TRACK, trackpoint.getLinea());
 
        // insert row
        long track_id = db.insert(TABLE_TRACKPOINT, null, values);
 
        return track_id;
    }
 
    /**
     * getting all trackpoints
     * */
    public List<Trackpoint> getAllTracks() {
        List<Trackpoint> trackpoints = new ArrayList<Trackpoint>();
        String selectQuery = "SELECT  * FROM " + TABLE_TRACKPOINT;
 
        Log.e(LOG, selectQuery);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Trackpoint t = new Trackpoint();
                t.setId(c.getInt((c.getColumnIndex(KEY_IDTRACK))));
                t.setIstante((c.getLong(c.getColumnIndex(KEY_ISTANTE))));
                t.setLatitude(c.getDouble(c.getColumnIndex(KEY_LATITUDE)));
                t.setLongitude(c.getDouble(c.getColumnIndex(KEY_LONGITUDE)));
                t.setSpeed((c.getDouble(c.getColumnIndex(KEY_SPEED))));
                t.setLinea((c.getString(c.getColumnIndex(KEY_LINEA_TRACK))));
 
                // adding to tags list
                trackpoints.add(t);
            } while (c.moveToNext());
        }
        return trackpoints;
    }
    
    /**
     * Retrieve delle tracce
     * */
    public List<Trackpoint> getAllTrackpointsByTrackid(long trackid) {
        List<Trackpoint> trackpoints = new ArrayList<Trackpoint>();
 
        String selectQuery = "SELECT  * FROM " + TABLE_TRACKPOINT + " td WHERE td."
                + KEY_IDTRACK + " = '" + trackid+"'";
 
        Log.e(LOG, selectQuery);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
            	Trackpoint tp = new Trackpoint();
                tp.setId(c.getInt((c.getColumnIndex(KEY_IDTRACK))));
                tp.setIstante((c.getLong(c.getColumnIndex(KEY_ISTANTE))));
                tp.setLatitude(c.getDouble(c.getColumnIndex(KEY_LATITUDE)));
                tp.setLongitude(c.getDouble(c.getColumnIndex(KEY_LONGITUDE)));
                tp.setSpeed((c.getDouble(c.getColumnIndex(KEY_SPEED))));
                tp.setLinea((c.getString(c.getColumnIndex(KEY_LINEA_TRACK))));
 
                // adding to fermata list
                trackpoints.add(tp);
            } while (c.moveToNext());
        }
 
        return trackpoints;
    }
    
    /**
     * Retrieve delle tracce per linea
     * */
    public List<Trackpoint> getAllTrackpointsByLinea(String linea) {
        List<Trackpoint> trackpoints = new ArrayList<Trackpoint>();
 
        String selectQuery = "SELECT  * FROM " + TABLE_TRACKPOINT + " td WHERE td."
                + KEY_LINEA_TRACK + " = '" + linea+"'";
 
        Log.e(LOG, selectQuery);
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
            	Trackpoint tp = new Trackpoint();
                tp.setId(c.getInt((c.getColumnIndex(KEY_IDTRACK))));
                tp.setIstante((c.getLong(c.getColumnIndex(KEY_ISTANTE))));
                tp.setLatitude(c.getDouble(c.getColumnIndex(KEY_LATITUDE)));
                tp.setLongitude(c.getDouble(c.getColumnIndex(KEY_LONGITUDE)));
                tp.setSpeed((c.getDouble(c.getColumnIndex(KEY_SPEED))));
                tp.setLinea((c.getString(c.getColumnIndex(KEY_LINEA_TRACK))));
 
                // adding to fermata list
                trackpoints.add(tp);
            } while (c.moveToNext());
        }
 
        return trackpoints;
    }
 
    /**
     * Eliminazione trackpoint
     */
    public void deleteTrackpoint(long trackid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRACKPOINT, KEY_IDTRACK + " = ?",
                new String[] { String.valueOf(trackid) });
    }
 
    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
 
    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
