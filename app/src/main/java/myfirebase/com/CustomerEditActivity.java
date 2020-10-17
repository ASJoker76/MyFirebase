package myfirebase.com;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import myfirebase.com.app.AppController;
import myfirebase.com.model.Requests;

public class CustomerEditActivity extends AppCompatActivity {

    String id, username,email,role,nama,img_url,alamat,no_telepon;
    EditText txt_email, txt_password, txt_confirm_password, txt_alamat, txt_phone, txt_nama, txt_url;
    TextView txt_username;
    ImageView photoProfile;
    ConnectivityManager conMgr;

    //upload foto
    static public Uri uri;
    Bitmap bitmap, decoded;
    private String selectedFilePath;
    int bitmap_size = 100; // range 1 - 100
    private String updatefoto = Server.URL + "update_foto.php";
    int success;

    private String url = Server.URL + "update_customer.php";

    private static final String TAG = CustomerEditActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";
    public static final String session_status = "session_status";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        inisialisasi();

        username = getIntent().getStringExtra("username");
        nama = getIntent().getStringExtra("nama");
        email = getIntent().getStringExtra("email");
        alamat = getIntent().getStringExtra("alamat");
        no_telepon = getIntent().getStringExtra("no_telepon");
        img_url = getIntent().getStringExtra("img_url");

        txt_username.setText(username);
        txt_email.setText(email);
        txt_alamat.setText(alamat);
        txt_nama.setText(nama);
        txt_phone.setText(no_telepon);
        txt_alamat.setText(alamat);
        txt_url.setText(img_url);

        Picasso.get().load(Server.URL_UPLOAD + img_url).fit().centerCrop().placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_error).into(photoProfile);
    }

    private void inisialisasi() {
        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_nama = (EditText) findViewById(R.id.txt_nama);
        txt_alamat = (EditText) findViewById(R.id.txt_alamat);
        txt_phone = (EditText) findViewById(R.id.txt_phone);
        txt_url = (EditText) findViewById(R.id.txt_url);
        photoProfile = (ImageView) findViewById(R.id.imageprofile);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void update(View view) {

        String username = txt_username.getText().toString();
        String email = txt_email.getText().toString();
        String nama = txt_nama.getText().toString();
        String alamat = txt_alamat.getText().toString();
        String no_telepon = txt_phone.getText().toString();

        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            checkcustomer(username,email,nama, alamat, no_telepon);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkcustomer(final String username,final String email,final String nama,final String alamat,final String no_telepon) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(CustomerEditActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Update Response: " + response.toString());
                pDialog.dismissWithAnimation();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Successfully Update!", jObj.toString());

                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                        new SweetAlertDialog(CustomerEditActivity.this)
                                .setTitleText("Berhasil Update, Silahkan Login Kembali")
                                .show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        new SweetAlertDialog(CustomerEditActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Ada yang salah!")
                                .show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                pDialog.dismissWithAnimation();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("email", email);
                params.put("nama", nama);
                params.put("alamat", alamat);
                params.put("no_telepon", no_telepon);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
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
                        txt_url.setText(selectedFilePath);
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
        String path = txt_url.getText().toString();
        final SweetAlertDialog pDialog = new SweetAlertDialog(CustomerEditActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
                                Toast.makeText(CustomerEditActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(CustomerEditActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(CustomerEditActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(Server.TAG_GAMBAR, getStringImage(decoded));
                params.put(Server.TAG_IMG_URL, txt_url.getText().toString().trim());
                params.put(Server.TAG_USERNAME, username);

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }
}
