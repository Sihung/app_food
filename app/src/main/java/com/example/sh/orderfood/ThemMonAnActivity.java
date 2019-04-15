package com.example.sh.orderfood;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sh.orderfood.model.GlobalVariable;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static com.example.sh.orderfood.model.GlobalVariable.database;
import static com.example.sh.orderfood.model.GlobalVariable.databaseReference;

public class ThemMonAnActivity extends AppCompatActivity {
    ImageButton btnCapture;
    ImageButton btnChoose;
    ImageView imgPicture;
    Bitmap selectedBitmap;
    EditText edtTenMon, edtGia;
    private Uri mImageUri = null;
    String image;
    private Uri filePath;
    String TAG="MainThemMonAnActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_mon_an);
        addControls();
        addEvents();
        GlobalVariable.storage = FirebaseStorage.getInstance();
        GlobalVariable.storageReference = GlobalVariable.storage.getReference();
    }

    public void addControls() {
        btnCapture = findViewById(R.id.btnCapture);
        btnChoose = findViewById(R.id.btnChoose);
        imgPicture = findViewById(R.id.imgPicture);
        edtTenMon = findViewById(R.id.edtTenMon);
        edtGia = findViewById(R.id.edtGia);
    }

    public void addEvents() {
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePicture();
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

    }

    //xử lý chọn hình
    private void choosePicture() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 200);//one can be replaced with any action code
    }

    //xử lý chụp hình
    private void capturePicture() {
        Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("--------------","ssssssssssssssss" + cInt);
        startActivityForResult(cInt, 100);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //xử lý lấy ảnh trực tiếp lúc chụp hình:
            selectedBitmap = (Bitmap) data.getExtras().get("data");
            Log.d("-------------","selectbitmap : " + selectedBitmap);
            imgPicture.setImageBitmap(selectedBitmap);
            mImageUri = data.getData();
            filePath = mImageUri;
            Log.d("------------------","selectbitmap : " + mImageUri);

        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            try {
                //xử lý lấy ảnh chọn từ điện thoại:
                Uri imageUri = data.getData();
                Log.d("CPC-Deli", "imageUri: " + imageUri);
                String getUri = getRealPathFromURI(this.getApplicationContext(), imageUri);
                Log.d("CPC-Deli", "getUri: " + getUri);
                filePath = imageUri;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void xuLyThemMoi(View view) {
        try {
            Log.d(TAG,"nen hinh"+filePath);
            mImageUri = null;
            //boolean uploadFinished = false;
            if (filePath != null) {
                Log.d("qqqqqqqqqqq","lllllllll");
                String imgName = "DanhMuc/" + UUID.randomUUID().toString();
                final StorageReference ref = GlobalVariable.storageReference.child(imgName);

                //Uri file = Uri.fromFile(new File("DanhMuc/"));
                UploadTask uploadTask = ref.putFile(filePath);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            mImageUri = task.getResult();

                            if (mImageUri != null) {
                                //Kết nối tới node có tên là Danh muc (node này do ta định nghĩa trong CSDL Firebase)
                                databaseReference = database.getReference().child("DanhMuc");
                                String ten = edtTenMon.getText().toString();
                                int gia = Integer.parseInt(edtGia.getText().toString());
//                                String NodeName = "Dm" + (GlobalVariable.MonAnCount + 1);
                                String NodeName = ten;
                                databaseReference.child(NodeName).child("tenMonAn").setValue(ten);
                                databaseReference.child(NodeName).child("gia").setValue(gia);
                                databaseReference.child(NodeName).child("imageUrl").setValue(mImageUri.toString());
                                Toast.makeText(ThemMonAnActivity.this, "Cap Nhat Thanh Cong", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(ThemMonAnActivity.this, "Cap Nhat That Bai", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

            finish();
        } catch (Exception ex) {
            Toast.makeText(this, "Error:" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

}
