package com.example.prielproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Play extends AppCompatActivity implements Runnable {
    final int TURN_TIME = 10;
    ArrayList<String> words;
    ArrayList<List<String>> forbiddenWords;
    int i = 0;
    int[] scores;
    int teams;
    int rounds;
    int current_team = 1;
    int current_round = 1;
    int current_time = TURN_TIME;
    private Thread timerThread;
    boolean isFirstStart = true;

    Button Skip,Next;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d("Play","Started activity");
        db = FirebaseFirestore.getInstance();
        words = new ArrayList<>();
        forbiddenWords = new ArrayList<>();

        Intent intent = getIntent();
        String origin = intent.getStringExtra("ORIGIN_ACTIVITY");
        if ("GameSettings".equals(origin)) {
            teams = Integer.parseInt(intent.getStringExtra("teams"));
            rounds = Integer.parseInt(intent.getStringExtra("rounds"));
            scores = new int[teams];
        }

        Skip = findViewById(R.id.btnSkip);
        Next = findViewById(R.id.btnNext);

        Skip.setOnClickListener(view -> {
            scores[current_team-1]--;
            i++;
            refreseh_words();
        });

        Next.setOnClickListener(view -> {
            scores[current_team-1]++;
            i++;
            refreseh_words();
        });

        Skip.setEnabled(false);
        Skip.setBackgroundColor(0xFFBDBDBD);
        Next.setEnabled(false);
        Next.setBackgroundColor(0xFFBDBDBD);

    }



    private void startTimer() {
        timerThread = new Thread(() -> {
            while (current_time > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }

                runOnUiThread(() -> {
                    current_time--;
                    refresh_clock();
                });
            }

            runOnUiThread(() -> {
                Log.d("Play","starting TurnEnd");
                i++;
                Intent intent = new Intent(Play.this, TurnEnd.class);
                intent.putExtra("score", scores[current_team - 1]);
                intent.putExtra("team", current_team);
                startActivity(intent);
            });
        });

        timerThread.start();
    }


    public void refreseh_words() {
        TextView Word = findViewById(R.id.Word);
        TextView ForbiddenWord = findViewById(R.id.forbidden);
        Word.setText(words.get(i));
        ForbiddenWord.setText(forbiddenWords.get(i).toString());
    }

    public void refresh_clock() {
        TextView timer = findViewById(R.id.timer);
        timer.setText(String.format("%d", current_time));
    }

    @Override
    public void run() {
        SystemClock.sleep(1000);
        current_time--;
        refresh_clock();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Play","OnResume Active");
        TextView currentGroupTv = findViewById(R.id.NameGroup);
        currentGroupTv.setText("Group " + current_team);
        db.collection("aliasWords").get().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                for(DocumentSnapshot doc:task.getResult().getDocuments()){
                    words.add(doc.getString("word"));
                    forbiddenWords.add((List<String>)doc.get("forbiddenWords"));
                }

                Log.d("PlayActivity",words.toString());
                Log.d("PlayActivity",forbiddenWords.toString());

                if (!isFirstStart) {
                    current_team++;
                    currentGroupTv.setText("Group " + current_team);
                    if (current_team > teams) {
                        current_team = 1;
                        current_round++;
                    }
                }

                isFirstStart = false;

                if (current_round > rounds) {
                    Intent gameOverIntent = new Intent(this, GameOver.class);
                    gameOverIntent.putExtra("scores",scores);
                    startActivity(gameOverIntent);
                    finish();
                    return;
                }

                current_time = TURN_TIME;
                refreseh_words();
                refresh_clock();
                startTimer();
                Skip.setEnabled(true);
                Skip.setBackgroundColor(0xFFFF9800);
                Next.setEnabled(true);
                Next.setBackgroundColor(0x4CAF50);



            }
            else{
                Toast.makeText(this, "Error getting the words", Toast.LENGTH_SHORT).show();
                this.onDestroy();
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (timerThread != null) {
            timerThread.interrupt();
            current_time = TURN_TIME;
        }

    }


}