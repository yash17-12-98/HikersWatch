package com.yashuthar.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    public void updateLocationInfo(Location location){

        Log.i("locationInfo", location.toString());

        TextView latTextview = (TextView) findViewById(R.id.latTextview);
        TextView lonTextview = (TextView) findViewById(R.id.lonTextview);
        TextView accTextview = (TextView) findViewById(R.id.accTextview);
        TextView altTextview = (TextView) findViewById(R.id.altTextview);

        latTextview.setText("Latitude : " + location.getLatitude());
        lonTextview.setText("Longitude : " + location.getLongitude());
        accTextview.setText("Accuracy : " + location.getAccuracy());
        altTextview.setText("Altitude : " + location.getAltitude());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            String address = " couldn't find address";
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(listAddress != null && listAddress.size() > 0){
                address = "Address : \n";
                Log.i("PlaceInfo", listAddress.get(0).toString());

                if(listAddress.get(0).getSubThoroughfare() != null){
                    address += listAddress.get(0).getSubThoroughfare() + "\n";
                }
                if(listAddress.get(0).getThoroughfare() != null){
                    address += listAddress.get(0).getThoroughfare() + "\n";
                }
                if(listAddress.get(0).getLocality() != null){
                    address += listAddress.get(0).getLocality() + "\n";
                }
                if(listAddress.get(0).getPostalCode() != null){
                    address += listAddress.get(0).getPostalCode() + "\n";
                }
                if(listAddress.get(0).getCountryName() != null){
                    address += listAddress.get(0).getCountryName();
                }
            }
            TextView addressTextview = (TextView) findViewById(R.id.addressTextview);
            addressTextview.setText(address);

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            startlistening();
        }
    }
    public void startlistening(){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(Build.VERSION.SDK_INT < 23){
            startlistening();
        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                if (location != null) {
                    updateLocationInfo(location);
                }
            }
        }
    }
}