package com.example.prielproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Objects;

public class Play extends AppCompatActivity implements Runnable {

    String[] Words = {"טלוויזיה", "טלפון", "מחשב",};
    String[] ForbiddenWords = {"מסך,לצפות,סרטים,סדרות,חדשות", "וואטסאפ,שיחות,הודעות,אפליקציות", "מסך,עכבר,מקלדת,כרום",};
    int i = 0;
    int[] scores;
    int teams;
    int rounds;
    int current_team = 1;
    int current_round = 1;
    int current_time = 10;
    private Thread timerThread;
    boolean isFirstStart = true;

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
        Intent intent = getIntent();
        String origin = intent.getStringExtra("ORIGIN_ACTIVITY");
        if ("GameSettings".equals(origin)) {
            teams = Integer.parseInt(intent.getStringExtra("teams"));
            rounds = Integer.parseInt(intent.getStringExtra("rounds"));
            scores = new int[teams];
        }
        refreseh_words();
        refresh_clock();

        Button Skip = findViewById(R.id.btnSkip);
        Button Next = findViewById(R.id.btnNext);

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

        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (current_time>0) {
                    try {
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                current_time--;
                                refresh_clock();
                            }
                        });
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                Intent turn_end_activity = new Intent(Play.this,TurnEnd.class);
                turn_end_activity.putExtra("score",scores[current_team-1]);
                turn_end_activity.putExtra("team",current_team);
                startActivity(turn_end_activity);
            }
        });
        backgroundThread.start();
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
        Word.setText(Words[i]);
        ForbiddenWord.setText(ForbiddenWords[i]);
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

        if (!isFirstStart) {
            current_team++;
            if (current_team > teams) {
                current_team = 1;
                current_round++;
            }
        }

        isFirstStart = false;

        if (current_round > rounds) {
            startActivity(new Intent(this, GameOver.class));
            finish();
            return;
        }

        current_time = 10;
        refreseh_words();
        refresh_clock();
        startTimer();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (timerThread != null) {
            timerThread.interrupt();
        }
    }


}