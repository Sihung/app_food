package com.example.sh.orderfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.sh.orderfood.model.Adapter;
import com.example.sh.orderfood.model.CartAdapter;
import com.example.sh.orderfood.model.*;
import com.example.sh.orderfood.model.MonAn;
import com.example.sh.orderfood.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.example.sh.orderfood.model.GlobalVariable.databaseReference;
import static com.example.sh.orderfood.model.GlobalVariable.database;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainAddToCartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseUser user;
    private FirebaseAuth authu;
    TextView txtGia;
    Button btnXacNhan;
    List<MonAn> cart = new ArrayList<>();
    CartAdapter cartAdapter;
    ImageView imageView;
    List<Order> listOrder = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_to_cart);
//        databaseReference = database.getReference("Orders");
        databaseReference = database.getReference("OrdersTest");
        recyclerView = findViewById(R.id.ListCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtGia = findViewById(R.id.txtGia);
        btnXacNhan = findViewById(R.id.btn_XacNhan);


        LoadGioHang();
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XacNhan();
            }
        });
    }

    public static String XuLyNgay() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dt = dateFormat.format(date);
        Log.d("---------------", "date: " + dt);
        return dt;
    }

    private void XacNhan() {
        String yyyymmdd = XuLyNgay();

        authu = FirebaseAuth.getInstance();
        user = authu.getCurrentUser();
        String Uid = user.getUid();
        String id_user = Uid;
//        String id_user = GlobalVariable.currentKH.ID;
        for (int i = 0; i < cart.size(); i++) {
            String tenmon = cart.get(i).getTenMonAn();
            Log.d("----------","000000"+ tenmon);
            int soluong = cart.get(i).soLuong;
            int gia = cart.get(i).getGia();

            Bundle extra = new Bundle();
            extra.putSerializable("xacnhan", (Serializable) cart);
            Order od = new Order(soluong,gia,tenmon,id_user);
            // push data into firebase
//            databaseReference.child(yyyymmdd).child(id_user).child(tenmon).setValue(od);
            databaseReference.child(yyyymmdd).child(tenmon).child(id_user).setValue(od);

        }
        finish();
    }

    private void LoadGioHang() {
        Bundle extra = getIntent().getBundleExtra("monans");
        ArrayList<MonAn> objects = (ArrayList<MonAn>) extra.getSerializable("objests");
        cart = objects;
        cartAdapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(cartAdapter);

        //dem tong so gia
        int tong = 0;
        for (MonAn mn : objects) {
            tong += mn.tongtien;
            Log.d("----------", "Tong tien: " + tong);
        }
        txtGia.setText(tong + " VND");
    }

}
