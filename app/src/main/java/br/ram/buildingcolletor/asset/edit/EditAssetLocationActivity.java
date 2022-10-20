package br.ram.buildingcolletor.asset.edit;

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
import br.ram.buildingcolletor.asset.AssetsActivity;

public class EditAssetLocationActivity extends AppCompatActivity {

    protected Button buttonAssetsList, buttonNext, buttonBack, buttonLocation, buttonDelete;
    private Intent nextActivity;
    protected FusedLocationProviderClient fusedLocationProviderClient;
    protected SupportMapFragment mapFragment;
    protected TextView address;
    protected String addressText = "";
    protected String id, name, area, details;
    protected Location location;
    protected MarkerOptions options;
    protected double lat;
    protected double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_asset_location);

        //References
        buttonNext = findViewById(R.id.button_next_location_edit);
        buttonBack = findViewById(R.id.button_back_location_edit);
        buttonLocation = findViewById(R.id.button_current_location_assets_edit);
        buttonDelete = findViewById(R.id.button_drop_location_assets_edit);
        buttonAssetsList = findViewById(R.id.button1_location_assets_edit);
        address = findViewById(R.id.address_edit);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_edit);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getAndSetIntentData();

        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(EditAssetLocationActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    getLocation();

                } else {
                    ActivityCompat.requestPermissions(EditAssetLocationActivity.this,
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
                Toast.makeText(EditAssetLocationActivity.this, "Location Delete", Toast.LENGTH_SHORT).show();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(addressText.equals("")) {

                    Toast.makeText(EditAssetLocationActivity.this, "Please, get Current Location", Toast.LENGTH_SHORT).show();

                } else{

                    try {
                        //Get GoogleMaps Location
                        lat = options.getPosition().latitude;
                        lon = options.getPosition().longitude;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    nextActivity = new Intent(EditAssetLocationActivity.this, EditAssetDetailsActivity.class);
                    nextActivity.putExtra("id", id);
                    nextActivity.putExtra("name",name);
                    nextActivity.putExtra("area", area);
                    nextActivity.putExtra("location", addressText);
                    nextActivity.putExtra("latitude",lat);
                    nextActivity.putExtra("longitude", lon);
                    nextActivity.putExtra("details", details);
                    finish();
                    startActivity(nextActivity);
                }

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(EditAssetLocationActivity.this, EditAssetInfoActivity.class);
                nextActivity.putExtra("id", id);
                nextActivity.putExtra("name", name);
                nextActivity.putExtra("area", area);
                nextActivity.putExtra("location",addressText);
                nextActivity.putExtra("latitude",lat);
                nextActivity.putExtra("longitude", lon);
                nextActivity.putExtra("details", details);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(EditAssetLocationActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });
    }

    private void getAndSetIntentData() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("area")
                && getIntent().hasExtra("location") && getIntent().hasExtra("details")
                && getIntent().hasExtra("latitude") && getIntent().hasExtra("longitude")) {

            //Geting Data from Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            area = getIntent().getStringExtra("area");
            addressText = getIntent().getStringExtra("location");
            details = getIntent().getStringExtra("details");
            lat = getIntent().getExtras().getDouble("latitude");
            lon = getIntent().getExtras().getDouble("longitude");

            //Setting Data from Intent
            address.setText(addressText);

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

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(EditAssetLocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(EditAssetLocationActivity.this, Locale.getDefault());
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

                            Toast.makeText(EditAssetLocationActivity.this, "Current Location get Sucessfully!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } else {
            ActivityCompat.requestPermissions(EditAssetLocationActivity.this,
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