package com.shubhamwithcode.mymall;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class Splash_Activity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                  //  sleep(4000);

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    if (auth.getCurrentUser() == null){
                        Intent intentSplash = new Intent(Splash_Activity.this, Register_Activity.class);
                        startActivity(intentSplash);
                        finish();
                    }else {
                        FirebaseFirestore.getInstance().collection("USERS").document(auth.getCurrentUser().getUid()).update("last_seen", FieldValue.serverTimestamp())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Intent intentMain = new Intent(Splash_Activity.this, MainActivity.class);
                                            startActivity(intentMain);
                                            finish();
                                        }else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(Splash_Activity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }

        };
        thread.start();
    }
}