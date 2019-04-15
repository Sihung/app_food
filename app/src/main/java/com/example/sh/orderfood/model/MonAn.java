package com.example.sh.orderfood.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MonAn implements Serializable {
    String tenMonAn, imageUrl;
    int gia;
    int id;
    public int soLuong;
    public int tongtien;

    public ArrayList<String> Mon =new ArrayList<String>();
    public int getTongtien() {
        return tongtien;
    }

    public void setTongtien(int tongtien) {
        this.tongtien = tongtien;
    }

    public MonAn() {
    }

    public MonAn(String tenMonAn, String imageUrl, int gia) {
        this.tenMonAn = tenMonAn;
        this.imageUrl = imageUrl;
        this.gia = gia;
        this.soLuong = 1;
        this.tongtien = gia*this.soLuong;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public MonAn(String tenMonAn, String imageUrl, int gia, int id, int soLuong) {
        this.tenMonAn = tenMonAn;
        this.imageUrl = imageUrl;
        this.gia = gia;
        this.id = id;
        this.soLuong = soLuong;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }
}
