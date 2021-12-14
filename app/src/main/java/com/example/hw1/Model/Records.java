package com.example.hw1.Model;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.R;
import com.example.hw1.Utils.CallBack_List;
import com.example.hw1.Utils.DataManager;
import com.example.hw1.Utils.MSP;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

public class Records extends AppCompatActivity {
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Read Data From MSP
        String fromJson = MSP.getInstance(this).getStrSP("dataManager", "");
        if (fromJson.isEmpty()) {
            dataManager = new DataManager();
        } else {
            dataManager = new Gson().fromJson(fromJson, DataManager.class);
        }

        //Show Records Header
        MaterialTextView header = findViewById(R.id.fragmentList_RV_Cols);
        if(dataManager.getRecords().isEmpty()){
            header.setText(R.string.RecordsInfoEmpty);
        }else{
            header.setText(R.string.RecordsInfo);
        }

        //Map Fragment
        MapFragment mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.Records_FL_ShowLocation, mapFragment).commit();


        //List Fragment
        ListFragment listFragment = new ListFragment();
        listFragment.setActivity(this);
        getSupportFragmentManager().beginTransaction().add(R.id.Records_FL_ShowRecords, listFragment).commit();

        CallBack_List callBack_list = mapFragment::changeMap;
        listFragment.setActivity(this);
        listFragment.setCallbackList(callBack_list);


        //Toolbar
        ImageView menu = findViewById(R.id.Main_Textview_ToolBarExitToMenu);
        ImageView gameRestart = findViewById(R.id.Main_Textview_ToolBarGameRestart);
        ImageView gamePause = findViewById(R.id.Main_Textview_ToolBarGamePause);

        //Exit To Menu
        menu.setOnClickListener(v -> goToMainScreen());
        //Restart
        gameRestart.setImageResource(android.R.color.transparent);
        //Pause
        gamePause.setImageResource(android.R.color.transparent);

    }

    public void goToMainScreen() {
        Records.this.finish();
        Intent intentLoadMainScreen = new Intent(Records.this, MainActivity.class);
        startActivity(intentLoadMainScreen);
    }


}