package com.example.sh.orderfood;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.sh.orderfood.fragment.FragmentChiTiet;
import com.example.sh.orderfood.fragment.FragmentDeXuat;
import com.example.sh.orderfood.fragment.FragmentNhanXetDanhGia;
import com.example.sh.orderfood.fragment.FragmentTongQuan;

import java.util.ArrayList;
import java.util.List;

public class MainActivityChiTiet extends AppCompatActivity {
    private ViewPager pager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_chitietmonan);


            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            pager= (ViewPager) findViewById(R.id.view_pager);


            setupViewPager(pager);
            tabLayout.setupWithViewPager(pager);
        toolbar=(Toolbar)findViewById(R.id.toolbar_ctma);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivityChiTiet.this, MainHomeActivity.class);
        startActivity(intent);
    }
});



    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentTongQuan(), "Tổng Quan");

        adapter.addFragment(new FragmentChiTiet(), "Chi Tiết");
        adapter.addFragment(new FragmentNhanXetDanhGia(), "Nhận Xét & Đánh Giá");
        adapter.addFragment(new FragmentDeXuat(), "Đề Xuất");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_chitietmonan, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
