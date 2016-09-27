package com.linux_girl.myinventory;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lani on 8/8/2016.
 * Displays the Product in Detail and includes various buttons to receive, sell etc...
 */
public class ProductActivity extends AppCompatActivity {

    public static final String LOG_TAG = ProductActivity.class.getName();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    int rowId;

    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;

    // When the system is ready for the

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        Intent intent = getIntent();
        String mId = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        final int rowId = Integer.parseInt(mId);

        Cursor cursor = dbHelper.getProduct(rowId);

        LinearLayout layout = (LinearLayout) findViewById(R.id.display);
        bindView(layout, this, cursor);

        Button deleteButton = (Button) findViewById(R.id.delete);
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Prompt user for deletion
                final int id = rowId;
                AlertDialog dialog = AskOption(id);
                dialog.show();
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

        /**
         * Sell product button
         */
        Button sellButton = (Button) findViewById(R.id.sell_product);
        sellButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                TextView quantityView = (TextView) findViewById(R.id.product_quantity);
                TextView priceView = (TextView) findViewById(R.id.product_price);
                String qtyText = quantityView.getText().toString();
                int qty = Integer.parseInt(qtyText);

                if(qty > 0) {
                    qty = qty -1;
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.Inventory.PRODUCT_QUANTITY, qty);

                    dbHelper.updateProduct(rowId, values);
                }

                String total = priceView.getText().toString();
                double totalPrice = Double.parseDouble(total) * 1;

                ContentValues sales = new ContentValues();
                sales.put(DatabaseContract.Inventory.PRODUCT_ID_NO, rowId);
                sales.put(DatabaseContract.Inventory.DATE_RECEIVED, dbHelper.getCurrentTimestamp());
                sales.put(DatabaseContract.Inventory.QUANTITY_SOLD, 1);
                sales.put(DatabaseContract.Inventory.TOTAL_PRICE, totalPrice);

                dbHelper.insertSales(sales);

                String EXTRA_MESSAGE = "my key";
                Intent i = new Intent(ProductActivity.this, ProductActivity.class);
                i.putExtra(EXTRA_MESSAGE, String.valueOf(rowId));
                startActivity(i);
            }
        });

        /**
         * Contact / Order Button
         */
        Button orderBtn = (Button) findViewById(R.id.order);
        orderBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Get Phone Number from Database
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                int NOT_EXIST = -1;
                String TABLENAME = DatabaseContract.Inventory.SUPPLIER_TABLE_NAME;
                String SUPPLIERNAME = DatabaseContract.Inventory.SUPPLIER_NAME;
                TextView SUPPLIERVIEW = (TextView) findViewById(R.id.product_supplier);
                String SUPPLIER = SUPPLIERVIEW.getText().toString();

                String query = "SELECT * FROM " + TABLENAME + " WHERE " + SUPPLIERNAME + " LIKE '" + SUPPLIER +"'";

                Cursor supplierInfo = db.rawQuery(query, null);

                if(supplierInfo.getCount() > 0 ) {
                    supplierInfo.moveToFirst();
                    String str = supplierInfo.getString(supplierInfo.getColumnIndex(DatabaseContract.Inventory.SUPPLIER_CONTACT));
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse("tel:" + str));
                    startActivity(i);
                } else {
                    Toast.makeText(ProductActivity.this, "No Contact Found", Toast.LENGTH_LONG).show();
                }
                supplierInfo.close();
//
            }
        });
    }
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        //Cursor cursor = dbHelper.getProduct(rowId);
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
    public AlertDialog AskOption(final int rowId) {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.mipmap.ic_launcher)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        final int id = rowId;
                        dbHelper.deleteProducts(id);
                        Intent addIntent = new Intent(ProductActivity.this, MainActivity.class);
                        startActivity(addIntent);
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }
}