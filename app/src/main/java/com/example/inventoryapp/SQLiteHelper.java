package com.example.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {


    public static String DATABASE_NAME = "product_database";
    private static final int DATABASE_VERSION = 5;

    private static final String tbl_product = "CREATE TABLE "
            + "tbl_product" + "(" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "product_name" + " VARCHAR(255), "
            + "product_unit" + " VARCHAR(255), "
            + "product_price" + " DOUBLE, "
            + "product_dateofexpiry" + " VARCHAR(255), "
            + "product_image" + " INTEGER, "
            + "product_availableinventory" + " INTEGER );";

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tbl_product(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + " " +
                "product_name" + " vARCHAR(255), " +
                "product_unit" + " VARCHAR(255), "+
                "product_price" + " DOUBLE, "+
                "product_dateofexpiry" + " VARCHAR(255), "+
                "product_image" + " VARCHAR(255), "+
                "product_availableinventory" + " INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists tbl_product");
        onCreate(db);
    }

    public boolean CreateProduct(String productname, String productunit, double productprice, String productdateofexpiry, int productavailableinventory, String productimage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_name", productname);
        values.put("product_unit", productunit);
        values.put("product_price", productprice);
        values.put("product_dateofexpiry", productdateofexpiry);
        values.put("product_availableinventory", productavailableinventory);
        values.put("product_image", productimage);
        long insert = db.insert("tbl_product", null, values);
        if(insert == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor getAllProduct() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT *, product_price * product_availableinventory  AS product_inventorycost FROM tbl_product",null);
        return cursor;
    }

    public boolean updateProduct(String id,String productname, String productunit, double productprice, String productdateofexpiry, int productavailableinventory, String productimage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_name", productname);
        values.put("product_unit", productunit);
        values.put("product_price", productprice);
        values.put("product_dateofexpiry", productdateofexpiry);
        values.put("product_availableinventory", productavailableinventory);
        values.put("product_image", productimage);
        db.update("tbl_product", values, "id = ?",new String[] { id });
        return true;
    }

    public Integer deleteProduct (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("tbl_product", "id = ?",new String[] {id});
    }
}
