package com.example.sh.orderfood;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sh.orderfood.model.Adapter;
import com.example.sh.orderfood.model.GlobalVariable;
import com.example.sh.orderfood.model.KhachHang;
import com.example.sh.orderfood.model.MonAn;
import com.example.sh.orderfood.model.Order;
import com.example.sh.orderfood.model.OrderAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.sh.orderfood.model.GlobalVariable.databaseReference;
import static com.example.sh.orderfood.model.GlobalVariable.database;

public class MainDatMonActivity extends AppCompatActivity implements View.OnClickListener {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    OrderAdapter orderAdapter;
    RecyclerView recyclerview;
    List<Order> dsOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dat_mon);


        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Orders");

        // đổ dữ liệu ra list view


    }


    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mYear1 = c.get(Calendar.YEAR);
            mMonth1 = c.get(Calendar.MONTH);
            mDay1 = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            txtTime.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            SoSanhNgay();
                        }
                    }, mYear1, mMonth1, mDay1);
            datePickerDialog.show();
        }

    }

    private boolean SoSanhNgay() {
        if ((mYear < mYear1) && (mMonth < mMonth1) && (mDay < mDay1)) {
            showAlertDialog();
        } else
            LoadData();
        return true;
    }

    private void LoadData() {
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                        dsOrder = new ArrayList<>();
                        // StringBuffer stringbuffer = new StringBuffer();
                        for (com.google.firebase.database.DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            Order order = dataSnapshot1.getValue(Order.class);
                            String name = order.getTenMonAn();
                            int gia = order.getGia();
                            int soluong = order.getSoLuong();
                            order.setTenMonAn(name);
                            order.setGia(gia);
                            order.setSoLuong(soluong);
                            dsOrder.add(order);

                        }
                        OrderAdapter recycler = new OrderAdapter(dsOrder);
                        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(MainDatMonActivity.this);
                        recyclerview.setLayoutManager(layoutmanager);
                        recyclerview.setItemAnimator(new DefaultItemAnimator());
                        recyclerview.setAdapter(recycler);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lịch Sử");
        builder.setMessage("Đến ngày không được nhỏ hơn từ ngày");
        builder.setCancelable(false);
        builder.setPositiveButton("OK Nha", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                txtTime.setText("");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}



