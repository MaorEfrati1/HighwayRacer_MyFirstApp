package com.example.hw1.Model;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.hw1.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static int gameMode = 0;
    public static boolean isSensorMode = false;
    public static boolean isSensorSpeedMode = false;
    public static String getCarColor = "Red";
    private double longitude = 0;
    private double latitude = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //clean Records JSON
        //MSP.getInstance(this).clearMSP();

        //Get location
        locationPermission();

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

        //Choose car color
        TextInputLayout til_ChooseCarColor;
        AutoCompleteTextView act_ChooseCarColor;

        ArrayList<String> arrayList_ChooseCarColor;
        ArrayAdapter<String> arrayAdapter_ChooseCarColor;


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

            //get location FiveLane
            Intent intent = new Intent(this, ThreeLaneRoad.class);
            Bundle bundle = new Bundle();
            bundle.putDouble(ThreeLaneRoad.LATITUDE, latitude);
            bundle.putDouble(ThreeLaneRoad.LONGITUDE, longitude);
            intent.putExtra("bundle", bundle);
            startActivity(intent);

        });

        mediumBtn.setOnClickListener(v -> {
            gameMode = 2;
            MainActivity.this.finish();

            //get location FiveLane
            Intent intent = new Intent(this, FiveLaneRoad.class);
            Bundle bundle = new Bundle();
            bundle.putDouble(FiveLaneRoad.LATITUDE, latitude);
            bundle.putDouble(FiveLaneRoad.LONGITUDE, longitude);
            intent.putExtra("bundle", bundle);
            startActivity(intent);

        });

        hardBtn.setOnClickListener(v -> {
            gameMode = 3;
            MainActivity.this.finish();

            //get location FiveLane
            Intent intent = new Intent(this, FiveLaneRoad.class);
            Bundle bundle = new Bundle();
            bundle.putDouble(FiveLaneRoad.LATITUDE, latitude);
            bundle.putDouble(FiveLaneRoad.LONGITUDE, longitude);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        });

        //Save switch "SensorMode" state
        SharedPreferences sensorModeStateSp = getSharedPreferences("SensorMode", MODE_PRIVATE);
        SensorMode.setChecked(sensorModeStateSp.getBoolean("value", false));
        isSensorMode = sensorModeStateSp.getBoolean("value", false);


        //SensorMode
        SensorMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                toast("Sensors Mode Activate");
                isSensorMode = true;
                SensorSpeedMode.setEnabled(true);
                SensorSpeedMode.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = getSharedPreferences("SensorMode", MODE_PRIVATE).edit();
                editor.putBoolean("value", true);
                editor.apply();
            } else {
                toast("Sensors Mode Disabled");
                isSensorMode = false;
                SensorSpeedMode.setEnabled(false);
                SensorSpeedMode.setChecked(false);
                isSensorSpeedMode = false;
                SensorSpeedMode.setVisibility(View.INVISIBLE);
                SharedPreferences.Editor editor = getSharedPreferences("SensorMode", MODE_PRIVATE).edit();
                editor.putBoolean("value", false);
                editor.apply();
            }
        });

        //Save switch "SensorSpeedMode" state
        if (!isSensorMode) {
            SensorSpeedMode.setEnabled(false);
            SensorSpeedMode.setVisibility(View.INVISIBLE);
        } else {
            SensorSpeedMode.setVisibility(View.VISIBLE);
        }

        SharedPreferences sensorSpeedModeStateSp = getSharedPreferences("SensorSpeedMode", MODE_PRIVATE);
        SensorSpeedMode.setChecked(sensorSpeedModeStateSp.getBoolean("value", false));
        isSensorSpeedMode = sensorSpeedModeStateSp.getBoolean("value", false);

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
        showRecords.setOnClickListener(v -> {
            MainActivity.this.finish();
            Intent intentLoadGameOverScreen = new Intent(MainActivity.this, Records.class);
            startActivity(intentLoadGameOverScreen);

        });

        //Choose car color
        til_ChooseCarColor = findViewById(R.id.Main_DropDown_TilChooseCarColor);
        act_ChooseCarColor = findViewById(R.id.Main_DropDown_ActChooseCarColor);

        arrayList_ChooseCarColor = new ArrayList<>();
        arrayList_ChooseCarColor.add("Red");
        arrayList_ChooseCarColor.add("White");
        arrayList_ChooseCarColor.add("Yellow");

        arrayAdapter_ChooseCarColor = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayList_ChooseCarColor);
        act_ChooseCarColor.setAdapter(arrayAdapter_ChooseCarColor);

        act_ChooseCarColor.setOnItemClickListener((parent, arg1, pos, id) -> getCarColor = Objects.requireNonNull(til_ChooseCarColor.getEditText()).getText().toString());
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

    public static String getCarColor() {
        return getCarColor;
    }


    private void locationPermission() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        latitude = myLocation.getLatitude();
        longitude = myLocation.getLongitude();
    }
}




