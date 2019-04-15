package com.example.sh.orderfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sh.orderfood.model.InfoUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainDoiThongTin extends AppCompatActivity {
    private ListView listView;
    private Toolbar toolbar;
    private Button btn_DoiTT;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    final private static String TAG = "MainThongTinActivity";
    private DatabaseReference ref = database.getReference();
    ArrayList<String> array = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_doithongtin);
        listView = (ListView) findViewById(R.id.lvthongtin);
        btn_DoiTT = (Button) findViewById(R.id.btn_Doitt);

        btn_DoiTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDoiThongTin.this, MainThongTinActivity.class);
                startActivity(intent);
            }
        });

        toolbar=(Toolbar)findViewById(R.id.toolbar_dtt);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDoiThongTin.this, MainHomeActivity.class);
                startActivity(intent);
            }
        });



//}else {
//    String ten = "", sdt = "", ngaysinh = "", gioitinh = "";
//
//    inSertData(ten, sdt, ngaysinh, gioitinh);
//
//    array.add(ten);
//    array.add(sdt);
//    array.add(ngaysinh);
//    array.add(gioitinh);
//}
    }

    @Override
    protected void onResume() {
        super.onResume();

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    InfoUser infoUser = new InfoUser();
                    if (ds.child(userID).getValue() != null) {
                        Log.d(TAG, "du lieu: " + ds.child(userID).getValue());
                        infoUser.setTen(ds.child(userID).getValue(InfoUser.class).getTen());
                        infoUser.setSdt(ds.child(userID).getValue(InfoUser.class).getSdt());
                        infoUser.setNgaysinh(ds.child(userID).getValue(InfoUser.class).getNgaysinh());
                        infoUser.setGioitinh(ds.child(userID).getValue(InfoUser.class).getGioitinh());

                        array.add(infoUser.getTen());
                        array.add(infoUser.getGioitinh());
                        array.add(infoUser.getSdt());
                        array.add(infoUser.getNgaysinh());
                        listView.setAdapter(adapter);

                    } else {
                        String ten = "", sdt = "", ngaysinh = "", gioitinh = "";
                        array.add(ten);
                        array.add(sdt);
                        array.add(ngaysinh);
                        array.add(gioitinh);
                        listView.setAdapter(adapter);
                        inSertData(ten, sdt, ngaysinh, gioitinh);


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void inSertData(String ten, String sdt, String ngaysinh, String gioitinh) {

        DatabaseReference ref = database.getReference().child("info_user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        InfoUser account = new InfoUser(ten, sdt, ngaysinh, gioitinh);
        ref.setValue(account);


    }

}


