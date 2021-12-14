package com.example.hw1.Model;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static com.example.hw1.Model.MainActivity.getCarColor;
import static com.example.hw1.Model.MainActivity.getIsSensorMode;
import static com.example.hw1.Model.MainActivity.getIsSensorSpeedMode;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.R;
import com.example.hw1.Utils.DataManager;
import com.example.hw1.Utils.MSP;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreeLaneRoad extends AppCompatActivity {

    //game setting
    private final int laneRoad = 3;
    private final int screenWidth = 360;
    private Timer timer;
    private static final int DELAY = 70;
    private boolean onPause = false;
    private boolean ClickArrows = false;
    public static Record record = new Record();
    public DataManager dataManager = new DataManager();


    //car color
    private final String getCarColor = getCarColor();

    //hearts
    private int heartCount = 3;

    //KM
    private float KmCount = 0;

    //coin
    private ImageView coin;

    private int coinsCounter = 0;
    private AtomicBoolean addCoins;
    private int coinCrashPos = 1320;
    private int coinSpeed = 60;
    private float coinGetXAfterPause;
    private float coinGetYAfterPause;
    private boolean coinAddAfterPause;
    private int coinStartPos = -240;


    //CountDown Timer
    private CountDownTimer countDownTimer;
    private long timeLeft = 4000;
    private boolean timerRunning;
    private TextView countTimer;


    //cars setting
    private ImageView raceCar;
    private ImageView redCar;
    private ImageView blueCar;

    private int carsSpeed = 60;
    private float redCarGetYAfterPause;
    private float blueCarGetYAfterPause;
    private int countCarFinishLane = 0;
    private int raceCarPos = 1;
    private int crashPos = 1200;
    private int redCarStartPos = -240;
    private int blueCarStartPos = -120;
    private float redCarGetXAfterPause;
    private float blueCarGetXAfterPause;
    private float raceCarGetXAfterPause;


    //Sensor Mode
    private boolean isSensorMode = getIsSensorMode();
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean sensorMovement = false;
    private boolean checkUpDown = false;

    //Sensor Speed Mode
    private final boolean isSensorSpeedMode = getIsSensorSpeedMode();

    //Get Location
    private double longitude = 0;
    private double latitude = 0;
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";


    private final SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];

            if (Math.abs(x) > Math.abs(y)) {
                if (sensorMovement) {

                    //Tilt Right
                    if (x < 0) {
                        if (raceCar.getX() <= screenWidth) {
                            raceCar.setX(raceCar.getX() + screenWidth);
                        }
                    }
                    //Tilt Left
                    if (x > 0) {
                        if (raceCar.getX() >= screenWidth) {
                            raceCar.setX(raceCar.getX() - screenWidth);
                        }
                    }
                }
            } else {
                if (isSensorSpeedMode) {

                    //Tilt Up
                    if (y < -3) {
                        carsSpeed = 90;
                        coinSpeed = 90;

                        crashPos = 1260;
                        blueCarStartPos = 0;
                        coinCrashPos = 1350;

                        redCarStartPos = -180;
                        coinStartPos = -180;

                        if (checkUpDown) {
                            checkUpDown = false;
                            redCar.setY(redCarStartPos);
                            blueCar.setY(blueCarStartPos);

                            coin.setY(coinStartPos);
                        }
                    }
                    //Tilt Down
                    if (y > 3) {
                        carsSpeed = 30;
                        coinSpeed = 30;

                        crashPos = 1200;
                        blueCarStartPos = 0;
                        redCarStartPos = -120;

                        checkUpDown = true;
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
        setContentView(R.layout.activity_three_lane_road);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Get location
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        latitude = bundle.getDouble(LATITUDE);
        longitude = bundle.getDouble(LONGITUDE);

        //Toolbar
        ImageView menu = findViewById(R.id.Main_Textview_ToolBarExitToMenu);
        ImageView gameRestart = findViewById(R.id.Main_Textview_ToolBarGameRestart);
        ImageView gamePause = findViewById(R.id.Main_Textview_ToolBarGamePause);

        //KM
        TextView Km = findViewById(R.id.textView_KM);

        //coin
        TextView coins = findViewById(R.id.textView_Coin);
        coin = findViewById(R.id.ImageView_Gameplay_coin);
        coin.setImageResource(android.R.color.transparent);
        coin.setX(coin.getX() + 100);

        //hearts
        ImageView heart3 = findViewById(R.id.ImageView_Abilities_Heart3);
        ImageView heart2 = findViewById(R.id.ImageView_Abilities_Heart2);
        ImageView heart1 = findViewById(R.id.ImageView_Abilities_Heart1);

        //Obstacles
        blueCar = findViewById(R.id.ImageView_Gameplay_BlueCar);
        redCar = findViewById(R.id.ImageView_Gameplay_RedCar);

        //Race car
        raceCar = findViewById(R.id.ImageView_RaceCarMovement_RaceCar);

        //get Car Color
        switch (getCarColor) {
            case "Red":
                raceCar.setImageResource(R.drawable.red_race_car);
                break;
            case "White":
                raceCar.setImageResource(R.drawable.white_race_car);
                break;
            case "Yellow":
                raceCar.setImageResource(R.drawable.yellow_race_car);
                break;
        }

        //Right arrow button
        ImageView Right_Arrow = findViewById(R.id.ImageButton_RightLeftBtn_RightArrow);

        //Left arrow button
        ImageView Left_Arrow = findViewById(R.id.ImageButton_RightLeftBtn_LeftArrow);

        //Timer
        countTimer = findViewById(R.id.textView_CountDownTimer);
        countTimer.setX(countTimer.getX() - 80);

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

        //SensorsMode
        if (isSensorMode) {
            initSensor();
            if (isSensorExists(TYPE_ACCELEROMETER)) {
                Left_Arrow.setImageResource(android.R.color.transparent);
                Right_Arrow.setImageResource(android.R.color.transparent);
            } else {
                isSensorMode = false;
                toast("Sensor node isn't available in this phone");
            }
        }

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

        //Language support
        String language = Locale.getDefault().getDisplayLanguage();
        if (language.equals("English")) {
            menu.setRotation(360);
            countTimer.setX(countTimer.getX() + 160);
            redCar.setX((float) (-2 * getScreenWidth() / laneRoad));
            blueCar.setX((float) (2 * getScreenWidth() / laneRoad));
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
        int getRedCarPosFinishLane = crashPos - 120;
        if (checkUpDown) {
            getRedCarPosFinishLane = crashPos - 180;
        }
        if (redCar.getY() == getRedCarPosFinishLane) {
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
                coin.setY(coinStartPos);
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
            //add Record
            @SuppressLint("DefaultLocale") float temp = Float.parseFloat(String.format("%.1f", KmCount));
            record.setKm(temp);
            record.setCoins(coinsCounter);
            record.setSensorModeActivate(isSensorMode);
            record.setLat(latitude);
            record.setLon(longitude);
            record.setGameMode("Easy");
            saveRecord();

            //game over
            ThreeLaneRoad.this.finish();
            goToGameOverScreen();
            vibrate(1000);
            timer.cancel();

        }
    }

    public void saveRecord() {
        String fromJSON = MSP.getInstance(this).getStrSP("dataManager", "");

        if (fromJSON.isEmpty()) {
            dataManager = new DataManager();
        } else {
            dataManager = new Gson().fromJson(fromJSON, DataManager.class);
        }

        dataManager.addRecord(record);

        String jsonRecords = new Gson().toJson(dataManager);
        MSP.getInstance(this).putStringSP("dataManager", jsonRecords);
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

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
    }

    public boolean isSensorExists(int sensorType) {
        return (sensorManager.getDefaultSensor(sensorType) != null);
    }

}