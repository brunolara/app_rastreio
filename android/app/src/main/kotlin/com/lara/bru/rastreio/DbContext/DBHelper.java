package com.lara.bru.rastreio.DbContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lara.bru.rastreio.Cordinate;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private SQLiteDatabase db = null;
    private PostDbHelper mDbHelper = null;

    public DBHelper(Context context){
        mDbHelper = new PostDbHelper(context);
        db = mDbHelper.getWritableDatabase();
    }

    public boolean saveCordinates(Cordinate cord){
        String currentDate = "" + (System.currentTimeMillis() / 1000L);
        ContentValues values = new ContentValues();

        values.put(PostContract.PostEntry.COLUMN_NAME_TOKEN, cord.token);
        values.put(PostContract.PostEntry.COLUMN_NAME_LAN, cord.lan);
        values.put(PostContract.PostEntry.COLUMN_NAME_LNG, cord.lng);
        values.put(PostContract.PostEntry.COLUMN_NAME_DATA, currentDate);
        values.put(PostContract.PostEntry.COLUMN_NAME_STATUS, 0);

        return db.insert(PostContract.PostEntry.TABLE_NAME, null, values) != 0 ;
    }

    public List<Cordinate> getAll(){
        List<Cordinate> list = new ArrayList<>();
        Cordinate aux;
        String str = "SELECT id,token,lng,lan,data FROM "+PostContract.PostEntry.TABLE_NAME;
        Cursor cur = db.rawQuery(str,
                null);

        if(cur.getCount() != 0){
            cur.moveToFirst();
            do{
                aux = new Cordinate(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4),0);
                list.add(aux);

            }while (cur.moveToNext());
        }
        cur.close();
        return list;
    }


    public List<Cordinate> getUnsync(){
        List<Cordinate> list = new ArrayList<>();
        Cordinate aux;
        String str = "SELECT id,token,lng,lan,data FROM "+PostContract.PostEntry.TABLE_NAME+" WHERE "+ PostContract.PostEntry.COLUMN_NAME_STATUS+ " = 0";
        Cursor cur = db.rawQuery(str,
                null);

        if(cur.getCount() != 0){
            cur.moveToFirst();
            do{
                aux = new Cordinate(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4),0);
                list.add(aux);

            }while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public void changeStatus(int id, int status){
        String sql = "UPDATE  "+PostContract.PostEntry.TABLE_NAME+" SET STATUS = " + status + " WHERE id = " + id;
        Cursor c = db.rawQuery(sql, null) ;
        c.moveToFirst();
        c.close();
    }
}
