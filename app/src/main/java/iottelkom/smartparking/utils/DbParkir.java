package iottelkom.smartparking.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by A450CC on 11/7/2017.
 */

public class DbParkir {
    //class untuk menyimpan record
    public static class Akun{
        public String username;
        public String password;
    }

    public static class Place{
        public String nama;
        public String wilayah;
        public double lat;
        public double lon;
        public String pic;
    }

    private SQLiteDatabase db;
    private final OpenHelper dbHelper;

    public DbParkir(Context c){
        dbHelper = new OpenHelper(c);
    }

    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public long insertAkun(String username, String password){
        ContentValues newVal = new ContentValues();
        newVal.put("USERNAME", username);
        newVal.put("PASSWORD", password);
        System.out.println(username);
        return db.insert("TABLE_USER", null, newVal);
    }

    public long insertPlace(String nama, String wilayah, double lat, double lon, String pic){
        ContentValues newVal = new ContentValues();
        newVal.put("NAMA", nama);
        newVal.put("WILAYAH", wilayah);
        newVal.put("LAT", lat);
        newVal.put("LON", lon);
        newVal.put("PIC", pic);
        return db.insert("TABLE_PLACE", null, newVal);
    }

    //ambil data akun berdasarkan username
    public Akun getAkun(String username){
        Cursor cur = null;
        Akun A = new Akun();

        //kolom yang diambil
        String[] cols = new String[] {"ID", "USERNAME", "PASSWORD"};
        //parameter, akan mengganti ? pada USERNAME=?
        String[] param = {username};

        cur = db.query("TABLE_USER",cols,"USERNAME=?",param,null,null,null);

        if(cur.getCount()>0){   //ada data? ambil
            cur.moveToFirst();
            A.username = cur.getString(1);
            A.password = cur.getString(2);
        }
        cur.close();
        return A;
    }

    public Place getPlace(String nama){
        Cursor cur = null;
        Place P = new Place();

        String[] cols = new String[] {"ID", "NAMA", "WILAYAH", "LAT", "LON", "PIC"};
        String[] param = {nama};

        cur = db.query("TABLE_PLACE", cols, "NAMA=?", param, null, null, null);

        if(cur.getCount() > 0){
            cur.moveToFirst();
            P.nama = cur.getString(1);
            P.wilayah = cur.getString(2);
            P.lat = cur.getDouble(3);
            P.lon = cur.getDouble(4);
            P.pic = cur.getString(5);
        }
        cur.close();
        return P;
    }

    public ArrayList<Place> getPlaceByWilayah(String wilayah){
        Cursor cur = null;
        ArrayList<Place> P = new ArrayList<>();

        String[] cols = new String[] {"ID", "NAMA", "WILAYAH", "LAT", "LON", "PIC"};
        String[] param = {wilayah};

        cur = db.query("TABLE_PLACE", cols, "WILAYAH=?", param, null, null, null);
        if(cur.moveToFirst()){
            do{
                Place plc = new Place();
                plc.nama = cur.getString(1);
                plc.wilayah = cur.getString(2);
                plc.lat = cur.getDouble(3);
                plc.lon = cur.getDouble(4);
                plc.pic = cur.getString(5);
                P.add(plc);
            }while(cur.moveToNext());
        }
        cur.close();
        return P;
    }

    //ambil semua data akun (hati2 kalau datanya banyak)
    //menggunaka raw query
    public ArrayList<Akun> getAllUser(){
        Cursor cur = null;
        ArrayList<Akun> out = new ArrayList<>();
        cur = db.rawQuery("SELECT username, password FROM TABLE_USER Limit 10", null);
        if(cur.moveToFirst()){
            do{
                Akun akn = new Akun();
                akn.username = cur.getString(0);
                akn.password = cur.getString(1);
                out.add(akn);
            }while(cur.moveToNext());
        }
        cur.close();
        System.out.println(out);
        return out;
    }

    public ArrayList<Place> getAllPlace(){
        Cursor cur = null;
        ArrayList<Place> out = new ArrayList<>();
        cur = db.rawQuery("SELECT nama, wilayah, lat, lon, pic FROM TABLE_PLACE", null);
        if(cur.moveToFirst()){
            do{
                Place plc = new Place();
                plc.nama = cur.getString(0);
                plc.wilayah = cur.getString(1);
                plc.lat = cur.getDouble(2);
                plc.lon = cur.getDouble(3);
                plc.pic = cur.getString(4);
                out.add(plc);
            }while(cur.moveToNext());
        }
        cur.close();
        return out;
    }

    public ArrayList<String> getAllWilayah(){
        Cursor cur = null;
        ArrayList<String> out = new ArrayList<>();
        cur = db.rawQuery("SELECT wilayah FROM TABLE_PLACE", null);
        if(cur.moveToFirst()){
            do{
                out.add(cur.getString(0));
            }while(cur.moveToNext());
        }
        cur.close();
        return  out;
    }

    public void delAllUser(){
        db.delete("TABLE_USER",null,null);
    }

    public void delAllPlace(){
        db.delete("TABLE_PLACE",null,null);
    }
}
