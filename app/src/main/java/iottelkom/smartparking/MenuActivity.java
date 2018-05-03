package iottelkom.smartparking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import iottelkom.smartparking.utils.SMPreferences;

public class MenuActivity extends AppCompatActivity implements OnItemSelectedListener, OnItemClickListener {

    public static final String EXTRA_MESG_GEDUNG = "com.iottelkom.smartparking.GEDUNG";
    private static final String[]items = {"UPI", "PT. Telkom Gerlong"};
    private SMPreferences smPreferences;

    ListView lvPlace;
    CustomListAdapter adapterL;
    ArrayList<String> gedung = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        smPreferences = new SMPreferences(this);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String>adapterS = new ArrayAdapter<>(MenuActivity.this,
                android.R.layout.simple_spinner_dropdown_item, items);

        lvPlace = findViewById(R.id.lvPlace);
        lvPlace.setOnItemClickListener(this);
        adapterL = new CustomListAdapter(this, R.layout.listviewlayout, gedung);
        lvPlace.setAdapter(adapterL);

        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterS);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        smPreferences.store(SMPreferences.KEY_LOGIN,SMPreferences.NOT_LOGIN);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {

        switch (index){
            case 0:
                gedung.clear();
                gedung.add("FPMIPA-A");
                gedung.add("FPMIPA-C");
                gedung.add("FPOK");
                gedung.add("FIP");
                gedung.add("FPIPS");
                gedung.add("FPEB");
                gedung.add("FPBS");
                adapterL.notifyDataSetChanged();
                break;
            case 1:
                gedung.clear();
                gedung.add("Oasis");
                gedung.add("BDV");
                gedung.add("DDS");
                adapterL.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class CustomListAdapter extends ArrayAdapter{
        private ArrayList<String> arrRowGedung;
        private Context context;

        public CustomListAdapter(Context context, int resource, ArrayList<String> arrRowGedung){
            super(context,resource);
            this.arrRowGedung = arrRowGedung;
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            LayoutInflater li = LayoutInflater.from(this.context);
            View rowView = li.inflate(R.layout.listviewlayout, null, true);

            TextView tvGedung = rowView.findViewById(R.id.tvGedung);

            tvGedung.setText(arrRowGedung.get(position));

            return rowView;
        }

        @Override
        public int getCount(){
            return arrRowGedung.size();
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = view.findViewById(R.id.tvGedung);
        String idChosen = textView.getText().toString();

        Intent detail = new Intent(this, MainActivity.class);
        detail.putExtra(EXTRA_MESG_GEDUNG, ""+idChosen);
        startActivity(detail);
    }

    public void btnMapsOnClick(View view){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
