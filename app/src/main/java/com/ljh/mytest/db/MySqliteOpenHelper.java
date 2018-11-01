package com.ljh.mytest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqliteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "MySqliteOpenHelper";
    private static final String DATABASE_NAME = "book.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "create table bookinfo ( id integer primary key autoincrement, book_name text, author text, price text)";

    public MySqliteOpenHelper(Context context){
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private MySqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d(TAG,"New MySqliteOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate 创建表");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
