package com.fpoly.pro1121.userapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper instance;
    public static synchronized DbHelper getInstance(Context context) {
        if(instance==null){
            instance = new DbHelper(context);
        }
        return instance;
    }

    private DbHelper(@Nullable Context context) {
        super(context, "food", null, 1);
    }

    public static final String TABLE_NAME ="ProductOrder";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ID_USER ="idUser";
    public static final String COLUMN_ID_PRODUCT ="idProduct";
    public static final String COLUMN_PRICE_PRODUCT ="priceProduct";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT_PRICE ="unitPrice";

    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ( "
            +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "
            +COLUMN_ID_USER+" TEXT ,"
            +COLUMN_ID_PRODUCT+" TEXT , "
            +COLUMN_PRICE_PRODUCT+" INTEGER , "
            +COLUMN_QUANTITY+" INTEGER , "
            +COLUMN_UNIT_PRICE+" INTEGER )";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
