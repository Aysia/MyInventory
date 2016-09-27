package com.linux_girl.myinventory;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Lani on 8/9/2016.
 * The AddActivity class was created to implement a UI in which the user
 * can enter Products into the Database
 */
public class AddActivity extends AppCompatActivity {
    /**
     * Tag for the Logs
     */
    public static final String LOG_TAG = AddActivity.class.getName();
    /**
     * Call the database helper
     */
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    /**
     * Globalize the bitmapImg
     */
    Bitmap bitmapImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_view);

        // Assign Buttons
        final Button submitButton = (Button) findViewById(R.id.button_submit);
        final Button imageUploadBtn = (Button) findViewById(R.id.add_image);

        /**
         * OnClickLister for Submit button
         */
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        /**
                         * Create ContentValues based on user input to be updated to the database
                         */
                        ContentValues values = new ContentValues();

                        EditText etProductName = (EditText) findViewById(R.id.add_product_name);
                        String productName = etProductName.getText().toString();
                        EditText etSupplier = (EditText) findViewById(R.id.add_supplier);
                        String supplier = etSupplier.getText().toString();
                        EditText etQuantity = (EditText) findViewById(R.id.add_quantity);
                        String quantity = etQuantity.getText().toString();
                        EditText etPrice = (EditText) findViewById(R.id.add_price);
                        String price = etPrice.getText().toString();
                        ImageView imageView = (ImageView) findViewById(R.id.image_view);
                        imageView.setTag(bitmapImg);
                        Bitmap b = (Bitmap) imageView.getTag();

                        /**
                         * Validate that all fields are not empty
                         */

                        if (TextUtils.isEmpty(productName)
                                || TextUtils.isEmpty(supplier)
                                || TextUtils.isEmpty(quantity)
                                || TextUtils.isEmpty(price)
                                || b == null
                                ) {
                            Toast.makeText(AddActivity.this, "All Fields Required.",
                                    Toast.LENGTH_LONG).show();
                        } else {

                            values.put(DatabaseContract.Inventory.PRODUCT_NAME, productName);
                            values.put(DatabaseContract.Inventory.PRODUCT_SUPPLIER, supplier);
                            values.put(DatabaseContract.Inventory.PRODUCT_QUANTITY, quantity);
                            values.put(DatabaseContract.Inventory.PRODUCT_PRICE, price);
                            values.put(DatabaseContract.Inventory.PRODUCT_IMAGE, BitmapUtility.getImageBytes(bitmapImg));

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
                    }
                });

        // OnClickListener for image upload button with ID add_image
        imageUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1234:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                    try {
                        bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        ImageView imageView = (ImageView) findViewById(R.id.image_view);
                        imageView.setImageBitmap(bitmapImg);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
