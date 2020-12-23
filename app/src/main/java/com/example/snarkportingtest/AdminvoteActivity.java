package com.example.snarkportingtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminvoteActivity extends AppCompatActivity implements IngCandidateAdapter.OnItemClickListener {

    TextView tv_votetitle;
    TextView tv_votestart;
    TextView tv_votehypen;
    TextView tv_voteend;
    TextView tv_votetype;
    TextView tv_votenote;

    Button btn_votecandidateinfo;
    Button btn_voteback;
    Button btn_votemodifycomplete;

    String voted;

    Toolbar toolbar;

    Date now;
    int today_y,today_m,today_d;
    int s_y,s_m,s_d;
    int e_y,e_m,e_d;

    private RecyclerView rv_candidatelist;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Candidate> candidates;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminvote);


        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동 조절

        setSupportActionBar(toolbar);

        Intent getintent = getIntent();
        Votedetail votedetail = (Votedetail) getintent.getExtras().get("vote");

        final String vote_title = votedetail.getTitle();

        rv_candidatelist.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        rv_candidatelist.setLayoutManager(layoutManager);
        candidates = new ArrayList<>(); // Candidate 객체를 담을 어레이 리스트(어댑터 쪽으로)
        candidates.clear(); // 기존 배열 초기화

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Candidate"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.child(vote_title).getChildren()) { // 반복문으로 데이터 리스트를 추출
                    Candidate candidate  = snapshot.getValue(Candidate.class); // 만들어둔 Candidate 객체에 데이터를 담는다.
                    candidates.add(candidate); // 담은 데이터들을 배열에 넣고 리사이클뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // DB를 가져오던 중 에러 발생시
                Log.e("AdminvoteActivity", String.valueOf(databaseError.toException()));
            }
        });

        tv_votetitle.setText(votedetail.getTitle());
        tv_votestart.setText(votedetail.getStart());
        tv_voteend.setText(votedetail.getEnd());
        tv_votetype.setText(votedetail.getType());
        tv_votenote.setText(votedetail.getNote());

        GetDate();  // dialog date 설정

        rv_candidatelist.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));  // 투표목록 구분선

        adapter = new IngCandidateAdapter(candidates, this, this);
        rv_candidatelist.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        tv_votestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AdminvoteActivity.this, startlistener, s_y,s_m-1,s_d);
                dialog.show();
            }
        });
        tv_voteend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AdminvoteActivity.this, endlistener, e_y,e_m-1,e_d);
                dialog.show();
            }
        });


        // 뒤로가기 버튼
        btn_voteback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        // 투표 완료 버튼 (TAG_ADMIN_SDK)
        btn_votemodifycomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
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
                date = s_y+"."+s_m+"."+s_d;
                tv_votestart.setText(date);
//                startdate = false;
            } else {
//                startdate = true;
                Log.d("TAG3", "afterTextChanged: "+compare);
                tv_votestart.setText(date);
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
                s_date = dateFormat.parse(tv_votestart.getText().toString());
                e_date = dateFormat.parse(String.valueOf(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int compare = s_date.compareTo(e_date);
            if(compare >= 0){
                Toast.makeText(getApplicationContext(),"시작 이후 날짜를 선택하세요",Toast.LENGTH_SHORT).show();
                e_y = s_y;
                e_m = s_m;
                e_d = s_d;
                date = e_y+"."+e_m+"."+e_d;
                tv_voteend.setText(date);
//                enddate = false;
            } else {
//                enddate = true;
                Log.d("TAG4", "afterTextChanged: "+compare);
                tv_voteend.setText(date);
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
    }

    @Override
    public void onItemClick(int position) {
        voted = String.valueOf(position);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        btn_voteback.performClick();
    }


    private void GetDate() {
        String date_tmp_2 = tv_votestart.getText().toString();

        String[] date_tmp = date_tmp_2.split("\\.");
        s_y = Integer.parseInt(date_tmp[0]);
        s_m = Integer.parseInt(date_tmp[1]);
        s_d = Integer.parseInt(date_tmp[2]);

        date_tmp = tv_voteend.getText().toString().split("\\.");
        e_y = Integer.parseInt(date_tmp[0]);
        e_m = Integer.parseInt(date_tmp[1]);
        e_d = Integer.parseInt(date_tmp[2]);
    }

    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        tv_votetitle = findViewById(R.id.tv_votetitle);
        tv_votestart = findViewById(R.id.tv_votestart);
        tv_votehypen = findViewById(R.id.tv_votehypen);
        tv_voteend = findViewById(R.id.tv_voteend);
        tv_votetype = findViewById(R.id.tv_votetype);
        tv_votenote = findViewById(R.id.tv_votenote);

        btn_votecandidateinfo = findViewById(R.id.btn_votecandidateinfo);
        btn_voteback = findViewById(R.id.btn_voteback);
        btn_votemodifycomplete = findViewById(R.id.btn_votemodifycomplete);

        rv_candidatelist = findViewById(R.id.rv_candidatelist);
    }
    private void TextSizeSet() {
        tv_votetitle.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        tv_votestart.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        tv_votehypen.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        tv_voteend.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        tv_votetype.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        tv_votenote.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_votecandidateinfo.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_voteback.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_votemodifycomplete.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
    }
}