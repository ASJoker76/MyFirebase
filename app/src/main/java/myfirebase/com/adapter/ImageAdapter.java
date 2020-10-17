package myfirebase.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import java.util.List;

import myfirebase.com.R;
import myfirebase.com.Server;
import myfirebase.com.ViewBarangActivity;
import myfirebase.com.model.Requests;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Requests> mUploads;
    private OnItemClickListener mListener;
    public ImageAdapter(Context context, List<Requests> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_request_barang, parent, false);
        return new ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        final Requests movie = mUploads.get(position);

        holder.tv_nama.setText(movie.getNama());
        holder.tv_harga.setText(movie.getHarga());
        holder.tv_stok.setText(movie.getStok());
        Picasso.get().load(Server.URL_UPLOAD2 + movie.getUrl()).fit().centerCrop().placeholder(R.drawable.sayuran)
                .error(R.drawable.ic_error).into(holder.tv_url);

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_nama, tv_harga,tv_stok;
        public ImageView tv_url;
        public ImageViewHolder(View itemView) {
            super(itemView);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_harga = itemView.findViewById(R.id.tv_harga);
            tv_stok = itemView.findViewById(R.id.tv_stok);
            tv_url = itemView.findViewById(R.id.tv_url);

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