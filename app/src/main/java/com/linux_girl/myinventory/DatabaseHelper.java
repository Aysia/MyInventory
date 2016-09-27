package com.linux_girl.myinventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lani on 8/8/2016.
 * Manipulate the Database here! I love databases!
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null,
                DatabaseContract.DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Inventory.CREATE_PRODUCTS_TABLE);
        db.execSQL(DatabaseContract.Inventory.CREATE_SALES_TABLE);
        db.execSQL(DatabaseContract.Inventory.CREATE_SUPPLIER_TABLE);
    }

    // Method is called during an upgrade of the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.Inventory.DELETE_PRODUCT_TABLE);
        db.execSQL(DatabaseContract.Inventory.DELETE_SALES_TABLE);
        db.execSQL(DatabaseContract.Inventory.DELETE_SUPPLIER_TABLE );
        onCreate(db);
    }

    public void deleteDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DatabaseContract.Inventory.DELETE_PRODUCT_TABLE);
        db.execSQL(DatabaseContract.Inventory.DELETE_SALES_TABLE);
        db.execSQL(DatabaseContract.Inventory.DELETE_SUPPLIER_TABLE);
    }

    public long insertProduct(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DatabaseContract.Inventory.PRODUCTS_TABLE_NAME, null, values);

        return newRowId;
    }

    public long insertSupplier(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DatabaseContract.Inventory.SUPPLIER_TABLE_NAME, null, values);
        return newRowId;
    }

    /**
     * Get the Product ID
     */
    public void preFillDatabase(String productName, String supplier,
                                byte[] image, int quantity, double price) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Inventory.PRODUCT_NAME, productName);
        values.put(DatabaseContract.Inventory.PRODUCT_SUPPLIER, supplier);
        values.put(DatabaseContract.Inventory.PRODUCT_IMAGE, image);
        values.put(DatabaseContract.Inventory.PRODUCT_PRICE, price);
        values.put(DatabaseContract.Inventory.PRODUCT_QUANTITY, quantity);

        db.insert(
                DatabaseContract.Inventory.PRODUCTS_TABLE_NAME, null, values);

    }

    /** Always prefill Suppliers */
    public void addSupplier(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();

        db.insert(
                DatabaseContract.Inventory.SUPPLIER_TABLE_NAME, null, values);

    }


    public void insertSales(ContentValues values) {

        SQLiteDatabase db = getWritableDatabase();


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DatabaseContract.Inventory.SALES_TABLE_NAME, null, values);
    }

    public Cursor getProduct(int rowId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                DatabaseContract.Inventory.PRODUCT_ID,
                DatabaseContract.Inventory.PRODUCT_NAME,
                DatabaseContract.Inventory.PRODUCT_SUPPLIER,
                DatabaseContract.Inventory.PRODUCT_PRICE,
                DatabaseContract.Inventory.PRODUCT_IMAGE,
                DatabaseContract.Inventory.PRODUCT_QUANTITY
        };
        Cursor cursor = db.query(
                DatabaseContract.Inventory.PRODUCTS_TABLE_NAME,
                projection, DatabaseContract.Inventory.PRODUCT_ID + "=" + rowId, null, null, null, null
        );
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public Cursor readInventory(String selection, String[] selectionArgs) {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseContract.Inventory.PRODUCT_ID,
                DatabaseContract.Inventory.PRODUCT_NAME,
                DatabaseContract.Inventory.PRODUCT_SUPPLIER,
                DatabaseContract.Inventory.PRODUCT_PRICE,
                DatabaseContract.Inventory.PRODUCT_QUANTITY
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DatabaseContract.Inventory.PRODUCT_ID + " DESC";

        Cursor cursor = db.query(
                DatabaseContract.Inventory.PRODUCTS_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder
        );
        return cursor;
    }

    // update habit based on _ID
    public int updateProduct(int ID, ContentValues values) {
        SQLiteDatabase db = getReadableDatabase();

        // Which row to update, based on the ID
        String selection = DatabaseContract.Inventory.PRODUCT_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(ID)};

        int count = db.update(
                DatabaseContract.Inventory.PRODUCTS_TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }

    // Delete row based on _ID
    public void deleteProducts(int product_id) {
        SQLiteDatabase db = getWritableDatabase();
        // Define 'where' part of query.
        String selection = DatabaseContract.Inventory.PRODUCT_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(product_id)};
        // Issue SQL statement.
        db.delete(DatabaseContract.Inventory.PRODUCTS_TABLE_NAME, selection, selectionArgs);
    }

    // Delete all data in the database
    public void deleteTablesData() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseContract.Inventory.PRODUCTS_TABLE_NAME);
        db.execSQL("DELETE FROM " + DatabaseContract.Inventory.SALES_TABLE_NAME);
        //db.execSQL("DELETE FROM " + DatabaseContract.Inventory.SUPPLIER_TABLE_NAME);

    }

    public String getCurrentTimestamp() {
        String currentTimeStamp =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return currentTimeStamp;
    }
}
