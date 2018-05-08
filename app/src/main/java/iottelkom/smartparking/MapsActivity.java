package iottelkom.smartparking;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    String gdChosen = "";
    private static final int MY_PERMISSIONS_REQUEST = 99;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private Marker mNow;
    tempatParkir tParkir;

    protected synchronized void buildGoogleApiClient() {
        Log.e("cek","buildgac");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        Log.e("cek","create locreq");
        mLocationRequest = LocationRequest.create();
        //10 detik sekali minta lokasi (5000ms = 5 detik)
        mLocationRequest.setInterval(5000);
        //tapi tidak boleh lebih cepat dari 3 detik
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    public void ambilLokasi() {
        Log.e("cek","ambil lokasi");
        /* mulai Android 6 (API 23), pemberian persmission
           dilakukan secara dinamik (tdk diawal)
           untuk jenis2 persmisson tertentu, termasuk lokasi
        */
        // cek apakah sudah diijinkan oleh user, jika belum tampilkan dialog
        // pastikan permission yg diminta cocok dgn manifest
        if (ActivityCompat.checkSelfPermission (this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
        {
            //belum ada ijin, tampilkan dialog
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
            return;
        }
        //ambil lokasi terakhir
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e("cek","onreqperres");
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ambilLokasi();
            } else {
                //permssion tidak diberikan, tampilkan pesan
                AlertDialog ad = new AlertDialog.Builder(this).create();
                ad.setMessage("Tidak mendapat ijin, tidak dapat mengambil lokasi");
                ad.show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buildGoogleApiClient();
        createLocationRequest();

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        tParkir = new tempatParkir();
        tParkir.addLatLong(-6.861967, 107.589866);
        tParkir.addNama("Gedung FPMIPA-A");
        tParkir.addLatLong(-6.860418, 107.589889);
        tParkir.addNama("Gedung FPMIPA-C");
        tParkir.addLatLong(-6.861049, 107.590006);
        tParkir.addNama("Gedung FPOK");
        tParkir.addLatLong(-6.860932, 107.593793);
        tParkir.addNama("Gedung FPBS");
        tParkir.addLatLong(-6.862487, 107.593060);
        tParkir.addNama("Gedung Pascasarjana");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in GIK and move the camera
        LatLng gik = new LatLng(-6.860418, 107.589889);
        mNow = mMap.addMarker(new MarkerOptions().position(gik).title("Posisi sekarang."));
        ArrayList<LatLng> LatLong = tParkir.getLatLong();
        int i = 0;
        LatLng oLatLong;
        for(String index : tParkir.getNama()){
            Log.e("cek",index);
            Log.e("cek",""+LatLong.size());
            oLatLong = LatLong.get(i);
            mMap.addMarker(new MarkerOptions().position(oLatLong).title(index).icon(BitmapDescriptorFactory.fromResource(R.drawable.pincar)));
            i++;
        }

        // batas UPI, kiri-bawah dan kanan-atas (urutan harus spt itu)
        LatLngBounds UPI = new LatLngBounds(
                new LatLng(-6.863273, 107.587212),new LatLng(-6.858025, 107.597839));

        // Set kamera sesuai batas UPI
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset dari edges

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                gdChosen = marker.getTitle();
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent main = new Intent(MapsActivity.this, MainActivity.class);
                main.putExtra(MenuActivity.EXTRA_MESG_GEDUNG, "" + gdChosen);
                Log.e("cek",main.getStringExtra(MenuActivity.EXTRA_MESG_GEDUNG));
                startActivity(main);
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(UPI, width, height, padding));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("cek","con");
        ambilLokasi();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("cek","locchange");
        LatLng pNow = new LatLng(location.getLatitude(),location.getLongitude());
        mNow.setPosition(pNow);
    }
}
