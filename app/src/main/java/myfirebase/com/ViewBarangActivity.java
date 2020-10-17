package myfirebase.com;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import myfirebase.com.model.Requests;
import myfirebase.com.model.WishList;
import myfirebase.com.model.WishListNew;

public class ViewBarangActivity extends AppCompatActivity {

    //private static final String TAG = "user";
    //private DatabaseReference database;

    private TextView etNama, etEmail, etDesk, txt_total, txt_tgl_pemesanan,txt_email,txt_nama, txt_alamat,txt_no_tlp,txt_id_wish;
    private EditText txt_jumlah_pesanan;
    private ProgressDialog loading;
    private Button btn_cancel, btn_save;
    private ImageView imageprofile;

    static public String sPid, sPnama, sPharga, sPstok, sPurl;
    String id_customer, username,email,role,nama,img_url,alamat,no_telepon;
    private DatabaseReference database;
    //SharedPreferences sharedpreferences;
    //Boolean session = false;

    //public static final String session_status = "session_status";
    //public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_barang);

        database = FirebaseDatabase.getInstance().getReference();

        sPid = getIntent().getStringExtra("id");
        sPnama = getIntent().getStringExtra("namaB");
        sPharga = getIntent().getStringExtra("harga");
        sPstok = getIntent().getStringExtra("stok");
        sPurl = getIntent().getStringExtra("url");

        id_customer = getIntent().getStringExtra("id_customer");
        username = getIntent().getStringExtra("username");
        nama = getIntent().getStringExtra("nama");
        email = getIntent().getStringExtra("email");
        alamat = getIntent().getStringExtra("alamat");
        no_telepon = getIntent().getStringExtra("no_telepon");
        img_url = getIntent().getStringExtra("img_url");

        /*sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        //session = sharedpreferences.getBoolean(session_status, false);
        //id = sharedpreferences.getString(Server.TAG_ID, null);
        username = sharedpreferences.getString(Server.TAG_USERNAME, null);
        email = sharedpreferences.getString(Server.TAG_EMAIL, null);
        role = sharedpreferences.getString(Server.TAG_ROLE, null);
        img_url = sharedpreferences.getString(Server.TAG_IMG_URL, null);
        nama = sharedpreferences.getString(Server.TAG_NAMA, null);
        alamat = sharedpreferences.getString(Server.TAG_ALAMAT, null);
        no_telepon = sharedpreferences.getString(Server.TAG_NO_TELEPON, null);*/

        //if (session) {
            //txt_email.setText(email);
            //txt_nama.setText(nama);
        //}

