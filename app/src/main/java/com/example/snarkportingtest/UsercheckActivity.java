package com.example.snarkportingtest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UsercheckActivity extends AppCompatActivity {

    TextView tv_userchecktest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercheck);

        tv_userchecktest = findViewById(R.id.tv_userchecktest);

        Intent intent = new Intent();
        String test = String.valueOf(intent.getExtras().getInt("test"));

        tv_userchecktest.setText(test);
    }
}