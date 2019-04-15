package com.example.sh.orderfood.model;

import java.io.Serializable;

public class KhachHang implements Serializable{
    private String Ten,soDienThoai,userID;
    private String Email;//Pass,
    private int Tuoi,Quyen;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    private String ID;

    public KhachHang() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public KhachHang(String userID , String ten, int tuoi, String soDienThoai, int quyen, String email) {
//        Pass = pass;
        this.userID = userID;
        this.Email = email;
        this.Ten = ten;
        this.Tuoi = tuoi;
        this.soDienThoai = soDienThoai;
        this.Quyen = quyen;

    }

    public KhachHang( String ten, int tuoi, String soDienThoai, int quyen, String email) {
//        Pass = pass;
        this.Email = email;
        this.Ten = ten;
        this.Tuoi = tuoi;
        this.soDienThoai = soDienThoai;
        this.Quyen = quyen;

    }

    //    public String getPass() {
//        return Pass;
//    }
//
//    public void setPass(String pass) {
//        Pass = pass;
//    }
//
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public int getTuoi() {
        return Tuoi;
    }

    public void setTuoi(int tuoi) {
        Tuoi = tuoi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public int getQuyen() {
        return Quyen;
    }

    public void setQuyen(int quyen) {
        Quyen = quyen;
    }
}
