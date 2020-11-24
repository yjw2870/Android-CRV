package com.example.snarkportingtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class AdminsignupActivity extends AppCompatActivity {

    TextView tv_adminsignup;
    TextView tv_id;
    TextView tv_pw;
    TextView tv_pwcheck;

    EditText et_adminsignupid;
    EditText et_adminsignuppwd;
    EditText et_adminsignuppwdcheck;

    Button btn_adminsignupuseridcheck;
    Button btn_adminsignupfinish;

    Toolbar toolbar;

    private boolean check_id = false;
    private boolean check_pw = false;
    private boolean check_groupname = false;

    private String adminid;
    private String adminpwd;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminsignup);


        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동조절

        toolbar.setTitle("VoteApp - Admin");
        setSupportActionBar(toolbar);

        // EditText filter 설정(ID, PW, PWcheck, name, phone number)
        et_adminsignupid.setFilters(new InputFilter[]{new MainActivity.EngNumInputFilter(), new InputFilter.LengthFilter(16)});
        et_adminsignuppwd.setFilters(new InputFilter[]{new MainActivity.EngNumInputFilter2(), new InputFilter.LengthFilter(16)});
        et_adminsignuppwdcheck.setFilters(new InputFilter[]{new MainActivity.EngNumInputFilter2(), new InputFilter.LengthFilter(16)});

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btn_adminsignupuseridcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminsignupActivity.this);
                adminid = et_adminsignupid.getText().toString().toLowerCase();
                // 수정필요(아이디 영어 + 숫자 확인)
                if (Pattern.matches("^[a-zA-Z][0-9a-zA-Z]*$", adminid)) {
                    if (!adminid.equals(null)) {
                        // DB 아이디와 중복체크 필요, racecondition 체크 (TAG_ADMIN_SDK)


                        // diglog box 띄우기
                        builder.setTitle("중복확인").setMessage("사용가능한 아이디입니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                check_id = true;
                                Toast.makeText(getApplicationContext(), "중복확인 완료", Toast.LENGTH_SHORT).show();
                            }
                        }).setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else if (adminid.equals(null)) {
                        check_id = false;
                        Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    check_id = false;
                    Toast.makeText(getApplicationContext(), "영문(소문자만)을 포함해 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_adminsignupfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ID 중복확인, 본인인증, 개인정보 동의 사항 확인후 활성화
                if (!check_id) {
                    Toast.makeText(getApplicationContext(), "아이디 중복확인 해주세요", Toast.LENGTH_SHORT).show();
                } else if (!check_pw) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    SignUp(); // firebase 연동 회원가입 함수
                }
            }
        });

        // 중복확인된 id 변경시 다시 중복확인 해야하게 flag 설정
        et_adminsignupid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().toLowerCase().equals(adminid)) {
                    check_id = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 8자 이상 pw 입력해야 비밀번호 확인 입력창 활성화
        et_adminsignuppwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() < 8) {
                    et_adminsignuppwdcheck.setEnabled(false);
                } else {
                    et_adminsignuppwdcheck.setEnabled(true);
                }

                if(s.toString().equals(et_adminsignuppwdcheck.getText().toString())) {
                    check_pw = true;
                    et_adminsignuppwdcheck.setTextColor(Color.parseColor("#000000"));
                } else {
                    check_pw = false;
                    et_adminsignuppwdcheck.setTextColor(Color.parseColor("#FF0000"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 비밀번호와 재확인이 같지 않을때 flag 설정 및 빨간 글씨 표시
        et_adminsignuppwdcheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adminpwd = et_adminsignuppwd.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(adminpwd)) {
                    check_pw = true;
                    et_adminsignuppwdcheck.setTextColor(Color.parseColor("#000000"));
                }
                else {
                    check_pw = false;
                    et_adminsignuppwdcheck.setTextColor(Color.parseColor("#FF0000"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // firebase 연동 회원가입
    private void SignUp() {
        String email = ((EditText)findViewById(R.id.et_adminsignupid)).getText().toString().toLowerCase() + "@admin.com";
        String password = ((EditText)findViewById(R.id.et_adminsignuppwd)).getText().toString();
        String passwordcheck = ((EditText)findViewById(R.id.et_adminsignuppwdcheck)).getText().toString();

        if (password.equals(passwordcheck)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            Intent intent = new Intent();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SignUp", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SignUp", "createUserWithEmail:failure", task.getException());
                                setResult(RESULT_CANCELED, intent);
                                finish();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show(); // 이 토스트 박스가 나오면 이상한것!!(절대 안나오는게 정상)
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        tv_adminsignup = findViewById(R.id.tv_adminsignup);
        tv_id = findViewById(R.id.tv_id);
        tv_pw = findViewById(R.id.tv_pw);
        tv_pwcheck = findViewById(R.id.tv_pwcheck);

        et_adminsignupid = findViewById(R.id.et_adminsignupid);
        et_adminsignuppwd = findViewById(R.id.et_adminsignuppwd);
        et_adminsignuppwdcheck = findViewById(R.id.et_adminsignuppwdcheck);

        btn_adminsignupuseridcheck = findViewById(R.id.btn_adminsignupuseridcheck);
        btn_adminsignupfinish = findViewById(R.id.btn_adminsignupfinish);
    }
    private void TextSizeSet() {
        tv_adminsignup.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/12));
        tv_id.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_pw.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_pwcheck.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        et_adminsignupid.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_adminsignuppwd.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_adminsignuppwdcheck.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_adminsignupuseridcheck.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_adminsignupfinish.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
    }
}