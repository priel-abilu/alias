package com.example.prielproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firestore.v1.WriteResult;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
    loadDataToFireStore();
    }


    public void loadDataToFireStore(){
        try {



            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // 2️⃣ קריאת קובץ JSON
            ObjectMapper mapper = new ObjectMapper();
            InputStream jsonFile = getResources().openRawResource(R.raw.words);

            List<Map<String, Object>> wordsList = mapper.readValue(
                    jsonFile,
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            // 3️⃣ העלאה ל-Firestore
            for (Map<String, Object> wordObject : wordsList) {

                String word = (String) wordObject.get("word");

                db.collection("aliasWords")
                        .document(word)
                        .set(wordObject);
            }

            System.out.println("✅ Upload completed!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
