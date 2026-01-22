package com.example.prielproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play = findViewById(R.id.play);
        Button gameRules = findViewById(R.id.gameRules);

        play.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GamesSettings.class);
            startActivity(intent);
        });

        gameRules.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RulesActivity.class);
            startActivity(intent);
        });
    }
}
