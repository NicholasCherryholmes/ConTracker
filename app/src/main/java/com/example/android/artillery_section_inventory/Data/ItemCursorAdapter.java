package com.example.android.artillery_section_inventory.Data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.artillery_section_inventory.R;


public class ItemCursorAdapter extends CursorAdapter {

    public static String SELECTEDITEM_SOLD_BUTTON;


    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false);
    }

    @Override

    public void bindView(final View view, final Context context, final Cursor mCursor) {


        TextView itemName = (TextView) view.findViewById(R.id.list_item_name);
        TextView itemQty = (TextView) view.findViewById(R.id.list_ItemQty);
        TextView locText = (TextView) view.findViewById(R.id.list_ItemLocation);
        TextView priceText = (TextView) view.findViewById(R.id.list_ItemPrice);
        //get values from cursor

        int nameColumnIndex = mCursor.getColumnIndex(DBhelper.KEY_NAME);
        int qtyColumnIndex = mCursor.getColumnIndex(DBhelper.KEY_QUANTITY_HAVE);
        int locColumnIndex = mCursor.getColumnIndex(DBhelper.KEY_LOCATION);
        int qtyNeedColumnIndex = mCursor.getColumnIndex(DBhelper.KEY_QUANTITY_WANT);
        int idColumnIndex = mCursor.getColumnIndex(DBhelper.KEY_ID);
        //Populate fields with values
        String itemNameString = mCursor.getString(nameColumnIndex);
        String QUANTITY_HAVESTRING = mCursor.getString(qtyColumnIndex);
        String locationString = mCursor.getString(locColumnIndex);
        String qtyNeededString = mCursor.getString(qtyNeedColumnIndex);
        SELECTEDITEM_SOLD_BUTTON = mCursor.getString(idColumnIndex);

        //Adds tags to the Sale Buttons as they are created, Quantity and ID



        itemName.setText(itemNameString);
        itemQty.setText(QUANTITY_HAVESTRING);
        locText.setText(locationString);
        priceText.setText(qtyNeededString);






        //todo add on destroy db close


    }


}