        etNama = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_harga);
        etDesk = findViewById(R.id.et_stok);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        imageprofile = findViewById(R.id.imageprofile);



        txt_jumlah_pesanan = findViewById(R.id.txt_jumlah_pesanan);

        txt_total = findViewById(R.id.txt_total);
        txt_tgl_pemesanan = findViewById(R.id.txt_tgl_pemesanan);
        txt_email = findViewById(R.id.txt_email);
        txt_nama = findViewById(R.id.txt_nama);
        txt_alamat = findViewById(R.id.txt_alamat);
        txt_no_tlp = findViewById(R.id.txt_no_tlp);
        txt_id_wish = findViewById(R.id.txt_id_wish);


        etNama.setText(sPnama);
        etEmail.setText(sPharga);
        etDesk.setText(sPstok);
        txt_id_wish.setText(id_customer);
        txt_email.setText(email);
        txt_nama.setText(nama);
        txt_alamat.setText(alamat);
        txt_no_tlp.setText(no_telepon);



        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String tgl = formatter.format(date);
        txt_tgl_pemesanan.setText(tgl);

        Picasso.get().load(Server.URL_UPLOAD2 + sPurl).fit().centerCrop().placeholder(R.drawable.sayuran)
                .error(R.drawable.ic_error).into(imageprofile);
        autocalculate();

        txt_jumlah_pesanan.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                autocalculate();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                autocalculate();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                autocalculate();
            }
        });
    }


    public void autocalculate(){
            int jumlahpesanan   = Integer.parseInt(txt_jumlah_pesanan.getText().toString());
            int harga           = Integer.parseInt(etEmail.getText().toString());

            int total           = jumlahpesanan * harga;

            txt_total.setText(String.valueOf(total));
    }

    public void minus(View view) {
        int jmlpesan        = Integer.parseInt(txt_jumlah_pesanan.getText().toString());
        if (jmlpesan <= 1){
            new SweetAlertDialog(ViewBarangActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Jumlah Stok Tidak Boleh Kurang Dari 1")
                    .show();
            txt_jumlah_pesanan.setText(String.valueOf(1));
        }
        else {
            int total = +jmlpesan - 1;
            txt_jumlah_pesanan.setText(String.valueOf(total));
        }
    }

    public void plus(View view) {
        int jmlpesan        = Integer.parseInt(txt_jumlah_pesanan.getText().toString());
        int maxstok        = Integer.parseInt(sPstok = getIntent().getStringExtra("stok"));
        if (jmlpesan >= maxstok){
            new SweetAlertDialog(ViewBarangActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Jumlah Stok Maksimum "+ maxstok +" ")
                    .show();
            txt_jumlah_pesanan.setText(String.valueOf(maxstok));
        }
        else {
            int total = +jmlpesan + 1;
            txt_jumlah_pesanan.setText(String.valueOf(total));
        }
    }

    public void min(View view) {
        txt_jumlah_pesanan.setText(String.valueOf(1));
    }

    public void max(View view) {
        int maxstok        = Integer.parseInt(sPstok = getIntent().getStringExtra("stok"));
        txt_jumlah_pesanan.setText(String.valueOf(maxstok));
    }

    public void back(View view) {
        
    }

    public void pindah(View view) {
        Intent detailIntent = new Intent(this, CustomerEditActivity.class);
        detailIntent.putExtra("username", username);
        detailIntent.putExtra("nama", nama);
        detailIntent.putExtra("email", email);
        detailIntent.putExtra("alamat", alamat);
        detailIntent.putExtra("no_telepon", no_telepon);
        detailIntent.putExtra("img_url", img_url);
        startActivity(detailIntent);
    }

    public void pesan(View view) {
        /*String Snama = txt_nama.getText().toString();
        String Semail = txt_email.getText().toString();
        String Salamat = txt_alamat.getText().toString();
        String Stgl = txt_tgl_pemesanan.getText().toString();
        String Stlp = txt_no_tlp.getText().toString();*/

        //String Surl = ;
        String Snama_barang = etNama.getText().toString();
        String Sharga = etEmail.getText().toString();
        String Sjumlah_pesanan = txt_jumlah_pesanan.getText().toString();
        String Stotal = txt_total.getText().toString();

        /*if (Snama.equals("")) {
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
        } */

        if (Snama_barang.equals("")) {
            etNama.setError("Silahkan masukkan nama barang");
            etNama.requestFocus();
        } else if (Sharga.equals("")) {
            etEmail.setError("Silahkan masukkan no harga");
            etEmail.requestFocus();
        } else if (Sjumlah_pesanan.equals("")) {
            txt_jumlah_pesanan.setError("Silahkan masukkan no jumlah pesanan");
            txt_jumlah_pesanan.requestFocus();
        } else if (Stotal.equals("")) {
            txt_total.setError("Silahkan masukkan total");
            txt_total.requestFocus();
        }
        else {
            loading = ProgressDialog.show(ViewBarangActivity.this,
                    null,
                    "Please wait...",
                    true,
                    false);

            //imageid = System.currentTimeMillis() + "." + GetFileExtension(FilePathUri);

            submitUser(new WishListNew(
                    sPurl,
                    Snama_barang.toLowerCase(),
                    Sharga.toLowerCase(),
                    Sjumlah_pesanan.toLowerCase(),
                    Stotal.toLowerCase()));
            //upload(imageid);
        }

    }

    private void submitUser(WishListNew wishListnew) {
        //Mendapatkan UserID dari pengguna yang Terautentikasi
        String getUserID = txt_id_wish.getText().toString();
        database.child("WishListBarang")
                .child(getUserID)
                .push()
                .setValue(wishListnew)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        loading.dismiss();

                        new SweetAlertDialog(ViewBarangActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Data Berhasil ditambahkan")
                                .show();

                        Intent detailIntent2 = new Intent(ViewBarangActivity.this, CheckoutActivity.class);
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