package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static int gameMode = 0;
    public static boolean isSensorMode = false;
    public static boolean isSensorSpeedMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Toolbar
        ImageView menu = findViewById(R.id.Main_Textview_ToolBarExitToMenu);
        ImageView gameRestart = findViewById(R.id.Main_Textview_ToolBarGameRestart);
        ImageView gamePause = findViewById(R.id.Main_Textview_ToolBarGamePause);

        //Game Modes
        Button easyBtn = findViewById(R.id.Main_Btn_EasyGameMode);
        Button mediumBtn = findViewById(R.id.Main_Btn_MediumGameMode);
        Button hardBtn = findViewById(R.id.Main_Btn_HardGameMode);

        //SensorMode
        SwitchMaterial SensorMode = findViewById(R.id.Main_Btn_SensorsMode);

        //SensorSpeedMode
        SwitchMaterial SensorSpeedMode = findViewById(R.id.Main_Btn_SensorsSpeedMode);

        //Show Records
        Button showRecords = findViewById(R.id.Main_Btn_ShowRecords);


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

        //Save switch "SensorMode" state
        SharedPreferences sensorModeStateSp = getSharedPreferences("SensorMode", MODE_PRIVATE);
        SensorMode.setChecked(sensorModeStateSp.getBoolean("value", false));
        isSensorMode =sensorModeStateSp.getBoolean("value", false);

        //SensorMode
        SensorMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                toast("Sensors Mode Activate");
                isSensorMode = true;
                SensorSpeedMode.setEnabled(true);
                SharedPreferences.Editor editor = getSharedPreferences("SensorMode", MODE_PRIVATE).edit();
                editor.putBoolean("value", true);
                editor.apply();
            } else {
                toast("Sensors Mode Disabled");
                isSensorMode = false;
                SensorSpeedMode.setEnabled(false);
                SensorSpeedMode.setChecked(false);
                isSensorSpeedMode = false;
                SharedPreferences.Editor editor = getSharedPreferences("SensorMode", MODE_PRIVATE).edit();
                editor.putBoolean("value", false);
                editor.apply();
            }
        });

        //Save switch "SensorSpeedMode" state
        if (!isSensorMode){
            SensorSpeedMode.setEnabled(false);
        }

        SharedPreferences sensorSpeedModeStateSp = getSharedPreferences("SensorSpeedMode", MODE_PRIVATE);
        SensorSpeedMode.setChecked(sensorSpeedModeStateSp.getBoolean("value", false));
        isSensorSpeedMode =sensorSpeedModeStateSp.getBoolean("value", false);

        //SensorSpeedMode
        SensorSpeedMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                toast("Sensors Speed Mode Activate");
                isSensorSpeedMode = true;
                SharedPreferences.Editor editor = getSharedPreferences("SensorSpeedMode", MODE_PRIVATE).edit();
                editor.putBoolean("value", true);
                editor.apply();
            } else {
                toast("Sensors Speed Mode Disabled");
                isSensorSpeedMode = false;
                SharedPreferences.Editor editor = getSharedPreferences("SensorSpeedMode", MODE_PRIVATE).edit();
                editor.putBoolean("value", false);
                editor.apply();
            }
        });



        //Show Records
        showRecords.setOnClickListener(v -> toast("Show Records"));

    }
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public static int getGameMode() {
        return gameMode;
    }

    public static boolean getIsSensorMode() {
        return isSensorMode;
    }

    public static boolean getIsSensorSpeedMode() {
        return isSensorSpeedMode;
    }
}




