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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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

// firebase 아이디 중복 확인 추가 필요
public class UsersignupActivity extends AppCompatActivity {

    EditText et_signupid;
    EditText et_signuppwd;
    EditText et_signuppwdcheck;
    EditText et_signupname;
    EditText et_signupphonenum;

    TextView tv_signup;
    TextView tv_id;
    TextView tv_pw;
    TextView tv_pwcheck;
    TextView tv_name;
    TextView tv_number;
    TextView tv_agreement;

    Button btn_signupuseridcheck;
    Button btn_signupuserverify;
    Button btn_signupfinish;

    Switch sw_signupuseragreement;

    Toolbar toolbar;

    private boolean check_id = false;
    private boolean check_pw = false;
    private boolean check_name = false;
    private boolean check_number = false;
    private boolean check_userverify = false;

    private String userid;
    private String userpwd;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersignup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동조절

        setSupportActionBar(toolbar);


        // EditText filter 설정(ID, PW, PWcheck, name, phone number)
        et_signupid.setFilters(new InputFilter[]{new MainActivity.EngNumInputFilter(), new InputFilter.LengthFilter(16)});
        et_signuppwd.setFilters(new InputFilter[]{new MainActivity.EngNumInputFilter2(), new InputFilter.LengthFilter(16)});
        et_signuppwdcheck.setFilters(new InputFilter[]{new MainActivity.EngNumInputFilter2(), new InputFilter.LengthFilter(16)});
        et_signupname.setFilters(new InputFilter[]{new MainActivity.KorInputFilter(), new InputFilter.LengthFilter(16)});
        et_signupphonenum.setFilters(new InputFilter[]{new MainActivity.NumInputFilter(), new InputFilter.LengthFilter(16)});


        btn_signupuseridcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UsersignupActivity.this);
                userid = et_signupid.getText().toString().toLowerCase();
                // 수정필요(아이디 영어 + 숫자 확인)
                if (Pattern.matches("^[a-zA-Z][0-9a-zA-Z]*$", userid)) {
                    if (!userid.equals(null)) {
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
                    else if (userid.equals(null)) {
                        check_id = false;
                        Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    check_id = false;
                    Toast.makeText(getApplicationContext(), "영문(소문자만)을 포함해 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_signupuserverify.setOnClickListener(new View.OnClickListener() {     // 본인인증 화면으로 이동
            @Override
            public void onClick(View v) {
                if (check_name & check_number) {
                    check_userverify = true;
                    Toast.makeText(getApplicationContext(),"본인인증 성공", Toast.LENGTH_SHORT).show();
                } else {
                    check_userverify = false;
                    Toast.makeText(getApplicationContext(),"본인인증 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_signupfinish.setOnClickListener(new View.OnClickListener() {        // 회원가입 완료
            @Override
            public void onClick(View v) {
                // ID 중복확인, 본인인증, 개인정보 동의 사항 확인후 활성화
                if (!check_id) {
                    Toast.makeText(getApplicationContext(), "아이디 중복확인 해주세요", Toast.LENGTH_SHORT).show();
                } else if (!check_pw) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                } else if (!check_userverify) {
                    Toast.makeText(getApplicationContext(), "본인인증이 필요합니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    SignUp(); // firebase 연동 회원가입 함수
                }
            }
        });

        // 중복확인된 id 변경시 다시 중복확인 해야하게 flag 설정
        et_signupid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().toLowerCase().equals(userid)) {
                    check_id = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 8자 이상 pw 입력해야 비밀번호 확인 입력창 활성화
        et_signuppwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() < 8) {
                    et_signuppwdcheck.setEnabled(false);
                } else {
                    et_signuppwdcheck.setEnabled(true);
                }

                if(s.toString().equals(et_signuppwdcheck.getText().toString())) {
                    check_pw = true;
                    et_signuppwdcheck.setTextColor(Color.parseColor("#000000"));
                } else {
                    check_pw = false;
                    et_signuppwdcheck.setTextColor(Color.parseColor("#FF0000"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 비밀번호와 재확인이 같지 않을때 flag 설정 및 빨간 글씨 표시
        et_signuppwdcheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                userpwd = et_signuppwd.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(userpwd)) {
                    check_pw = true;
                    et_signuppwdcheck.setTextColor(Color.parseColor("#000000"));
                }
                else {
                    check_pw = false;
                    et_signuppwdcheck.setTextColor(Color.parseColor("#FF0000"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 이름 공백여부 flag 설정
        et_signupname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(null)) {
                    check_name = true;
                } else {
                    check_name = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 번호 공백여부 flag 설정
        et_signupphonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(null)) {
                    check_number = true;
                } else {
                    check_number = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 개인정보 동의 여부 flag 설정
        sw_signupuseragreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sw_signupuseragreement.setText("동의");
                    btn_signupfinish.setEnabled(true);
                }
                else{
                    sw_signupuseragreement.setText("비동의");
                    btn_signupfinish.setEnabled(false);
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    // firebase 연동 회원가입
    private void SignUp() {
        String email = ((EditText)findViewById(R.id.et_signupid)).getText().toString().toLowerCase() + "@vote.com";
        String password = ((EditText)findViewById(R.id.et_signuppwd)).getText().toString();
        String passwordcheck = ((EditText)findViewById(R.id.et_signuppwdcheck)).getText().toString();

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
//                                intent.putExtra("ID", et_signupid.getText().toString());
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

    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        et_signupid = findViewById(R.id.et_signupid);
        et_signuppwd = findViewById(R.id.et_signuppwd);
        et_signuppwdcheck = findViewById(R.id.et_signuppwdcheck);
        et_signupname = findViewById(R.id.et_signupname);
        et_signupphonenum = findViewById(R.id.et_signupphonenum);

        tv_signup = findViewById(R.id.tv_signup);
        tv_id = findViewById(R.id.tv_id);
        tv_pw = findViewById(R.id.tv_pw);
        tv_pwcheck = findViewById(R.id.tv_pwcheck);
        tv_name = findViewById(R.id.tv_name);
        tv_number = findViewById(R.id.tv_number);
        tv_agreement = findViewById(R.id.tv_agreement);

        btn_signupuseridcheck = findViewById(R.id.btn_signupuseridcheck);
        btn_signupuserverify = findViewById(R.id.btn_signupuserverify);
        btn_signupfinish = findViewById(R.id.btn_signupfinish);

        sw_signupuseragreement = findViewById(R.id.sw_signupuseragreement);
    }
    private void TextSizeSet() {
        tv_signup.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/12));
        tv_id.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_pw.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_pwcheck.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_name.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_number.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_agreement.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));

        et_signupid.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_signuppwd.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_signuppwdcheck.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_signupname.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_signupphonenum.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));

        btn_signupuseridcheck.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_signupuserverify.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_signupfinish.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));

        sw_signupuseragreement.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
    }
}