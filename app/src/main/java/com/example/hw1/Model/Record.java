package com.example.hw1.Model;

public class Record implements Comparable<Record> {
    private float km = 0;
    private int coins = 0;
    private int imagePlace = 0;
    private String GameMode = "";
    private boolean SensorModeActivate = false;
    private double lat = 0.0;
    private double lon = 0.0;

    public int getImagePlace() {
        return imagePlace;
    }

    public Record setImagePlace(int imagePlace) {
        this.imagePlace = imagePlace;
        return this;
    }

    public Record() {
    }

    public float getKm() {
        return km;
    }

    public Record setKm(float km) {
        this.km = km;
        return this;
    }

    public int getCoins() {
        return coins;
    }

    public Record setCoins(int coins) {
        this.coins = coins;
        return this;
    }

    public String getGameMode() {
        return GameMode;
    }

    public Record setGameMode(String gameMode) {
        this.GameMode = gameMode;
        return this;
    }
    public double getLat() {
        return lat;
    }

    public Record setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Record setLon(double lon) {
        this.lon = lon;
        return this;
    }

    public boolean isSensorModeActivate() {
        return SensorModeActivate;
    }

    public Record setSensorModeActivate(boolean sensorModeActivate) {
        this.SensorModeActivate = sensorModeActivate;
        return this;
    }


    @Override
    public int compareTo(Record rec1) { // if new record is larger return 1 , else return -1
        if (rec1.getKm() != this.km) {
            if (rec1.getKm() > this.km)
                return 1;
            else
                return -1;
        } else {
            if (rec1.getCoins() > this.coins)
                return 1;
            else return -1;
        }
    }

}
