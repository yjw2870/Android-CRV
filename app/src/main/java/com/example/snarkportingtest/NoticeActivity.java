package com.example.snarkportingtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
//    private ArrayList<Votedetail> votelist;   // notice로 변경해야함 - notice에 맞는 class 필요(notification title,body / data)

    Toolbar toolbar;

    TextView tv_notice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동조절

        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // sqlite에서 알림DB 읽어오기

    }


    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        tv_notice = findViewById(R.id.tv_notice);

        recyclerView = findViewById(R.id.rv_noticelist); // 아이디 연결
    }
    private void TextSizeSet() {
        tv_notice.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/12));
    }
}