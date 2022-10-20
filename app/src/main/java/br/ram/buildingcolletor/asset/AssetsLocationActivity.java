package br.ram.buildingcolletor.asset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import br.ram.buildingcolletor.R;

public class AssetsLocationActivity extends AppCompatActivity {

    protected Button buttonAssetsList, buttonNext, buttonBack, buttonLocation, buttonDelete;
    private Intent nextActivity;
    protected FusedLocationProviderClient fusedLocationProviderClient;
    protected SupportMapFragment mapFragment;
    protected TextView address;
    protected String addressText = "";
    protected String assetName, assetArea, aDetails;
    protected Location location;
    protected MarkerOptions options;
    protected double lat;
    protected double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_location);

        //References
        buttonNext = findViewById(R.id.button_next_location);
        buttonBack = findViewById(R.id.button_back_location);
        buttonLocation = findViewById(R.id.button_current_location_assets);
        buttonDelete = findViewById(R.id.button_drop_location_assets);
        buttonAssetsList = findViewById(R.id.button1_location_assets);
        address = findViewById(R.id.address);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if(getIntent().hasExtra("name") && getIntent().hasExtra("area")) {

            //Geting Data from Intent
            assetName = getIntent().getStringExtra("name");
            assetArea = getIntent().getStringExtra("area");

        } else {
            Toast.makeText(AssetsLocationActivity.this, "No data.", Toast.LENGTH_SHORT).show();
        }

        if(getIntent().hasExtra("location")) {

            //Geting Data from Intent
            addressText = getIntent().getStringExtra("location");
            address.setText(addressText);

        }

        if(getIntent().hasExtra("details")) {

            //Geting Data from Intent
            aDetails = getIntent().getStringExtra("details");
        }

        if(getIntent().hasExtra("latitude") && getIntent().hasExtra("longitude")) {

            //Geting Data from Intent
            lat = getIntent().getExtras().getDouble("latitude");
            lon = getIntent().getExtras().getDouble("longitude");

                try {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng latLng = new LatLng(lat,
                                    lon);
                                options = new MarkerOptions().position(latLng)
                                        .title("I am here");
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
                                googleMap.addMarker(options);
                            }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

        }

        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(AssetsLocationActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    getLocation();

                } else {
                    ActivityCompat.requestPermissions(AssetsLocationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address.setText("");
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        googleMap.clear();
                    }
                });
                Toast.makeText(AssetsLocationActivity.this, "Location Delete", Toast.LENGTH_SHORT).show();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(addressText.equals("")) {

                        Toast.makeText(AssetsLocationActivity.this, "Please, get Current Location", Toast.LENGTH_SHORT).show();

                    } else{

                        try {
                            //Get GoogleMaps Location
                            lat = options.getPosition().latitude;
                            lon = options.getPosition().longitude;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        nextActivity = new Intent(AssetsLocationActivity.this, AssetsDetailsActivity.class);
                        nextActivity.putExtra("name",assetName);
                        nextActivity.putExtra("area", assetArea);
                        nextActivity.putExtra("location", addressText);
                        nextActivity.putExtra("latitude",lat);
                        nextActivity.putExtra("longitude", lon);
                        nextActivity.putExtra("details", aDetails);
                        finish();
                        startActivity(nextActivity);
                    }

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(AssetsLocationActivity.this, AssetsInfoActivity.class);
                nextActivity.putExtra("name", assetName);
                nextActivity.putExtra("area", assetArea);
                nextActivity.putExtra("location",addressText);
                nextActivity.putExtra("latitude",lat);
                nextActivity.putExtra("longitude", lon);
                nextActivity.putExtra("details", aDetails);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(AssetsLocationActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(AssetsLocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(AssetsLocationActivity.this,Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                    location.getLongitude(),1);

                            addressText = addresses.get(0).getAddressLine(0);

                            address.setText(Html.fromHtml(
                                    "<font color='#6200EE'><b>Location :</b><br></font>" +
                                            addressText
                            ));

                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    LatLng latLng = new LatLng(location.getLatitude(),
                                            location.getLongitude());
                                    options = new MarkerOptions().position(latLng)
                                            .title("I am here");
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
                                    googleMap.addMarker(options);
                                }
                            });

                            Toast.makeText(AssetsLocationActivity.this, "Current Location get Sucessfully!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } else {
            ActivityCompat.requestPermissions(AssetsLocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }
}