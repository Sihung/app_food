package com.example.sh.orderfood;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sh.orderfood.model.InfoUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainThongTinActivity extends AppCompatActivity {
    final private static String TAG = "MainThongTinActivity";
    private EditText edt_ten, edt_sdt;
    private TextView tv_namsinh;
    private Button btn_saveinfo;
    private RadioGroup radioGroupCharacter;
    private String gioitinh;
    private RadioButton rdb_nam, rdb_nu;
    private Toolbar toolbar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_thongtin);

        edt_ten = (EditText) findViewById(R.id.edt_nhapten);
        edt_sdt = (EditText) findViewById(R.id.edt_sdt);
        tv_namsinh = (TextView) findViewById(R.id.tv_namsinh);
        btn_saveinfo = (Button) findViewById(R.id.btn_save_info);
        radioGroupCharacter = (RadioGroup) findViewById(R.id.rdb_group);
        rdb_nam = (RadioButton) findViewById(R.id.rdb_nam);
        rdb_nu = (RadioButton) findViewById(R.id.rdb_nu);
        database = FirebaseDatabase.getInstance();

        initPreferences();

        tv_namsinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(MainThongTinActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: Date onCreate " + dayOfMonth + " / " + month + " / " + year);
                String date = dayOfMonth + " / " + month + " / " + year;
                tv_namsinh.setText(date);
            }
        };
        toolbar=(Toolbar)findViewById(R.id.toolbar_tt);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainThongTinActivity.this, MainDoiThongTin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


//        String ten = sharedPreferences.getString("ten", "");
//        edt_ten.setText(ten);
//        String sdt = sharedPreferences.getString("sdt", "");
//        edt_sdt.setText(sdt);
//        String ngay = sharedPreferences.getString("ngaysinh", "");
//        tv_namsinh.setText(ngay);
        String g = sharedPreferences.getString("gioitinh", "");
        if (!TextUtils.isEmpty(g)) {
            setRadioButtonState(g);

        }
        btn_saveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ktNhapThongTin();

            }
        });

        radioGroupCharacter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_nam:
                        gioitinh = "Nam";
                        break;
                    case R.id.rdb_nu:
                        gioitinh = "Nu";
                        break;
                }
            }
        });

    }


    private void initPreferences() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.d(TAG, "gioi tinh " + gioitinh);
//        String ten = edt_ten.getText().toString();
//        editor.putString("ten", ten);
//        editor.commit();
//        String sdt = edt_sdt.getText().toString();
//        editor.putString("sdt", sdt);
//        editor.commit();
//        String gt = gioitinh;
//        editor.putString("gioitinh", gt);
//        editor.commit();
//        String ngay = tv_namsinh.getText().toString();
//        editor.putString("ngaysinh", ngay);
//        editor.commit();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setRadioButtonState(String g) {
        if(!TextUtils.isEmpty(g)){
            if (g.equals("Nam")) {
            rdb_nam.setChecked(true);
            gioitinh = "Nam";
            }
            if (g.equals("Nu")) {
            rdb_nu.setChecked(true);
            gioitinh = "Nu";
            }
        }else gioitinh="";
    }

    private void ktNhapThongTin() {
        String ten = edt_ten.getText().toString();
        String sdt = edt_sdt.getText().toString().trim();
        if (TextUtils.isEmpty(ten)) {
            Toast.makeText(getApplicationContext(), "Họ Và Tên không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (ten.length() > 30) {
                Toast.makeText(getApplicationContext(), "Tên quá dài, xin vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (timKyTu(ten)) {
                Toast.makeText(getApplicationContext(), "Tên Không được nhập ký tự đặc biệt, xin vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        if (TextUtils.isEmpty(sdt)) {
            Toast.makeText(getApplicationContext(), "SĐT không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (sdt.length() > 11 || sdt.length() < 9) {
                Toast.makeText(getApplicationContext(), "SĐT không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        inSertData(edt_ten.getText().toString(), edt_sdt.getText().toString(), tv_namsinh.getText().toString(), gioitinh);
        Intent intent = new Intent(MainThongTinActivity.this, MainDoiThongTin.class);
        startActivity(intent);


    }

    protected void inSertData(String ten, String sdt, String ngaysinh, String gioitinh) {

        DatabaseReference ref = database.getReference().child("info_user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        InfoUser account = new InfoUser(ten, sdt, ngaysinh, gioitinh);
        ref.setValue(account);


    }


    public boolean timKyTu(String s) {


        Pattern p = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher m = p.matcher(s);
        boolean b = m.find();
        if (b == true)
            return true;
        else
            return false;
    }

}
