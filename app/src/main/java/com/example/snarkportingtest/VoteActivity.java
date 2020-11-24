package com.example.snarkportingtest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VoteActivity extends AppCompatActivity implements CandidateAdapter.OnItemClickListener {

    TextView tv_votedetailtitle;
    TextView tv_votedetailterm;
    TextView tv_votedetailtype;
    TextView tv_votedetailnote;

    Button btn_votecandidateinfo;
    Button btn_voteback;
    Button btn_votecomplete;

    TextView tv_votetest;

    String voted;

    Toolbar toolbar;

    private RecyclerView rv_votecandidatelist;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Candidate> candidates;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동 조절

        setSupportActionBar(toolbar);

        Intent getintent = getIntent();
        Votedetail votedetail = (Votedetail) getintent.getExtras().get("vote");

        final String vote_title = votedetail.getTitle();

        rv_votecandidatelist.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        rv_votecandidatelist.setLayoutManager(layoutManager);
        candidates = new ArrayList<>(); // Candidate 객체를 담을 어레이 리스트(어댑터 쪽으로)
        candidates.clear(); // 기존 배열 초기화

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Candidate"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 함수

                Log.d("Tag", String.valueOf(dataSnapshot.child(vote_title).getValue()));
                for(DataSnapshot snapshot : dataSnapshot.child(vote_title).getChildren()) { // 반복문으로 데이터 리스트를 추출
                    Candidate candidate  = snapshot.getValue(Candidate.class); // 만들어둔 Candidate 객체에 데이터를 담는다.
                    candidates.add(candidate); // 담은 데이터들을 배열에 넣고 리사이클뷰로 보낼 준비
                }
                // 기권표 추가
                Candidate candidate = new Candidate("https://firebasestorage.googleapis.com/v0/b/voteapptest-325df.appspot.com/o/%EA%B8%B0%EA%B6%8C.png?alt=media&token=130b56a0-ef2b-43cd-b4e6-2c3a4f50b7c6", "기권하기","","",0);
                candidates.add(candidate);
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // DB를 가져오던 중 에러 발생시
                Log.e("VoteActivity", String.valueOf(databaseError.toException()));
            }

        });

        tv_votedetailtitle.setText(votedetail.getTitle());
        tv_votedetailterm.setText(votedetail.getStart()+"-"+votedetail.getEnd());
        tv_votedetailtype.setText(votedetail.getType());
        tv_votedetailnote.setText(votedetail.getNote());

        rv_votecandidatelist.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));  // 투표목록 구분선

        adapter = new CandidateAdapter(candidates, this, this);
        rv_votecandidatelist.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결


        // 뒤로가기 버튼
        btn_voteback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title", tv_votedetailtitle.getText());
                intent.putExtra("voted", voted);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        // 투표 완료 버튼 (TAG_ADMIN_SDK)
        btn_votecomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                intent.putExtra("title", tv_votedetailtitle.getText());
                intent.putExtra("voted", voted);
                finish();
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        // 누구에게 투표 햇는지 저장하는 방식 고민
        tv_votetest.setText(String.valueOf(position));
        voted = String.valueOf(position);
        btn_votecomplete.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        btn_voteback.performClick();
    }

    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        tv_votedetailtitle = findViewById(R.id.tv_votedetailtitle);
        tv_votedetailterm = findViewById(R.id.tv_votedetailterm);
        tv_votedetailtype = findViewById(R.id.tv_votedetailtype);
        tv_votedetailnote = findViewById(R.id.tv_votedetailnote);

        tv_votetest = findViewById(R.id.tv_votetest);

        btn_votecandidateinfo = findViewById(R.id.btn_votecandidateinfo);
        btn_voteback = findViewById(R.id.btn_voteback);
        btn_votecomplete = findViewById(R.id.btn_votecomplete);

        rv_votecandidatelist = findViewById(R.id.rv_votecandidatelist);
    }
    private void TextSizeSet() {
        tv_votedetailtitle.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        tv_votedetailterm.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        tv_votedetailtype.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        tv_votedetailnote.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_votecandidateinfo.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_voteback.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_votecomplete.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
    }
}