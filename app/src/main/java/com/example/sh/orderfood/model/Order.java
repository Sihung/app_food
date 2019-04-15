package com.example.sh.orderfood.model;

import java.io.Serializable;

public class Order implements Serializable {
    int  soLuong, gia;
    String tenMonAn, idKhachHang;


    public Order() {
    }


    public Order(int soLuong, int gia,  String tenMonAn,String idKhachHang ) {
        this.soLuong = soLuong;
        this.idKhachHang = idKhachHang;
        this.tenMonAn = tenMonAn;
        this.gia = gia;
    }

    public Order(int soLuong, int gia,  String tenMonAn) {
        this.soLuong = soLuong;
        this.tenMonAn = tenMonAn;
        this.gia = gia;
    }
    public String getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }


    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }
}
