package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreeLaneRoad extends AppCompatActivity {
    private final int laneRoad = 3;
    private final int screenWidth = 360;

    private int heartCount = 3;
    private float KmCount = 0;
    private int coinCrashPos = 1320;
    private int coinSpeed = 60;
    private AtomicBoolean addCoins;
    private int coinsCounter = 0;
    private int countCarFinishLane = 0;
    private int raceCarPos = 1;
    private static final int DELAY = 70;
    private final int crashPos = 1200;
    private final int redCarStartPos = -240;
    private final int blueCarStartPos = -120;
    private Timer timer;

    private CountDownTimer countDownTimer;
    private long timeLeft = 4000;
    private boolean timerRunning;
    private TextView countTimer;

    private boolean onPause = false;
    private boolean ClickArrows = false;

    private float redCarGetYAfterPause;
    private float blueCarGetYAfterPause;
    private float coinGetYAfterPause;

    private float redCarGetXAfterPause;
    private float blueCarGetXAfterPause;
    private float raceCarGetXAfterPause;
    private float coinGetXAfterPause;

    private boolean coinAddAfterPause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_lane_road);
        //Toolbar
        ImageView menu = findViewById(R.id.Main_Textview_ToolBarExitToMenu);
        ImageView gameRestart = findViewById(R.id.Main_Textview_ToolBarGameRestart);
        ImageView gamePause = findViewById(R.id.Main_Textview_ToolBarGamePause);

        //KM
        TextView Km = findViewById(R.id.textView_KM);

        //coin
        TextView coins = findViewById(R.id.textView_Coin);
        ImageView coin = findViewById(R.id.ImageView_Gameplay_coin);
        coin.setImageResource(android.R.color.transparent);
        coin.setX(coin.getX() + 100);

        //hearts
        ImageView heart3 = findViewById(R.id.ImageView_Abilities_Heart3);
        ImageView heart2 = findViewById(R.id.ImageView_Abilities_Heart2);
        ImageView heart1 = findViewById(R.id.ImageView_Abilities_Heart1);

        //Timer
        countTimer = findViewById(R.id.textView_CountDownTimer);
        countTimer.setX(countTimer.getX() - 80);

        //Obstacles
        ImageView blueCar = findViewById(R.id.ImageView_Gameplay_BlueCar);
        ImageView redCar = findViewById(R.id.ImageView_Gameplay_RedCar);

        //Race car
        ImageView raceCar = findViewById(R.id.ImageView_RaceCarMovement_RaceCar);

        //Load "start engine" sound
        final MediaPlayer carStartEngine = MediaPlayer.create(this, R.raw.car_start_engine);

        //count down timer
        startStopTimer(carStartEngine);

        //KM text
        KmCount(Km);

        //add coin text
        addCoin(redCar, coins);

        //Exit To Menu
        menu.setOnClickListener(v -> goToMainScreen());

        //restart
        gameRestart.setOnClickListener(v -> {
            //Disable arrows
            ClickArrows = false;

            if (timeLeft == 0) {
                //count down timer
                timerReset(carStartEngine);


                timer.cancel();
                onPause = false;
                heartCount = 3;
                KmCount = 0;

                //coin reset
                coin.setY(0);
                coin.setImageResource(android.R.color.transparent);
                coinsCounter = 0;
                addCoins.set(false);
                countCarFinishLane = 0;
                coin.setX(((float) 1 * getScreenWidth() / laneRoad));

                heart1.setImageResource(R.drawable.heart);
                heart2.setImageResource(R.drawable.heart);
                heart3.setImageResource(R.drawable.heart);

                redCar.setImageResource(R.drawable.red_car);
                blueCar.setImageResource(R.drawable.blue_car);

                redCar.setY(redCarStartPos);
                blueCar.setY(blueCarStartPos);

                redCar.setX(0);
                blueCar.setX(((float) 2 * getScreenWidth() / laneRoad));

                raceCar.setX(((float) getScreenWidth() / laneRoad));

                gamePlay(blueCar, raceCar, heart1, heart2, heart3, redCar, Km, coin, coins);
            }
        });

        //Pause
        gamePause.setOnClickListener(v -> {
            //Disable arrows
            ClickArrows = false;

            if (timeLeft == 0) {
                if (!onPause) {
                    onPause = true;
                } else {
                    //count down timer
                    timer.cancel();
                    timerReset(carStartEngine);

                    redCarGetXAfterPause = redCar.getX();
                    blueCarGetXAfterPause = blueCar.getX();
                    raceCarGetXAfterPause = raceCar.getX();
                    coinGetXAfterPause = coin.getX();


                    redCarGetYAfterPause = redCar.getY();
                    blueCarGetYAfterPause = blueCar.getY();
                    coinGetYAfterPause = coin.getY();

                    coinAddAfterPause = addCoins.get();

                    gamePlay(blueCar, raceCar, heart1, heart2, heart3, redCar, Km, coin, coins);

                }
            }
        });


        //GamePlay Thread
        gamePlay(blueCar, raceCar, heart1, heart2, heart3, redCar, Km, coin, coins);

        //race_Car movement
        //Right arrow button
        ImageView Right_Arrow = findViewById(R.id.ImageButton_RightLeftBtn_RightArrow);

        //Left arrow button
        ImageView Left_Arrow = findViewById(R.id.ImageButton_RightLeftBtn_LeftArrow);

        //Language support
        String language = Locale.getDefault().getDisplayLanguage();
        if (language.equals("English")) {
            menu.setRotation(360);
            redCar.setX((float) (-2 * getScreenWidth() / laneRoad));
            Left_Arrow.setX((float) (2 * getScreenWidth() / laneRoad));
            Right_Arrow.setX((float) (-2 * getScreenWidth() / laneRoad));


        }

        Right_Arrow.setOnClickListener(v -> {
            if (ClickArrows) {
                if (raceCarPos == 1) {
                    raceCar.setX((0));
                    raceCarPos = 0;
                } else if (raceCarPos == 2) {
                    raceCar.setX(((float) getScreenWidth() / laneRoad));
                    raceCarPos = 1;
                }
            }
        });

        //Left arrow button
        Left_Arrow.setOnClickListener(v -> {
            if (ClickArrows) {
                if (raceCarPos == 0) {
                    raceCar.setX((float) (getScreenWidth() / laneRoad));
                    raceCarPos = 1;
                } else if (raceCarPos == 1) {
                    raceCar.setX((float) (2 * getScreenWidth() / laneRoad));
                    raceCarPos = 2;
                }
            }
        });
    }


    private void gamePlay(ImageView blueCar, ImageView raceCar, ImageView heart1, ImageView heart2, ImageView heart3, ImageView redCar, TextView Km, ImageView coin, TextView coins) {
        timer = new Timer();

        //coin
        addCoins = new AtomicBoolean();
        AtomicInteger randNewLaneCoin = new AtomicInteger();

        AtomicInteger randNewLaneBlueCar = new AtomicInteger();
        AtomicInteger randNewLaneRedCar = new AtomicInteger();
        AtomicBoolean isTransparent = new AtomicBoolean(false);

        ItemsPosInit(blueCar, raceCar, redCar, coin, randNewLaneRedCar, randNewLaneBlueCar, randNewLaneCoin);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (!onPause) {

                        //activate arrows
                        ClickArrows = true;

                        //timer reset
                        countTimer.setText("");
                        timeLeft = 0;

                        //start cars ride
                        startCarsRide(blueCar, redCar, isTransparent);

                        //red car new ride + crashed?
                        restartCarAndIsCrashed(randNewLaneRedCar, redCar, raceCar, heart1, heart2, heart3, redCarStartPos);

                        //blue car new ride + crashed?
                        restartCarAndIsCrashed(randNewLaneBlueCar, blueCar, raceCar, heart1, heart2, heart3, blueCarStartPos);

                        //cars in the same road
                        carInTheSameRoad(randNewLaneBlueCar, randNewLaneRedCar, blueCar, redCar, isTransparent);

                        //KM
                        KmCount(Km);

                        //add coin
                        addCoin(redCar, coins);

                        //Start coin movement
                        startCoinRide(coin, addCoins);

                        //restart Coin + IsEarned
                        restartCoinAndIsEarned(randNewLaneCoin, coin, raceCar);

                        //coin in the same road of cars
                        coinInTheSameRoad(randNewLaneBlueCar, randNewLaneRedCar, blueCar, redCar, randNewLaneCoin, coin);
                    }
                });
            }
        }, 3500, DELAY);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private int randNewLane() {
        return (int) (Math.random() * laneRoad);
    }

    private void vibrate(int milliseconds) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void KmCount(TextView Km) {
        String KmRes = String.format("%.1f", KmCount);
        Km.setText("      Km: " + KmRes);

        if (heartCount != 0) {
            KmCount += 0.003;
        }
    }

    @SuppressLint("SetTextI18n")
    private void addCoin(ImageView redCar, TextView coins) {
        coins.setText("  Coins : " + coinsCounter);

        if (redCar.getY() == crashPos - 120) {
            countCarFinishLane++;
            if (countCarFinishLane == 3) {
                addCoins.set(true);
            }
        }

    }

    private void ItemsPosInit(ImageView blueCar, ImageView raceCar, ImageView redCar, ImageView coin, AtomicInteger randNewLaneRedCar, AtomicInteger randNewLaneBlueCar, AtomicInteger randNewLaneCoin) {
        if (onPause) {

            //coin
            randNewLaneCoin.set((int) (coinGetXAfterPause / screenWidth));
            coin.setY(coinGetYAfterPause);
            addCoins.set(coinAddAfterPause);

            //race car
            raceCar.setX(raceCarGetXAfterPause);

            //cars
            randNewLaneRedCar.set((int) (redCarGetXAfterPause / screenWidth));
            randNewLaneBlueCar.set((int) (blueCarGetXAfterPause / screenWidth));

            redCar.setY(redCarGetYAfterPause);
            blueCar.setY(blueCarGetYAfterPause);

            onPause = false;
        } else {
            //coin lane start
            randNewLaneCoin.set(1);

            //cars lane start
            randNewLaneBlueCar.set(2);
            randNewLaneRedCar.set(0);
        }
    }

    //start cars ride
    private void startCarsRide(ImageView blueCar, ImageView redCar, AtomicBoolean isRedCarTransparent) {
        //blue car ride
        int carSpeed = 60;
        if (blueCar.getY() >= blueCarStartPos && blueCar.getY() < crashPos) {

            blueCar.setY(blueCar.getY() + carSpeed);
        }

        //red car ride
        if (redCar.getY() >= redCarStartPos && redCar.getY() < crashPos) {
            if (isRedCarTransparent.get()) {
                redCar.setY(redCarStartPos);
                redCar.setImageResource(R.drawable.red_car);
                isRedCarTransparent.set(false);
            }
            redCar.setY(redCar.getY() + carSpeed);
        }
    }

    //start coin ride
    private void startCoinRide(ImageView coin, AtomicBoolean addCoins) {

        if (addCoins.get()) {
            if (coin.getY() >= -240 && coin.getY() < coinCrashPos) {
                //coin move
                coin.setImageResource(R.drawable.coin);
                coin.setY(coin.getY() + coinSpeed);
            }
        }
    }

    //restart coin  + IsEarned?
    private void restartCoinAndIsEarned(AtomicInteger randNewLaneCoin, ImageView coin, ImageView raceCar) {

        if (coin.getY() == coinCrashPos) {
            int raceCarPosTemp = (int) (raceCar.getX() / screenWidth);

            //Earn a coin
            if (randNewLaneCoin.get() == raceCarPosTemp) {
                //coin sound
                final MediaPlayer coinSound = MediaPlayer.create(this, R.raw.coin_sound);
                coinSound.start();
                coinsCounter++;
            }

            addCoins.set(false);
            countCarFinishLane = 0;
            coin.setImageResource(android.R.color.transparent);


            //coin new ride
            randNewLaneCoin.set(randNewLane());
            coin.setX((float) (randNewLaneCoin.get() * getScreenWidth() / laneRoad) + 100);
            coin.setY(-120);
        }
    }

    //coin in the same road
    private void coinInTheSameRoad(AtomicInteger randNewLaneBlueCar, AtomicInteger randNewLaneRedCar, ImageView blueCar, ImageView redCar, AtomicInteger randNewLaneCoin, ImageView coin) {
        if (coin.getY() <= 400) {
            if ((randNewLaneBlueCar.get() == randNewLaneCoin.get() || randNewLaneRedCar.get() == randNewLaneCoin.get()) && (coin.getY() >= -240 && coin.getY() <= 300) && ((blueCar.getY() >= -240 && blueCar.getY() <= 300) || (redCar.getY() >= -240 && redCar.getY() <= 300))) {
                coin.setImageResource(android.R.color.transparent);

                randNewLaneCoin.set(randNewLane());
                coin.setX((float) (randNewLaneCoin.get() * getScreenWidth() / laneRoad) + 100);
                coin.setY(-240);
            }
        }
    }

    //restart car ride + IsCrashed?
    private void restartCarAndIsCrashed(AtomicInteger randNewLaneCar, ImageView car, ImageView raceCar, ImageView heart1, ImageView heart2, ImageView heart3, int carYStartPos) {
        if (car.getY() == crashPos) {

            //crashed
            int raceCarPosTemp = (int) (raceCar.getX() / screenWidth);
            if (randNewLaneCar.get() == raceCarPosTemp) {
                Crashed(timer, heart1, heart2, heart3);
            }

            //car new ride
            randNewLaneCar.set(randNewLane());
            car.setX((float) (randNewLaneCar.get() * getScreenWidth() / laneRoad));
            car.setY(carYStartPos);
        }
    }

    //cars in the same road
    private void carInTheSameRoad(AtomicInteger randNewLaneBlueCar, AtomicInteger randNewLaneRedCar, ImageView blueCar, ImageView redCar, AtomicBoolean isTransparent) {
        if ((randNewLaneBlueCar.get() == randNewLaneRedCar.get()) && ((redCar.getY() >= -240 && redCar.getY() <= 300) && (blueCar.getY() >= -240 && blueCar.getY() <= 300))) {

            redCar.setImageResource(android.R.color.transparent);
            randNewLaneRedCar.set(randNewLane());
            redCar.setX((float) (randNewLaneRedCar.get() * getScreenWidth() / laneRoad));
            isTransparent.set(true);
        }
    }

    //is crashed?
    private void Crashed(Timer timer, ImageView heart1, ImageView heart2, ImageView heart3) {
        //crash sound
        final MediaPlayer carCrashSound = MediaPlayer.create(this, R.raw.car_crash_sound);
        carCrashSound.start();

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
        ThreeLaneRoad.this.finish();
        Intent intentLoadGameOverScreen = new Intent(ThreeLaneRoad.this, GameOverScreen.class);
        startActivity(intentLoadGameOverScreen);

    }

    public void goToMainScreen() {
        timer.cancel();
        ThreeLaneRoad.this.finish();
        Intent intentLoadMainScreen = new Intent(ThreeLaneRoad.this, MainActivity.class);
        startActivity(intentLoadMainScreen);
    }

    private void timerReset(MediaPlayer carStartEngine) {
        timerRunning = false;
        countTimer.setText("3");
        timeLeft = 4000;
        startStopTimer(carStartEngine);
    }

    private void startStopTimer(MediaPlayer carStartEngine) {
        //car engine sound
        carStartEngine.start();

        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
            }
        }.start();
        timerRunning = true;
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;

    }

    private void updateTimer() {
        int sec = (int) timeLeft % 6000 / 1000;
        String timerLeftText = Integer.toString(sec);
        timeLeft += sec;

        countTimer.setText(timerLeftText);
    }
}