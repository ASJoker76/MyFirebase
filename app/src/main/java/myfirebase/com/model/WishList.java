package myfirebase.com.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class WishList implements Serializable{

    private String nama_customer;
    private String email;
    private String alamat;
    private String tgl;
    private String tlp;

    private String nama_barang;
    private String harga;
    private String jumlah_pesanan;
    private String status;
    private String total;
    private String key;

    public WishList(){

    }

    public WishList(String nama_customer, String email, String alamat, String tgl, String tlp, String nama_barang, String harga, String jumlah_pesanan,String status,String total) {
        this.nama_customer = nama_customer;
        this.email = email;
        this.alamat = alamat;
        this.tgl = tgl;
        this.tlp = tlp;
        this.nama_barang = nama_barang;
        this.harga = harga;
        this.jumlah_pesanan = jumlah_pesanan;
        this.status = status;
        this.total = total;
    }

    public String getNama_customer() {
        return nama_customer;
    }

    public void setNama_customer(String nama_customer) {
        this.nama_customer = nama_customer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getTlp() {
        return tlp;
    }

    public void setTlp(String tlp) {
        this.tlp = tlp;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return " "+nama_customer+"\n" +
                " "+email+"\n" +
                " "+alamat+"\n" +
                " "+tgl+"\n" +
                " "+tlp+"\n" +
                " "+nama_barang+"\n" +
                " "+harga+"\n" +
                " "+jumlah_pesanan+"\n" +
                " "+status+"\n" +
                " "+total;
    }

}
