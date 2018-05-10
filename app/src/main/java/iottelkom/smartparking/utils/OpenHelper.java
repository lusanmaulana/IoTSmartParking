package iottelkom.smartparking.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by A450CC on 11/7/2017.
 */

public class OpenHelper extends SQLiteOpenHelper {
    //kalau ada upgrade, increment versi database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dbUser.db";

    private static final String TABLE_USER =
            "CREATE TABLE TABLE_USER (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT)";
    private static final String TABLE_PLACE =
            "CREATE TABLE TABLE_PLACE (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAMA TEXT, WILAYAH TEXT, LAT DOUBLE, LON DOUBLE, PIC TEXT)";

    public OpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create database
        db.execSQL(TABLE_USER);
        db.execSQL(TABLE_PLACE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        //jika app diupgrade (diinstall yang baru) maka database akan dicreate ulang (data hilang)
        //jika tidak ingin hilang, bisa diproses disini
        db.execSQL("DROP TABLE IF EXISTS TB_AKUN");
    }
}
