package myfirebase.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import myfirebase.com.model.Requests;

public class MitraActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    String id, username,email,role,nama,img_url,alamat,no_telepon;

    private DatabaseReference database;

    private ArrayList<Requests> daftarReq;

    private ProgressDialog loading;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        id = getIntent().getStringExtra(Server.TAG_ID);
        username = getIntent().getStringExtra(Server.TAG_USERNAME);
        email = getIntent().getStringExtra(Server.TAG_EMAIL);
        role = getIntent().getStringExtra(Server.TAG_ROLE);
        nama = getIntent().getStringExtra(Server.TAG_NAMA);
        img_url = getIntent().getStringExtra(Server.TAG_IMG_URL);
        alamat = getIntent().getStringExtra(Server.TAG_ALAMAT);
        no_telepon = getIntent().getStringExtra(Server.TAG_NO_TELEPON);
    }

    public void logout(View view) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(LoginActivity.session_status, false);
        editor.putString(Server.TAG_ID, null);
        editor.putString(Server.TAG_USERNAME, null);
        editor.putString(Server.TAG_EMAIL, null);
        editor.putString(Server.TAG_ROLE, null);
        editor.putString(Server.TAG_IMG_URL, null);
        editor.putString(Server.TAG_NAMA, null);
        editor.putString(Server.TAG_ALAMAT, null);
        editor.putString(Server.TAG_NO_TELEPON, null);
        editor.commit();

        Intent intent = new Intent(MitraActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(getApplicationContext(),
                "Tolong klik BACK kembali untuk keluar aplikasi",
                Toast.LENGTH_SHORT);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void pindah(View view) {
        Intent intent = new Intent(MitraActivity.this, ListBarangMitraActivity.class);
        startActivity(intent);
    }
}