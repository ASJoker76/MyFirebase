package myfirebase.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import myfirebase.com.app.AppController;
import myfirebase.com.model.Requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ManajemenBarangActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    int Image_Request_Code = 7;
    Uri FilePathUri;
    ImageView imgview,imgview2;

    private static final String TAG = "user";
    private DatabaseReference database;

    private EditText etNama, etEmail, etDesk, etUrl, txt_path;
    private ProgressDialog loading;
    private Button btn_cancel, btn_save;

    private String sPid, sPnama, sPharga, sPstok,sPurl;

    static public Uri uri;
    Bitmap bitmap, decoded;
    private String selectedFilePath;
    int bitmap_size = 100; // range 1 - 100
    private String updatefoto = Server.URL2 + "upload_foto.php";
    int success;
    private static final String TAG2 = ManajemenBarangActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static String imageid;
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manajemenbarang);

        database = FirebaseDatabase.getInstance().getReference();

        sPid = getIntent().getStringExtra("id");
        sPnama = getIntent().getStringExtra("nama");
        sPharga = getIntent().getStringExtra("harga");
        sPstok = getIntent().getStringExtra("stok");
        sPurl = getIntent().getStringExtra("url");

        etNama = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_harga);
        etDesk = findViewById(R.id.et_stok);
        etUrl = findViewById(R.id.et_url);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        imgview = findViewById(R.id.image_view);



        etNama.setText(sPnama);
        etEmail.setText(sPharga);
        etDesk.setText(sPstok);
        etUrl.setText(sPurl);


        if (sPid.equals("")){
            btn_save.setText("Save");
            btn_cancel.setText("Cancel");
        } else {
            btn_save.setText("Edit");
            btn_cancel.setText("Delete");
            etUrl.setVisibility(View.VISIBLE);
            Picasso.get().load(Server.URL_UPLOAD2 + sPurl).fit().centerCrop().placeholder(R.drawable.sayuran)
                    .error(R.drawable.ic_error).into(imgview);
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Snama = etNama.getText().toString();
                String Semail = etEmail.getText().toString();
                String Sdesk = etDesk.getText().toString();
                String Surl = etUrl.getText().toString();
                //String path = txt_path.getText().toString();

                if (btn_save.getText().equals("Save")){
                    // perintah save

                    if (Snama.equals("")) {
                        etNama.setError("Silahkan masukkan nama");
                        etNama.requestFocus();
                    } else if (Semail.equals("")) {
                        etEmail.setError("Silahkan masukkan harga");
                        etEmail.requestFocus();
                    } else if (Sdesk.equals("")) {
                        etDesk.setError("Silahkan masukkan stok");
                        etDesk.requestFocus();
                    } else {
                        loading = ProgressDialog.show(ManajemenBarangActivity.this,
                                null,
                                "Please wait...",
                                true,
                                false);

                        imageid = System.currentTimeMillis() + "." + GetFileExtension(FilePathUri);

                        submitUser(new Requests(
                                Snama.toLowerCase(),
                                Semail.toLowerCase(),
                                Sdesk.toLowerCase(),
                                imageid));

                        upload(imageid);
                    }
                } else {
                    // perintah edit
                    if (Snama.equals("")) {
                        etNama.setError("Silahkan masukkan nama");
                        etNama.requestFocus();
                    } else if (Semail.equals("")) {
                        etEmail.setError("Silahkan masukkan harga");
                        etEmail.requestFocus();
                    } else if (Sdesk.equals("")) {
                        etDesk.setError("Silahkan masukkan stok");
                        etDesk.requestFocus();
                    } else {

                        if(FilePathUri != null) {

                            loading = ProgressDialog.show(ManajemenBarangActivity.this,
                                    null,
                                    "Please wait...",
                                    true,
                                    false);

                            imageid = System.currentTimeMillis() + "." + GetFileExtension(FilePathUri);

                            editUser(new Requests(
                                    Snama.toLowerCase(),
                                    Semail.toLowerCase(),
                                    Sdesk.toLowerCase(),
                                    imageid), sPid);

                            upload(imageid);
                            imgview.setImageResource(0);
                        }
                        else {

                            loading = ProgressDialog.show(ManajemenBarangActivity.this,
                                    null,
                                    "Please wait...",
                                    true,
                                    false);

                            editUser(new Requests(
                                    Snama.toLowerCase(),
                                    Semail.toLowerCase(),
                                    Sdesk.toLowerCase(),
                                    Surl.toLowerCase()), sPid);

                            imgview.setImageResource(0);
                        }
                    }
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_cancel.getText().equals("Cancel")) {
                    //tutup page
                    finish();
                } else {
                    database.child("Barang")
                            .child(sPid)
                            //.child("Mahasiswa")
                            //.child(data.getKey())
                            .removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    new SweetAlertDialog(ManajemenBarangActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Data Berhasil Dihapus")
                                            .show();

                                    etNama.setText("");
                                    etEmail.setText("");
                                    etDesk.setText("");
                                }
                            });



                }
            }
        });
    }

    private void submitUser(Requests requests) {
        database.child("Barang")
                .push()
                .setValue(requests)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        loading.dismiss();

                        etNama.setText("");
                        etEmail.setText("");
                        etDesk.setText("");

                        new SweetAlertDialog(ManajemenBarangActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Data Berhasil ditambahkan")
                                .show();

                    }

                });
    }

    private void editUser(Requests requests, String id) {
        database.child("Barang")
                .child(id)
                .setValue(requests)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        loading.dismiss();

                        etNama.setText("");
                        etEmail.setText("");
                        etDesk.setText("");
                        etUrl.setText("");

                        new SweetAlertDialog(ManajemenBarangActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Data Berhasil diedit")
                                .show();

                    }

                });
    }

    public void ambilgambar(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
    }

    private String getFileExtension(Uri filePath) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(filePath));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                FilePathUri = data.getData();
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 1024));
                selectedFilePath = getPath(uri);
                if (selectedFilePath != null) {
                  //  txt_path.setText(selectedFilePath);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }*/
        }
    }

    public void upload(final String namaurl){
        //String path = txt_path.getText().toString();
        final SweetAlertDialog pDialog = new SweetAlertDialog(ManajemenBarangActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
                                Toast.makeText(ManajemenBarangActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                imgview.setImageResource(0);
                            } else {
                                Toast.makeText(ManajemenBarangActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ManajemenBarangActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(Server.TAG_GAMBAR, getStringImage(decoded));
                params.put(Server.TAG_IMG_URL, namaurl);


                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        imgview.setImageBitmap(decoded);
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
            cursor = getContentResolver().query(FilePathUri, projection, null, null, null);

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

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
}
