package com.example.sh.orderfood;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.sh.orderfood.model.GlobalVariable;
import com.example.sh.orderfood.model.KhachHang;
import com.example.sh.orderfood.model.MonAn;
import com.example.sh.orderfood.model.Order;
import com.example.sh.orderfood.model.ThongKeAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MainQuanLyThongKeActivity extends AppCompatActivity {
    FirebaseUser user;
    private FirebaseAuth authu;
    ThongKeAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<MonAn> lsMonAn = new ArrayList<>();
    ArrayList<KhachHang> lsKhachHang = new ArrayList<>();
    ArrayList<MonAn> listDataHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();
    String tenMonAn;
    int gia;
    String khachHang;
    String ten;
    //    List<Order> listOr = new ArrayList<>();
    Order order;
    int soLuong;
    MonAn monAn1 = new MonAn();

    private List<String> listTenMon = new ArrayList<>();


//    private LinkedHashMap<String, MonAn> subjects = new LinkedHashMap<String, MonAn>();
//    private ArrayList<MonAn> deptList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quan_ly_thong_ke);
        listTenMon.add("Bùn Bo");
        listTenMon.add("Com Tam");
        expListView = findViewById(R.id.lvExp);

        listAdapter = new ThongKeAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        Firebase myFirebaseRef = new Firebase("https://android-app-a9908.firebaseio.com/54");

//        myFirebaseRef.child("Orders").addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                GlobalVariable.NgayCountCount = (int) dataSnapshot.getChildrenCount();
//                GlobalVariable.orderList.clear();
//
//                authu = FirebaseAuth.getInstance();
//                user = authu.getCurrentUser();
//                String Uid = user.getUid();
//
//                for (DataSnapshot ngaySnap : dataSnapshot.getChildren()) {
//                    String ngay = ngaySnap.getKey().toString();
//                    for (DataSnapshot userSnapShot : ngaySnap.getChildren()) {
//                        for (DataSnapshot monAnSnap : userSnapShot.getChildren()) {
//                            gia = GlobalVariable.GetIntValue(monAnSnap, "gia");
//                            khachHang = GlobalVariable.GetStringValue(monAnSnap, "idKhachHang");
//                            tenMonAn = monAnSnap.getKey().toString();
//                            soLuong = GlobalVariable.GetIntValue(monAnSnap, "soLuong");
//
//                            order = new Order(soLuong, gia, tenMonAn, khachHang);
//                            GlobalVariable.orderList.add(order);
//                        }
//                    }
//                }
//
//
//                prepareListData();
//
//                CountTenMon();
//
//
//                listAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
        myFirebaseRef.child("OrdersTest").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GlobalVariable.NgayCountCount = (int) dataSnapshot.getChildrenCount();
                GlobalVariable.orderList.clear();

                authu = FirebaseAuth.getInstance();
                user = authu.getCurrentUser();
                String Uid = user.getUid();

                for (DataSnapshot ngaySnap : dataSnapshot.getChildren()) {
                    for (DataSnapshot monAnSnapShot : ngaySnap.getChildren()) {
                        for (DataSnapshot userSnap : monAnSnapShot.getChildren()) {
                            gia = GlobalVariable.GetIntValue(userSnap, "gia");
                            khachHang = GlobalVariable.GetStringValue(userSnap, "idKhachHang");
                            tenMonAn = GlobalVariable.GetStringValue(userSnap,"tenMonAn");
//                            tenMonAn = userSnap.getKey().toString();
                            soLuong = GlobalVariable.GetIntValue(userSnap, "soLuong");
                            Log.d("-----------","========hihi:"+soLuong);
                            order = new Order(soLuong, gia, tenMonAn, khachHang);
                            GlobalVariable.orderList.add(order);
                        }
                    }
                }


                prepareListData();

                CountTenMon();


                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        myFirebaseRef.child("users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GlobalVariable.UsersCount = (int) dataSnapshot.getChildrenCount();
                GlobalVariable.khachHangList.clear();


                authu = FirebaseAuth.getInstance();
                user = authu.getCurrentUser();
                String Uid = user.getUid();


                for (DataSnapshot UserSnap : dataSnapshot.getChildren()) {
                    String userID = UserSnap.getKey().toString();
                    String email = UserSnap.child("email").getValue().toString();
                    int quyen = Integer.parseInt(UserSnap.child("quyen").getValue().toString());
                    String sodienthoai = UserSnap.child("soDienThoai").getValue().toString();
                    ten = UserSnap.child("ten").getValue().toString();
                    int tuoi = Integer.parseInt(UserSnap.child("tuoi").getValue().toString());
                    KhachHang khachHang = new KhachHang(userID, ten, tuoi, sodienthoai, quyen, email);

                    GlobalVariable.khachHangList.add(khachHang);
                }


                for (int i = 0; i < listDataHeader.size(); i++) {
                    List<String> listChild = new ArrayList<String>();

                    HashMap<String, Integer> listKhachHang = getListKhachHang(i);

                    for (HashMap.Entry<String, Integer> entry : listKhachHang.entrySet()) {
                        String tenKH = entry.getKey();
                        int soLuong = Integer.valueOf(entry.getValue());
                        Log.d("---------","aaaaaaaaaaaasoLuong"+soLuong);
                        String userInfo = tenKH + "|" + soLuong;
                        listChild.add(userInfo);
                    }

                    listDataChild.put(listDataHeader.get(i).getTenMonAn(), listChild);
                }
                CountKhachHang();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void CountTenMon() {
        Log.d("Deli-CPC", "listDataHeader: " + listDataHeader.size());
        listAdapter = new ThongKeAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    public void CountKhachHang() {
        for (int i = 0; i < GlobalVariable.khachHangList.size(); i++) {
            String t = GlobalVariable.khachHangList.get(i).getTen();
            Log.d("Deli-CPC", "size Khachhanglist: " + t);
        }
    }

    public HashMap<String, Integer> getListKhachHang(int pos) {
        HashMap<String, Integer> ret = new HashMap<>();


        ret.put(GlobalVariable.khachHangList.get(pos).getTen(), soLuong);

        return ret;
    }

    private void prepareListData() {
        listDataHeader.clear();
        listDataChild.clear();

        for (int i = 0; i < GlobalVariable.orderList.size(); i++) {
            Order order = GlobalVariable.orderList.get(i);

            String tenMonAn = order.getTenMonAn();
            int soLuong = order.getSoLuong();
            int gia = order.getGia() * soLuong;


            MonAn monAn = new MonAn();
            monAn.setTenMonAn(tenMonAn);
            monAn.setGia(Integer.valueOf(gia));
            monAn.setSoLuong(soLuong);

            listDataHeader.add(monAn);
        }


    }

}
