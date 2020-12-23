package com.example.snarkportingtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CandidateinfoActivity extends AppCompatActivity {

    Toolbar toolbar;

    private RecyclerView rv_candidateinfocard;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Candidate> candidates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidateinfo);

        FindViewID();   // layout view 아이디 연결

        rv_candidateinfocard.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv_candidateinfocard.setLayoutManager(layoutManager);
        candidates = new ArrayList<>(); // Votelist 객체를 담을 어레이 리스트(어댑터 쪽으로)
        candidates.clear(); // 기존 배열 초기화
        adapter = new CandidateinfoAdapter(candidates, this);

        Intent intent = getIntent();
        final String vote_title = (String) intent.getExtras().get("title");
        candidates.addAll((ArrayList<Candidate>) intent.getExtras().get("candidates"));
        Log.d("candidate", String.valueOf(candidates.get(0).getName()));
        adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

        rv_candidateinfocard.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결


    }
    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        rv_candidateinfocard = findViewById(R.id.rv_candidateinfocard);
    }
}