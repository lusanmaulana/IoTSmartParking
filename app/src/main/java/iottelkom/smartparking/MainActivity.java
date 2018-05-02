package iottelkom.smartparking;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import iottelkom.smartparking.utils.SMPreferences;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private SMPreferences smPreferences;
    GridView simpleGrid;
    MyAdapter myAdapter;
    ArrayList<Device> devlist = new ArrayList<>();
    ArrayList<String> dev = new ArrayList<>();
    private Handler handler = new Handler();
    int i = 0;
    int j = 0;
    int l = 0;

    //handler every 5 seconds and 2 seconds delay for getData from every devices
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                // PerformBackgroundTask this class is the class that extends AsynchTask
                for (String x : dev) {
                    new getData().execute(x);
                }
            } catch (Exception e) {

            }
            handler.postDelayed(this, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent detail = getIntent();
        TextView tvGedung = findViewById(R.id.tvGedung);
        tvGedung.setText(detail.getStringExtra(MenuActivity.EXTRA_MESG_GEDUNG));

        simpleGrid = findViewById(R.id.gridview);
        myAdapter = new MyAdapter(this,R.layout.devicelist,devlist);
        new getlistDevice().execute();
        simpleGrid.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        smPreferences = new SMPreferences(this);
        runnable.run();
    }

    @Override
    public void onDestroy(){
        handler.removeCallbacks(runnable);
        super.onDestroy();
        smPreferences.store(SMPreferences.KEY_LOGIN,SMPreferences.NOT_LOGIN);
    }

    private class getlistDevice extends AsyncTask<String, Void, String> {
        String out;
        Response response;

        @Override
        protected String doInBackground(String... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://platform.antares.id:8080/~/in-cse/in-name/SmartParking?fu=1&ty=3")
                        .get()
                        .addHeader("x-m2m-origin", "e8a9616f96a21e93:aeda7156c47ce66f")
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "a720dcde-9a06-5517-27e2-af038810e7ae")
                        .build();
                response = client.newCall(request).execute();
                out = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("No Connection");
                out = e.getMessage();
            }
            return out;
        }

        @Override
        protected void onPostExecute(String out) {

            try {
                String[] outSplit = out.split("\\t|,|;|\\.|\\?|!|-|:|@|\\[|\\]|\\(|\\)|\\{|\\}|_|\\*|/|\\s+|[\"^]");
                String prev = "NULL";
                for (String item : outSplit) {
                    if(prev.equals("SmartParking")){
                        dev.add(item);
                        devlist.add(new Device(item,R.drawable.redcar));
                        i++;
                    }
                    prev = item;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    //get value data from platform antares.id
    private class getData extends AsyncTask<String, Void, String> {
        String out;
        Response response;
        OkHttpClient client = new OkHttpClient();
        String id;
        @Override
        protected String doInBackground(String... params) {
            String identifier = params[0];
            id = identifier;
            System.out.println("doInBackground: (GetData)"+identifier);

            try {
                j = 0;
                Request request = new Request.Builder()
                        .url("http://platform.antares.id:8080/~/in-cse/in-name/SmartParking/"+identifier+"/la")
                        .get()
                        .addHeader("x-m2m-origin", "e8a9616f96a21e93:aeda7156c47ce66f")
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "54fc13d2-2400-879a-ccbf-1b568f9bdb7b")
                        .build();
                response = client.newCall(request).execute();
                out = response.body().string();
            } catch (Exception e) {
                j = 1;
                e.printStackTrace();
                out = e.getMessage();
                System.out.println("No Connection");
            }
            return out;
        }

        @Override
        protected void onPostExecute(String out) {
            String identifier = null;
            try {
                identifier = id;
                JSONObject output = (new JSONObject(out)).getJSONObject("m2m:cin");
                String outData = output.getString("con");
                int data = Integer.parseInt(outData.replaceAll("[\\D]", ""));
                if(j == 0){
                    cek(identifier,data);
                }else{
                    System.out.println("err");
                }

                simpleGrid.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Data did not exist");
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    //use in getData,to check every value in each devices, and update change value to gridview
    public void cek(String identifier, Integer data){
        System.out.println("DEVICE ID: " +identifier);

        for(String m : dev){ //someshit
            System.out.println("DEVICE M : " +m);
            if(l > dev.size()-1){
                l = 0;
            }
            if(m.equals(identifier)){
                System.out.println("INDEX L: "+l);

                if(data == 1){
                    devlist.set(l,new Device(identifier,R.drawable.redcar));
                }else if(data == 0){
                    devlist.set(l,new Device(identifier,R.drawable.greencar));
                }
                l++;
            }else{
                System.out.println("DATA NOT SAME");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.lout:
                smPreferences.store(SMPreferences.KEY_LOGIN, SMPreferences.NOT_LOGIN);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                handler.removeCallbacks(runnable);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
