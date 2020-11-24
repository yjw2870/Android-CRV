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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements AdminVotelistAdapter.OnItemClickListener {

    private static final int REQUEST_CODE = 777;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Votedetail> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    TextView tv_adminmain;
    TextView tv_adminid;
    TextView tv_votelist;

    Button btn_admincreatevote;
//    Button btn_admingomain;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동조절

        toolbar.setTitle("VoteApp - Admin");
        setSupportActionBar(toolbar);


        btn_admincreatevote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create = new Intent(AdminActivity.this, CreatevoteActivity.class);
                startActivity(create);
            }
        });

//        btn_admingomain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut(); // 로그아웃(firebase 로그인 연동)
////                Intent gomain = new Intent(AdminActivity.this, MainActivity.class);
////                startActivity(gomain);
//                finish();
//            }
//        });

        recyclerView = findViewById(R.id.rv_adminvotelist); // 아이디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // Votelist 객체를 담을 어레이 리스트(어댑터 쪽으로)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Votelist"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 함수
                arrayList.clear(); // 기존 배열 초기화
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 리스트를 추출
                    Votedetail votedetail  = snapshot.getValue(Votedetail.class); // 만들어둔 Votedetail 객체에 데이터를 담는다.
                    arrayList.add(votedetail); // 담은 데이터들을 배열에 넣고 리사이클뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // DB를 가져오던 중 에러 발생시
                Log.e("VoterActivity", String.valueOf(databaseError.toException()));
            }
        });

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));  // 투표목록 구분선

        adapter = new AdminVotelistAdapter(arrayList, this, this);
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String adminid = currentUser.getEmail().split("@")[0];
        tv_adminid.setText(adminid);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut(); // 로그아웃(firebase 로그인 연동)
    }


    @Override
    public void onItemClick(int position) {
        Votedetail votedetail = arrayList.get(position);
        Intent intent = new Intent(AdminActivity.this, VoteActivity.class);
        intent.putExtra("vote", votedetail);
        startActivityForResult(intent, REQUEST_CODE);
    }
    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        tv_adminmain = findViewById(R.id.tv_adminmain);
        tv_adminid = findViewById(R.id.tv_adminid);
        tv_votelist = findViewById(R.id.tv_votelist);

        btn_admincreatevote = findViewById(R.id.btn_admincreatevote);
//        btn_admingomain = findViewById(R.id.btn_admingomain);
    }
    private void TextSizeSet() {
        tv_adminmain.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/12));
        tv_adminid.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        tv_votelist.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_admincreatevote.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
//        btn_admingomain.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
    }
}