package com.example.prielproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

public class GameOver extends AppCompatActivity {

    TextView tvWinneris;
    TableLayout scoresTbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        int[] scores = intent.getIntArrayExtra("scores");

        tvWinneris = findViewById(R.id.tvWinneris);
        tvWinneris.setText("Winner team is: Team "+(winnerTeam(scores)+1));

        int[][] sortedScoresMatrix = sortScoresMatrix(convertScoresToMatrix(scores));
        for(int[] scoresRow:sortedScoresMatrix)
            Log.d("GameOver","["+scoresRow[0]+","+scoresRow[1]+"]");
        scoresTbl = findViewById(R.id.scoresTbl);

        for(int i = 0; i < sortedScoresMatrix.length ;i++){
            TableRow row = new TableRow(this);
            TextView rowIndexTv = new TextView(this);
            TextView teamNameTv = new TextView(this);
            TextView teamScoreTv = new TextView(this);

            TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(8,8,8,8);

            rowIndexTv.setLayoutParams(layoutParams);
            teamNameTv.setLayoutParams(layoutParams);
            teamScoreTv.setLayoutParams(layoutParams);

            rowIndexTv.setText(""+(i+1));
            teamNameTv.setText("Team " + (sortedScoresMatrix[i][0]+1));
            teamScoreTv.setText(sortedScoresMatrix[i][1] +" words");

            row.addView(rowIndexTv);
            row.addView(teamNameTv);
            row.addView(teamScoreTv);

            scoresTbl.addView(row);
        }
        for(int i :scores){
            Log.d("GameOver",""+i);
        }
    }

    public static int winnerTeam(int[] scores){
        int highestScore = 0, highestScoreTeam = 0;
        for(int i = 0; i<scores.length;i++){
            if(scores[i]>highestScore){
                highestScore = scores[i];
                highestScoreTeam = i;
            }
        }
        return highestScoreTeam;
    }

    public static int[][] convertScoresToMatrix(int[] scores){
        int[][] scoresMatrix = new int[scores.length][2];
        for(int i = 0; i<scores.length;i++){
            scoresMatrix[i][0] = i;
            scoresMatrix[i][1] = scores[i];
        }

        return scoresMatrix;
    }

    public static int[][] sortScoresMatrix(int[][] scoresMatrix){
        int[][] sortedScoresMatrix = (int[][]) Arrays.stream(scoresMatrix).sorted((t1, t2) -> {
            if(t1[1]>t2[1])
                return 1;
            else if(t1[1]<t2[1])
                return -1;
            else
                return 0;

        }).toArray(int[][]::new);


       return sortedScoresMatrix;
    }
}