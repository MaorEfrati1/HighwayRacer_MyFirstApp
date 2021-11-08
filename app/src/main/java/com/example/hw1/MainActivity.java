package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    //    private int score = 0;
    //    private int coins = 0;
    public int heartCount = 3;
    private int raceCarPos = 1;
    private static final int DELAY = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView raceCar = findViewById(R.id.ImageView_RaceCarMovement_RaceCar);

        ImageView blueCar1 = findViewById(R.id.ImageView_Gameplay_BlueCar2);
        ImageView blueCar2 = findViewById(R.id.ImageView_Gameplay_BlueCar1);

        ImageView heart3 = findViewById(R.id.ImageView_Abilities_Heart3);
        ImageView heart2 = findViewById(R.id.ImageView_Abilities_Heart2);
        ImageView heart1 = findViewById(R.id.ImageView_Abilities_Heart1);

        //obstacle(Car) movement
        obstacleMovement(blueCar1, raceCar, heart1, heart2, heart3, blueCar2);


        //race_Car movement
        //Right arrow button
        ImageButton btn_Right = findViewById(R.id.ImageButton_RightLeftBtn_Right);
        btn_Right.setOnClickListener(v -> {

            if (raceCarPos == 1) {
                raceCar.setX((0));
                raceCarPos = 0;
            } else if (raceCarPos == 2) {
                raceCar.setX(((float) getScreenWidth() / 3));
                raceCarPos = 1;
            }

        });

        //Left arrow button
        ImageButton btn_Left = findViewById(R.id.ImageButton_RightLeftBtn_Left);
        btn_Left.setOnClickListener(v -> {

            if (raceCarPos == 0) {
                raceCar.setX((float) (getScreenWidth() / 3));
                raceCarPos = 1;
            } else if (raceCarPos == 1) {
                raceCar.setX((float) (2 * getScreenWidth() / 3));
                raceCarPos = 2;
            }
        });
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private void obstacleMovement(ImageView blueCar1, ImageView raceCar, ImageView heart1, ImageView heart2, ImageView heart3, ImageView blueCar2) {
        Timer timer = new Timer();

        AtomicInteger randNewLaneBlueCar1 = new AtomicInteger();
        AtomicInteger randNewLaneBlueCar2 = new AtomicInteger();

        randNewLaneBlueCar1.set(1);
        randNewLaneBlueCar2.set(0);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    //ride
                    if (blueCar1.getY() >= 0 && blueCar1.getY() < 1140) {
                        blueCar1.setY(blueCar1.getY() + 60);
                        blueCar2.setY(blueCar2.getY() + 60);
                    }

                    if (blueCar1.getY() == 1140 && blueCar2.getY() == 1140) {

                        //is crashed?
//                       isCrashed(timer,  raceCar,  heart1,  heart2,  heart3,randNewLaneBlueCar1 , randNewLaneBlueCar2);

                        int raceCarPos1 = (int) (raceCar.getX() / 360);
                        if (randNewLaneBlueCar1.get() == raceCarPos1 || randNewLaneBlueCar2.get() == raceCarPos1) {
                            if (heartCount == 3) {
                                heart3.setImageResource(android.R.color.transparent);
                            } else if (heartCount == 2) {
                                heart2.setImageResource(android.R.color.transparent);
                            } else if (heartCount == 1) {
                                heart1.setImageResource(android.R.color.transparent);
                            }
                            heartCount--;
                            if (heartCount != 0) {
                                toast("You Crashed !");
                                vibrate(500);
                            } else {
                                toast("Game Over !");
                                vibrate(1000);
                                timer.cancel();
                            }
                        }


                        //new blue cars ride
                        randNewLaneBlueCar1.set(randNewLane());
                        randNewLaneBlueCar2.set(randNewLane());

                        blueCar1.setX((float) (randNewLaneBlueCar1.get() * getScreenWidth() / 3));
                        blueCar1.setY(0);

                        blueCar2.setX((float) (randNewLaneBlueCar2.get() * getScreenWidth() / 3));
                        blueCar2.setY(0);

                    }
                });
            }
        }, 0, DELAY);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private int randNewLane() {
        return (int) (Math.random() * 3);
    }

    private void vibrate(int milliseconds) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
    }


//    private void isCrashed(Timer timer, ImageView raceCar, ImageView heart1, ImageView heart2, ImageView heart3,AtomicInteger randNewLaneBlueCar1 ,AtomicInteger randNewLaneBlueCar2){
//        //is crashed?
//        int raceCarPos1 = (int) (raceCar.getX() / 360);
//        if (randNewLaneBlueCar1.get() == raceCarPos1 || randNewLaneBlueCar2.get() == raceCarPos1) {
//            if (heartCount == 3) {
//                heart3.setImageResource(android.R.color.transparent);
//            } else if (heartCount == 2) {
//                heart2.setImageResource(android.R.color.transparent);
//            } else if (heartCount == 1) {
//                heart1.setImageResource(android.R.color.transparent);
//            }
//            heartCount--;
//            if (heartCount != 0) {
//                toast("You Crashed !");
//                vibrate(500);
//            } else {
//                toast("Game Over !");
//                vibrate(1000);
//                timer.cancel();
//            }
//        }
//    }

}