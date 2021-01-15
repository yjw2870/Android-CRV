package com.example.snarkportingtest;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreatevoteActivity extends AppCompatActivity implements AdminCandidateAdapter.OnItemClickListener{

    private static final int REQUEST_CODE = 777;

    Toolbar toolbar;

    TextView tv_createvote;
    TextView tv_votetitle;
    TextView tv_voteterm;
    TextView tv_voteuser;
    TextView tv_votetype;
    TextView tv_candidatelist;

    EditText et_createvotetitle;
    EditText et_createvotestartdate;
    EditText et_createvoteenddate;
    EditText et_createvotenote;

    Switch sw_createvoteperiod;

    Button btn_admincreatevotefileupload;
    Button btn_addcandidate;
    Button btn_createvotecancle;
    Button btn_createvotefinish;

    RadioGroup rg_admincreatevotecategoty;
    RadioButton rb_admincreatevoteexponent, rb_admincreatevotepreference, rb_admincreatevoteagreement;

    //ListView lv_candidate;

    private RecyclerView rv_createvotecandidatelist;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Candidate> candidates;


    Date now;
    int today_y,today_m,today_d;
    int s_y,s_m,s_d;
    int e_y,e_m,e_d;

    private boolean title, startdate, enddate, voterlist, note;
    private int candidatenumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createvote);

        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동조절

        toolbar.setTitle("VoteApp - Admin");
        setSupportActionBar(toolbar);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String adminid = currentUser.getEmail().split("@")[0];

        rv_createvotecandidatelist.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        rv_createvotecandidatelist.setLayoutManager(layoutManager);
        candidates = new ArrayList<>(); // Candidate 객체를 담을 어레이 리스트(어댑터 쪽으로)
        candidates.clear(); // 기존 배열 초기화

        adapter = new AdminCandidateAdapter(candidates, this, this);
        rv_createvotecandidatelist.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        rv_createvotecandidatelist.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));  // 투표목록 구분선


        // 기간 입력 설정
        et_createvotestartdate.setKeyListener(null);    // 키입력 막기
        et_createvoteenddate.setKeyListener(null);      // 키입력 막기
        et_createvotestartdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DatePickerDialog dialog = new DatePickerDialog(CreatevoteActivity.this, startlistener, s_y,s_m-1,s_d);
                    dialog.show();
                }
            }
        });
        et_createvotestartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(CreatevoteActivity.this, startlistener, s_y,s_m-1,s_d);
                dialog.show();
            }
        });

        et_createvoteenddate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DatePickerDialog dialog = new DatePickerDialog(CreatevoteActivity.this, endlistener, e_y,e_m-1,e_d);
                    dialog.show();
                }
            }
        });
        et_createvoteenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(CreatevoteActivity.this, endlistener, e_y,e_m-1,e_d);
                dialog.show();
            }
        });

        sw_createvoteperiod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_createvotestartdate.setEnabled(false);
                    et_createvoteenddate.setEnabled(false);
                    sw_createvoteperiod.setText("수동");
                } else {
                    et_createvotestartdate.setEnabled(true);
                    et_createvoteenddate.setEnabled(true);
                    sw_createvoteperiod.setText("자동");
                }
            }
        });



        // 라디오 버튼 설정
        rg_admincreatevotecategoty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_admincreatevoteexponent){
                    // 대표자 선출 선택시
                    tv_candidatelist.setVisibility(View.VISIBLE);
                    btn_addcandidate.setVisibility(View.VISIBLE);
                    tv_candidatelist.setText("후보자 목록");
                    btn_addcandidate.setText("후보자 추가");
                }
                else if(checkedId == R.id.rb_admincreatevotepreference){
                    // 선호도 선택시
                    tv_candidatelist.setVisibility(View.VISIBLE);
                    btn_addcandidate.setVisibility(View.VISIBLE);
                    tv_candidatelist.setText("선호도 조사 목록");
                    btn_addcandidate.setText("선호도 목록 추가");
                }
                else if(checkedId == R.id.rb_admincreatevoteagreement){
                    // 찬반투표 선택시
                    tv_candidatelist.setVisibility(View.INVISIBLE);
                    btn_addcandidate.setVisibility(View.INVISIBLE);
                }
            }
        });

        // 리스트뷰 설정 (후보자)

        btn_addcandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatevoteActivity.this, AddcandidateActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // 취소 버튼 설정
        btn_createvotecancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreatevoteActivity.this);
                // diglog box 띄우기
                builder.setTitle("투표 생성").setMessage("투표 생성을 취소하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "투표 생성 취소", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        // 투표 생성 버튼 설정 (TAG_ADMIN_SDK)
        btn_createvotefinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreatevoteActivity.this);
                if(et_createvotetitle.getText().toString().trim().getBytes().length == 0) {     // 제목 공백 확인
                    builder.setTitle("투표생성").setMessage("제목을 입력하세요").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            et_createvotetitle.requestFocus();
                        }
                    }).setCancelable(false);
                } else if(!sw_createvoteperiod.isChecked() && (!startdate || !enddate)) {       // 기간 설정 확인
                    builder.setTitle("투표생성").setMessage("기간을 입력하세요").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            et_createvotestartdate.requestFocus();
                        }
                    }).setCancelable(false);
                } else if(et_createvotenote.getText().toString().trim().getBytes().length == 0) {   // 설명 공백 확인
                    builder.setTitle("투표생성").setMessage("설명을 입력하세요").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            et_createvotenote.requestFocus();
                        }
                    }).setCancelable(false);
                } else if(false) {      // 유권자 등록 확인

                } else if(candidatenumber == 0) {   // 후보자 등록 확인
                    builder.setTitle("투표생성").setMessage("후보자를 입력하세요").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            btn_addcandidate.performClick();
                        }
                    }).setCancelable(false);
                } else {
                    // diglog box 띄우기
                    builder.setTitle("투표 생성").setMessage("투표 생성 하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "투표 생성 완료", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setCancelable(false);
                }
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    private DatePickerDialog.OnDateSetListener startlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            s_y=year;
            s_m=month+1;
            s_d=dayOfMonth;
            String date = s_y+"."+s_m+"."+s_d;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            Date s_date = null;
            Date today = null;
            try {
                s_date = dateFormat.parse(String.valueOf(date));
                today = dateFormat.parse(dateFormat.format(now));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int compare = today.compareTo(s_date);
            if(compare >= 0){
                Toast.makeText(getApplicationContext(),"오늘 이후 날짜를 선택하세요",Toast.LENGTH_SHORT).show();
                s_y = today_y;
                s_m = today_m;
                s_d = today_d;
                et_createvotestartdate.setText(null);
                startdate = false;
            } else {
                startdate = true;
                et_createvotestartdate.setText(date);
            }
        }
    };
    private DatePickerDialog.OnDateSetListener endlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            e_y=year;
            e_m=month+1;
            e_d=dayOfMonth;
            String date = e_y+"."+e_m+"."+e_d;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            Date s_date = null;
            Date e_date = null;
            try {
                s_date = dateFormat.parse(et_createvotestartdate.getText().toString());
                e_date = dateFormat.parse(String.valueOf(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int compare = s_date.compareTo(e_date);
            if(compare >= 0){
                Toast.makeText(getApplicationContext(),"시작 이후 날짜를 선택하세요",Toast.LENGTH_SHORT).show();
                e_y = today_y;
                e_m = today_m;
                e_d = today_d;
                et_createvoteenddate.setText(null);
                enddate = false;
            } else {
                enddate = true;
                et_createvoteenddate.setText(date);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthformat = new SimpleDateFormat("MM");
        SimpleDateFormat dayformat = new SimpleDateFormat("dd");

        now = new Date();

        today_y = Integer.parseInt(yearformat.format(now));
        today_m = Integer.parseInt(monthformat.format(now));
        today_d = Integer.parseInt(dayformat.format(now));
        s_y = today_y;
        s_m = today_m;
        s_d = today_d;
        e_y = today_y;
        e_m = today_m;
        e_d = today_d;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        btn_createvotecancle.performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                candidatenumber++;
                String name = String.valueOf(data.getExtras().getString("name"));
                String group = String.valueOf(data.getExtras().getString("group"));
                String note = String.valueOf(data.getExtras().getString("note"));
                Log.d("TAG1", "onActivityResult: "+name+group+note);
                Candidate candidate = new Candidate(null,name, group, note);
                candidates.add(candidate);

                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }
        }
    }

    @Override
    public void onItemClick(int position) {

    }


    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        tv_createvote = findViewById(R.id.tv_createvote);
        tv_votetitle = findViewById(R.id.tv_votetitle);
        tv_voteterm = findViewById(R.id.tv_voteterm);
        tv_voteuser = findViewById(R.id.tv_voteuser);
        tv_votetype = findViewById(R.id.tv_votetype);
        tv_candidatelist = findViewById(R.id.tv_candidatelist);

        et_createvotetitle = findViewById(R.id.et_createvotetitle);
        et_createvotestartdate = findViewById(R.id.et_createvotestartdate);
        et_createvoteenddate = findViewById(R.id.et_createvoteenddate);
        et_createvotenote = findViewById(R.id.et_createvotenote);

        sw_createvoteperiod = findViewById(R.id.sw_createvoteperiod);

        btn_admincreatevotefileupload = findViewById(R.id.btn_admincreatevotefileupload);
        btn_addcandidate = findViewById(R.id.btn_addcandidate);
        btn_createvotecancle = findViewById(R.id.btn_createvotecancle);
        btn_createvotefinish = findViewById(R.id.btn_createvotefinish);

        rg_admincreatevotecategoty = findViewById(R.id.rg_admincreatevotecategoty);
        rb_admincreatevoteexponent = findViewById(R.id.rb_admincreatevoteexponent);
        rb_admincreatevotepreference = findViewById(R.id.rb_admincreatevotepreference);
        rb_admincreatevoteagreement = findViewById(R.id.rb_admincreatevoteagreement);

        rv_createvotecandidatelist = findViewById(R.id.rv_createvotecandidatelist); // 아이디 연결
        //lv_candidate = findViewById(R.id.lv_candidate);
    }
    private void TextSizeSet() {
        tv_createvote.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/12));
        tv_votetitle.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_voteterm.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_voteuser.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        tv_votetype.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_candidatelist.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_createvotetitle.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_createvotestartdate.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_createvoteenddate.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_createvotenote.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        sw_createvoteperiod.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/25));
        btn_admincreatevotefileupload.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_addcandidate.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_createvotecancle.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_createvotefinish.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        rb_admincreatevoteexponent.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        rb_admincreatevotepreference.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        rb_admincreatevoteagreement.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
    }

}