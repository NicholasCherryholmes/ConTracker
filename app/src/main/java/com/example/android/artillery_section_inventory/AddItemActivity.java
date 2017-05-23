package com.example.android.artillery_section_inventory;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.artillery_section_inventory.Data.DBhelper;
import com.example.android.artillery_section_inventory.Data.ItemCursorAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class AddItemActivity extends AppCompatActivity {

    private DBhelper mDBhelper;
    public Cursor cursor;
    public Bundle bundle;
    public long mPosition;
    public ImageView mImage;
    private int PICK_IMAGE_REQUEST = 1;
    public Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        mDBhelper = new DBhelper(this);


        bundle = getIntent().getExtras();
        if (bundle != null) {

            mPosition = bundle.getLong("itemPosition");
            Log.d("tag", "Bundle mPosition: " + mPosition);
            setTitle("Edit Item");

            SQLiteDatabase db = mDBhelper.getReadableDatabase();
            String[] projection = {
                    DBhelper.KEY_ID,
                    DBhelper.KEY_NAME,
                    DBhelper.KEY_QUANTITY_HAVE,
                    DBhelper.KEY_QUANTITY_WANT,
                    DBhelper.KEY_LOCATION,
                    DBhelper.KEY_ISSUED_BOOLEAN,
                    DBhelper.KEY_IMAGE};
            String selection = DBhelper.KEY_ID + " = " + mPosition;


            // Perform a query on the pets table

            cursor = db.query(
                    DBhelper.TABLE_INVENTORY,   // The table to query
                    projection,            // The columns to return
                    selection,                  // The columns for the WHERE clause
                    null,                  // The values for the WHERE clause
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null);                   // The sort order


//Identifies all the objects needed to reference later
            EditText mNameEditText = (EditText) findViewById(R.id.addinputname);
            EditText mQty = (EditText) findViewById(R.id.addinputQTYonHand);
            EditText mPrice = (EditText) findViewById(R.id.addinputPrice);
            RadioButton mArmyIssue = (RadioButton) findViewById(R.id.addinputARMYISSUE);
            RadioButton mPersonalIssue = (RadioButton) findViewById(R.id.addinputPERSONAL);
            Spinner mSpinner = (Spinner) findViewById(R.id.current_location_spinner);
            mImage = (ImageView) findViewById(R.id.image_display);

            // Gets the column indexes
            int nameColumnIndex = cursor.getColumnIndex(DBhelper.KEY_NAME);
            int qtyColumnIndex = cursor.getColumnIndex(DBhelper.KEY_QUANTITY_HAVE);
            int priceColumnIndex = cursor.getColumnIndex(DBhelper.KEY_QUANTITY_WANT);
            int armyIssueColumnIndex = cursor.getColumnIndex(DBhelper.KEY_ISSUED_BOOLEAN);
            int locationColumnIndex = cursor.getColumnIndex(DBhelper.KEY_LOCATION);
            int imageColumnIndex = cursor.getColumnIndex(DBhelper.KEY_IMAGE);
            cursor.moveToFirst();
            //Takes data from the database and stores in variables
            String itemNameString = cursor.getString(nameColumnIndex);
            String qtyOnHand = cursor.getString(qtyColumnIndex);
            String priceString = cursor.getString(priceColumnIndex);
            String location = cursor.getString(locationColumnIndex);
            int armyIssueBoo = cursor.getInt(armyIssueColumnIndex);


            byte[] imageByteArray = cursor.getBlob(imageColumnIndex);


            //This checks if the retrieved value already has an image, preventing a crash
            if (imageByteArray != null) {
                mBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                mImage.setImageBitmap(mBitmap);
            }


            mNameEditText.setText(itemNameString);
            mQty.setText(qtyOnHand);
            mPrice.setText(priceString);
            if (armyIssueBoo == 1) {
                mArmyIssue.setChecked(true);
            } else if (armyIssueBoo == 0) {
                mPersonalIssue.setChecked(true);
            }

            //Bulky spinner code, I believe I found a more concise way to do this in the future, but for now I used many if/else statements

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.LocationItemArray, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(adapter);
            if (location.equals("Unknown / Lost / Missing")) {
                mSpinner.setSelection(8, true);

            } else if (location.equals("Section Connex")) {
                mSpinner.setSelection(1, true);
            } else if (location.equals("Gunner Box")) {
                mSpinner.setSelection(2, false);
            } else if (location.equals("On Mission")) {
                mSpinner.setSelection(3, false);
            } else if (location.equals("In Custody of Soldier")) {
                mSpinner.setSelection(4, false);
            } else if (location.equals("Supply")) {
                mSpinner.setSelection(5, false);
            } else if (location.equals("Maintenance")) {
                mSpinner.setSelection(6, false);
            } else if (location.equals("Borrowed")) {
                mSpinner.setSelection(7, false);
            }
        } else {
            Log.d("tag", "Bundle mPosition null: ");
            setTitle("Add New Item");

            Spinner spinner = (Spinner) findViewById(R.id.current_location_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.LocationItemArray, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

        }

    }


    public void QuantityPlus(View view) {

        EditText edittext = (EditText) findViewById(R.id.addinputQTYonHand);
        String textString = edittext.getText().toString();
        if (textString.equalsIgnoreCase("")) {
            edittext.setText("0");

        }
        String textString2 = edittext.getText().toString();

        int quantity = Integer.parseInt(textString2);

        quantity = quantity + 1;

        textString = Integer.toString(quantity);

        edittext.setText(textString);
    }

    public void QuantityMinus(View view) {

        EditText edittext = (EditText) findViewById(R.id.addinputQTYonHand);
        String textString = edittext.getText().toString();
        if (textString.equalsIgnoreCase("")) {
            edittext.setText("0");

        }
        String textString2 = edittext.getText().toString();
        int quantity = Integer.parseInt(textString2);
        if (quantity > 0) {
            quantity = quantity - 1;

        }

        textString = Integer.toString(quantity);

        edittext.setText(textString);
    }

    public void addImage(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.AddPicture)), 1);
    }

    // Help from Codetheory.in/android-pick-select-image-from-gallery-with-intents/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.image_display);
                imageView.setImageBitmap(mBitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Takes the UserInput and enters it as a row in the Table
    public void SaveEntry(View view) {
        EditText mNameEditText = (EditText) findViewById(R.id.addinputname);
        EditText mQuantityonHand = (EditText) findViewById(R.id.addinputQTYonHand);
        EditText mPrice = (EditText) findViewById(R.id.addinputPrice);
        RadioButton mIsIssue = (RadioButton) findViewById(R.id.addinputARMYISSUE);
        Spinner mLocation = (Spinner) findViewById(R.id.current_location_spinner);


        //Stores input in Strings
        String nameString = mNameEditText.getText().toString().trim();
        String qtyOnHandString = mQuantityonHand.getText().toString().trim();
        String priceString = mPrice.getText().toString().trim();
        String locationString = mLocation.getSelectedItem().toString().trim();

        if (nameString.equalsIgnoreCase("") || priceString.equalsIgnoreCase("") || locationString.equalsIgnoreCase("(Choose Item Location)") || mBitmap == null) {
            Toast.makeText(this, getString(R.string.partialinput_error), Toast.LENGTH_SHORT).show();
            return;
        } else {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            mBitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);

            byte mImageByte[] = stream.toByteArray();

            //Stores Strings as a ContentValues object
            ContentValues values = new ContentValues();
            values.put(DBhelper.KEY_NAME, nameString);
            values.put(DBhelper.KEY_QUANTITY_HAVE, qtyOnHandString);
            values.put(DBhelper.KEY_QUANTITY_WANT, priceString);
            values.put(DBhelper.KEY_LOCATION, locationString);
            values.put(DBhelper.KEY_ISSUED_BOOLEAN, mIsIssue.isChecked());
            values.put(DBhelper.KEY_IMAGE, mImageByte);

            SQLiteDatabase db = mDBhelper.getWritableDatabase();

            //Differentiates between Editing an item, and adding a new item
            if (getTitle() == "Edit Item") {
//Had to grab the bundle again, wasn't sure if I could make the bundle a global variable since it came in as an Intent Extra

                db.update(DBhelper.TABLE_INVENTORY, values, "_id = " + mPosition, null);
                db.close();
                Toast.makeText(this, mPosition + " updated", Toast.LENGTH_SHORT).show();
            } else {


                db.insert(DBhelper.TABLE_INVENTORY, null, values);
                db.close();
                Toast.makeText(this, nameString + " added", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void ConfirmDeleteEntry(View view) {
//                Dialog Confirmation for Item Deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_confirm);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                DeleteEntry();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    public void DeleteEntry() {
        SQLiteDatabase db = mDBhelper.getWritableDatabase();
        db.delete(DBhelper.TABLE_INVENTORY, "_id = " + mPosition, null);
        Toast.makeText(this, mPosition + " deleted", Toast.LENGTH_SHORT).show();
        db.close();
        Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
        startActivity(intent);

    }

    public void OrderItem(View view) {

        EditText mNameEditText = (EditText) findViewById(R.id.addinputname);

        String nameString = mNameEditText.getText().toString().trim();


        String[] TO = {"unitsupply@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Order Request for: " + nameString);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Need to place an order for more " + nameString + ". We need (amount desired)");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AddItemActivity.this,
                    getString(R.string.noemail), Toast.LENGTH_SHORT).show();
        }
    }

    public void itemSoldButton(View view) {

        Toast.makeText(this, ItemCursorAdapter.SELECTEDITEM_SOLD_BUTTON, Toast.LENGTH_SHORT).show();

    }


    //Closes the cursor on app termination
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        Log.d("tag", "Cursor(AddItemActivity) Closed");
    }
}







