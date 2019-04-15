package com.example.sh.orderfood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static com.example.sh.orderfood.model.GlobalVariable.databaseReference;
import com.example.sh.orderfood.model.KhachHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainDangKyActivity extends AppCompatActivity {

    private boolean mFinished = false;
    private static final String TAG = "haha";
    private EditText edtEmail, edtPass, edtNhapLaiPass;
    private Button btnDangKy,btnQuayLai,btnQuenMatKhau;

    private FirebaseAuth authu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dang_ky);
        authu=FirebaseAuth.getInstance();
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPass = (EditText) findViewById(R.id.edt_password);
        edtNhapLaiPass=(EditText)findViewById(R.id.edt_nhaplaipassword);


        btnDangKy = (Button) findViewById(R.id.btn_register_email);
        btnQuayLai=(Button) findViewById(R.id.btn_back_login);
        btnQuenMatKhau=(Button) findViewById(R.id.btn_reset_password);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangKy();
            }
        });

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainDangKyActivity.this,MainDangNhapActivity.class);
                startActivity(intent);
                onStop();
            }
        });
        btnQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDangKyActivity.this, MainResetPassActivity.class);
                startActivity(intent);
            }
        });
    }

    private void DangKy() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final String email = edtEmail.getText().toString().trim();
        final String password = edtPass.getText().toString().trim();
        String nhaplaipass =edtNhapLaiPass.getText().toString().trim();

        /*  For string.isEmpty(), a null string value will throw a NullPointerException
            TextUtils will always return a boolean value.
        */
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Email không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Password không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password quá ngắn, nhập ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(nhaplaipass)) {
            Toast.makeText(getApplicationContext(), "Nhập lại password không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        if (password.equals(nhaplaipass)==false) {
            Toast.makeText(getApplicationContext(), "Password nhập lại sai!", Toast.LENGTH_SHORT).show();
            return;
        }

        authu.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainDangKyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            final FirebaseUser userGM = FirebaseAuth.getInstance().getCurrentUser();
                            //Gửi 1 email xác thực tài khoản
                            userGM.sendEmailVerification().addOnCompleteListener(MainDangKyActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    Toast.makeText(MainDangKyActivity.this, "Đăng Ký Thành Công, Vui Lòng Xác Email Thực Trước Khi Đăng Nhập.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainDangKyActivity.this,MainDangNhapActivity.class);
                                    startActivity(intent);
                                    finish();
                                    onStop();



                                }
                            });

                        }else {

                            //Log.e(TAG, "onComplete: Failed=" + task.getException().getMessage());
                            Toast.makeText(MainDangKyActivity.this, "Đăng Ký Thất Bại, Vui Lòng Nhập Lại Email.", Toast.LENGTH_SHORT).show();

                            //ThemData();
                        }

                    }

                });
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseAuth.getInstance().signOut();
    }

    private boolean isNumeric(String string) {
        try {
            int amount = Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
