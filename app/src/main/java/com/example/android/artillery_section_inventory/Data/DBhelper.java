package com.example.android.artillery_section_inventory.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Agent Cherryholmes on 5/12/2017.
 */

public class DBhelper extends SQLiteOpenHelper {

    //Database Version
    public static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "sectionInventory.db";
    // Table Name
    public static final String TABLE_INVENTORY = "itemList";
    // ItemList Column Names
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "Item_Name";
    public static final String KEY_QUANTITY_HAVE = "Quantity_Have";
    public static final String KEY_QUANTITY_WANT = "Quantity_Want";
    public static final String KEY_ISSUED_BOOLEAN = "ARMY_ISSUE_BOOLEAN";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_IMAGE = "image_data";


    //CONSTRUCTOR
    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_INVENTORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_QUANTITY_HAVE + " Integer,"
                + KEY_QUANTITY_WANT + " Integer,"
                + KEY_ISSUED_BOOLEAN + " Integer,"
                + KEY_LOCATION + " TEXT,"
                + KEY_IMAGE + " BLOB);";
        db.execSQL(CREATE_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_INVENTORY);

    }
}
