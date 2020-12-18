package com.kamizos.bookbinder;

public class DBContract {
    private DBContract() {}

    public static final int DB_VERSION = 1;
    public static final String DBName = "BookDB";
    public static final String TABLENAME = "Book";
    public static final String COL_ISBN = "ISBN";
    public static final String COL_TITLE = "Book_title";
    public static final String COL_BOOKLINK = "Book_link";
    public static final String COL_IMAGELINK = "Book_imglink";
    public static final String COL_AUTHOR = "Book_author";
    public static final String COL_PRICE = "Book_price";
    public static final String COL_PUBLISHER = "Publisher";
    public static final String COL_DATE = "Publish_Date";
    public static final String COL_IMAGE = "Book_img";
    public static final String COL_MEMO = "Book_memo";
    public static final String COL_BEFORE = "Book_before";
    public static final String COL_AFTER = "Book_after";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLENAME + " " + "(" +
            COL_ISBN + " VARCHAR(13) PRIMARY KEY, "+
            COL_TITLE + " VARCHAR, " +
            COL_BOOKLINK + " VARCHAR, " +
            COL_IMAGELINK + " VARCHAR, " +
            COL_AUTHOR + " VARCHAR, " +
            COL_PRICE + " INTEGER, " +
            COL_PUBLISHER + " VARCHAR, " +
            COL_DATE + " VARCHAR, " +
            COL_IMAGE + " BLOB, " +
            COL_MEMO + " VARCHAR," +
            COL_BEFORE + " VARCHAR," +
            COL_AFTER + " VARCHAR " + ")";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLENAME;
    public static final String SELECT = "SELECT * FROM " + TABLENAME + " ORDER BY " + COL_TITLE + " ASC";
    public static final String INSERT = "INSERT OR REPLACE INTO " + TABLENAME + " " + "(" + COL_ISBN + ") VALUES";
    public static final String DELETE = "DELETE FROM " + TABLENAME + "WHERE " + COL_ISBN + " = ";

    public static final String iTABLENAME = "Isbn";
    public static final String iCOL_ISBN = "ISBN";
    public static final String iCREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + iTABLENAME + " " + "(" + iCOL_ISBN + " VARCHAR(13) PRIMARY KEY" + ")";
    public static final String iDROP_TABLE = "DROP TABLE IF EXISTS " + iTABLENAME;
    public static final String iSELECT = "SELECT * FROM " + iTABLENAME;
    public static final String iINSERT = "INSERT OR REPLACE INTO " + iTABLENAME + " " + "(" + iCOL_ISBN + ") VALUES";
    public static final String iDELETE = "DELETE FROM " + iTABLENAME + "WHERE " + iCOL_ISBN + " = ";
}
