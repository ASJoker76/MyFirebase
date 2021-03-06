package myfirebase.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import myfirebase.com.LoginActivity;
import myfirebase.com.R;
import myfirebase.com.Server;
import myfirebase.com.ViewBarangActivity;
import myfirebase.com.model.Requests;

//import android.support.v7.widget.RecyclerView;

public class RequestAdapterRecyclerView2 extends RecyclerView.Adapter<RequestAdapterRecyclerView2.MyViewHolder> {

    private List<Requests> moviesList;
    private Activity mActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layout;
        public TextView tv_nama, tv_harga,tv_stok;
        public ImageView tv_url;


        public MyViewHolder(View view) {
            super(view);
            rl_layout = view.findViewById(R.id.rl_layout);
            tv_nama = view.findViewById(R.id.tv_nama);
            tv_harga = view.findViewById(R.id.tv_harga);
            tv_stok = view.findViewById(R.id.tv_stok);
            tv_url = view.findViewById(R.id.tv_url);
        }
    }

    public RequestAdapterRecyclerView2(List<Requests> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.mActivity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request_barang, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Requests movie = moviesList.get(position);

        holder.tv_nama.setText(movie.getNama());
        holder.tv_harga.setText(movie.getHarga());
        holder.tv_stok.setText(movie.getStok());
        Picasso.get().load(Server.URL_UPLOAD2 + movie.getUrl()).fit().centerCrop().placeholder(R.drawable.sayuran)
                .error(R.drawable.ic_error).into(holder.tv_url);
        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goDetail = new Intent(mActivity, ViewBarangActivity.class);
                goDetail.putExtra("id", movie.getKey());
                goDetail.putExtra("namaB", movie.getNama());
                goDetail.putExtra("harga", movie.getHarga());
                goDetail.putExtra("stok", movie.getStok());
                goDetail.putExtra("url", movie.getUrl());
                /*goDetail.putExtra("email", Server.TAG_EMAIL);
                goDetail.putExtra("namac", Server.TAG_NAMA);
                goDetail.putExtra("alamat", Server.TAG_ALAMAT);*/
                mActivity.startActivity(goDetail);


            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}
