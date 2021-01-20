package com.example.snarkportingtest;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "firebaseservice";
    @Override
    public void onNewToken(@NonNull String token) {
//        super.onNewToken(s);
        Log.d("token", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String data = remoteMessage.getData().toString();
            Log.d(TAG, "Message data payload: " + data);//remoteMessage.getData());

            try {
                JSONObject jsonObject = new JSONObject(data);
                int vote_id = jsonObject.getInt("vote_id");
                DBHelper helper;
                SQLiteDatabase db;
                helper = new DBHelper(getApplicationContext(), "userdb.db",null, 1);
                db = helper.getWritableDatabase();

                // notice(vote_id) insert to votelist
                ContentValues values = new ContentValues();
                values.put("vote_id", vote_id);
                values.put("title", "test_title_"+vote_id);
                db.insert("votelist", null, values);

                Cursor c = db.rawQuery("select * from votelist;", null);
                if(c.moveToFirst()) {
                    while(!c.isAfterLast()){
                        Log.d("TAG_READ_votelist", "" + c.getInt(c.getColumnIndex("vote_id")) +" "+ c.getString(c.getColumnIndex("title")));
                        c.moveToNext();
                    }
                }

                Log.d("TAG_SQLITE", "suc");

            } catch (Exception e) {
                e.printStackTrace();
            }

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody() + remoteMessage.getNotification().getTitle());
        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Schedule async work using WorkManager.
     */
//    private void scheduleJob() {
//        // [START dispatch_job]
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .build();
//        WorkManager.getInstance().beginWith(work).enqueue();
//        // [END dispatch_job]
//    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
//    private void handleNow() {
//        Log.d(TAG, "Short lived task is done.");
//    }
}