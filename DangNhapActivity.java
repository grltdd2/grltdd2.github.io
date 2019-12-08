package com.example.shoesshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cuahangsach.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DangNhapActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    EditText edtEmail,edtMatKhau;
    Button btnDangNhap,btnChuyenSangDangKi;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        addControls();
        addEvents();
        firebaseAuth=FirebaseAuth.getInstance();
    }

    private void addEvents() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nếu mật khẩu hoặc email chưa có hiển thị thông báo
                if (edtEmail.getText().toString().equals("") || edtMatKhau.getText().toString().equals("")) {
                    Toast.makeText(DangNhapActivity.this, "Điền đầy đủ thông tin!", Toast.LENGTH_LONG).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtMatKhau.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // nếu đăng nhập thành công
                            if (task.isSuccessful()) {
                                // truyền email sang màn hình MainActivity
                                Intent intentTruyenEmail = new Intent(DangNhapActivity.this, MainActivity.class);
                                intentTruyenEmail.putExtra("emailDangNhap", edtEmail.getText().toString());
                                startActivity(intentTruyenEmail);
                                Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(DangNhapActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });
        btnChuyenSangDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChuyenSangDangKi=new Intent(DangNhapActivity.this,DangKiActivity.class);
                startActivity(intentChuyenSangDangKi);
            }
        });
    }

    private void addControls() {
        edtEmail=(EditText)findViewById(R.id.edtEmail);
        edtMatKhau=(EditText)findViewById(R.id.edtMatKhau);
        btnDangNhap=(Button)findViewById(R.id.btnDangNhap);
        btnChuyenSangDangKi=(Button) findViewById(R.id.btnChuyenSangDangKi);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }
}
