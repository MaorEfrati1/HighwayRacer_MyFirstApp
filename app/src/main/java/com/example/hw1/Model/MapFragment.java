package com.example.hw1.Model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hw1.R;
import com.example.hw1.Utils.CallBack_List;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {
    private CallBack_List callBack_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map , container , false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(googleMap -> {

            LatLng City = new LatLng(32.0805782, 34.8673394);
            googleMap.addMarker(new MarkerOptions().position(City).title("Marker in Petah-Tikva"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(City));
        });
        return view;
    }


    public void changeMap(double lat , double lon){
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(googleMap -> {
            LatLng latLng = new LatLng(lat, lon);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(latLng.latitude + " : " + latLng.longitude);
            googleMap.clear();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            googleMap.addMarker(markerOptions);
        });

    }
    public void setCallBack_list(CallBack_List callBack_list) {
        this.callBack_list = callBack_list;
    }
}