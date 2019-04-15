package com.example.sh.orderfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainResetPassActivity extends AppCompatActivity {
    private EditText edt_ResetEmail;
    private Button btn_ResetPass, btn_BackLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reset_pass);

        edt_ResetEmail=(EditText)findViewById(R.id.edt_resetemail);
        btn_ResetPass=(Button)findViewById(R.id.btn_resetpass);
        btn_BackLogin=(Button)findViewById(R.id.btn_backlogin2);
        auth= FirebaseAuth.getInstance();
        btn_BackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainResetPassActivity.this, MainDangNhapActivity.class);
                startActivity(intent);
            }
        });


        btn_ResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=edt_ResetEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Vui Lòng Nhập Email Đã Đăng Ký!", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainResetPassActivity.this, "Đã Gửi Yêu Cầu Đặt Lại Mật Khẩu, Vui Lòng Kiểm Tra Email!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainResetPassActivity.this, MainDangNhapActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainResetPassActivity.this, "Không Thể Gửi Yêu Cầu Đặt Lại!", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
            }
        });
    }
}
