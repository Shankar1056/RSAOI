package bigappcompany.com.rsi.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


import bigappcompany.com.rsi.Model.PDFModel;

public class SongsDBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "songsManager";

    private static final String TABLE_RINGS = "ringtones";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";

    private static final String KEY_LOC_RING = "song_loc_ring";
    private static final String KEY_RING = "ringtone";


    public SongsDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RINGS_TABLE = "CREATE TABLE " + TABLE_RINGS + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_RING + " TEXT,"
                + KEY_LOC_RING + " TEXT)";
        db.execSQL(CREATE_RINGS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RINGS);

        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 


    public void addRing(String id,String url,String loc_path) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id); // Song Name
        values.put(KEY_RING, url); // Song URl
        values.put(KEY_LOC_RING, loc_path); // Song Description
        // Inserting Row
        db.insert(TABLE_RINGS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }



    public String getRing(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RINGS, new String[] { KEY_ID,KEY_RING,
                        KEY_LOC_RING}, KEY_ID + "=?",
                new String[] { id }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            try {
                if(cursor.getCount()>0) {

                    if (cursor.getString(2) != null&&!cursor.getString(2).equals("")) {
                        return (cursor.getString(2));
                    } else
                        return null;
                }
                else
                    return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else return null;
    }



    /*// Updating single song
    public int updateSong(PDFModel song) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, song.getTitle()); // Song Name
        values.put(KEY_PATH, song.getMusicUrl()); // Song URl
        values.put(KEY_DESC, song.getDescription()); // Song Description
        values.put(KEY_IMAGE, song.getAlbumArt()); // Song image
        values.put(KEY_LOC_PATH, song.getLocal_path()); // Song localpath
        values.put(KEY_lyricist, song.getLyricist()); // Song lyricist
        values.put(KEY_lyrics, song.getLyrics()); // Song lyrics
        values.put(KEY_ID, song.getId()); // Song id
        values.put(KEY_SINGER, song.getSinger()); // Song singer

        // updating row
        return db.update(TABLE_SONGS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(song.getId()) });
    }
 
    // Getting contacts Count
    public int getSongsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SONGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count=cursor.getCount();
        cursor.close();
 
        // return count
        return count;
    }*/
 
}