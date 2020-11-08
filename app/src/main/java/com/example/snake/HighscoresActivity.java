package com.example.snake;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HighscoresActivity extends AppCompatActivity {
    private SharedPreferences sp;

    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        // Load
        sp = getSharedPreferences("Game", Context.MODE_PRIVATE);
        playerName = sp.getString("playerName", null);
    }
}