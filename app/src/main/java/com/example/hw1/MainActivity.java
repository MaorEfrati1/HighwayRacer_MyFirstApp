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
import java.util.concurrent.atomic.AtomicBoolean;
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

        ImageView blueCar = findViewById(R.id.ImageView_Gameplay_BlueCar);
        ImageView redCar = findViewById(R.id.ImageView_Gameplay_Red_car);

        ImageView heart3 = findViewById(R.id.ImageView_Abilities_Heart3);
        ImageView heart2 = findViewById(R.id.ImageView_Abilities_Heart2);
        ImageView heart1 = findViewById(R.id.ImageView_Abilities_Heart1);

        //GamePlay
        gamePlay(blueCar, raceCar, heart1, heart2, heart3, redCar);

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


    private void gamePlay(ImageView blueCar, ImageView raceCar, ImageView heart1, ImageView heart2, ImageView heart3, ImageView redCar) {
        Timer timer = new Timer();

        AtomicInteger randNewLaneBlueCar = new AtomicInteger();
        AtomicInteger randNewLaneRedCar = new AtomicInteger();

        randNewLaneBlueCar.set(1);
        randNewLaneRedCar.set(0);
        AtomicBoolean isTransparent = new AtomicBoolean(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    //blue car ride
                    if (blueCar.getY() >= 0 && blueCar.getY() < 1140) {

                        blueCar.setY(blueCar.getY() + 60);
                    }
                    //red car ride
                    if (redCar.getY() >= -120 && redCar.getY() < 1140) {
                        if (isTransparent.get()) {

                            redCar.setY(-120);

                            redCar.setImageResource(R.drawable.red_car);
                            isTransparent.set(false);
                        }
                        redCar.setY(redCar.getY() + 60);
                    }

                    //same road situation - for 1 live down
//                    if (redCar.getY() >= 0 && redCar.getY() <= 100 && blueCar.getY() >= 0 && blueCar.getY() <= 100) {
//
//                        if (randNewLaneBlueCar.get() == randNewLaneRedCar.get()) {
//                            // 1 car show
//                            blueCar.setImageResource(android.R.color.transparent);
//
//
//                            //crashed
//                            int raceCarPos1 = (int) (raceCar.getX() / 360);
//                            if (randNewLaneBlueCar.get() == raceCarPos1 && randNewLaneRedCar.get() == raceCarPos1) {
//                                Crashed(timer, heart1, heart2, heart3);
//                            }
//
//                            //new blue car ride
//                            randNewLaneBlueCar.set(randNewLane());
//
//                            //blueCar.setImageResource(R.drawable.blue_car);
//                            blueCar.setX((float) (randNewLaneBlueCar.get() * getScreenWidth() / 3));
//                            blueCar.setY(0);
//
//                            //new red car ride
//                            randNewLaneRedCar.set(randNewLane());
//
//                            redCar.setX((float) (randNewLaneRedCar.get() * getScreenWidth() / 3));
//                            redCar.setY(-120);
//
//                        }
//                    }

                    //red car new ride + crashed?
                    if (redCar.getY() == 1140) {

                        //crashed
                        int raceCarPos1 = (int) (raceCar.getX() / 360);
                        if (randNewLaneRedCar.get() == raceCarPos1) {
                            Crashed(timer, heart1, heart2, heart3);
                        }

                        // red car new ride
                        randNewLaneRedCar.set(randNewLane());
                        redCar.setX((float) (randNewLaneRedCar.get() * getScreenWidth() / 3));
                        redCar.setY(-120);
                    }

                    //blue car new ride + crashed?
                    if (blueCar.getY() == 1140) {

                        //is crashed?
                        int raceCarPos1 = (int) (raceCar.getX() / 360);
                        if (randNewLaneBlueCar.get() == raceCarPos1) {
                            Crashed(timer, heart1, heart2, heart3);
                        }

                        // blue car new ride
                        randNewLaneBlueCar.set(randNewLane());
                        blueCar.setX((float) (randNewLaneBlueCar.get() * getScreenWidth() / 3));
                        blueCar.setY(0);
                    }

                    if (randNewLaneBlueCar.get() == randNewLaneRedCar.get() && (redCar.getY() >= -120 && redCar.getY() <= 300 && blueCar.getY() >= -120 && blueCar.getY() <= 300)) {
                        redCar.setImageResource(android.R.color.transparent);
                        //toast("ok");
                        randNewLaneRedCar.set(randNewLane());
                        redCar.setX((float) (randNewLaneRedCar.get() * getScreenWidth() / 3));
                        isTransparent.set(true);
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


    private void Crashed(Timer timer, ImageView heart1, ImageView heart2, ImageView heart3) {
        //is crashed?
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

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

}




