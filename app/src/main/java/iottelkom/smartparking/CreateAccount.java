package iottelkom.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import iottelkom.smartparking.utils.DbParkir;

/**
 * Created by kawakibireku on 11/13/17.
 */

public class CreateAccount extends AppCompatActivity {
    int i;
    int status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Button btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DbParkir db = new DbParkir(getApplicationContext());
                db.open();

                EditText etUsername = findViewById(R.id.etNewUsername);
                EditText etPassword = findViewById(R.id.etNewPassword);

                String uname = etUsername.getText().toString();
                String pass = etPassword.getText().toString();

                ArrayList<DbParkir.Akun> akun = db.getAllUser();
                i = akun.size();

                if(uname.isEmpty() || pass.isEmpty()){
                    Toasty.error(getApplicationContext(), "Mohon masukan data.", Toast.LENGTH_SHORT, true).show();
                }else {
                    if (i == 0) {
                        System.out.println("input");
                        db.insertAkun(uname, pass);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toast t = Toast.makeText(getApplicationContext(), "Akun berhasil dibuat.", Toast.LENGTH_LONG);
                        t.show();
                    } else {
                        for (DbParkir.Akun index : akun) {
                            System.out.println("for");
                            if (uname.equals(index.username)) {
                                System.out.println("cek username");
                                status = 1;
                            }
                        }
                    }
                    if(status == 1){
                        Toasty.error(getApplicationContext(), "Nama pengguna telah ada.", Toast.LENGTH_SHORT, true).show();
                        status = 0;
                    }else{
                        db.insertAkun(uname, pass);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toast t = Toast.makeText(getApplicationContext(), "Akun berhasil dibuat.", Toast.LENGTH_LONG);
                        t.show();
                    }
                }
                db.close();
            }
        });

    }




}
