package com.example.snarkportingtest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String pk_table = "CREATE TABLE if not exists pk ("
                + "_id integer primary key autoincrement,"
                + "vote_id integer,"
                + "pub_key text,"
                + "voted integer);";
        String votelist_table = "CREATE TABLE if not exists votelist ("
                + "_id integer primary key autoincrement,"
                + "vote_id integer,"
                + "title text);";
        String salt_table = "CREATE TABLE if not exists salt ("
                + "_id integer primary key autoincrement,"
                + "salt text);";
        String notice_table = "CREATE TABLE if not exists notice ("
                + "_id integer primary key autoincrement,"
                + "notice text);";

        db.execSQL(pk_table);
        db.execSQL(votelist_table);
        db.execSQL(salt_table);
        db.execSQL(notice_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String pk_table = "DROP TABLE if exists pk";
        String votelist_table = "DROP TABLE if exists votelist";
        String salt_table = "DROP TABLE if exists salt";
        String notice_table = "DROP TABLE if exists notice";

        db.execSQL(pk_table);
        db.execSQL(votelist_table);
        db.execSQL(salt_table);
        db.execSQL(notice_table);
        onCreate(db);
    }
}
