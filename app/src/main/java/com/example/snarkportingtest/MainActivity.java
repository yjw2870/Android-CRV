package com.example.snarkportingtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

//import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static Context context_main;
    public int standardSize_X, standardSize_Y;
    public float density;

    Button btn_login;
    Button btn_signup;
    Button btn_admin;

    TextView tv_login;

    EditText et_id;
    EditText et_pwd;

    String str;

    Toolbar toolbar;

    private static final int REQUEST_CODE = 777;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        getStanderSize();
        context_main = this;

        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동조절

        setSupportActionBar(toolbar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // EditText Filter 설정(ID, PW 입력창)
        et_id.setFilters(new InputFilter[]{new EngNumInputFilter(), new InputFilter.LengthFilter(16)});
        et_pwd.setFilters(new InputFilter[]{new EngNumInputFilter2(), new InputFilter.LengthFilter(16)});

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_id.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                } else if(et_pwd.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                } else {
                    SignIn(et_id.getText().toString() + "@vote.com", et_pwd.getText().toString());
                }

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UsersignupActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminloginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        et_id.setText(null);
        et_pwd.setText(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // EditText Filter 설정(영어+숫자, 영어+숫자+특수문자, 한글, 숫자)
    protected static class EngNumInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]*$");

            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    }
    protected static class EngNumInputFilter2 implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()]*$");

            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    }
    protected static class KorInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[ㄱ-ㅎ가-힣]*$");

            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    }
    protected static class NumInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[0-9]*$");

            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
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

                            Intent intent = new Intent(MainActivity.this, UsermainActivity.class);
                            str = et_id.getText().toString();
                            intent.putExtra("str", str);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SIGNIN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }
    public void getStanderSize() {
        Point ScreenSize = getScreenSize(this);
        density  = getResources().getDisplayMetrics().density;

        standardSize_X = (int) (ScreenSize.x / density);
        standardSize_Y = (int) (ScreenSize.y / density);
    }

    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        btn_login = findViewById(R.id.btn_login);
        btn_admin = findViewById(R.id.btn_admin);
        btn_signup = findViewById(R.id.btn_signup);

        tv_login= findViewById(R.id.tv_login);

        et_id = findViewById(R.id.et_id);
        et_pwd = findViewById(R.id.et_pwd);

    }
    private void TextSizeSet() {
        tv_login.setTextSize((float) (standardSize_X/12));
        et_id.setTextSize((float) (standardSize_X/20));
        et_pwd.setTextSize((float) (standardSize_X/20));

        btn_login.setTextSize((float) (standardSize_X/20));
        btn_signup.setTextSize((float) (standardSize_X/20));
        btn_admin.setTextSize((float) (standardSize_X/20));
    }

}