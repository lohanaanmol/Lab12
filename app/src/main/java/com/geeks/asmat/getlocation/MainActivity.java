package com.geeks.asmat.getlocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button locationBtn;
    Button resetBtn;
    TextView location_view;
    FusedLocationProviderClient locationProvider;
    int PERMISSIONS_REQUEST_CODE = 11;
    String[] PERMISSIONS = { Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.ACCESS_FINE_LOCATION};
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location_view = findViewById(R.id.location);
        locationBtn = findViewById(R.id.locationBtn);
        resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location_view.setText("");
            }
        });
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasPermissions(PERMISSIONS)){
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                }
                else{
                    init();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            init();
        }
        else{
            Toast.makeText(this, "Permission Required" , Toast.LENGTH_LONG).show();
        }
    }

    public boolean hasPermissions(String ... permissions){
        for(String permission : permissions)
            return ActivityCompat.checkSelfPermission(this , permission) == PackageManager.PERMISSION_GRANTED;
        return true;
    }

    public void init(){
        locationProvider = new FusedLocationProviderClient(this );
        try {
            locationProvider.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    try {
                        geocoder = new Geocoder(getApplicationContext());
                        List<Address> addressList = geocoder.getFromLocation(latitude , longitude , 1);
                        Address address = addressList.get(0);
                        Log.d("Error" , address.toString());
                        location_view.setText(address.getAddressLine(0));
                    }
                    catch (IOException e){
                        Log.d("ERROR" , e.getMessage());
                    }

                }
            });
        }catch (SecurityException e){
            Toast.makeText(this , "Please Grant Permissions" , Toast.LENGTH_LONG).show();
        }


    }
}
