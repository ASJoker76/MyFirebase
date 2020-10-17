package myfirebase.com;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import myfirebase.com.adapter.WishListAdapter;
import myfirebase.com.model.Requests;
import myfirebase.com.model.WishList;
import myfirebase.com.model.WishListNew;

public class CheckoutActivity extends AppCompatActivity implements WishListAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private WishListAdapter mAdapter;
    public ArrayList<WishListNew> mUploads;
    private DatabaseReference database;
    public StorageReference storageReference;
    public DatabaseReference databaseReference;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    //private ArrayList<WishListNew> daftarReq;
    //private WishListAdapter requestAdapterRecyclerView;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;
    String id_customer, username,email,role,nama,img_url,alamat,no_telepon;

    private TextView etNama, etEmail, etDesk, txt_total, txt_tgl_pemesanan,txt_email,txt_nama, txt_alamat,txt_no_tlp,txt_id_wish,txt_total_barang,txt_total_harga,txt_total_qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        id_customer = getIntent().getStringExtra("id_customer");
        username = getIntent().getStringExtra("username");
        nama = getIntent().getStringExtra("nama");
        email = getIntent().getStringExtra("email");
        alamat = getIntent().getStringExtra("alamat");
        no_telepon = getIntent().getStringExtra("no_telepon");
        img_url = getIntent().getStringExtra("img_url");

        database = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = findViewById(R.id.rc_list_request);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mAdapter = new WishListAdapter(CheckoutActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(CheckoutActivity.this);

        String getUserID = id_customer;
        storageReference = FirebaseStorage.getInstance().getReference("WishListBarang");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("WishListBarang").child(getUserID);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("WishListBarang").child(getUserID);

        final SweetAlertDialog pDialog = new SweetAlertDialog(CheckoutActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);
        pDialog.show();

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    WishListNew upload = postSnapshot.getValue(WishListNew.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);

                    int total = 0;
                    String totalnama = "";
                    String totalharga = "";
                    String totalqty = "";
                    for(int i=0;i<mUploads.size();i++){
                        WishListNew food_items = mUploads.get(i);
                        int price = Integer.parseInt(food_items.getTotal());//getPrice() is a getter method in your POJO class.
                        total += price;
                        String  total_nama = food_items.getNama_barang();//getPrice() is a getter method in your POJO class.
                        totalnama += total_nama + " ,";
                        String  total_harga = food_items.getHarga();//getPrice() is a getter method in your POJO class.
                        totalharga += total_harga + " ,";
                        String  total_qty = food_items.getJumlah_pesanan();//getPrice() is a getter method in your POJO class.
                        totalqty += total_qty + " ,";
                    }
                    txt_total.setText(String.valueOf(total));
                    txt_total_barang.setText(totalnama);
                    txt_total_harga.setText(totalharga);
                    txt_total_qty.setText(totalqty);
                }
                mAdapter.notifyDataSetChanged();
                //mProgressCircle.setVisibility(View.INVISIBLE);
                pDialog.dismissWithAnimation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CheckoutActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressCircle.setVisibility(View.INVISIBLE);
                pDialog.dismissWithAnimation();
            }
        });






        txt_email = findViewById(R.id.txt_email);
        txt_nama = findViewById(R.id.txt_nama);
        txt_alamat = findViewById(R.id.txt_alamat);
        txt_no_tlp = findViewById(R.id.txt_no_tlp);
        txt_tgl_pemesanan = findViewById(R.id.txt_tgl_pemesanan);
        txt_id_wish = findViewById(R.id.txt_id_wish);
        txt_total = findViewById(R.id.txt_total);
        txt_total_barang = findViewById(R.id.txt_total_barang);
        txt_total_harga = findViewById(R.id.txt_total_harga);
        txt_total_qty = findViewById(R.id.txt_total_qty);

        //mUploads = new ArrayList<>();
        //ArrayList<WishListNew> foodList = new ArrayList<>();
        /*int total = 0;
        for(int i=0;i<mUploads.size();i++){
            WishListNew food_items = mUploads.get(i);
            int price = Integer.parseInt(food_items.getTotal());//getPrice() is a getter method in your POJO class.
            total += price;
        }*/

        //txt_total.setText(String.valueOf(total));

        txt_id_wish.setText(id_customer);
        txt_email.setText(email);
        txt_nama.setText(nama);
        txt_alamat.setText(alamat);
        txt_no_tlp.setText(no_telepon);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String tgl = formatter.format(date);
        txt_tgl_pemesanan.setText(tgl);


    }

    @Override
    public void onItemClick(int position) {
        //Intent detailIntent = new Intent(this, ViewBarangActivity.class);
        //WishListNew clickedItem = mUploads.get(position);
        //detailIntent.putExtra("id", clickedItem.getKey());
        //detailIntent.putExtra("total", clickedItem.getTotal());

        //int jumlahpesanan   =+ Integer.parseInt(clickedItem.getTotal());
        //int harga           = Integer.parseInt(etEmail.getText().toString());

        //int total           = jumlahpesanan * harga;



        //detailIntent.putExtra("namaB", clickedItem.getNama());
        //detailIntent.putExtra("harga", clickedItem.getHarga());
        //detailIntent.putExtra("stok", clickedItem.getStok());
        //detailIntent.putExtra("url", clickedItem.getUrl());

        //startActivity(detailIntent);
    }

    public void pesen(View view) {
        
        String Snama = txt_nama.getText().toString();
        String Semail = txt_email.getText().toString();
        String Salamat = txt_alamat.getText().toString();
        String Stgl = txt_tgl_pemesanan.getText().toString();
        String Stlp = txt_no_tlp.getText().toString();
        
        String Snama_barang = txt_total_barang.getText().toString();
        String Sharga = txt_total_harga.getText().toString();
        String Sjumlah_pesanan = txt_total_qty.getText().toString();
        String Stotal = txt_total.getText().toString();
        String Sstatus = "Dalam Proses";

        if (Snama.equals("")) {
            txt_nama.setError("Silahkan masukkan nama");
            txt_nama.requestFocus();
        } else if (Semail.equals("")) {
            txt_email.setError("Silahkan masukkan email");
            txt_email.requestFocus();
        } else if (Salamat.equals("")) {
            txt_alamat.setError("Silahkan masukkan alamat");
            txt_alamat.requestFocus();
        } else if (Stgl.equals("")) {
            txt_tgl_pemesanan.setError("Silahkan masukkan tgl");
            txt_tgl_pemesanan.requestFocus();
        } else if (Stlp.equals("")) {
            txt_no_tlp.setError("Silahkan masukkan no telp");
            txt_no_tlp.requestFocus();
        }
        else {
            loading = ProgressDialog.show(CheckoutActivity.this,
                    null,
                    "Please wait...",
                    true,
                    false);

            submitUser(new WishList(
                    Snama.toLowerCase(),
                    Semail.toLowerCase(),
                    Salamat.toLowerCase(),
                    Stgl.toLowerCase(),
                    Stlp.toLowerCase(),
                    Snama_barang.toLowerCase(),
                    Sharga.toLowerCase(),
                    Sjumlah_pesanan.toLowerCase(),
                    Sstatus.toLowerCase(),
                    Stotal.toLowerCase()));
            //upload(imageid);*/
        }
    }

    private void submitUser(WishList wishList) {
        //Mendapatkan UserID dari pengguna yang Terautentikasi
        String getUserID = txt_id_wish.getText().toString();
        database.child("Transaksi")
                .child(getUserID)
                .push()
                .setValue(wishList)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        loading.dismiss();

                        new SweetAlertDialog(CheckoutActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Pemesanan Berhasil")
                                .show();

                        Intent detailIntent2 = new Intent(CheckoutActivity.this, StatusActivity.class);
                        detailIntent2.putExtra("id_customer", id_customer);
                        detailIntent2.putExtra("username", username);
                        detailIntent2.putExtra("nama", nama);
                        detailIntent2.putExtra("email", email);
                        detailIntent2.putExtra("alamat", alamat);
                        detailIntent2.putExtra("no_telepon", no_telepon);
                        detailIntent2.putExtra("img_url", img_url);
                        startActivity(detailIntent2);
                    }

                });
    }

    public void edit(View view) {
        Intent detailIntent = new Intent(this, CustomerEditActivity.class);
        detailIntent.putExtra("username", username);
        detailIntent.putExtra("nama", nama);
        detailIntent.putExtra("email", email);
        detailIntent.putExtra("alamat", alamat);
        detailIntent.putExtra("no_telepon", no_telepon);
        detailIntent.putExtra("img_url", img_url);
        startActivity(detailIntent);
    }
}
