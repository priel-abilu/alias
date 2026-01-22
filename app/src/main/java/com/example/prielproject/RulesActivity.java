package com.example.prielproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rules);

    Button btnBack = findViewById(R.id.btnBack);
    btnBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent go = new Intent(RulesActivity.this,MainActivity.class);
            startActivity(go);
        }
    });


    }
}
