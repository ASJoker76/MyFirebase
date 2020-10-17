package myfirebase.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import cn.pedant.SweetAlert.SweetAlertDialog;
import myfirebase.com.adapter.ImageAdapter;
import myfirebase.com.adapter.RequestAdapterRecyclerView2;
import myfirebase.com.app.AppController;
import myfirebase.com.model.Requests;

public class CustomerActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener{

    TextView txt_id, txt_username,profileName,profileEmail,txt_path;
    ImageView photoProfile;
    String id_customer, username,email,role,nama,img_url,alamat,no_telepon;
    SharedPreferences sharedpreferences;

    public static final String session_status = "session_status";

    private DatabaseReference database;

    private ArrayList<Requests> daftarReq;
    private RequestAdapterRecyclerView2 requestAdapterRecyclerView2;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    boolean doubleBackToExitPressedOnce = false;

    //upload foto
    static public Uri uri;
    Bitmap bitmap, decoded;
    private String selectedFilePath;
    int bitmap_size = 100; // range 1 - 100
    private String updatefoto = Server.URL + "update_foto.php";
    int success;
    private static final String TAG = CustomerActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";
    ConnectivityManager conMgr;

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    //private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    public StorageReference storageReference;
    public DatabaseReference databaseReference;
    private ValueEventListener mDBListener;
    private ArrayList<Requests> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);


        mRecyclerView = findViewById(R.id.rc_list_request);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mAdapter = new ImageAdapter(CustomerActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(CustomerActivity.this);

        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Barang");

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Barang");

        final SweetAlertDialog pDialog = new SweetAlertDialog(CustomerActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);
        pDialog.show();
        /*loading = ProgressDialog.show(CustomerActivity.this,
                null,
                "Please wait...",
                true,
                false);*/

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Requests upload = postSnapshot.getValue(Requests.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                //mProgressCircle.setVisibility(View.INVISIBLE);
                pDialog.dismissWithAnimation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CustomerActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressCircle.setVisibility(View.INVISIBLE);
                pDialog.dismissWithAnimation();
            }
        });


        txt_username = (TextView) findViewById(R.id.txt_username);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        id_customer = getIntent().getStringExtra(Server.TAG_ID);
        username = getIntent().getStringExtra(Server.TAG_USERNAME);
        email = getIntent().getStringExtra(Server.TAG_EMAIL);
        role = getIntent().getStringExtra(Server.TAG_ROLE);
        nama = getIntent().getStringExtra(Server.TAG_NAMA);
        img_url = getIntent().getStringExtra(Server.TAG_IMG_URL);
        alamat = getIntent().getStringExtra(Server.TAG_ALAMAT);
        no_telepon = getIntent().getStringExtra(Server.TAG_NO_TELEPON);

        profileName = (TextView) findViewById(R.id.profileName);
        profileEmail = (TextView) findViewById(R.id.profileEmail);


        txt_path = (TextView) findViewById(R.id.txt_path);
        photoProfile = (ImageView) findViewById(R.id.photoProfile);

        profileName.setText(username);
        profileEmail.setText(email);

        onloadgambar();

    }


    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, ViewBarangActivity.class);
        Requests clickedItem = mUploads.get(position);
        detailIntent.putExtra("id", clickedItem.getKey());
        detailIntent.putExtra("namaB", clickedItem.getNama());
        detailIntent.putExtra("harga", clickedItem.getHarga());
        detailIntent.putExtra("stok", clickedItem.getStok());
        detailIntent.putExtra("url", clickedItem.getUrl());

        detailIntent.putExtra("id_customer", id_customer);
        detailIntent.putExtra("username", username);
        detailIntent.putExtra("nama", nama);
        detailIntent.putExtra("email", email);
        detailIntent.putExtra("alamat", alamat);
        detailIntent.putExtra("no_telepon", no_telepon);
        detailIntent.putExtra("img_url", img_url);

        startActivity(detailIntent);
    }


    public void onloadgambar(){
        if(img_url.matches("")) {
            Picasso.get().load(Server.URL_UPLOAD + "userakun.png").into(photoProfile);
        }
        else {
            Picasso.get().load(Server.URL_UPLOAD + img_url).fit().centerCrop().placeholder(R.drawable.ic_account)
                    .error(R.drawable.ic_error).into(photoProfile);
        }
    }


    public void logout(View view) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(CustomerActivity.session_status, false);
        editor.putString(Server.TAG_ID, null);
        editor.putString(Server.TAG_USERNAME, null);
        editor.putString(Server.TAG_EMAIL, null);
        editor.putString(Server.TAG_ROLE, null);
        editor.putString(Server.TAG_IMG_URL, null);
        editor.putString(Server.TAG_NAMA, null);
        editor.putString(Server.TAG_ALAMAT, null);
        editor.putString(Server.TAG_NO_TELEPON, null);
        editor.commit();

        Intent intent = new Intent(CustomerActivity.this, LoginActivity.class);
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

    public void gantifoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                try {
                    uri = data.getData();
                    //mengambil fambar dari Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                    setToImageView(getResizedBitmap(bitmap, 1024));
                    selectedFilePath = getPath(uri);
                    if (selectedFilePath != null) {
                        txt_path.setText(selectedFilePath);
                    }

                    upload();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        photoProfile.setImageBitmap(decoded);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getPath(Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(uri, projection, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void upload(){
        String path = txt_path.getText().toString();
        final SweetAlertDialog pDialog = new SweetAlertDialog(CustomerActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading Upload Image...");
        pDialog.setCancelable(false);
        pDialog.show();
        //final ProgressDialog loading = ProgressDialog.show(this, "Mengupload Gambar...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updatefoto,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());
                                Toast.makeText(CustomerActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(CustomerActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        pDialog.dismissWithAnimation();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        pDialog.dismissWithAnimation();

                        //menampilkan toast
                        Toast.makeText(CustomerActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(Server.TAG_GAMBAR, getStringImage(decoded));
                params.put(Server.TAG_IMG_URL, txt_path.getText().toString().trim());
                params.put(Server.TAG_USERNAME, username);

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
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

    public void wishlist(View view) {
        Intent detailIntent2 = new Intent(CustomerActivity.this, CheckoutActivity.class);
        detailIntent2.putExtra("id_customer", id_customer);
        detailIntent2.putExtra("username", username);
        detailIntent2.putExtra("nama", nama);
        detailIntent2.putExtra("email", email);
        detailIntent2.putExtra("alamat", alamat);
        detailIntent2.putExtra("no_telepon", no_telepon);
        detailIntent2.putExtra("img_url", img_url);
        startActivity(detailIntent2);
    }
}
