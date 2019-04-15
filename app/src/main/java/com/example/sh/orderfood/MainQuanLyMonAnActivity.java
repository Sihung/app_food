package com.example.sh.orderfood;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sh.orderfood.model.Adapter;
import com.example.sh.orderfood.model.AdapterQuanLyMonAn;
import com.example.sh.orderfood.model.GlobalVariable;
import com.example.sh.orderfood.model.MonAn;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainQuanLyMonAnActivity extends AppCompatActivity {

    TextView txtQuanLyMonAn;
    FloatingActionButton actionButton;
    ListView lvQuanLyMon;
    List<MonAn> dsThucDon;
    AdapterQuanLyMonAn adapterQuanLyMonAn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quan_ly_mon_an);

        lvQuanLyMon = findViewById(R.id.lv_QuanLyMenu);
        txtQuanLyMonAn = findViewById(R.id.txtQuanLyMonAn);

        actionButton = findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(MainQuanLyMonAnActivity.this, ThemMonAnActivity.class);
                    startActivity(intent);
                }

        });

        Firebase myFirebaseRef = new Firebase("https://android-app-a9908.firebaseio.com/");

        // đổ dữ liệu ra list view
        dsThucDon = new ArrayList<>();
        adapterQuanLyMonAn = new AdapterQuanLyMonAn(MainQuanLyMonAnActivity.this, R.layout.itemquanlymon, dsThucDon);
        lvQuanLyMon.setAdapter(adapterQuanLyMonAn);

        myFirebaseRef.child("DanhMuc").addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GlobalVariable.MonAnCount = (int) dataSnapshot.getChildrenCount();
                dsThucDon.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    try {
                        String tenMonAn = snapshot.child("tenMonAn").getValue().toString();
                        String imageUrl = snapshot.child("imageUrl").getValue().toString();
                        int gia = Integer.parseInt(snapshot.child("gia").getValue().toString());
                        Log.d("CPC-Deli", "imageUrl: " + imageUrl);
                        dsThucDon.add(new MonAn(tenMonAn, imageUrl, gia));
                        Toast.makeText(MainQuanLyMonAnActivity.this, "Them Thanh Cong", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainQuanLyMonAnActivity.this, "Them That Bai", Toast.LENGTH_SHORT).show();
                    }
                    adapterQuanLyMonAn.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}
