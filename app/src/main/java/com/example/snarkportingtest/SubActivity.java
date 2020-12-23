package com.example.snarkportingtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;

public class SubActivity extends AppCompatActivity {
    String task =  "/data/data/com.example.snarkportingtest/files/" ;
    String mode = "all";
//     Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    public void CopyFromPackage(int resID, String target) throws IOException
    {
        FileOutputStream lOutputStream = openFileOutput(target, Context.MODE_PRIVATE);
        InputStream lInputStream = getResources().openRawResource(resID);
        int readByte;
        byte[] buff = new byte[999999];

        while (( readByte = lInputStream.read(buff))!=-1)
        {
            lOutputStream.write(buff,0, readByte);
        }

        lOutputStream.flush();
        lOutputStream.close();
        lInputStream.close();
    }
    public void CopyIfNotExist(int resID, String target) throws IOException
    {
        File targetFile = new File(target);
        if (!targetFile.exists())
        {
            CopyFromPackage(resID,targetFile.getName());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        String text = null;
        try {
            text = task + "votearith.txt";
            CopyIfNotExist(R.raw.votearith, text);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        text = null;
        try {
            text = task + "voteinput.txt";
            CopyIfNotExist(R.raw.voteinput, text);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Example of a call to a native method
//        TextView tv = findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI("vote", "setup"));

        Intent start_intent = getIntent();
        String vote = (String) start_intent.getExtras().get("vote");
        String setup = (String) start_intent.getExtras().get("setup");

        final String result = stringFromJNI("vote", "setup");


        AlertDialog.Builder builder = new AlertDialog.Builder(SubActivity.this);
        builder.setTitle("proof 확인").setMessage("Proof 확인 : "+result).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                intent.putExtra("result", result);
                finish();
            }
        }).setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String task, String mode);
}
