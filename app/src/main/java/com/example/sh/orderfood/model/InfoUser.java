package com.example.sh.orderfood.model;

public class InfoUser {
    public String ten;
    public String sdt;
    public String ngaysinh;
    public String gioitinh;

    public InfoUser() {
    }

    public InfoUser(String ten, String sdt, String ngaysinh, String gioitinh) {
        this.sdt=sdt;
        this.ten=ten;
        this.ngaysinh=ngaysinh;
        this.gioitinh=gioitinh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }
}
