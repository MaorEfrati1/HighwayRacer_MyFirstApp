package com.example.hw1;

import static com.example.hw1.MainActivity.getGameMode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Locale;


public class GameOverScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);
        //Toolbar
        ImageView menu = findViewById(R.id.Main_Textview_ToolBarExitToMenu);
        ImageView gameRestart = findViewById(R.id.Main_Textview_ToolBarGameRestart);
        ImageView gamePause = findViewById(R.id.Main_Textview_ToolBarGamePause);

        //Game over sound
        final MediaPlayer gameOverSound = MediaPlayer.create(this,R.raw.game_over);
        gameOverSound.start();

        //Language support
        String language = Locale.getDefault().getDisplayLanguage();
        if (language.equals("English")) {
            menu.setRotation(360);
        }

        //Exit To Menu
        menu.setOnClickListener(v -> goToMainScreen());

        //restart
        gameRestart.setOnClickListener(v -> goToGameScreen());

        //Pause
        gamePause.setImageResource(android.R.color.transparent);


    }

    public void goToMainScreen() {
        GameOverScreen.this.finish();
        Intent intentLoadMainScreen = new Intent(GameOverScreen.this, MainActivity.class);
        startActivity(intentLoadMainScreen);
    }

    public void goToGameScreen() {
        int mode = getGameMode();
        Intent intentLoadMainScreen = null;

        if (mode == 1) {
            intentLoadMainScreen = new Intent(GameOverScreen.this, ThreeLaneRoad.class);
        } else if (mode == 2) {
            intentLoadMainScreen = new Intent(GameOverScreen.this, com.example.hw1.FiveLaneRoad.class);
        } else if (mode == 3) {
            intentLoadMainScreen = new Intent(GameOverScreen.this, com.example.hw1.FiveLaneRoad.class);
        }
        GameOverScreen.this.finish();
        startActivity(intentLoadMainScreen);
    }
}