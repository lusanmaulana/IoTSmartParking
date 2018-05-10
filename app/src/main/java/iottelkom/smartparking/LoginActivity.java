package iottelkom.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import iottelkom.smartparking.utils.SMPreferences;
import iottelkom.smartparking.utils.DbParkir;

/**
 * Created by kawakibireku on 11/7/17.
 */

public class LoginActivity extends AppCompatActivity{

    private EditText etxtUname;
    private EditText etxtPass;
    private SMPreferences smPreferences;
    private CheckBox cbKeepSignIn;
    int i;
    int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        String TAG = this.getClass().getSimpleName();

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCreate = findViewById(R.id.btnCreate);
        etxtUname = findViewById(R.id.etxtUsername);
        etxtPass = findViewById(R.id.etxtPassword);

        cbKeepSignIn = findViewById(R.id.cbKeepSignIn);

        smPreferences = new SMPreferences(this);

        String lastLoginStatus = smPreferences.read(SMPreferences.KEY_REMEMBER_ME);
        if(lastLoginStatus!=null && lastLoginStatus.equals(SMPreferences.REMEMBER_ME) ){
            String username = smPreferences.read(SMPreferences.KEY_USERNAME);
            String password = smPreferences.read(SMPreferences.KEY_PASSWORD);
            etxtUname.setText(username);
            etxtPass.setText(password);
            cbKeepSignIn.isChecked();

            Log.d(TAG,"logged in");
        } else {
            Log.d(TAG,"gak login");
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etxtUname.getText().toString();
                String password = etxtPass.getText().toString();

                DbParkir db = new DbParkir(getApplicationContext());
                db.open();
                i=0;
                ArrayList<DbParkir.Akun> akun = db.getAllUser();
                for(DbParkir.Akun index : akun) {
                    if (username.equals(index.username) && password.equals(index.password)) {
                        if (cbKeepSignIn.isChecked()) {
                            smPreferences.store(SMPreferences.KEY_LOGIN, SMPreferences.LOGGED_IN);
                            smPreferences.store(SMPreferences.KEY_REMEMBER_ME, SMPreferences.REMEMBER_ME);
                            smPreferences.store(SMPreferences.KEY_USERNAME, username);
                        }else{
                            smPreferences.clearAll();
                        }
                        status = 1;
                    }

                }
                if(status == 1){
                    smPreferences.store(SMPreferences.KEY_LOGIN, SMPreferences.LOGGED_IN);
                    smPreferences.store(SMPreferences.KEY_USERNAME, username);
                    smPreferences.store(SMPreferences.KEY_PASSWORD, password);
                    Toasty.success(getApplicationContext(), String.format("Welcome %s", username), Toast.LENGTH_SHORT, true).show();
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                    finish();
                    status = 0;
                } else {
                    Toasty.error(getApplicationContext(), "Failed to authenticate", Toast.LENGTH_SHORT, true).show();
                }
                db.close();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
                startActivity(intent);
            }
        });

        DbParkir db = new DbParkir(getApplicationContext());
        db.open();
        ArrayList<DbParkir.Place> plc = db.getAllPlace();
        if(plc.size() == 0){
            db.insertPlace("FPMIPA-C","UPI",-6.860485, 107.589896, "temp");
            db.insertPlace("FPOK","UPI",-6.861039, 107.589971, "temp");
            db.insertPlace("FPMIPA-A","UPI",-6.862009, 107.589885, "temp");
            db.insertPlace("FIP","UPI",-6.861326, 107.590828, "temp");
            db.insertPlace("University Center","UPI",-6.860741, 107.592310, "temp");
            db.insertPlace("FPEB","UPI",-6.860645, 107.592932, "temp");
            db.insertPlace("Pascasarjana","UPI",-6.862614, 107.592889, "temp");
            db.insertPlace("FPBS","UPI",-6.860957, 107.593726, "temp");
            db.insertPlace("Museum Pendidikan Nasional","UPI",-6.860036, 107.593817, "temp");
            db.insertPlace("Gedung PKM","UPI",-6.862584, 107.593645, "temp");
            db.insertPlace("Masjid Al-Furqon","UPI",-6.863350, 107.594058, "temp");
            db.insertPlace("FPTK","UPI",-6.864122, 107.593999, "temp");
            db.insertPlace("Gedung OASIS","Telkom Gegerkalong",-6.873642, 107.586658, "temp");
            db.insertPlace("Bandung Digital Valley","Telkom Gegerkalong",-6.873328, 107.586967, "temp");
            db.insertPlace("Gedung DDS","Telkom Gegerkalong",-6.873195, 107.587410, "temp");
        }
        db.close();
    }
}
