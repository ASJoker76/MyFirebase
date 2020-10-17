package myfirebase.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import myfirebase.com.ManajemenBarangActivity;
import myfirebase.com.R;
import myfirebase.com.Server;
import myfirebase.com.model.Requests;
import myfirebase.com.model.WishListNew;

//import android.support.v7.widget.RecyclerView;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishViewHolder> {

    private Context mContext;
    private List<WishListNew> mUploads;
    private WishListAdapter.OnItemClickListener mListener;

    public WishListAdapter(Context context, List<WishListNew> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public WishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_request_transaksi, parent, false);
        return new WishViewHolder(v);
    }


    @Override
    public void onBindViewHolder(WishViewHolder holder, int position) {

        final WishListNew movie = mUploads.get(position);

        holder.txt_nama.setText(movie.getNama_barang());
        holder.txt_harga.setText(movie.getHarga());
        holder.txt_stok.setText(movie.getJumlah_pesanan());
        holder.txt_total.setText(movie.getTotal());
        Picasso.get().load(Server.URL_UPLOAD2 + movie.getUrl()).fit().centerCrop().placeholder(R.drawable.sayuran)
                .error(R.drawable.ic_error).into(holder.txt_url);
    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class WishViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_nama, txt_harga,txt_id_wish,txt_total;
        public EditText txt_stok;
        public ImageView txt_url;
        public WishViewHolder(View itemView) {
            super(itemView);

            txt_nama = itemView.findViewById(R.id.txt_nama);
            txt_harga = itemView.findViewById(R.id.txt_harga);
            txt_stok = itemView.findViewById(R.id.txt_stok);
            txt_total = itemView.findViewById(R.id.txt_total);
            txt_url = itemView.findViewById(R.id.txt_url);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


}
