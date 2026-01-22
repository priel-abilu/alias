package com.example.prielproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GamesSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_games_settings);
        Button start = findViewById(R.id.Start);
        start.setOnClickListener(view -> {
            RadioGroup teams_RG = findViewById(R.id.number_of_teams);
            RadioGroup rounds_RG = findViewById(R.id.number_of_rounds);

            String teams = ((RadioButton) findViewById(teams_RG.getCheckedRadioButtonId())).getText().toString();
            String rounds = ((RadioButton) findViewById(rounds_RG.getCheckedRadioButtonId())).getText().toString();

            Intent play_activity = new Intent(GamesSettings.this,Play.class);
            play_activity.putExtra("teams",teams);
            play_activity.putExtra("rounds",rounds);
            startActivity(play_activity);
        });

    }
}