package com.linux_girl.myinventory;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Lani on 8/9/2016.
 * Created AddSupplier to input Supplier name and Contact Information
 * The Supplier Name is made PRIMARY - so the phone number can be obtained
 * with just the name
 */
public class AddSuplierActivity extends AppCompatActivity {
    public static final String LOG_TAG = AddSuplierActivity.class.getName();
    DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_supplier);

        // Assign BUttons
        final Button submitButton = (Button) findViewById(R.id.button_submit);

        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        ContentValues values = new ContentValues();
                        EditText supplierName = (EditText) findViewById(R.id.supplier_name);
                        values.put(DatabaseContract.Inventory.SUPPLIER_NAME, supplierName.getText().toString());

                        EditText supplierContact = (EditText) findViewById(R.id.supplier_contact);
                        values.put(DatabaseContract.Inventory.SUPPLIER_CONTACT, supplierContact.getText().toString());

                        long rowId = dbHelper.insertSupplier(values);

                        if (rowId > 0) {
                            Toast.makeText(getBaseContext(),
                                    "New Supplier Added to Database.", Toast.LENGTH_LONG).show();

                            Intent mainIntent = new Intent(AddSuplierActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                        } else {
                            Toast.makeText(getBaseContext(),
                                    "Error adding supplier to Database", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        }
    }
