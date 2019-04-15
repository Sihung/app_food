package com.example.sh.orderfood;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sh.orderfood.model.Adapter;
import com.example.sh.orderfood.model.GlobalVariable;
import com.example.sh.orderfood.model.MonAn;
import com.example.sh.orderfood.model.PushNotifi;
import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ListView lvMon;
    List<MonAn> dsThucDon;
    Adapter adapterThucDon;
    TextView txtGia, txt_ThucDonTheoNgay, txtTenKhachhang;
    int id;
    Context context;
//    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Push();


        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);

        //đưa dữ liệu lên firebase
        lvMon = findViewById(R.id.lv_Mon);
        txtGia = findViewById(R.id.txtGia);
        txt_ThucDonTheoNgay = findViewById(R.id.txtThucDonTheoNgay);

        String ng = MainAddToCartActivity.XuLyNgay();
        txt_ThucDonTheoNgay.setText("Thực Đơn " + ng);
        Firebase myFirebaseRef = new Firebase("https://android-app-a9908.firebaseio.com/");

        // đổ dữ liệu ra list view
        dsThucDon = new ArrayList<>();
        adapterThucDon = new Adapter(MainHomeActivity.this, R.layout.items, dsThucDon);
        lvMon.setAdapter(adapterThucDon);

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

                        adapterThucDon.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        txtTenKhachhang = (TextView) header.findViewById(R.id.txt_Ten);

//        KhachHang khachHang = new KhachHang();
//        String ten = khachHang.Ten;
//        txtTenKhachhang.setText("Xin chào, " + GlobalVariable.currentKH.getTen());


        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lvmonAns.size() > 0) {
                    Bundle extra = new Bundle();
                    extra.putSerializable("objests", (Serializable) lvmonAns);
                    Intent intent = new Intent(MainHomeActivity.this, MainAddToCartActivity.class);
                    intent.putExtra("monans", extra);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.quanlymonan, menu);
        menuInflater.inflate(R.menu.quanlythongke, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.QuanLyMonAn) {
            //mở màn hình thêm ở đây
            Intent intent = new Intent(MainHomeActivity.this, MainQuanLyMonAnActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.QuanLyThongKe) {
            Intent intent = new Intent(MainHomeActivity.this, MainQuanLyThongKeActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Menu) {
            Intent intent = new Intent(MainHomeActivity.this, MainHomeActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_DatMon) {

        } else if (id == R.id.nav_LichSu) {
            Intent intent = new Intent(MainHomeActivity.this, MainLichSuActivity.class);
            startActivity(intent);
        } else if (id==R.id.nav_setting){
            Intent intent = new Intent(MainHomeActivity.this, MainDoiThongTin.class);
            startActivity(intent);
        } else if (id == R.id.nav_DangXuat) {

                LoginManager.getInstance().logOut();


            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(MainHomeActivity.this, MainDangNhapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public List<MonAn> lvmonAns = new ArrayList<>();

    public int indexOfMonAn(MonAn monAn) {
        for (int i = 0; i < lvmonAns.size(); i++) {
            if (monAn.getTenMonAn().equalsIgnoreCase(lvmonAns.get(i).getTenMonAn())) {
                return i;
            }
        }
        return -1;
    }

    public void XuLy(MonAn monAn) {
        Log.d("Deli-CPC", "Xu Ly Mon An");
        if (monAn != null) {
            int i = indexOfMonAn(monAn);
            if (i >= 0) // Mon An da ton tai
                lvmonAns.set(i, monAn);
            else
                lvmonAns.add(monAn);

        }
    }

    public void Push() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 35);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(MainHomeActivity.this, PushNotifi.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainHomeActivity.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainHomeActivity.this.getSystemService(MainHomeActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


}
