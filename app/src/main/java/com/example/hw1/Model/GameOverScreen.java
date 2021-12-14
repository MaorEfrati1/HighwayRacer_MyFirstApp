package com.example.hw1.Model;

import static com.example.hw1.Model.MainActivity.getGameMode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.hw1.R;

import java.util.Locale;


public class GameOverScreen extends AppCompatActivity {
    private double longitude = 0;
    private double latitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //get Location
        locationPermission();

        //Toolbar
        ImageView menu = findViewById(R.id.Main_Textview_ToolBarExitToMenu);
        ImageView gameRestart = findViewById(R.id.Main_Textview_ToolBarGameRestart);
        ImageView gamePause = findViewById(R.id.Main_Textview_ToolBarGamePause);

        //Game over sound
        final MediaPlayer gameOverSound = MediaPlayer.create(this, R.raw.game_over);
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
            Bundle bundle = new Bundle();
            bundle.putDouble(ThreeLaneRoad.LATITUDE, latitude);
            bundle.putDouble(ThreeLaneRoad.LONGITUDE, longitude);
            intentLoadMainScreen.putExtra("bundle", bundle);
            startActivity(intentLoadMainScreen);
        } else if (mode == 2) {
            intentLoadMainScreen = new Intent(GameOverScreen.this, FiveLaneRoad.class);
            Bundle bundle = new Bundle();
            bundle.putDouble(FiveLaneRoad.LATITUDE, latitude);
            bundle.putDouble(FiveLaneRoad.LONGITUDE, longitude);
            intentLoadMainScreen.putExtra("bundle", bundle);
            startActivity(intentLoadMainScreen);
        } else if (mode == 3) {
            intentLoadMainScreen = new Intent(GameOverScreen.this, FiveLaneRoad.class);
            Bundle bundle = new Bundle();
            bundle.putDouble(FiveLaneRoad.LATITUDE, latitude);
            bundle.putDouble(FiveLaneRoad.LONGITUDE, longitude);
            intentLoadMainScreen.putExtra("bundle", bundle);
            startActivity(intentLoadMainScreen);
        }
        GameOverScreen.this.finish();
        startActivity(intentLoadMainScreen);
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