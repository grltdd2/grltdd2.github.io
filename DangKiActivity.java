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

public class DangKiActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    EditText edtEmail,edtMatKhauLan1,edtMatKhauLan2;
    Button btnDangKi;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        addControls();
        addEvents();
        firebaseAuth=FirebaseAuth.getInstance();

    }

    private void addEvents() {
        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nếu email hoặc mật khẩu chưa có hiển thị thông báo
                if((edtEmail.getText().toString().equals(""))||(edtMatKhauLan1.getText().toString().equals(""))||(edtMatKhauLan2.getText().toString().equals(""))) {
                    Toast.makeText(DangKiActivity.this, "Điền đầy đủ thông tin!", Toast.LENGTH_LONG).show();
                }else {
                    //nếu mật khẩu lần 1,2 không  khớp hiển thị thông báo
                    if (edtMatKhauLan1.getText().toString().equals(edtMatKhauLan2.getText().toString())) {
                        Toast.makeText(DangKiActivity.this, "Mật khẩu khớp!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DangKiActivity.this, "Mật khẩu không khớp, nhập lại!", Toast.LENGTH_SHORT).show();
                    }
                    if (edtMatKhauLan1.getText().toString().equals(edtMatKhauLan2.getText().toString())) {
                        firebaseAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtMatKhauLan1.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //nếu dăng nhập thành công
                                if (task.isSuccessful()) {
                                    Intent intentChuyenVeDangNhap = new Intent(DangKiActivity.this, DangNhapActivity.class);
                                    startActivity(intentChuyenVeDangNhap);
                                    Toast.makeText(DangKiActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_LONG).show();
                                }else{
                                    // đăng nhập thất bại tại vì email đã tồn tại
                                    Toast.makeText(DangKiActivity.this, "Email đã tồn tại! ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
            }
        });
    }

    private void addControls() {
        edtEmail=(EditText)findViewById(R.id.edtEmail1);
        edtMatKhauLan1=(EditText)findViewById(R.id.edtMatKhau1);
        edtMatKhauLan2=(EditText)findViewById(R.id.edtMatKhau2);
        btnDangKi=(Button)findViewById(R.id.btnDangKi);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }
}
