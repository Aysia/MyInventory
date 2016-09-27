package com.linux_girl.myinventory;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Lani on 8/12/2016.
 * UpdateActivity class displays a form with the values of a specific product
 * based on the ID passed by ProductActivity. Values may be altered or kept the same
 * and updated to the database.
 */
public class UpdateActivity extends AppCompatActivity {

    // tag for the Log
    public static final String LOG_TAG = UpdateActivity.class.getName();
    // Database Helper
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    // BitmapUtility
    BitmapUtility bitmap = new BitmapUtility();
    // Global bitmapImg so it can be accessed and passed onto the dbase
    Bitmap bitmapImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_product_view);

        // Get the intent started in ProductActivity and get the Extra Message
        // called ProductID
        Intent intent = getIntent();
        String rowID = intent.getStringExtra("ProductID");
        // Parse string to int
        final int productId = Integer.parseInt(rowID);

        // get the product data from the database based on the productID
        Cursor cursor = dbHelper.getProduct(productId);

        // We have the data - now grab the view (Layout) and call the method
        // bindView to display our data within the cursor
        LinearLayout layout = (LinearLayout) findViewById(R.id.update_view);
        detailView(layout, this, cursor);

        Button submitButton = (Button) findViewById(R.id.button_submit);

        //Commented out to sync with Project Rubic
        //Button bImageUp = (Button) findViewById(R.id.add_image);

//        // OnClickListener for image upload button with ID add_image
//        bImageUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Intent i = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                final int ACTIVITY_SELECT_IMAGE = 1234;
//                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
//            }
//        });

        // OnClickListener for Submit Button
        // Here we get the values from the form and update the database
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        TextView idView = (TextView) findViewById(R.id.product_id);
                        idView.getText();



                        EditText shipmentReceived = (EditText) findViewById(R.id.shipment);
                        TextView currentQty = (TextView) findViewById(R.id.quantity);

                        if(Utils.isEmpty(shipmentReceived)) {
                            Toast.makeText(getBaseContext(),R.string.validate_quantity_text, Toast.LENGTH_LONG).show();
                        }

                        String shipmentQty = shipmentReceived.getText().toString();
                        String current = currentQty.getText().toString();
                        int received = Integer.parseInt(shipmentQty);
                        int newQuantity = received + Integer.parseInt(current);
                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.Inventory.PRODUCT_QUANTITY, newQuantity);

                        int isUpdated = dbHelper.updateProduct(productId, values);

                        if (isUpdated > 0) {
                            Toast.makeText(getBaseContext(),
                                    "Shipment Updated.", Toast.LENGTH_LONG).show();

                            Intent mainIntent = new Intent(UpdateActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                        } else {
                            Toast.makeText(getBaseContext(),
                                    "Error updating quantity to Database", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    /**
     * Method to receive product shipment
     */
    public void detailView(View view, Context context, Cursor cursor) {
        TextView myText = (TextView) findViewById(R.id.product_id);
        myText.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_ID)));

        // Get TextView with id product_name and display Product name from current cursor
        TextView nameView = (TextView) findViewById(R.id.product_name);
        nameView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_NAME)));

        // Find TextView with id product_supplier and set it's text to Supplier
        TextView supplierView = (TextView) findViewById(R.id.supplier);
        supplierView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_SUPPLIER)));

        // Quanity View
        TextView qtyView = (TextView) findViewById(R.id.quantity);
        qtyView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_QUANTITY)));

        // Shipment View
        EditText shipmentView = (EditText) findViewById(R.id.shipment);
        shipmentView.setText("");

        // Find TextView with id product_price and set it's text to the price
        TextView priceView = (TextView) findViewById(R.id.price);
        priceView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_PRICE)));

        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        if(cursor.getBlob(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_IMAGE)) != null) {
            imageView.setImageBitmap(BitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_IMAGE))));
        } else {
            imageView.setVisibility(imageView.GONE);
        }
    }


    /**
     * Method to update the current image in the database of the product
     */
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

    public boolean hasImage(Bitmap bitmap) {
        if(bitmap != null) {
            return true;
        } else {
            return false;
        }
    }
}
