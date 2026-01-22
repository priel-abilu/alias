package com.example.prielproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TurnEnd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_turn_end);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        int team = intent.getIntExtra("team", 0);

        TextView team_TV = findViewById(R.id.team);
        TextView score_TV = findViewById(R.id.score);
        team_TV.setText(String.format("%d",team));
        score_TV.setText(String.format("%d",score));

        Button continue_btn = findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(view -> {
            Intent play_activity = new Intent(TurnEnd.this,Play.class);
            finish();
        });
    }
}