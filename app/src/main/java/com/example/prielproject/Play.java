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
        teams = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("teams")));
        rounds = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("rounds")));
        scores = new int[teams];
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("teams",teams);
        outState.putInt("rounds",rounds);
        outState.putIntArray("scores",scores);
        outState.putInt("current_team",current_team);
        outState.putInt("current_round",current_round);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        teams = savedInstanceState.getInt("teams");
        rounds = savedInstanceState.getInt("rounds");
        scores = savedInstanceState.getIntArray("scores");
        current_team = savedInstanceState.getInt("current_team")+1;
        if(current_team>teams){
            current_round = savedInstanceState.getInt("current_round") +1;
            current_team = 1;
        }
        else
            current_round = savedInstanceState.getInt("current_round");
    }
}