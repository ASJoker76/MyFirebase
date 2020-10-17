package myfirebase.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import myfirebase.com.adapter.RequestAdapterRecyclerView;
import myfirebase.com.model.Requests;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListBarangMitraActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    String id, username,email,role,nama,img_url,alamat,no_telepon;

    private DatabaseReference database;

    private ArrayList<Requests> daftarReq;
    private RequestAdapterRecyclerView requestAdapterRecyclerView;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_barang_mitra);

        database = FirebaseDatabase.getInstance().getReference();

        rc_list_request = findViewById(R.id.rc_list_request);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rc_list_request.setLayoutManager(mLayoutManager);
        rc_list_request.setItemAnimator(new DefaultItemAnimator());

        loading = ProgressDialog.show(ListBarangMitraActivity.this,
                null,
                "Please wait...",
                true,
                false);

        database.child("Barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                daftarReq = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    /**
                     * Mapping data pada DataSnapshot ke dalam object Wisata
                     * Dan juga menyimpan primary key pada object Wisata
                     * untuk keperluan Edit dan Delete data
                     */
                    Requests requests = noteDataSnapshot.getValue(Requests.class);
                    requests.setKey(noteDataSnapshot.getKey());

                    /**
                     * Menambahkan object Wisata yang sudah dimapping
                     * ke dalam ArrayList
                     */
                    daftarReq.add(requests);
                }

                /**
                 * Inisialisasi adapter dan data hotel dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                requestAdapterRecyclerView = new RequestAdapterRecyclerView(daftarReq, ListBarangMitraActivity.this);
                rc_list_request.setAdapter(requestAdapterRecyclerView);
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /**
                 * Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint error nya
                 * ke LogCat
                 */
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                loading.dismiss();
            }
        });

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

    public void klik(View view) {
        startActivity(new Intent(ListBarangMitraActivity.this, ManajemenBarangActivity.class)
                .putExtra("id", "")
                .putExtra("nama", "")
                .putExtra("harga", "")
                .putExtra("stok", ""));
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

        Intent intent = new Intent(ListBarangMitraActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }
}
