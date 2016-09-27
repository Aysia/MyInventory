package com.linux_girl.myinventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.inspector.database.SqliteDatabaseDriver;
import com.facebook.stetho.inspector.protocol.module.Database;


public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getName();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        // Empty Tables in Database
        //dbHelper.deleteTablesData();
        //dbHelper.deleteDatabase();

        final Cursor cursor = dbHelper.readInventory(null, null);
        if (cursor.getCount() == 0) {
            preFillProducts();
            preFillSuppliers();
        }

        // Find ListView to populate
        ListView listProductView = (ListView) findViewById(R.id.list_view);

        //Create empty View for reporting no results
        listProductView.setEmptyView(findViewById(R.id.empty_list_item));

        // Setup cursor adapter using cursor from last step
        CustomAdapter adapter = new CustomAdapter(this, cursor, 0);
        // Attach cursor adapter to the ListView
        listProductView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent
        // to open the ProductActivity
        listProductView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String id = cursor.getString(cursor.getColumnIndex(DatabaseContract.Inventory.PRODUCT_ID));

                // Create a new intent and pass the current product id to the product activity
                Intent productIntent = new Intent(MainActivity.this, ProductActivity.class);
                String message = id;
                productIntent.putExtra(EXTRA_MESSAGE, message);
                startActivity(productIntent);
            }
        });
        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Create a new intent to view the News Article URI
                Intent productIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(productIntent);
            }
        });

        Button supplierButton = (Button) findViewById(R.id.add_supplier);
        supplierButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Create a new intent to view the News Article URI
                Intent supplierIntent = new Intent(MainActivity.this, AddSuplierActivity.class);
                startActivity(supplierIntent);
            }
        });

    }

    /**
     * Created method to prefill products for testing and evaluation
     */
    private void preFillSuppliers() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues sValues = new ContentValues();
        sValues.put(DatabaseContract.Inventory.SUPPLIER_NAME, "Samsung");
        sValues.put(DatabaseContract.Inventory.SUPPLIER_CONTACT, "1-800-726-7864");

        db.insert(
                DatabaseContract.Inventory.SUPPLIER_TABLE_NAME, null, sValues);
    }

    /**
     * Created method to prefill suppliers for testing and evaluation
     */
    private void preFillProducts() {
        ContentValues pValues = new ContentValues();
        pValues.put(DatabaseContract.Inventory.PRODUCT_NAME, "Galaxy Note 7");
        pValues.put(DatabaseContract.Inventory.SUPPLIER_NAME, "Samsung");
        pValues.put(DatabaseContract.Inventory.PRODUCT_PRICE, 29.34);
        pValues.put(DatabaseContract.Inventory.PRODUCT_QUANTITY, 10);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.insert(
                DatabaseContract.Inventory.PRODUCTS_TABLE_NAME, null, pValues);
    }

    public void myClickHandler(View v) {
        //reset all the listView items background colours
        //before we set the clicked one..

        ListView listView = (ListView) findViewById(R.id.list_view);

        //get the row the clicked button is in
        LinearLayout listParent = (LinearLayout) v.getParent();

        TextView product_id = (TextView) listParent.getChildAt(0);
        TextView product = (TextView) listParent.getChildAt(1);
        TextView quantity = (TextView) listParent.getChildAt(2);
        TextView price = (TextView) listParent.getChildAt(3);
        Button sellBtn = (Button) listParent.getChildAt(4);

        //Get the current Product Id
        int productID = Integer.parseInt(product_id.getText().toString());

        // Update the Quantity to reflect the Sale
        String currentQty = quantity.getText().toString();
        int qty = getQuantity(currentQty);

        if (qty > 0) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Inventory.PRODUCT_QUANTITY, qty);
            dbHelper.updateProduct(productID, values);
        } else {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Inventory.PRODUCT_QUANTITY, 0);
            dbHelper.updateProduct(productID, values);
        }

        //TODO: Push Sale Method Here
        String stringPrice = price.getText().toString();
        String toReplace = "$";
        String doublePrice = stringPrice.replace(toReplace, "");
        double totalPrice = Double.parseDouble(doublePrice) * 1;

        ContentValues sales = new ContentValues();
        sales.put(DatabaseContract.Inventory.PRODUCT_ID_NO, productID);
        sales.put(DatabaseContract.Inventory.DATE_RECEIVED, dbHelper.getCurrentTimestamp());
        sales.put(DatabaseContract.Inventory.QUANTITY_SOLD, 1);
        sales.put(DatabaseContract.Inventory.TOTAL_PRICE, totalPrice);

        dbHelper.insertSales(sales);

        sellBtn.setText(product_id.getText());
        sellBtn.setText(product.getText());
        sellBtn.setText(String.valueOf(qty));
        sellBtn.setText(price.getText());
        sellBtn.setText(R.string.dollar_sign);

        listView.invalidateViews();
        listParent.refreshDrawableState();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    /**
     * Called when the user clicks the product
     */
    public static final String EXTRA_MESSAGE = "my key";

    public void viewPoductPage() {
        // Do something in response to click
        Intent intent = new Intent(this, ProductActivity.class);
        TextView textView = (TextView) findViewById(R.id.product_name);
        String message = textView.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private int getQuantity(String quantity) {
        int qty = Integer.parseInt(quantity);
        int updatedQty = qty - 1;
        return updatedQty;
    }

    public void onResume() {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        Cursor cursor = dbHelper.readInventory(null, null);
    }
}

