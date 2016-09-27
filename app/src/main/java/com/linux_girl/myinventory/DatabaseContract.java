package com.linux_girl.myinventory;

import android.provider.BaseColumns;

/**
 * Created by Lani on 8/8/2016.
 * Created DatabaseContract with Constants for the Database
 */
public class DatabaseContract {
    /**
     * When a change is made to the database, increment the DATABASE_VERSION
     */
    public static final int DATABASE_VERSION = 7;
    // Name of the Database
    public static final String DATABASE_NAME = "database.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";
    private static final String INTEGER = " INT";
    private static final String DOUBLE = " DOUBLE";
    private static final String BLOB = " BLOB";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor. Not sure that I would keep this, as I
    // utilize these constants everywhere and would prefer to instantiate it on each activity
    private DatabaseContract() {
    }

    /**
     * Inner class to create the table @PRODUCTS with constants referencing
     * the columns of @TABLE1
     */
    public static abstract class Inventory implements BaseColumns {
        // Table Name
        public static final String PRODUCTS_TABLE_NAME = "Products";
        public static final String SALES_TABLE_NAME = "Sales";
        public static final String SUPPLIER_TABLE_NAME = "Suppliers";

        // Table constants for Products
        public static final String PRODUCT_ID = _ID;
        public static final String PRODUCT_NAME = "product";
        public static final String PRODUCT_SUPPLIER = "supplier";
        public static final String PRODUCT_IMAGE = "image";
        public static final String PRODUCT_QUANTITY = "quantity";
        public static final String PRODUCT_PRICE = "price";

        // Table constants for Sales
        public static final String SALES_ID = _ID;
        public static final String PRODUCT_ID_NO = "product_id";
        public static final String DATE_RECEIVED = "date_received";
        public static final String QUANTITY_SOLD = "quantity";
        public static final String SHIPMENT_DATE = "shipment_date";
        public static final String TOTAL_PRICE = "total_price";

        // Table constants for Suppliers
        public static final String SUPPLIER_ID = _ID;
        public static final String SUPPLIER_NAME = "supplier";
        public static final String SUPPLIER_CONTACT = "supplier_phone";

        /**
         * create the products table
         */
        public static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                PRODUCTS_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                PRODUCT_NAME + TEXT_TYPE + COMMA_SEP +
                PRODUCT_SUPPLIER + TEXT_TYPE + COMMA_SEP +
                PRODUCT_IMAGE + BLOB + COMMA_SEP +
                PRODUCT_PRICE + DOUBLE + COMMA_SEP +
                PRODUCT_QUANTITY + INTEGER + " )";

        public static final String CREATE_SALES_TABLE = "CREATE TABLE " +
                SALES_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                PRODUCT_ID_NO + INTEGER + COMMA_SEP +
                DATE_RECEIVED + TEXT_TYPE + COMMA_SEP +
                QUANTITY_SOLD + INTEGER + COMMA_SEP +
                SHIPMENT_DATE + INTEGER + COMMA_SEP +
                TOTAL_PRICE + DOUBLE + " )";

        public static final String CREATE_SUPPLIER_TABLE = "CREATE TABLE " +
                SUPPLIER_TABLE_NAME + " (" +
                SUPPLIER_NAME +  TEXT_TYPE + " PRIMARY KEY NOT NULL" + COMMA_SEP +
                SUPPLIER_CONTACT + TEXT_TYPE + " )";

        /**
         * Constant to delete the tables
         */
        public static final String DELETE_PRODUCT_TABLE = "DROP TABLE IF EXISTS " + PRODUCTS_TABLE_NAME;
        public static final String DELETE_SALES_TABLE = "DROP TABLE IF EXISTS " + SALES_TABLE_NAME;
        public static final String DELETE_SUPPLIER_TABLE = "DROP TABLE IF EXISTS " + SUPPLIER_TABLE_NAME;
    }

}

