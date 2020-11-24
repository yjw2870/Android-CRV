package com.example.snarkportingtest;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminloginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 777;

    TextView tv_adminlogin;

    EditText et_adminid;
    EditText et_adminpwd;

    Button btn_adminsignup;
    Button btn_adminlogin;

    Toolbar toolbar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);


        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동조절

        toolbar.setTitle("VoteApp - Admin");
        setSupportActionBar(toolbar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // EditText Filter 설정(ID, PW 입력창)
        et_adminid.setFilters(new InputFilter[]{new MainActivity.EngNumInputFilter(), new InputFilter.LengthFilter(16)});
        et_adminpwd.setFilters(new InputFilter[]{new MainActivity.EngNumInputFilter2(), new InputFilter.LengthFilter(16)});


        btn_adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_adminid.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                } else if(et_adminpwd.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                } else {
                    SignIn(et_adminid.getText().toString() + "@admin.com", et_adminpwd.getText().toString());
                }
            }
        });

        btn_adminsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminloginActivity.this, AdminsignupActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        et_adminid.setText(null);
        et_adminpwd.setText(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "관리자 회원가입 완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "관리자 회원가입 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGNIN", "signInWithEmail:success");
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(AdminloginActivity.this, AdminActivity.class);
                            String adminid = et_adminid.getText().toString().toLowerCase();
                            intent.putExtra("adminid", adminid);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SIGNIN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        tv_adminlogin = findViewById(R.id.tv_adminlogin);

        et_adminid = findViewById(R.id.et_adminid);
        et_adminpwd = findViewById(R.id.et_adminpwd);

        btn_adminsignup = findViewById(R.id.btn_adminsignup);
        btn_adminlogin = findViewById(R.id.btn_adminlogin);

    }
    private void TextSizeSet() {
        tv_adminlogin.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/12));
        et_adminid.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_adminpwd.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_adminsignup.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_adminlogin.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
    }
}