package com.example.snarkportingtest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddcandidateActivity extends AppCompatActivity {

    TextView tv_createvote;
    TextView tv_candidatename;
    TextView tv_candidategroup;
    TextView tv_candidatenote;

    EditText et_candidatename;
    EditText et_candidategroup;
    EditText et_candidatenote;

    ImageView iv_profile;

    Button btn_profileupload;
    Button btn_candidatecancle;
    Button btn_candidateadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcandidate);


        FindViewID();   // layout view 아이디 연결
        TextSizeSet();  // text size 자동조절

        btn_candidateadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_candidatename.getText().toString();
                String group = et_candidategroup.getText().toString();
                String note = et_candidatenote.getText().toString();

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                intent.putExtra("name", name);
                intent.putExtra("group", group);
                intent.putExtra("note", note);
                Log.d("TAG", "onClick: "+name+group+note);
                finish();
            }
        });




    }

    private void FindViewID() {
        tv_createvote = findViewById(R.id.tv_createvote);
        tv_candidatename = findViewById(R.id.tv_candidatename);
        tv_candidategroup = findViewById(R.id.tv_candidategroup);
        tv_candidatenote = findViewById(R.id.tv_candidatenote);

        et_candidatename = findViewById(R.id.et_candidatename);
        et_candidategroup = findViewById(R.id.et_candidategroup);
        et_candidatenote = findViewById(R.id.et_candidatenote);

        iv_profile = findViewById(R.id.iv_profile);

        btn_profileupload = findViewById(R.id.btn_profileupload);
        btn_candidatecancle = findViewById(R.id.btn_candidatecancle);
        btn_candidateadd = findViewById(R.id.btn_candidateadd);
    }

    private void TextSizeSet() {
        tv_createvote.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/12));
        tv_candidatename.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_candidategroup.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        tv_candidatenote.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        et_candidatename.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_candidategroup.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        et_candidatenote.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_profileupload.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_candidatecancle.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        btn_candidateadd.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
    }
}