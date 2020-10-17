package myfirebase.com.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class WishListNew implements Serializable{

    private String url;
    private String nama_barang;
    private String harga;
    private String jumlah_pesanan;
    private String total;
    private String key;

    public WishListNew(){

    }

    public WishListNew(String url, String nama_barang, String harga, String jumlah_pesanan, String total) {
        this.url = url;
        this.nama_barang = nama_barang;
        this.harga = harga;
        this.jumlah_pesanan = jumlah_pesanan;
        this.total = total;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJumlah_pesanan() {
        return jumlah_pesanan;
    }

    public void setJumlah_pesanan(String jumlah_pesanan) {
        this.jumlah_pesanan = jumlah_pesanan;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return " "+url+"\n" +
                " "+nama_barang+"\n" +
                " "+harga+"\n" +
                " "+jumlah_pesanan+"\n" +
                " "+total;
    }

}
