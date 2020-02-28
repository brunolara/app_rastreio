package com.lara.bru.rastreio.DbContext;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_POSTS =
            "CREATE TABLE " + PostContract.PostEntry.TABLE_NAME + " (" +
                    PostContract.PostEntry._ID + " INTEGER PRIMARY KEY," +
                    PostContract.PostEntry.COLUMN_NAME_TOKEN + TEXT_TYPE + COMMA_SEP +
                    PostContract.PostEntry.COLUMN_NAME_LAN + TEXT_TYPE + COMMA_SEP +
                    PostContract.PostEntry.COLUMN_NAME_LNG + TEXT_TYPE + COMMA_SEP +
                    PostContract.PostEntry.COLUMN_NAME_DATA + TEXT_TYPE + COMMA_SEP +
                    PostContract.PostEntry.COLUMN_NAME_STATUS + INT_TYPE
                    + " )";

    private static final String SQL_DELETE_POSTS =
            "DROP TABLE IF EXISTS " + PostContract.PostEntry.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SyncGeo.db";


    public PostDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_POSTS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_POSTS);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
