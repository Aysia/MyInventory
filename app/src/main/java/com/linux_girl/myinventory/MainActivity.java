package com.linux_girl.myinventory;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getName();
    DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Uncomment the lines below to initialize database with values
//        dbHelper.insertProduct("Galaxy Note 7", "Samsung", null, 29.34, 10);
//        dbHelper.insertProduct("Galaxy S7", "Samsung", null, 23.17, 10);
//        dbHelper.insertProduct("Galaxy S7 Edge","Samsung", null, 26.50, 10);

        final Cursor cursor = dbHelper.readInventory(null, null);
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

}
