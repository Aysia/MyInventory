package com.linux_girl.myinventory;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Lani on 8/8/2016.
 */
class CustomAdapter extends CursorAdapter {
    // CursorAdapter will handle all the moveToFirst(), getCount() logic for you :)

    public CustomAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_inventory_view, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView product = (TextView) view.findViewById(R.id.product_name);
        product.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_NAME)));

        TextView quantity = (TextView) view.findViewById(R.id.product_quantity);
        quantity.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_QUANTITY)));

        TextView price = (TextView) view.findViewById(R.id.product_price);
        String thePrice = cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_PRICE));
        String dollarFormat = context.getString(R.string.dollar_sign);
        dollarFormat += thePrice;
        price.setText(dollarFormat);
    }

    public static final int getCursorPosition(Cursor cursor) {
        return cursor.getPosition();
    }
}