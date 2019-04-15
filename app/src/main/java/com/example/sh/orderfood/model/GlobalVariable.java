package com.example.sh.orderfood.model;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable {
    public static int MonAnCount = 0;
    public static int NgayCountCount = 0;
    public static int UsersCount = 0;
    public static KhachHang currentKH;
    public static List<KhachHang> khachHangList = new ArrayList<>();
    public static List<Order> orderList = new ArrayList<>();
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static FirebaseStorage storage;
    public static StorageReference storageReference;
    public static Firebase firebase;
    public static DatabaseReference databaseReference;

    public static int GetIntValue(DataSnapshot snapshot, String key)
    {
        int ret = 0;
        try
        {
            ret = Integer.valueOf(snapshot.child(key).getValue().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return ret;
    }

    public static String GetStringValue(DataSnapshot snapshot, String key)
    {
        String ret = "";
        try
        {
            ret = snapshot.child(key).getValue().toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return ret;
    }
}
