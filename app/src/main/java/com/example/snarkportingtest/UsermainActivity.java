package com.example.snarkportingtest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 알림, 설정 버튼 설정하기!!!!!!!!!
public class UsermainActivity extends AppCompatActivity implements VotelistAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Votedetail> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    Toolbar toolbar;

    private static final int REQUEST_CODE = 777;

    TextView tv_votetest;
    TextView tv_uservotelist;

    private String user_id;
    private String[] votelist = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermain);

        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동조절

        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // Votelist 객체를 담을 어레이 리스트(어댑터 쪽으로)

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));  // 투표목록 구분선

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // user id 확인 및 설정
        if(user != null) {
            user_id = user.getEmail().split("@")[0];
            Log.d("user_id", user_id);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference = database.getReference("User"); // DB 테이블 연결 - user data 확인(votelist)
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(user_id).getValue(User.class);
                votelist = user.getVotelist().split(",");
                Log.d("votelist", votelist[0] + votelist[1]);
                ReadDB("Votelist");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // DB를 가져오던 중 에러 발생시
                Log.e("UsermainActivity", String.valueOf(databaseError.toException()));
            }
        });
    }

    // 뒤로가기 하단 버튼 클릭시 로그아웃
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut(); // 로그아웃(firebase 로그인 연동)
    }

    // recyclerview 투표 선택시 투표 화면 이동
    @Override
    public void onItemClick(int position) {
        Votedetail votedetail = arrayList.get(position);
        Intent intent = new Intent(UsermainActivity.this, VoteActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("vote", votedetail);
        startActivityForResult(intent, REQUEST_CODE);
    }

    // 투표 화면에서 돌아올 때(toastbox)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "투표 완료", Toast.LENGTH_SHORT).show();
                // 투표 선택 정보 확인
                String candidate = String.valueOf(data.getExtras().getString("voted"));
                String title = String.valueOf(data.getExtras().getString("title"));
                tv_votetest.setText(title+" : "+candidate);
            } else {
                Toast.makeText(getApplicationContext(), "투표 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitem, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notice:
                Toast.makeText(this, "공지사항", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                Toast.makeText(this, "설정",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void ReadDB(String DB_path){
        if (DB_path == "User") {
            databaseReference = database.getReference(DB_path); // DB 테이블 연결 - user data 확인(votelist)
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.child(user_id).getValue(User.class);
                    votelist = user.getVotelist().split(",");
                    Log.d("votelist", votelist[0] + votelist[1]);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // DB를 가져오던 중 에러 발생시
                    Log.e("UsermainActivity", String.valueOf(databaseError.toException()));
                }
            });
        } else if(DB_path == "Votelist") {
            arrayList.clear(); // 기존 배열 초기화
            for(String title : votelist) {
                Log.d("title", title);
                databaseReference = database.getReference(DB_path+"/"+title); // DB 테이블 연결 - user data 확인(votelist)
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // 파이어베이스 데이터베이스의 데이터를 받아오는 함수
                        Votedetail votedetail = dataSnapshot.getValue(Votedetail.class);
                        Log.d("votedetail", votedetail.getTitle());
                        arrayList.add(votedetail);

                        adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // DB를 가져오던 중 에러 발생시
                        Log.e("VoterActivity", String.valueOf(databaseError.toException()));
                    }
                });
            }

            adapter = new VotelistAdapter(arrayList, this, this);
            recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
        }
    }

    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        tv_votetest = findViewById(R.id.tv_votetest);
        tv_uservotelist = findViewById(R.id.tv_uservotelist);

        recyclerView = findViewById(R.id.rv_uservotelist); // 아이디 연결
    }
    private void TextSizeSet() {
        tv_uservotelist.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/12));
    }
}

