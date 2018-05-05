package iottelkom.smartparking;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class tempatParkir {

    ArrayList<LatLng> latlong = new ArrayList<>();
    ArrayList<String> nama = new ArrayList<>();

    public tempatParkir(){

    }

    public ArrayList<LatLng> getLatLong() {
        return this.latlong;
    }

    public ArrayList<String> getNama(){
        return this.nama;
    }

    public void setLatLong(ArrayList<LatLng> iLatLong){
        this.latlong = iLatLong;
    }

    public void setNama(ArrayList<String> iNama){
        this.nama = iNama;
    }

    public void addLatLong(double iLat, double iLong){
        LatLng iLatLong = new LatLng(iLat,iLong);
        this.latlong.add(iLatLong);
    }

    public void addNama(String iNama){
        this.nama.add(iNama);
    }
}
