package com.example.hw1.Utils;

import com.example.hw1.Model.Record;

import java.util.ArrayList;

public class DataManager {
    public ArrayList<Record> records = new ArrayList<>();
    private static final int MAX_RECORDS = 10;

    public DataManager() {
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public void addRecord(Record record) {
        this.records.add(record);
        sortByKm();
        if (this.records.size() > MAX_RECORDS)
            this.records.remove(MAX_RECORDS);

    }

    private void sortByKm() {
        records.sort((o1, o2) -> (int) (o2.getKm()*10 - o1.getKm()*10));
    }



}
