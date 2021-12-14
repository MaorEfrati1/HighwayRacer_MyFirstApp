package com.example.hw1.Adapters;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw1.R;
import com.example.hw1.Model.Record;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment fragment;
    private final ArrayList<Record> records;
    private RecordClickListener recordClickListener;

    public RecordAdapter(Fragment fragment, ArrayList<Record> records) {
        this.records = records;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_record_item, parent, false);
        return new RecordViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecordViewHolder recordViewHolder = (RecordViewHolder) holder;
        Record record = getItem(position);

        recordViewHolder.List_Record_IV_Place.setImageResource(getRecordImage(position));
        recordViewHolder.List_Record_TV_KM.setText(Float.toString(record.getKm()));
        recordViewHolder.List_Record_TV_Coins.setText(Integer.toString(record.getCoins()));
        recordViewHolder.List_Record_TV_SensorMode.setText(Boolean.toString(record.isSensorModeActivate()));
        recordViewHolder.List_Record_TV_GameMode.setText(record.getGameMode());

    }

    public RecordAdapter setRecordClickListener(RecordClickListener recordClickListener) {
        this.recordClickListener = recordClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    private Record getItem(int position) {
        return records.get(position);
    }

    public interface RecordClickListener {
        void recordItemClick(Record record, int position);
    }


    public class RecordViewHolder extends RecyclerView.ViewHolder {


        private final AppCompatImageView List_Record_IV_Place;
        private final MaterialTextView List_Record_TV_KM;
        private final MaterialTextView List_Record_TV_Coins;
        private final MaterialTextView List_Record_TV_SensorMode;
        private final MaterialTextView List_Record_TV_GameMode;


        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            List_Record_IV_Place = itemView.findViewById(R.id.List_Record_IV_Place);
            List_Record_TV_KM = itemView.findViewById(R.id.List_Record_TV_KM);
            List_Record_TV_Coins = itemView.findViewById(R.id.List_Record_TV_Coins);
            List_Record_TV_SensorMode = itemView.findViewById(R.id.List_Record_TV_SensorMode);
            List_Record_TV_GameMode = itemView.findViewById(R.id.List_Record_TV_GameMode);

            itemView.setOnClickListener(v -> recordClickListener.recordItemClick(getItem(getAdapterPosition()),getAdapterPosition()));
        }

    }


    private int getRecordImage(int position) {
        int imgId = 0;
        switch (position) {
            case 0:
                imgId = R.drawable.place_1;
                break;
            case 1:
                imgId = R.drawable.place_2;
                break;
            case 2:
                imgId = R.drawable.place_3;
                break;
            case 3:
                imgId = R.drawable.place_4;
                break;
            case 4:
                imgId = R.drawable.place_5;
                break;
            case 5:
                imgId = R.drawable.place_6;
                break;
            case 6:
                imgId = R.drawable.place_7;
                break;
            case 7:
                imgId = R.drawable.place_8;
                break;
            case 8:
                imgId = R.drawable.place_9;
                break;
            case 9:
                imgId = R.drawable.place_10;
                break;
        }
        return imgId;
    }
}
