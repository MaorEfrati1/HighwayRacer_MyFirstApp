package com.example.hw1.Model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw1.Adapters.RecordAdapter;
import com.example.hw1.R;
import com.example.hw1.Utils.CallBack_List;
import com.example.hw1.Utils.DataManager;
import com.example.hw1.Utils.MSP;
import com.google.gson.Gson;

public class ListFragment extends Fragment {

    private RecyclerView fragmentList_RV_Records;
    private AppCompatActivity activity;
    private CallBack_List callbackList;

    public void setCallbackList(CallBack_List callbackList) {
        this.callbackList = callbackList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);

        String msp = MSP.getInstance(activity).getStrSP("dataManager", "");
        DataManager dataManager;
        if (msp.isEmpty()) {
            dataManager = new DataManager();
        } else {
            dataManager = new Gson().fromJson(msp, DataManager.class);
        }

        RecordAdapter recordAdapter = new RecordAdapter(this, dataManager.getRecords());

        fragmentList_RV_Records.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentList_RV_Records.setHasFixedSize(true);
        fragmentList_RV_Records.setItemAnimator(new DefaultItemAnimator());
        fragmentList_RV_Records.setAdapter(recordAdapter);

        recordAdapter.setRecordClickListener((record, position) -> {
            if (callbackList != null) {
                double lat = record.getLat();
                double lon = record.getLon();
                callbackList.setMapLocation(lat, lon);
            }

        });
        return view;
    }

    private void findViews(View view) {
        fragmentList_RV_Records = view.findViewById(R.id.fragmentList_RV_Records);
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

}