package com.linux_girl.myinventory;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Lani on 8/12/2016.
 */
public class UpdateActivity extends AppCompatActivity {

    public static final String LOG_TAG = UpdateActivity.class.getName();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    BitmapUtility bitmap = new BitmapUtility();
    private static int SELECTED_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_product_view);

        Intent intent = getIntent();
        String productID = intent.getStringExtra("ProductID");
        final int rowId = Integer.parseInt(productID);

        Cursor cursor = dbHelper.getProduct(rowId);

        LinearLayout layout = (LinearLayout) findViewById(R.id.update_view);
        bindView(layout, this, cursor);

        Button submitButton = (Button) findViewById(R.id.button_submit);
        Button mImageUp = (Button) findViewById(R.id.add_image);

        // OnClickListener for image upload button with ID add_image
        mImageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });

        // OnClickListener for Submit Button
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        TextView idView = (TextView) findViewById(R.id.product_id);
                        idView.getText().toString();

                        ContentValues values = new ContentValues();
                        EditText mProductName = (EditText) findViewById(R.id.add_product_name);
                        values.put(DatabaseContract.Inventory.PRODUCT_NAME, mProductName.getText().toString());

                        EditText mSupplier = (EditText) findViewById(R.id.add_supplier);
                        values.put(DatabaseContract.Inventory.PRODUCT_SUPPLIER, mSupplier.getText().toString());

                        EditText mQuantity = (EditText) findViewById(R.id.add_quantity);
                        values.put(DatabaseContract.Inventory.PRODUCT_QUANTITY, mQuantity.getText().toString());

                        EditText mPrice = (EditText) findViewById(R.id.add_price);
                        values.put(DatabaseContract.Inventory.PRODUCT_PRICE, mPrice.getText().toString());

                        Button imgButton = (Button) findViewById(R.id.add_image);
                        Log.i(LOG_TAG, "Image: " + imgButton.getText().toString());

                        int isUpdated = dbHelper.updateProduct(rowId, values);

                        if (isUpdated > 0) {
                            Toast.makeText(getBaseContext(),
                                    "Product Updated.", Toast.LENGTH_LONG).show();

                            Intent mainIntent = new Intent(UpdateActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                        } else {
                            Toast.makeText(getBaseContext(),
                                    "Error updating product to Database", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView myText = (TextView) findViewById(R.id.product_id);
        myText.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_ID)));

        // Get Textview with id product_name and display Product name from current cursor
        EditText nameView = (EditText) findViewById(R.id.add_product_name);
        nameView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_NAME)));

        // Find Textview with id product_supplier and set it's text to Supplier
        EditText supplierView = (EditText) findViewById(R.id.add_supplier);
        supplierView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_SUPPLIER)));

        // Find Textview with id product_quantity and set it's text to the quantity
        EditText quantityView = (EditText) findViewById(R.id.add_quantity);
        quantityView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_QUANTITY)));

        // Find Textview with id product_price and set it's text to the price
        EditText priceView = (EditText) findViewById(R.id.add_price);
        priceView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_PRICE)));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1234:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();

                    String[] filePathColumn = {DatabaseContract.Inventory.PRODUCT_IMAGE};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);

                    Bitmap image = BitmapFactory.decodeFile(filePath);
                    Log.i(LOG_TAG, "Image: " + image);
                    //byte[] myImage = BitmapUtility.getImageBytes(image);

                }
        }
    }

    ;

}
