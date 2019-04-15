package com.example.sh.orderfood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sh.orderfood.model.GlobalVariable;
import com.example.sh.orderfood.model.KhachHang;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainDangNhapActivity extends AppCompatActivity implements TextWatcher,
        CompoundButton.OnCheckedChangeListener{

    private static final String TAG = "MainDangNhapActivity";
    private ImageView imgLogo;
    private EditText edtEmail, edtPass;
    CheckBox checkBox;
    private Button btnDangNhap, btnDangKy,btnLostPass;
    private LoginButton btn_FB_Login;
    private CallbackManager callbackManager;
    private FirebaseAuth authu;
    List<KhachHang> listKhachHang;
    FirebaseUser user;
    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String KEY_REMEMBER = "remember";
    private static final String PREF_PASSWORD = "Password";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        imgLogo = (ImageView) findViewById(R.id.imglogo);
        edtEmail = (EditText) findViewById(R.id.edt_username);
        edtPass = (EditText) findViewById(R.id.edt_password);
        btnDangNhap = (Button) findViewById(R.id.btn_login_email);
        btnDangKy = (Button) findViewById(R.id.btn_register);
        btnLostPass=(Button)findViewById(R.id.btn_lost_pass);
        btn_FB_Login=(LoginButton)findViewById(R.id.btn_fb_login);
        checkBox = findViewById(R.id.checkBox);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getBoolean(KEY_REMEMBER, false))
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);

        edtEmail.setText(sharedPreferences.getString(PREF_UNAME,""));
        edtPass.setText(sharedPreferences.getString(PREF_PASSWORD,""));

        edtEmail.addTextChangedListener((TextWatcher) this);
        edtPass.addTextChangedListener((TextWatcher) this);
        checkBox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);


        callbackManager= CallbackManager.Factory.create();
        btn_FB_Login.setReadPermissions("email", "public_profile");
        btn_FB_Login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });



    }

    protected void onResume() {

        super.onResume();
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainDangNhapActivity.this,MainDangKyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangNhap();
            }
        });
        btnLostPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDangNhapActivity.this, MainResetPassActivity.class);
                startActivity(intent);
            }
        });

        authu = FirebaseAuth.getInstance();
        if (authu!=null){

            SkipLogin();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        managePrefs();
    }

        private void managePrefs(){
        if(checkBox.isChecked()){
            editor.putString(PREF_UNAME, edtEmail.getText().toString().trim());
            editor.putString(PREF_PASSWORD, edtPass.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        }else{
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(PREF_UNAME);//editor.putString(KEY_PASS,"");
            editor.remove(PREF_PASSWORD);//editor.putString(KEY_USERNAME, "");
            editor.apply();
        }
    }
    private void DangNhap(){
        if (isNetworkAvailable(this))
        {
            String email = edtEmail.getText().toString();
            String pass = edtPass.getText().toString();

            if (TextUtils.isEmpty(email)){
                Toast.makeText(getApplicationContext(), "Email không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(pass)){
                Toast.makeText(getApplicationContext(), "Password không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            authu.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (checkIfEmailVerified()==true) {

                                        // Sign in success, update UI with the signed-in user's information
                                        Intent intent = new Intent(MainDangNhapActivity.this, MainHomeActivity.class);
                                        startActivity(intent);
                                        edtEmail.getText().clear();
                                        edtPass.getText().clear();
                                        user = authu.getCurrentUser();
                                        String Uid = user.getUid();
                                        GlobalVariable.currentKH = getThongTinKH(Uid);

                                }
                                else {
                                    Toast.makeText(MainDangNhapActivity.this, "Vui Lòng Xác Thực Email Trước Khi Đăng Nhập.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainDangNhapActivity.this, "Email hoặc mật khẩu không đúng.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            /*No internet so  Give a custom Toast / Alert dialogue and exit the application */
            Toast.makeText(this, "Kiểm tra kết nối internet của bạn", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleFacebookAccessToken(AccessToken accessToken){
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        authu.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "signInWithCredential:success");
                                Intent intent = new Intent(MainDangNhapActivity.this, MainActivityChiTiet.class);
                                startActivity(intent);
                                edtEmail.getText().clear();
                                edtPass.getText().clear();
                                user = authu.getCurrentUser();
                                String Uid = user.getUid();
                                GlobalVariable.currentKH = getThongTinKH(Uid);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainDangNhapActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private boolean checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            // user is verified, so you can finish this activity or send user to activity which you want.
/*            finish();
            Toast.makeText(MainDangNhapActivity.this, "Đăng Nhập Thành Công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainDangNhapActivity.this,MainHomeActivity.class);
            startActivity(intent);*/
            return true;
        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            //  Toast.makeText(MainDangNhapActivity.this, "Vui Lòng Xác Thực Email Trước Khi Đăng Nhập!", Toast.LENGTH_SHORT).show();
            //  FirebaseAuth.getInstance().signOut();
            return false;
            //restart this activity

        }
    }
    private KhachHang getThongTinKH(String uid) {
        Log.d("..........", "dddddddddddddd");
        KhachHang khachHang = new KhachHang();
//        khachHang.ID = uid;
//        khachHang.Ten = "haha";
        khachHang.setID(uid);
        khachHang.getTen();
        return khachHang;
    }

    public void SkipLogin() {
        if (isNetworkAvailable(this))
        {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            if (auth.getCurrentUser() != null) {
                // User is signed in (getCurrentUser() will be null if not signed in)
                Intent intent = new Intent(this, MainHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            Toast.makeText(this, "Kiểm tra kết nối internet của bạn", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    public static boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean isLoggedInFaceBook(AccessToken accessToken) {
         accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

}