package com.example.snarkportingtest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.snarkportingtest.MainActivity.ip;
import static com.example.snarkportingtest.MainActivity.port;

public class VoteActivity extends AppCompatActivity implements IngCandidateAdapter.OnItemClickListener {

    TextView tv_votedetailtitle;
    TextView tv_votedetailterm;
    TextView tv_votedetailtype;
    TextView tv_votedetailnote;

    Button btn_votecandidateinfo;
    Button btn_voteback;
    Button btn_votecomplete;

    Toolbar toolbar;

    private String voted;

    private String user_id;
    private int voted_position;
    private String[] votelist;

    private RecyclerView rv_votecandidatelist;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Candidate> candidates;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    private Handler mHandler;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String id_check_status = "";

    private Date now = new Date();
    private Date start = null;
    private Date end = null;

    private String vote_state;

    // Mysql DB
    private String jsonString;
    private int vote_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동 조절

        setSupportActionBar(toolbar);

        Intent getintent = getIntent();
        user_id = (String) getintent.getExtras().get("user_id");
        Votedetail votedetail = (Votedetail) getintent.getExtras().get("vote");

        vote_id = votedetail.getVote_id();

        rv_votecandidatelist.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        rv_votecandidatelist.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));  // 투표목록 구분선
        layoutManager = new LinearLayoutManager(this);
        rv_votecandidatelist.setLayoutManager(layoutManager);
        candidates = new ArrayList<>(); // Candidate 객체를 담을 어레이 리스트(어댑터 쪽으로)
        candidates.clear(); // 기존 배열 초기화

//        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
//
//        databaseReference = database.getReference("Candidate"); // DB 테이블 연결
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // 파이어베이스 데이터베이스의 데이터를 받아오는 함수
//
//                Log.d("Tag", String.valueOf(dataSnapshot.child(vote_title).getValue()));
//                for(DataSnapshot snapshot : dataSnapshot.child(vote_title).getChildren()) { // 반복문으로 데이터 리스트를 추출
//                    Candidate candidate  = snapshot.getValue(Candidate.class); // 만들어둔 Candidate 객체에 데이터를 담는다.
//                    candidates.add(candidate); // 담은 데이터들을 배열에 넣고 리사이클뷰로 보낼 준비
//                }
//                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // DB를 가져오던 중 에러 발생시
//                Log.e("VoteActivity", String.valueOf(databaseError.toException()));
//            }
//
//        });

        tv_votedetailtitle.setText(votedetail.getTitle());
        tv_votedetailterm.setText(votedetail.getStart()+"-"+votedetail.getEnd());
        tv_votedetailtype.setText(votedetail.getType());
        tv_votedetailnote.setText(votedetail.getNote());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            now = dateFormat.parse(dateFormat.format(now));
            start = dateFormat.parse(votedetail.getStart());
            end = dateFormat.parse(votedetail.getEnd());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int now_start = now.compareTo(start);
        int now_end = now.compareTo(end);
        if(now_start >= 0) {
            if(now_end > 0) {
                // 시작 - 종료 - 현재 // 종료된 투표
                adapter = new EndCandidateAdapter(candidates, this);
                vote_state = "end";
            } else {
                // 시작 - 현재 - 종료 // 진행중 투표
                adapter = new IngCandidateAdapter(candidates, this, this);
                vote_state = "ing";
            }
        } else {
            // 현재 - 시작 - 종료 // 시작전 투표
            adapter = new BeforeCandidateAdapter(candidates, this);
            vote_state = "before";
        }

        rv_votecandidatelist.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        // Mysql DB 연동
        DB_check task = new DB_check();
        task.execute("http://192.168.219.100:80/project/candidate_read.php");

        btn_votecandidateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoteActivity.this, CandidateinfoActivity.class);
                intent.putExtra("candidates", candidates);
                startActivity(intent);
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
        btn_votecomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VoteActivity.this);
                builder.setTitle("투표확인").setMessage("\"" + voted + "\" 에게 투표하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        connect();
                        intent.putExtra("title", tv_votedetailtitle.getText());
                        intent.putExtra("voted", voted);
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

    }

    @Override
    public void onItemClick(int position) {
        // 누구에게 투표 햇는지 저장하는 방식 고민
        voted = candidates.get(position).getName();

        voted_position = position;
//        Log.d("voted_position", String.valueOf(voted_position));

        btn_votecomplete.setEnabled(true);
        btn_votecomplete.setText("'"+voted+"' 투표");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        btn_voteback.performClick();
    }

    private void connect(){
        mHandler = new Handler();
        Thread checkUpdate = new Thread() {
            public void run() {
                try {
                    socket = new Socket(ip, port);
                    Log.d("서버 접속됨", "서버 접속됨");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    dos = new DataOutputStream(socket.getOutputStream());   // output에 보낼꺼 넣음
                    dis = new DataInputStream(socket.getInputStream());     // input에 받을꺼 넣어짐

                    dos.writeUTF("vote_position");
                    dos.writeUTF(tv_votedetailtitle.getText().toString()+","+voted_position+","+user_id);
                    Log.d("data sending :", "title");
//                    dos.writeInt(voted_position);
//                    Log.d("data sending :", "position");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    byte[] read_data = new byte[4];
                    dis.read(read_data);
                    id_check_status = new String(read_data);
                    Log.d("id_check_status", id_check_status);
                    Log.d("id_check_status", String.valueOf(id_check_status.equals("fail")));
                }catch (Exception e){
                }
            }
        };
        checkUpdate.start();

        try {
            checkUpdate.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Mysql DB
    private class DB_check extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(VoteActivity.this, "Please wait...DB Loading...", null, true, true);
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverUrl = (String) strings[0];

            try {
                String selectData = "vote_id=" + vote_id;
                URL url = new URL(serverUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(selectData.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("TAG_DB", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            jsonString = s;
            doParse();
            progressDialog.dismiss();
            Log.d("TAG_DB_total", s);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        private void doParse(){
            try{
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("candidate");

                for(int i = 0; i<jsonArray.length(); i++){
                    Candidate candidate = new Candidate();
                    JSONObject item = jsonArray.getJSONObject(i);
                    candidate.setCandidate_id(item.getInt("candidate_id"));
                    candidate.setVote_id(item.getInt("vote_id"));
                    candidate.setName(item.getString("name"));
                    candidate.setGroup(item.getString("group"));
                    candidate.setProfile(item.getString("profile"));
                    candidate.setNote(item.getString("note"));

                    candidates.add(candidate);
                    adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                }
                // 기권 추가하기
                candidates.add(new Candidate("https://firebasestorage.googleapis.com/v0/b/voteapptest-325df.appspot.com/o/%EA%B8%B0%EA%B6%8C.png?alt=media&token=130b56a0-ef2b-43cd-b4e6-2c3a4f50b7c6","기권",null, null));
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG_DB_error",e.getMessage());
            }
        }
    }

    private void FindViewID() {
        toolbar = findViewById(R.id.toolbar);

        tv_votedetailtitle = findViewById(R.id.tv_votedetailtitle);
        tv_votedetailterm = findViewById(R.id.tv_votedetailterm);
        tv_votedetailtype = findViewById(R.id.tv_votedetailtype);
        tv_votedetailnote = findViewById(R.id.tv_votedetailnote);

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