package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class GameOverScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);
        //Toolbar
        ImageView menu = findViewById(R.id.Main_Textview_ToolBarExitToMenu);
        ImageView gameRestart = findViewById(R.id.Main_Textview_ToolBarGameRestart);
        ImageView gamePause = findViewById(R.id.Main_Textview_ToolBarGamePause);

        //Exit To Menu
        menu.setOnClickListener(v -> {
            //Will be updated soon
            toast("Will be updated soon");
        });

        //restart
        gameRestart.setOnClickListener(v -> goToMainScreen());

        //Pause
        gamePause.setOnClickListener(v -> {
            //Will be updated soon
            toast("Will be updated soon");
        });

    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void goToMainScreen() {
        Intent intentLoadMainScreen = new Intent(GameOverScreen.this, MainActivity.class);
        startActivity(intentLoadMainScreen);
    }


}