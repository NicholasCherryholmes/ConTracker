package com.example.android.artillery_section_inventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.artillery_section_inventory.Data.DBhelper;
import com.example.android.artillery_section_inventory.Data.ItemCursorAdapter;


public class MainActivity extends AppCompatActivity {

    private DBhelper mDBhelper;
    public Cursor cursor;
    public static ItemCursorAdapter itemAdapter;
    public static ListView lvItems;

    // ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBhelper = new DBhelper(this);


    }


    // ON CREATE OPTIONS MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.inventory_menu_main, menu);
        return true;
    }

    //ACTION BAR MENU when clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SQLiteDatabase db = mDBhelper.getWritableDatabase();
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {


            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:


                db.delete(DBhelper.TABLE_INVENTORY, null, null);
                onStart();
                return true;
//Populate Standard Items, that the app is primarily designed to track.
            case R.id.action_add_standard_entries_entries:

                String[] INSERT_ROWS = getResources().getStringArray(R.array.artilleryEquipmentRows);


                for (int i = 0; i < 33; i++)
                    db.execSQL(INSERT_ROWS[i]);
                onStart();
                return true;


//ABOUT APP INFO
            case R.id.action_display_appInfo:

                AlertDialog aboutDialog = new AlertDialog.Builder(MainActivity.this).create();
                aboutDialog.setTitle(MainActivity.this.getString(R.string.aboutDialogTitle));
                aboutDialog.setMessage(MainActivity.this.getString(R.string.aboutDialogMessage));
                //TODO set a icon
                aboutDialog.setButton(AlertDialog.BUTTON_NEUTRAL, MainActivity.this.getString(R.string.confirmDialogOk),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }


                        });
                aboutDialog.show();
                return true;


            // Add new Item Button (Shows up as a + sign)
            case R.id.action_additem:
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }


    // Populates the list
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDBhelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DBhelper.KEY_ID,
                DBhelper.KEY_NAME,
                DBhelper.KEY_QUANTITY_HAVE,
                DBhelper.KEY_QUANTITY_WANT,
                DBhelper.KEY_LOCATION,
                DBhelper.KEY_ISSUED_BOOLEAN};

        // Perform a query on the pets table
        cursor = db.query(
                DBhelper.TABLE_INVENTORY,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        cursor.moveToFirst();

        ListView lvItems = (ListView) findViewById(R.id.main_list_view);
        ItemCursorAdapter itemAdapter = new ItemCursorAdapter(this, cursor);
        lvItems.setAdapter(itemAdapter);


//Assigns Empty View for when no items in Database
        View emptyView = findViewById(R.id.empty_view);
        lvItems.setEmptyView(emptyView);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long passID) {


                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra("itemPosition", passID);
                startActivity(intent);
            }
        });

    }


    //Closes the cursor on app termination
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        Log.d("tag", "Cursor(AddItemActivity) Closed");
    }
}

