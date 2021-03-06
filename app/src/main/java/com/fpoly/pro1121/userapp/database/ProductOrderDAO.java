package com.fpoly.pro1121.userapp.database;

import static com.fpoly.pro1121.userapp.database.DbHelper.COLUMN_ID;
import static com.fpoly.pro1121.userapp.database.DbHelper.COLUMN_ID_PRODUCT;
import static com.fpoly.pro1121.userapp.database.DbHelper.COLUMN_ID_USER;
import static com.fpoly.pro1121.userapp.database.DbHelper.COLUMN_PRICE_PRODUCT;
import static com.fpoly.pro1121.userapp.database.DbHelper.COLUMN_QUANTITY;
import static com.fpoly.pro1121.userapp.database.DbHelper.COLUMN_UNIT_PRICE;
import static com.fpoly.pro1121.userapp.database.DbHelper.TABLE_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fpoly.pro1121.userapp.model.ProductOrder;

import java.util.ArrayList;
import java.util.List;

public class ProductOrderDAO {

    private static ProductOrderDAO instance;
    SQLiteDatabase db;
    DbHelper dbHelper;

    private ProductOrderDAO(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public static synchronized ProductOrderDAO getInstance(Context context) {
        if (instance == null) {
            instance = new ProductOrderDAO(context);
        }
        return instance;
    }

    public boolean insertProductOrder(ProductOrder productOrder) {
        db = dbHelper.getWritableDatabase();
        try {
            // nếu product đã tồn tại trong giõ hàng -> cập nhật số lượng, tổng tiền
            if (isProductOrderExists(productOrder)) {
                ContentValues values = new ContentValues();
                productOrder.setQuantity(productOrder.getQuantity() + getQuantityProductOrderExists(productOrder));
                int unitPrice = productOrder.getUnitPrice();
                values.put(COLUMN_QUANTITY, productOrder.getQuantity());
                values.put(COLUMN_UNIT_PRICE, unitPrice);
                int result = db.update(TABLE_NAME, values, COLUMN_ID_USER + " = ? AND " + COLUMN_ID_PRODUCT + " = ? ", new String[]{productOrder.getIdUser(), productOrder.getIdProduct()});
                return result > 0;
            } else {
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID_USER, productOrder.getIdUser());
                values.put(COLUMN_ID_PRODUCT, productOrder.getIdProduct());
                values.put(COLUMN_PRICE_PRODUCT, productOrder.getPriceProduct());
                values.put(COLUMN_QUANTITY, productOrder.getQuantity());
                values.put(COLUMN_UNIT_PRICE, productOrder.getUnitPrice());
                long result = db.insert(TABLE_NAME, null, values);
                return result > 0;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

        }

    }

    public boolean isProductOrderExists(ProductOrder productOrder) {
        db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT " + COLUMN_UNIT_PRICE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_USER + " = ? AND " + COLUMN_ID_PRODUCT + " = ? ", new String[]{productOrder.getIdUser(), productOrder.getIdProduct()});
            return cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressLint("Range")
    public int getQuantityProductOrderExists(ProductOrder productOrder) {
        db = dbHelper.getReadableDatabase();
        int quantityExists = 0;
        try {
            Cursor cursor = db.rawQuery("SELECT " + COLUMN_QUANTITY + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_USER + " = ? AND " + COLUMN_ID_PRODUCT + " = ? ", new String[]{productOrder.getIdUser(), productOrder.getIdProduct()});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                quantityExists = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return quantityExists;
    }

    @SuppressLint("Range")
    public int getUnitPriceAllProductOrder(String idUser) {
        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT SUM(unitPrice) as unit FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_USER + " = ? ", new String[]{idUser});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                db.setTransactionSuccessful();
                return cursor.getInt(cursor.getColumnIndex("unit"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return 0;
    }

    @SuppressLint("Range")
    public List<ProductOrder> getAllProductOrder(String idUser) {
        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        List<ProductOrder> list = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_USER + " = ? ", new String[]{idUser});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String userID = cursor.getString(cursor.getColumnIndex(COLUMN_ID_USER));
                    String productID = cursor.getString(cursor.getColumnIndex(COLUMN_ID_PRODUCT));
                    int price = cursor.getInt(cursor.getColumnIndex(COLUMN_PRICE_PRODUCT));
                    int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));
                    int unitPrice = cursor.getInt(cursor.getColumnIndex(COLUMN_UNIT_PRICE));
                    ProductOrder productOrder = new ProductOrder(id, userID, productID, price, quantity, unitPrice);
                    list.add(productOrder);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.endTransaction();
            db.close();
        }
        return list;
    }

    public boolean deleteProductOrder(int idProductOrder) {
        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int result = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(idProductOrder)});
            db.setTransactionSuccessful();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void clickUpdateQuantity(ProductOrder productOrder, boolean isAdd) {
        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            if (isAdd) {
                productOrder.setQuantity(productOrder.getQuantity() + 1);
                values.put(COLUMN_QUANTITY, productOrder.getQuantity());
                values.put(COLUMN_UNIT_PRICE, productOrder.getUnitPrice());
                db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(productOrder.getId())});
                db.setTransactionSuccessful();
            } else {
                productOrder.setQuantity(productOrder.getQuantity() - 1);
                values.put(COLUMN_QUANTITY, productOrder.getQuantity());
                values.put(COLUMN_UNIT_PRICE, productOrder.getUnitPrice());
                db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(productOrder.getId())});
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean deleteAllProductOrder(String idUser) {
        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int result = db.delete(TABLE_NAME, COLUMN_ID_USER + " = ?", new String[]{idUser});
            db.setTransactionSuccessful();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    // get tổng số lượng sản phẩm trong giõ hàng
    @SuppressLint("Range")
    public int getQuantityProductsOrder(String idUser){
        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        int sum = 0;
        try {
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT SUM(quantity) FROM "+TABLE_NAME+" WHERE "+COLUMN_ID_USER+" = ? ",new String[]{idUser});
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                sum = cursor.getInt(0);
            }
            cursor.close();
            db.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
            sum = 0;
        }
        finally{
            db.endTransaction();
            db.close();
        }
        return sum;
    }
}
