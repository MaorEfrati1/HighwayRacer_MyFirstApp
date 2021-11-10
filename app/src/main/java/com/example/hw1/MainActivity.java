package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private int heartCount = 3;
    private int raceCarPos = 1;
    private static final int DELAY = 70;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        ImageView menu = findViewById(R.id.Main_Textview_ToolBarExitToMenu);
        ImageView gameRestart = findViewById(R.id.Main_Textview_ToolBarGameRestart);
        ImageView gamePause = findViewById(R.id.Main_Textview_ToolBarGamePause);

        //hearts
        ImageView heart3 = findViewById(R.id.ImageView_Abilities_Heart3);
        ImageView heart2 = findViewById(R.id.ImageView_Abilities_Heart2);
        ImageView heart1 = findViewById(R.id.ImageView_Abilities_Heart1);

        //Obstacles
        ImageView blueCar = findViewById(R.id.ImageView_Gameplay_BlueCar);
        ImageView redCar = findViewById(R.id.ImageView_Gameplay_RedCar);

        //Race car
        ImageView raceCar = findViewById(R.id.ImageView_RaceCarMovement_RaceCar);


        //Exit To Menu
        menu.setOnClickListener(v -> {
            //Will be updated soon
            toast("Will be updated soon");
        });

        //restart
        gameRestart.setOnClickListener(v -> {
            timer.cancel();
            heartCount = 3;
            heart1.setImageResource(R.drawable.heart);
            heart2.setImageResource(R.drawable.heart);
            heart3.setImageResource(R.drawable.heart);

            redCar.setY(-120);
            blueCar.setY(0);

            redCar.setX(0);
            blueCar.setX(((float) 1 * getScreenWidth() / 3));

            gamePlay(blueCar, raceCar, heart1, heart2, heart3, redCar);
        });

        //Pause
        gamePause.setOnClickListener(v -> {
            //Will be updated soon
            toast("Will be updated soon");
        });


        //GamePlay Thread
        gamePlay(blueCar, raceCar, heart1, heart2, heart3, redCar);

        //race_Car movement
        //Right arrow button
        ImageView Right_Arrow = findViewById(R.id.ImageButton_RightLeftBtn_RightArrow);

        //Left arrow button
        ImageView Left_Arrow = findViewById(R.id.ImageButton_RightLeftBtn_LeftArrow);

        //Language support
        String language = Locale.getDefault().getDisplayLanguage();
        if (language.equals("English")) {
            redCar.setX((float) (-2 * getScreenWidth() / 3));
            Left_Arrow.setX((float) (2 * getScreenWidth() / 3));
            Right_Arrow.setX((float) (-2 * getScreenWidth() / 3));


        }

        Right_Arrow.setOnClickListener(v -> {

            if (raceCarPos == 1) {
                raceCar.setX((0));
                raceCarPos = 0;
            } else if (raceCarPos == 2) {
                raceCar.setX(((float) getScreenWidth() / 3));
                raceCarPos = 1;
            }
        });

        //Left arrow button
        Left_Arrow.setOnClickListener(v -> {

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
        timer = new Timer();

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
                    if (blueCar.getY() >= 0 && blueCar.getY() < 1200) {

                        blueCar.setY(blueCar.getY() + 60);
                    }

                    //red car ride
                    if (redCar.getY() >= -120 && redCar.getY() < 1200) {
                        if (isTransparent.get()) {
                            redCar.setY(-120);
                            redCar.setImageResource(R.drawable.red_car);
                            isTransparent.set(false);
                        }
                        redCar.setY(redCar.getY() + 60);
                    }

                    //red car new ride + crashed?
                    if (redCar.getY() == 1200) {

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
                    if (blueCar.getY() == 1200) {

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

                    //cars in the same road
                    if (randNewLaneBlueCar.get() == randNewLaneRedCar.get() && (redCar.getY() >= -120 && redCar.getY() <= 250 && blueCar.getY() >= -120 && blueCar.getY() <= 250)) {
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
            //game over
            goToGameOverScreen();
            vibrate(1000);
            timer.cancel();

        }
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public void goToGameOverScreen() {
        Intent intentLoadGameOverScreen = new Intent(MainActivity.this, GameOverScreen.class);
        startActivity(intentLoadGameOverScreen);
    }

}




