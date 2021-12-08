package com.example.hw1;

import static com.example.hw1.MainActivity.getGameMode;
import static com.example.hw1.MainActivity.getIsSensorMode;
import static com.example.hw1.MainActivity.getIsSensorSpeedMode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FiveLaneRoad extends AppCompatActivity {

    //game setting
    private final int laneRoad = 5;
    private final int screenWidth = 216;
    private Timer timer;
    private static final int DELAY = 70;
    private boolean onPause = false;
    private boolean ClickArrows = false;
    private final int mode = getGameMode();

    //hearts
    private int heartCount = 3;

    //KM
    private float KmCount = 0;

    //coin
    private ImageView coin;

    private int coinsCounter = 0;
    private AtomicBoolean addCoins;
    private int countCarFinishLane = 0;
    private int coinCrashPos = 1440;
    private int coinSpeed = 60;
    private int coinStartPos = -240;
    private float coinGetYAfterPause;
    private float coinGetXAfterPause;
    private boolean coinAddAfterPause;

    //CountDownTimer
    private CountDownTimer countDownTimer;
    private long timeLeft = 4000;
    private boolean timerRunning;
    private TextView countTimer;

    //cars setting
    private ImageView raceCar;
    private ImageView blueCar;
    private ImageView redCar;
    private ImageView yellowCar;

    private float redCarGetYAfterPause;
    private float blueCarGetYAfterPause;
    private float yellowCarGetYAfterPause;

    private float redCarGetXAfterPause;
    private float blueCarGetXAfterPause;
    private float yellowCarGetXAfterPause;

    private float raceCarGetXAfterPause;

    private int raceCarPos = 2;
    private int carsSpeed = 60;
    private int yellowCarSpeed = 90;
    private int crashPos = 1380;
    private int yellowCrashPos = 1440;
    private int blueCarStartPos = 0;
    private int redCarStartPos = -120;
    private int yellowCarStartPos = -180;

    //Sensor Mode
    private final boolean isSensorMode = getIsSensorMode();
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean sensorMovement = false;
    private boolean checkUpDown = false;

    //Sensor Speed Mode
    private final boolean isSensorSpeedMode = getIsSensorSpeedMode();
    private boolean sensorSpeed = true;


    private final SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];

            if (Math.abs(x) > Math.abs(y)) {

                if (sensorMovement) {
                    //Tilt Right
                    if (x < 0) {
                        if (raceCar.getX() <= screenWidth * 3) {
                            raceCar.setX(raceCar.getX() + screenWidth);
                        }
                    }
                    //Tilt Left
                    if (x > 0) {
                        if (raceCar.getX() >= 216) {
                            raceCar.setX(raceCar.getX() - screenWidth);
                        }
                    }
                }
            } else {
                if (isSensorSpeedMode) {
                    if (sensorSpeed) {

                        //Tilt Up
                        if (y < -3) {

                            carsSpeed = 90;
                            yellowCarSpeed = 120;
                            coinSpeed = 90;

                            crashPos = 1350;
                            yellowCrashPos = 1440;
                            coinCrashPos = 1440;
                            blueCarStartPos = 0;
                            redCarStartPos = -180;
                            yellowCarStartPos = -240;
                            coinStartPos = -180;


                            if (checkUpDown) {
                                checkUpDown = false;

                                redCar.setY(redCarStartPos);
                                blueCar.setY(blueCarStartPos);
                                yellowCar.setY(yellowCarStartPos);

                                coin.setY(coinStartPos);
                            }
                        }
                        //Tilt Down
                        if (y > 3) {
                            carsSpeed = 30;
                            coinSpeed = 30;

                            yellowCarSpeed = 60;
                            crashPos = 1380;
                            yellowCrashPos = 1440;
                            blueCarStartPos = 0;
                            redCarStartPos = -120;
                            yellowCarStartPos = -180;

                            checkUpDown = true;

                        }
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorMode) {
            sensorManager.registerListener(accSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister Sensor listener
        if (isSensorMode) {
            sensorManager.unregisterListener(accSensorEventListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_lane_road);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Toolbar
        ImageView menu = findViewById(R.id.Main_Textview_ToolBarExitToMenu);
        ImageView gameRestart = findViewById(R.id.Main_Textview_ToolBarGameRestart);
        ImageView gamePause = findViewById(R.id.Main_Textview_ToolBarGamePause);

        //score
        TextView Km = findViewById(R.id.textView_KM);

        //hearts
        ImageView heart3 = findViewById(R.id.ImageView_Abilities_Heart3);
        ImageView heart2 = findViewById(R.id.ImageView_Abilities_Heart2);
        ImageView heart1 = findViewById(R.id.ImageView_Abilities_Heart1);

        //Obstacles
        blueCar = findViewById(R.id.ImageView_Gameplay_BlueCar);
        redCar = findViewById(R.id.ImageView_Gameplay_RedCar);
        yellowCar = findViewById(R.id.ImageView_Gameplay_yellowCar);

        //Race car
        raceCar = findViewById(R.id.ImageView_RaceCarMovement_RaceCar);

        //Right arrow button
        ImageView Right_Arrow = findViewById(R.id.ImageButton_RightLeftBtn_RightArrow);

        //Left arrow button
        ImageView Left_Arrow = findViewById(R.id.ImageButton_RightLeftBtn_LeftArrow);

        //Timer
        countTimer = findViewById(R.id.textView_CountDownTimer);

        //Load "start engine" sound
        final MediaPlayer carStartEngine = MediaPlayer.create(this, R.raw.car_start_engine);

        //count down timer
        startStopTimer(carStartEngine);

        //coin
        TextView coins = findViewById(R.id.textView_Coin);
        coin = findViewById(R.id.ImageView_Gameplay_coin);
        coin.setImageResource(android.R.color.transparent);
        coin.setX(coin.getX());

        //add coin set text
        addCoin(redCar, coins);

        //KM set text
        KmCount(Km);

        //SensorsMode
        if (isSensorMode) {
            Left_Arrow.setImageResource(android.R.color.transparent);
            Right_Arrow.setImageResource(android.R.color.transparent);
            //if (isSensorExists(sensor.TYPE_ACCELEROMETER)){
            initSensor();
            //}
        }


        //setup for hard mode
        if (mode == 3) {
            sensorSpeed = false;
            carsSpeed = 90;
            yellowCarSpeed = 120;
            coinSpeed = 90;

            crashPos = 1350;
            yellowCrashPos = 1440;
            coinCrashPos = 1440;

            redCarStartPos = -180;
            yellowCarStartPos = -240;
            coinStartPos = -180;
        }

        //Exit To Menu
        menu.setOnClickListener(v -> goToMainScreen());

        //restart
        gameRestart.setOnClickListener(v -> {
            //Disable arrows
            ClickArrows = false;

            //Disable sensor
            sensorMovement = false;
            checkUpDown = false;

            if (timeLeft == 0) {
                //count down timer
                timerReset(carStartEngine);

                timer.cancel();
                heartCount = 3;
                KmCount = 0;

                //coin reset
                coin.setY(0);
                coin.setImageResource(android.R.color.transparent);
                coinsCounter = 0;
                addCoins.set(false);
                countCarFinishLane = 0;
                coin.setX(((float) 2 * getScreenWidth() / laneRoad));

                heart1.setImageResource(R.drawable.heart);
                heart2.setImageResource(R.drawable.heart);
                heart3.setImageResource(R.drawable.heart);

                redCar.setImageResource(R.drawable.red_car);
                blueCar.setImageResource(R.drawable.blue_car);
                yellowCar.setImageResource(R.drawable.yellow_race_car);

                redCar.setY(redCarStartPos);
                blueCar.setY(blueCarStartPos);
                yellowCar.setY(yellowCarStartPos);

                redCar.setX(0);
                blueCar.setX(((float) 1 * getScreenWidth() / laneRoad));
                yellowCar.setX(((float) 3 * getScreenWidth() / laneRoad));

                raceCar.setX(((float) 2 * getScreenWidth() / laneRoad));

                gamePlay(blueCar, raceCar, heart1, heart2, heart3, redCar, yellowCar, Km, coin, coins);
            }
        });

        //Pause
        gamePause.setOnClickListener(v -> {
            //Disable arrows
            ClickArrows = false;

            //Disable sensor
            sensorMovement = false;

            if (timeLeft == 0) {
                if (!onPause) {
                    onPause = true;
                } else {
                    //count down timer
                    timer.cancel();
                    timerReset(carStartEngine);

                    redCarGetXAfterPause = redCar.getX();
                    blueCarGetXAfterPause = blueCar.getX();
                    yellowCarGetXAfterPause = yellowCar.getX();
                    raceCarGetXAfterPause = raceCar.getX();
                    coinGetXAfterPause = coin.getX();

                    redCarGetYAfterPause = redCar.getY();
                    blueCarGetYAfterPause = blueCar.getY();
                    yellowCarGetYAfterPause = yellowCar.getY();
                    coinGetYAfterPause = coin.getY();

                    coinAddAfterPause = addCoins.get();

                    gamePlay(blueCar, raceCar, heart1, heart2, heart3, redCar, yellowCar, Km, coin, coins);
                }
            }
        });


        //GamePlay Thread
        gamePlay(blueCar, raceCar, heart1, heart2, heart3, redCar, yellowCar, Km, coin, coins);

        //Language support
        String language = Locale.getDefault().getDisplayLanguage();
        if (language.equals("English")) {
            menu.setRotation(360);
            redCar.setX((float) (-4 * getScreenWidth() / laneRoad));
            blueCar.setX((float) (-2 * getScreenWidth() / laneRoad));
            yellowCar.setX((float) (2 * getScreenWidth() / laneRoad));
            Left_Arrow.setX((float) (3.5 * getScreenWidth() / laneRoad));
            Right_Arrow.setX((float) (-3.5 * getScreenWidth() / laneRoad));
        }

        Right_Arrow.setOnClickListener(v -> {
            if (ClickArrows) {
                if (raceCarPos == 1) {
                    raceCar.setX((0));
                    raceCarPos = 0;
                } else if (raceCarPos == 2) {
                    raceCar.setX(((float) getScreenWidth() / laneRoad));
                    raceCarPos = 1;
                } else if (raceCarPos == 3) {
                    raceCar.setX((2 * (float) getScreenWidth() / laneRoad));
                    raceCarPos = 2;
                } else if (raceCarPos == 4) {
                    raceCar.setX((3 * (float) getScreenWidth() / laneRoad));
                    raceCarPos = 3;
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
                } else if (raceCarPos == 2) {
                    raceCar.setX((float) (3 * getScreenWidth() / laneRoad));
                    raceCarPos = 3;
                } else if (raceCarPos == 3) {
                    raceCar.setX((float) (4 * getScreenWidth() / laneRoad));
                    raceCarPos = 4;
                }
            }
        });
    }


    private void gamePlay(ImageView blueCar, ImageView raceCar, ImageView heart1, ImageView heart2, ImageView heart3, ImageView redCar, ImageView yellowCar, TextView Km, ImageView coin, TextView coins) {
        AtomicBoolean isRedCarTransparent = new AtomicBoolean(false);
        AtomicBoolean isYellowCarTransparent = new AtomicBoolean(false);

        //coin
        addCoins = new AtomicBoolean();
        AtomicInteger randNewLaneCoin = new AtomicInteger();

        //cars lane random
        AtomicInteger randNewLaneBlueCar = new AtomicInteger();
        AtomicInteger randNewLaneRedCar = new AtomicInteger();
        AtomicInteger randNewLaneYellowCar = new AtomicInteger();

        carPosInit(blueCar, raceCar, redCar, yellowCar, randNewLaneRedCar, randNewLaneBlueCar, randNewLaneYellowCar, coin, randNewLaneCoin);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (!onPause) {
                        //activate arrows
                        if (isSensorMode) {
                            ClickArrows = false;
                            sensorMovement = true;
                            checkUpDown = true;
                        } else {
                            ClickArrows = true;
                        }
                        //timer reset
                        countTimer.setText("");
                        timeLeft = 0;

                        //start cars ride
                        startCarsRide(blueCar, redCar, yellowCar, isRedCarTransparent, isYellowCarTransparent);

                        //blue car new ride + crashed?
                        restartCarAndIsCrashed(randNewLaneBlueCar, blueCar, raceCar, heart1, heart2, heart3, blueCarStartPos);

                        //red car new ride + crashed?
                        restartCarAndIsCrashed(randNewLaneRedCar, redCar, raceCar, heart1, heart2, heart3, redCarStartPos);

                        //yellow car new ride + crashed?
                        restartCarAndIsCrashed(randNewLaneYellowCar, yellowCar, raceCar, heart1, heart2, heart3, yellowCarStartPos);

                        YellowCarInTheSameRoad(randNewLaneBlueCar, randNewLaneRedCar, randNewLaneYellowCar, yellowCar, isYellowCarTransparent, randNewLaneCoin);

                        //cars in the same road
                        carInTheSameRoad(randNewLaneBlueCar, randNewLaneRedCar, randNewLaneYellowCar, blueCar, redCar, yellowCar, isRedCarTransparent, isYellowCarTransparent);

                        //score
                        KmCount(Km);

                        //add coin
                        addCoin(redCar, coins);

                        //Start coin movement
                        startCoinRide(coin, addCoins);

                        //restart Coin + IsEarned
                        restartCoinAndIsEarned(randNewLaneCoin, coin, raceCar);

                        //coin in the same road of cars
                        coinInTheSameRoad(randNewLaneBlueCar, randNewLaneRedCar, blueCar, redCar, randNewLaneCoin, coin, randNewLaneYellowCar, yellowCar);
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
            if (mode == 3) {
                KmCount += 0.006;
            } else {
                KmCount += 0.003;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void addCoin(ImageView redCar, TextView coins) {
        coins.setText("  Coins : " + coinsCounter);
        int getRedCarPosFinishLane = crashPos - 120;
        if (mode == 3 || checkUpDown) {
            getRedCarPosFinishLane = crashPos - 180;
        }
        if (redCar.getY() == getRedCarPosFinishLane) {
            countCarFinishLane++;
            if (countCarFinishLane == 2) {
                addCoins.set(true);
            }
        }

    }

    private void carPosInit(ImageView blueCar, ImageView raceCar, ImageView redCar, ImageView yellowCar, AtomicInteger randNewLaneRedCar, AtomicInteger randNewLaneBlueCar, AtomicInteger randNewLaneYellowCar, ImageView coin, AtomicInteger randNewLaneCoin) {
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
            randNewLaneYellowCar.set((int) (yellowCarGetXAfterPause / screenWidth));

            redCar.setY(redCarGetYAfterPause);
            blueCar.setY(blueCarGetYAfterPause);
            yellowCar.setY(yellowCarGetYAfterPause);

            onPause = false;
        } else {
            //cars lane start
            randNewLaneRedCar.set(0);
            randNewLaneBlueCar.set(1);
            randNewLaneYellowCar.set(3);

            //coin lane start
            randNewLaneCoin.set(4);
        }
    }

    //yellow car in the same road of another cars or coin
    private void YellowCarInTheSameRoad(AtomicInteger randNewLaneBlueCar, AtomicInteger randNewLaneRedCar, AtomicInteger randNewLaneYellowCar, ImageView yellowCar, AtomicBoolean isYellowCarTransparent, AtomicInteger randNewLaneCoin) {
        if (yellowCar.getY() <= 500) {
            if ((randNewLaneBlueCar.get() == randNewLaneYellowCar.get() || randNewLaneRedCar.get() == randNewLaneYellowCar.get() || randNewLaneCoin.get() == randNewLaneYellowCar.get())) {
                yellowCar.setImageResource(android.R.color.transparent);
                isYellowCarTransparent.set(true);

                randNewLaneYellowCar.set(randNewLane());
                yellowCar.setX((float) (randNewLaneYellowCar.get() * getScreenWidth() / laneRoad));
                yellowCar.setY(-90);
            }
        }
    }

    //start cars ride
    private void startCarsRide(ImageView blueCar, ImageView redCar, ImageView yellowCar, AtomicBoolean isRedCarTransparent, AtomicBoolean isYellowCarTransparent) {
        //blue car ride
        if (blueCar.getY() >= blueCarStartPos && blueCar.getY() < crashPos) {
            blueCar.setY(blueCar.getY() + carsSpeed);
        }

        //red car ride
        if (redCar.getY() >= redCarStartPos && redCar.getY() < crashPos) {
            if (isRedCarTransparent.get()) {
                redCar.setY(redCarStartPos);
                redCar.setImageResource(R.drawable.red_car);
                isRedCarTransparent.set(false);
            }
            redCar.setY(redCar.getY() + carsSpeed);
        }

        //yellow car ride
        if (yellowCar.getY() >= yellowCarStartPos && yellowCar.getY() < yellowCrashPos) {
            if (isYellowCarTransparent.get()) {
                yellowCar.setY(yellowCarStartPos);
                yellowCar.setImageResource(R.drawable.yellow_race_car);
                isYellowCarTransparent.set(false);
            }
            yellowCar.setY(yellowCar.getY() + yellowCarSpeed);
        }

    }

    //start coin ride
    private void startCoinRide(ImageView coin, AtomicBoolean addCoins) {
        if (addCoins.get()) {
            if (coin.getY() >= coinStartPos && coin.getY() < coinCrashPos) {
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
            //  coin.setImageResource(android.R.color.transparent);


            //coin new ride
            randNewLaneCoin.set(randNewLane());
            coin.setX((float) (randNewLaneCoin.get() * getScreenWidth() / laneRoad));
            coin.setY(coinStartPos);
        }
    }

    //coin in the same road
    private void coinInTheSameRoad(AtomicInteger randNewLaneBlueCar, AtomicInteger randNewLaneRedCar, ImageView blueCar, ImageView redCar, AtomicInteger randNewLaneCoin, ImageView coin, AtomicInteger randNewLaneYellowCar, ImageView yellowCar) {
        if (coin.getY() <= 500) {
            if ((randNewLaneBlueCar.get() == randNewLaneCoin.get() || randNewLaneRedCar.get() == randNewLaneCoin.get() || randNewLaneYellowCar.get() == randNewLaneCoin.get()) && (coin.getY() >= -240 && coin.getY() <= 300) && ((blueCar.getY() >= -240 && blueCar.getY() <= 300) || (redCar.getY() >= -240 && redCar.getY() <= 300) || (yellowCar.getY() >= -240 && yellowCar.getY() <= 300))) {
                coin.setImageResource(android.R.color.transparent);

                randNewLaneCoin.set(randNewLane());
                coin.setX((float) (randNewLaneCoin.get() * getScreenWidth() / laneRoad));
                coin.setY(coinStartPos);
            }
        }
    }

    //restart car ride + IsCrashed?
    private void restartCarAndIsCrashed(AtomicInteger randNewLaneCar, ImageView Car, ImageView raceCar, ImageView heart1, ImageView heart2, ImageView heart3, int carYPos) {

        if (Car.getY() == crashPos || Car.getY() == yellowCrashPos) {

            //crashed
            int raceCarPos1 = (int) (raceCar.getX() / screenWidth);
            if (randNewLaneCar.get() == raceCarPos1) {
                Crashed(timer, heart1, heart2, heart3);
            }

            //car new ride
            randNewLaneCar.set(randNewLane());
            Car.setX((float) (randNewLaneCar.get() * getScreenWidth() / laneRoad));
            Car.setY(carYPos);
        }
    }

    //cars in the same road
    private void carInTheSameRoad(AtomicInteger randNewLaneBlueCar, AtomicInteger randNewLaneRedCar, AtomicInteger randNewLaneYellowCar, ImageView blueCar, ImageView redCar, ImageView yellowCar, AtomicBoolean isRedCarTransparent, AtomicBoolean isYellowCarTransparent) {

        //blue and red cars in the same lane
        if (randNewLaneBlueCar.get() == randNewLaneRedCar.get() && (redCar.getY() >= -240 && redCar.getY() <= 300 && blueCar.getY() >= -240 && blueCar.getY() <= 300)) {
            redCar.setImageResource(android.R.color.transparent);
            randNewLaneRedCar.set(randNewLane());
            redCar.setX((float) (randNewLaneRedCar.get() * getScreenWidth() / laneRoad));
            isRedCarTransparent.set(true);
        }

        //blue and yellow cars in the same lane
        if (randNewLaneBlueCar.get() == randNewLaneYellowCar.get() && (yellowCar.getY() >= -240 && yellowCar.getY() <= 300 && blueCar.getY() >= -240 && blueCar.getY() <= 300)) {
            yellowCar.setImageResource(android.R.color.transparent);
            randNewLaneYellowCar.set(randNewLane());
            yellowCar.setX((float) (randNewLaneYellowCar.get() * getScreenWidth() / laneRoad));
            isYellowCarTransparent.set(true);
        }

        //red and yellow cars in the same lane
        if (randNewLaneYellowCar.get() == randNewLaneRedCar.get() && (redCar.getY() >= -240 && redCar.getY() <= 250 && yellowCar.getY() >= -300 && yellowCar.getY() <= 300)) {
            redCar.setImageResource(android.R.color.transparent);
            yellowCar.setImageResource(android.R.color.transparent);

            randNewLaneRedCar.set(randNewLane());
            randNewLaneYellowCar.set(randNewLane());

            redCar.setX((float) (randNewLaneRedCar.get() * getScreenWidth() / laneRoad));
            yellowCar.setX((float) (randNewLaneYellowCar.get() * getScreenWidth() / laneRoad));

            isRedCarTransparent.set(true);
            isYellowCarTransparent.set(true);
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
        FiveLaneRoad.this.finish();
        Intent intentLoadGameOverScreen = new Intent(com.example.hw1.FiveLaneRoad.this, GameOverScreen.class);
        startActivity(intentLoadGameOverScreen);

    }

    public void goToMainScreen() {
        timer.cancel();
        com.example.hw1.FiveLaneRoad.this.finish();
        Intent intentLoadMainScreen = new Intent(com.example.hw1.FiveLaneRoad.this, MainActivity.class);
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

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

//    public boolean isSensorExists(int sensorType) {
//        return (sensorManager.getDefaultSensor(sensorType) != null);
//    }

}