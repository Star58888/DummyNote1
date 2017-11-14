package com.star.dummynote1;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Star on 2017/11/7.
 */

public class DB {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "mytable";
    private static final String DATABASE_CREATE =
//            "CREATE TABLE notes(_id INTEGER PRIMARY KEY,noteTEXT,created INTEGER);";
//            "CREATE TABLE IF NOT EXISTS mytable(_id INTEGER PRIMARY KEY, note TEXT NOT NULL, created INTEGER);";
            "CREATE TABLE IF NOT EXISTS mytable" + "(_id INTEGER PRIMARY KEY, note TEXT NOT NULL, created INTEGER);";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        Context mCtx;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mCtx = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            Toast.makeText(mCtx , "更新完畢" , Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS  mytable" );
            onCreate(db);
        }
    }
    private Context mCtx = null;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DB(Context ctx) {
        this.mCtx = ctx;
    }

    public DB open() throws SQLException {
        dbHelper = new DatabaseHelper(mCtx);
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public  static  final String KEY_ROWID = "_id";
    public  static  final String KEY_NOTE = "note";
    public  static  final String KEY_CREATED = "created";

    String[] strCols = new String[]
            {
                    KEY_ROWID,
                    KEY_NOTE,
                    KEY_CREATED
            };

    public Cursor get (long roeId) throws SQLException {
        Cursor cursor = db.query(true , DATABASE_TABLE ,strCols , KEY_ROWID + "=" + roeId ,
                null , null , null , null , null   );
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getAll() {
        //SQLite 寫法
//        return db.rawQuery("SELECT * FROM mytable", null);

        return  db.query(DATABASE_TABLE ,
                strCols,
                null ,
                null ,
                null ,
                null ,
                null );
    }

    public long create(String noteName)
    {
        Date now = new Date();  //抓時間 ; 使用Java.util的資料庫
        ContentValues args = new ContentValues();
        args.put(KEY_NOTE , noteName);
        args.put(KEY_CREATED , now.getTime());
        return  db.insert(DATABASE_TABLE , null , args);   //insert傳回id值
    }

    public boolean delete(long rowId)
    {
        if(rowId > 0) {
            return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
        }
        else {
            return db.delete(DATABASE_TABLE, null, null) > 0;
        }
    }

    public boolean delete() {
        return delete(-1);
    }

    public boolean update(long rowId , String note)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NOTE , note);
        return  db.update(DATABASE_TABLE , args , KEY_ROWID + "=" + rowId , null) > 0;
    }

}
