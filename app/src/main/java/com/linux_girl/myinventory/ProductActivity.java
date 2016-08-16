package com.linux_girl.myinventory;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.URL;

/**
 * Created by Lani on 8/8/2016.
 */
public class ProductActivity extends AppCompatActivity {

    public static final String LOG_TAG = ProductActivity.class.getName();
    DatabaseHelper dbHelper = new DatabaseHelper(this);

    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;

    // When the system is ready for the

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);

        Intent intent = getIntent();
        String mId = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        final int rowId = Integer.parseInt(mId);

        Cursor cursor = dbHelper.getProduct(rowId);

        LinearLayout layout = (LinearLayout) findViewById(R.id.display);
        bindView(layout, this, cursor);

        Button deleteButton = (Button) findViewById(R.id.delete);
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Create a new intent to view the News Article URI
                dbHelper.deleteProducts(rowId);
                Intent addIntent = new Intent(ProductActivity.this, MainActivity.class);
                startActivity(addIntent);
            }
        });

        Button updateButton = (Button) findViewById(R.id.update);
        updateButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Create a new intent to view the News Article URI
                Intent myIntent = new Intent();
                myIntent.setClassName("com.linux_girl.myinventory", "com.linux_girl.myinventory.UpdateActivity");
                //Intent updateIntent = new Intent(ProductActivity.this, UpdateActivity.class);

                myIntent.putExtra("ProductID", String.valueOf(rowId));
                startActivity(myIntent);
            }
        });

//        Button sellButton = (Button) findViewById(R.id.sell);
//        sellButton.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                // Create a new intent to view the News Article URI
//                dbHelper.sellProduct(rowId);
//                Intent addIntent = new Intent(ProductActivity.this, ProductActivity.class);
//                startActivity(addIntent);
//                Cursor cursor = dbHelper.getProduct(rowId);
//                LinearLayout layout = (LinearLayout) findViewById(R.id.display);
//                bindView(layout, ProductActivity.this, cursor);
//            }
//        });
    }


    public void bindView(View view, Context context, Cursor cursor) {

        // Get Textview with id product_name and display Product name from current cursor
        TextView nameView = (TextView) findViewById(R.id.product_name);
        nameView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_NAME)));

        // Find Textview with id product_supplier and set it's text to Supplier
        TextView supplierView = (TextView) findViewById(R.id.product_supplier);
        supplierView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_SUPPLIER)));

        // Find Textview with id product_quantity and set it's text to the quantity
        TextView quantityView = (TextView) findViewById(R.id.product_quantity);
        quantityView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_QUANTITY)));

        // Find Textview with id product_price and set it's text to the price
        TextView priceView = (TextView) findViewById(R.id.product_price);
        priceView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_PRICE)));

        // Display Image
        ImageView imageView = (ImageView) findViewById(R.id.product_image);
        //Bitmap productImage = BitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_IMAGE)));
        if (cursor.getBlob(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_IMAGE)) != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(cursor.getBlob(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_IMAGE)),
                    0, cursor.getBlob(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_IMAGE)).length));
        }
    }
}