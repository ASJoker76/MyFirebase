package myfirebase.com;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import myfirebase.com.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.graphics.Color;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    EditText txt_username, txt_password;
    Intent intent;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL + "login.php";

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username,email, role, img_url, nama, alamat,no_telepon;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);

        // Cek session login jika TRUE maka langsung buka SiswaActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(Server.TAG_ID, null);
        username = sharedpreferences.getString(Server.TAG_USERNAME, null);
        email = sharedpreferences.getString(Server.TAG_EMAIL, null);
        role = sharedpreferences.getString(Server.TAG_ROLE, null);
        img_url = sharedpreferences.getString(Server.TAG_IMG_URL, null);
        nama = sharedpreferences.getString(Server.TAG_NAMA, null);
        alamat = sharedpreferences.getString(Server.TAG_ALAMAT, null);
        no_telepon = sharedpreferences.getString(Server.TAG_NO_TELEPON, null);

        if (session) {
            String x = role;
            switch (x) {

                case "Customer":
                    // Memanggil main activity
                    Intent intent = new Intent(LoginActivity.this, CustomerActivity.class);
                    intent.putExtra(Server.TAG_ID, id);
                    intent.putExtra(Server.TAG_USERNAME, username);
                    intent.putExtra(Server.TAG_EMAIL, email);
                    intent.putExtra(Server.TAG_ROLE, role);
                    intent.putExtra(Server.TAG_IMG_URL, img_url);
                    intent.putExtra(Server.TAG_NAMA, nama);
                    intent.putExtra(Server.TAG_ALAMAT, alamat);
                    intent.putExtra(Server.TAG_NO_TELEPON, no_telepon);
                    finish();
                    startActivity(intent);
                    break;
                case "Mitra":
                    Intent intent3 = new Intent(LoginActivity.this, MitraActivity.class);
                    intent3.putExtra(Server.TAG_ID, id);
                    intent3.putExtra(Server.TAG_USERNAME, username);
                    intent3.putExtra(Server.TAG_EMAIL, email);
                    intent3.putExtra(Server.TAG_ROLE, role);
                    intent3.putExtra(Server.TAG_IMG_URL, img_url);
                    intent3.putExtra(Server.TAG_NAMA, nama);
                    intent3.putExtra(Server.TAG_ALAMAT, alamat);
                    intent3.putExtra(Server.TAG_NO_TELEPON, no_telepon);
                    finish();
                    startActivity(intent3);
                    break;
                case "Developer":

                    break;
                default:
                    //Toast.makeText(getApplicationContext(), level, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    public void gantipage(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    public void login(View view) {
        String username = txt_username.getText().toString();
        String password = txt_password.getText().toString();

        // mengecek kolom yang kosong
        if (username.trim().length() > 0 && password.trim().length() > 0) {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                checkLogin(username, password);
            } else {
                Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
            }
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
        }
    }

    private void checkLogin(final String username, final String password) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                pDialog.dismissWithAnimation();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        String img_url = jObj.getString(Server.TAG_IMG_URL);
                        String nama = jObj.getString(Server.TAG_NAMA);
                        String alamat = jObj.getString(Server.TAG_ALAMAT);
                        String no_telepon = jObj.getString(Server.TAG_NO_TELEPON);
                        String role = jObj.getString(Server.TAG_ROLE);
                        String email = jObj.getString(Server.TAG_EMAIL);
                        String username = jObj.getString(Server.TAG_USERNAME);
                        String id = jObj.getString(Server.TAG_ID);

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(Server.TAG_ID, id);
                        editor.putString(Server.TAG_USERNAME, username);
                        editor.putString(Server.TAG_EMAIL, email);
                        editor.putString(Server.TAG_ROLE, role);
                        editor.putString(Server.TAG_IMG_URL, img_url);
                        editor.putString(Server.TAG_NAMA, nama);
                        editor.putString(Server.TAG_ALAMAT, alamat);
                        editor.putString(Server.TAG_NO_TELEPON, no_telepon);
                        editor.commit();

                        switch (role) {
                            case "Customer":
                                // Memanggil main activity
                                Intent intent = new Intent(LoginActivity.this, CustomerActivity.class);
                                intent.putExtra(Server.TAG_ID, id);
                                intent.putExtra(Server.TAG_USERNAME, username);
                                intent.putExtra(Server.TAG_EMAIL, email);
                                intent.putExtra(Server.TAG_ROLE, role);
                                intent.putExtra(Server.TAG_IMG_URL, img_url);
                                intent.putExtra(Server.TAG_NAMA, nama);
                                intent.putExtra(Server.TAG_ALAMAT, alamat);
                                intent.putExtra(Server.TAG_NO_TELEPON, no_telepon);
                                finish();
                                startActivity(intent);
                                break;
                            case "Mitra":
                                Intent intent3 = new Intent(LoginActivity.this, MitraActivity.class);
                                intent3.putExtra(Server.TAG_ID, id);
                                intent3.putExtra(Server.TAG_USERNAME, username);
                                intent3.putExtra(Server.TAG_EMAIL, email);
                                intent3.putExtra(Server.TAG_ROLE, role);
                                intent3.putExtra(Server.TAG_IMG_URL, img_url);
                                intent3.putExtra(Server.TAG_NAMA, nama);
                                intent3.putExtra(Server.TAG_ALAMAT, alamat);
                                intent3.putExtra(Server.TAG_NO_TELEPON, no_telepon);
                                finish();
                                startActivity(intent3);
                            case "Developer":
                                break;
                            default:
                                //Toast.makeText(getApplicationContext(), level, Toast.LENGTH_LONG).show();
                                break;
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
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
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {

    }
}