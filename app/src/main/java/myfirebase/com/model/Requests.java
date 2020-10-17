package myfirebase.com.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Requests implements Serializable{

    private String nama;
    private String harga;
    private String stok;
    public String url;
    private String key;

    public Requests(){

    }

    public Requests(String nama, String harga, String stok,String url) {
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.url = url;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return " "+nama+"\n" +
                " "+harga+"\n" +
                " "+stok+"\n" +
                " "+url;
    }

}
