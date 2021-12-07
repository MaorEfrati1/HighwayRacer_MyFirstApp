package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static int gameMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        ImageView menu = findViewById(R.id.Main_Textview_ToolBarExitToMenu);
        ImageView gameRestart = findViewById(R.id.Main_Textview_ToolBarGameRestart);
        ImageView gamePause = findViewById(R.id.Main_Textview_ToolBarGamePause);

        Button easyBtn = findViewById(R.id.Main_Btn_Easy);
        Button mediumBtn = findViewById(R.id.Main_Btn_Medium);
        Button hardBtn = findViewById(R.id.Main_Btn_Hard);

        //Language support
        String language = Locale.getDefault().getDisplayLanguage();
        if (language.equals("English")) {
            menu.setRotation(360);
        }

        //toolbar

        menu.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });
        gameRestart.setImageResource(android.R.color.transparent);
        gamePause.setImageResource(android.R.color.transparent);
        //buttons

        easyBtn.setOnClickListener(v -> {
            gameMode = 1;
            MainActivity.this.finish();
            Intent intentLoadGameOverScreen = new Intent(MainActivity.this, ThreeLaneRoad.class);
            startActivity(intentLoadGameOverScreen);
        });

        mediumBtn.setOnClickListener(v -> {
            gameMode = 2;
            MainActivity.this.finish();
            Intent intentLoadGameOverScreen = new Intent(MainActivity.this, FiveLaneRoad.class);
            startActivity(intentLoadGameOverScreen);
        });

        hardBtn.setOnClickListener(v -> {
            gameMode = 3;
            MainActivity.this.finish();
            Intent intentLoadGameOverScreen = new Intent(MainActivity.this, FiveLaneRoad.class);
            startActivity(intentLoadGameOverScreen);
        });


    }

    public static int getGameMode() {
        return gameMode;
    }
}




