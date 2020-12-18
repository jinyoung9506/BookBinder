package com.kamizos.bookbinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase bdb = null;
    private SQLiteDatabase idb = null;

    private DBHelper(Context context) {
        super(context, DBContract.DBName , null, DBContract.DB_VERSION);
        if (bdb == null) {
            bdb = getWritableDatabase();
        }
        if (idb == null) {
            idb = getWritableDatabase();
        }
    }

    private static volatile DBHelper instance;

    public static DBHelper getInstance(Context context) {
        if (instance == null){
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }

    public ArrayList<Book> getbAll() {
        ArrayList<Book> list = new ArrayList<>();

        if (bdb == null) {
            return list;
        }
        Cursor cursor = bdb.rawQuery(DBContract.SELECT, null);

        while (cursor.moveToNext()) {
            list.add(
                    new Book(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getBlob(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11)
                    )
            );

        }
        return list;
    }

    public ArrayList<ISBN> getiAll() {
        ArrayList<ISBN> list = new ArrayList<>();

        if (idb == null) {
            return list;
        }
        Cursor cursor = idb.rawQuery(DBContract.iSELECT, null);

        while (cursor.moveToNext()) {
            list.add(new ISBN(cursor.getString(0)));
        }

        return list;
    }

    public void insert(Book book) {
        ContentValues values = new ContentValues();
        values.put(DBContract.COL_ISBN, book.getISBN());
        values.put(DBContract.COL_TITLE, book.getTitle());
        values.put(DBContract.COL_BOOKLINK, book.getLink());
        values.put(DBContract.COL_IMAGELINK, book.getImageURL());
        values.put(DBContract.COL_AUTHOR, book.getAuthor());
        values.put(DBContract.COL_PRICE, book.getPrice());
        values.put(DBContract.COL_PUBLISHER, book.getPublisher());
        values.put(DBContract.COL_DATE, book.getPubdate());
        values.put(DBContract.COL_IMAGE, book.getImage());
        values.put(DBContract.COL_MEMO, book.getMemo());
        values.put(DBContract.COL_BEFORE, book.getBefore());
        values.put(DBContract.COL_AFTER, book.getAfter());

        bdb.insert(DBContract.TABLENAME, null, values);
    }

    public void delete(String isbn) {
        String selection = DBContract.COL_ISBN + " = ?";
        String[] selectionArgs = new String[] { isbn };
        bdb.delete(DBContract.TABLENAME, selection, selectionArgs);
    }


    public void iinsert(ISBN isbn) {
        ContentValues values = new ContentValues();
        values.put(DBContract.iCOL_ISBN, isbn.getISBN());
        idb.insert(DBContract.iTABLENAME, null, values);
    }

    public void idelete(String isbn) {
        String selection = DBContract.iCOL_ISBN+ " = ?";
        String[] selectionArgs = new String[] { isbn };
        idb.delete(DBContract.iTABLENAME, selection, selectionArgs);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBContract.CREATE_TABLE);
        db.execSQL(DBContract.iCREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.DROP_TABLE);
        db.execSQL(DBContract.iDROP_TABLE);
        onCreate(db);
    }
}
