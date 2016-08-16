package com.linux_girl.myinventory;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lani on 8/9/2016.
 */
public class AddActivity extends AppCompatActivity {
    public static final String LOG_TAG = AddActivity.class.getName();
    DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_view);

        // Review input and verify
        final Button mButton = (Button) findViewById(R.id.button_submit);
        final EditText mEditProduct = (EditText) findViewById(R.id.add_product_name);
        final EditText mEditSupplier = (EditText) findViewById(R.id.add_supplier);
        final EditText mEditPrice = (EditText) findViewById(R.id.add_price);
        final EditText mEditQty = (EditText) findViewById(R.id.add_quantity);
        final Button mImageUp = (Button) findViewById(R.id.add_image);

        mButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        ContentValues values = new ContentValues();
                        EditText mProductName = (EditText) findViewById(R.id.add_product_name);
                        values.put(DatabaseContract.Inventory.PRODUCT_NAME, mProductName.getText().toString());

                        EditText mSupplier = (EditText) findViewById(R.id.add_supplier);
                        values.put(DatabaseContract.Inventory.PRODUCT_SUPPLIER, mSupplier.getText().toString());

                        EditText mQuantity = (EditText) findViewById(R.id.add_quantity);
                        values.put(DatabaseContract.Inventory.PRODUCT_QUANTITY, mQuantity.getText().toString());

                        EditText mPrice = (EditText) findViewById(R.id.add_price);
                        values.put(DatabaseContract.Inventory.PRODUCT_PRICE, mPrice.getText().toString());

                        long rowId = dbHelper.insertProduct(values);

                        if (rowId > 0) {
                            Toast.makeText(getBaseContext(),
                                    "New Product Added to Database.", Toast.LENGTH_LONG).show();

                            Intent mainIntent = new Intent(AddActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                        } else {
                            Toast.makeText(getBaseContext(),
                                    "Error adding product to Database", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        mImageUp.setOnClickListener(new View.OnClickListener() {
            int SELECT_PICTURE = 0;

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

                Log.i(LOG_TAG, "Select: " + SELECT_PICTURE);
            }
        });

    }
}
