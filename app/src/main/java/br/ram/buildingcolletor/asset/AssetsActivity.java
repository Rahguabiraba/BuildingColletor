package br.ram.buildingcolletor.asset;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import br.ram.buildingcolletor.asset.custom.CustomAdapterAsset;
import br.ram.buildingcolletor.database.DataBaseSQL;
import br.ram.buildingcolletor.R;
import br.ram.buildingcolletor.RecyclerViewClickListener;

public class AssetsActivity extends AppCompatActivity implements RecyclerViewClickListener {

    //Variables
    private ViewStub myViewStub;
    protected View assetsGroup;
    protected Button buttonNewAsset, buttonAssetList, buttonExport, buttonNext;
    private Intent nextActivity;
    protected ArrayList<String> id, name, area, location, details;
    protected ArrayList<String> idElement, idAsset, nameElement, uniformatElement, categoryElement, typeElement, yearElement, quantityElement, conditionElement;
    protected ArrayList<String> idRequirement, idElements, typeRequirement, detailsRequirement;
    protected ArrayList<Double> latitude, longitude;
    protected double lat, lon;
    protected ConstraintLayout layoutGoogleMap;
    protected File file;
    protected int position;
    protected CustomAdapterAsset customAdapter;
    protected RecyclerView recyclerView;
    protected DataBaseSQL database;
    protected FusedLocationProviderClient fusedLocationProviderClient;
    protected SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets);

        //Reference
        myViewStub = findViewById(R.id.emptyStateLayout);
        buttonNewAsset = findViewById(R.id.button1_assets);
        buttonAssetList = findViewById(R.id.button2_assets);
        buttonExport = findViewById(R.id.button4_assets);
        buttonNext = findViewById(R.id.button_next_asset_description);
        assetsGroup = findViewById(R.id.assetsGroup);
        layoutGoogleMap = findViewById(R.id.layoutGoogleMap);
        recyclerView = findViewById(R.id.asset_rv);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_asset);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Conection with DataBase
        database = new DataBaseSQL(this);

        id = new ArrayList<>();
        name = new ArrayList<>();
        area = new ArrayList<>();
        location = new ArrayList<>();
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        details = new ArrayList<>();

        idElement = new ArrayList<>();
        idAsset = new ArrayList<>();
        nameElement = new ArrayList<>();
        uniformatElement = new ArrayList<>();
        categoryElement = new ArrayList<>();
        typeElement = new ArrayList<>();
        yearElement = new ArrayList<>();
        quantityElement = new ArrayList<>();
        conditionElement = new ArrayList<>();

        idRequirement = new ArrayList<>();
        idElements = new ArrayList<>();
        typeRequirement = new ArrayList<>();
        detailsRequirement = new ArrayList<>();

        //llamada del método para coger los datos de la base de datos
        storeDataArrays();

        storeDataElement();

        storeDataRequirement();

        //llamada del adaptador. Los datos aquí son enviados para el Adaptador y después son puestos en el recycler view
        customAdapter = new CustomAdapterAsset(AssetsActivity.this,this, name);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Button to add Asset
        buttonNewAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(AssetsActivity.this, AssetsPhotoActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonAssetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(AssetsActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonExport.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(AssetsActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);

                Workbook wb = new HSSFWorkbook();

                Cell cell = null;
                Sheet sheet = null;

                sheet = wb.createSheet("Asset Information");

                //Now column and row
                Row row = sheet.createRow(0);

                cell = row.createCell(0);
                cell.setCellValue("ID");

                cell = row.createCell(1);
                cell.setCellValue("NAME");

                cell = row.createCell(2);
                cell.setCellValue("AREA");

                cell = row.createCell(3);
                cell.setCellValue("LOCATION");

                cell = row.createCell(4);
                cell.setCellValue("DETAILS");

                sheet.setColumnWidth(0,(20 * 100));
                sheet.setColumnWidth(1,(30 * 200));
                sheet.setColumnWidth(2,(30 * 200));
                sheet.setColumnWidth(3,(30 * 200));
                sheet.setColumnWidth(4,(30 * 200));

                for (int i = 0; i < id.size(); i++) {
                    Row rowAsset = sheet.createRow(i + 1);

                    cell = rowAsset.createCell(0);
                    cell.setCellValue(id.get(i));

                    cell = rowAsset.createCell(1);
                    cell.setCellValue(name.get(i));

                    cell = rowAsset.createCell(2);
                    cell.setCellValue(area.get(i));

                    cell = rowAsset.createCell(3);
                    cell.setCellValue(location.get(i));

                    cell = rowAsset.createCell(4);
                    cell.setCellValue(details.get(i));

                    sheet.setColumnWidth(0,(20 * 100));
                    sheet.setColumnWidth(1,(30 * 200));
                    sheet.setColumnWidth(2,(30 * 200));
                    sheet.setColumnWidth(3,(30 * 200));
                    sheet.setColumnWidth(4,(30 * 200));
                }

                Cell cell2 = null;
                Sheet sheet2 = null;

                sheet2 = wb.createSheet("Element Information");

                //Now column and row
                Row row2 = sheet2.createRow(0);

                cell2 = row2.createCell(0);
                cell2.setCellValue("ID");

                cell2 = row2.createCell(1);
                cell2.setCellValue("IDASSET");

                cell2 = row2.createCell(2);
                cell2.setCellValue("NAME");

                cell2 = row2.createCell(3);
                cell2.setCellValue("UNIFORMAT");

                cell2 = row2.createCell(4);
                cell2.setCellValue("CATEGORY");

                cell2 = row2.createCell(5);
                cell2.setCellValue("TYPE");

                cell2 = row2.createCell(6);
                cell2.setCellValue("YEAR");

                cell2 = row2.createCell(7);
                cell2.setCellValue("QUANTITY");

                cell2 = row2.createCell(8);
                cell2.setCellValue("CONDITION");

                sheet2.setColumnWidth(0,(20 * 100));
                sheet2.setColumnWidth(1,(20 * 100));
                sheet2.setColumnWidth(2,(30 * 200));
                sheet2.setColumnWidth(3,(30 * 200));
                sheet2.setColumnWidth(4,(30 * 200));
                sheet2.setColumnWidth(5,(30 * 200));
                sheet2.setColumnWidth(6,(30 * 200));
                sheet2.setColumnWidth(7,(30 * 200));
                sheet2.setColumnWidth(8,(30 * 200));


                for (int i = 0; i < idElement.size(); i++) {
                    Row rowElement = sheet2.createRow(i + 1);

                    cell2 = rowElement.createCell(0);
                    cell2.setCellValue(idElement.get(i));

                    cell2 = rowElement.createCell(1);
                    cell2.setCellValue(idAsset.get(i));

                    cell2 = rowElement.createCell(2);
                    cell2.setCellValue(nameElement.get(i));

                    cell2 = rowElement.createCell(3);
                    cell2.setCellValue(uniformatElement.get(i));

                    cell2 = rowElement.createCell(4);
                    cell2.setCellValue(categoryElement.get(i));

                    cell2 = rowElement.createCell(5);
                    cell2.setCellValue(typeElement.get(i));

                    cell2 = rowElement.createCell(6);
                    cell2.setCellValue(yearElement.get(i));

                    cell2 = rowElement.createCell(7);
                    cell2.setCellValue(quantityElement.get(i));

                    cell2 = rowElement.createCell(8);
                    cell2.setCellValue(conditionElement.get(i));

                    sheet2.setColumnWidth(0,(20 * 100));
                    sheet2.setColumnWidth(1,(20 * 100));
                    sheet2.setColumnWidth(2,(30 * 200));
                    sheet2.setColumnWidth(3,(30 * 200));
                    sheet2.setColumnWidth(4,(30 * 200));
                    sheet2.setColumnWidth(5,(30 * 200));
                    sheet2.setColumnWidth(6,(30 * 200));
                    sheet2.setColumnWidth(7,(30 * 200));
                    sheet2.setColumnWidth(8,(30 * 200));
                }

                Cell cell3 = null;
                Sheet sheet3 = null;

                sheet3 = wb.createSheet("Requirements Information");

                //Now column and row
                Row row3 = sheet3.createRow(0);

                cell3 = row3.createCell(0);
                cell3.setCellValue("ID");

                cell3 = row3.createCell(1);
                cell3.setCellValue("IDELEMENT");

                cell3 = row3.createCell(2);
                cell3.setCellValue("REQUIREMENT TYPE");

                cell3 = row3.createCell(3);
                cell3.setCellValue("DESCRIPTION");

                sheet3.setColumnWidth(0,(20 * 100));
                sheet3.setColumnWidth(1,(30 * 200));
                sheet3.setColumnWidth(2,(30 * 200));
                sheet3.setColumnWidth(3,(30 * 200));


                for (int i = 0; i < idRequirement.size(); i++) {
                    Row rowRequirement = sheet3.createRow(i + 1);

                    cell3 = rowRequirement.createCell(0);
                    cell3.setCellValue(idRequirement.get(i));

                    cell3 = rowRequirement.createCell(1);
                    cell3.setCellValue(idElements.get(i));

                    cell3 = rowRequirement.createCell(2);
                    cell3.setCellValue(typeRequirement.get(i));

                    cell3 = rowRequirement.createCell(3);
                    cell3.setCellValue(detailsRequirement.get(i));


                    sheet3.setColumnWidth(0,(20 * 100));
                    sheet3.setColumnWidth(1,(30 * 200));
                    sheet3.setColumnWidth(2,(30 * 200));
                    sheet3.setColumnWidth(3,(30 * 200));
                }

                try {

                    String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/Buildings";

                    File fileDir = new File(path);
                    if (!fileDir.exists())
                        fileDir.mkdir();

                    String mPath = path + "/Buildings_" + new Date().getTime() + ".xls";

                    file = new File(mPath);
                    FileOutputStream fOut = new FileOutputStream(file);

                    wb.write(fOut);

                    Toast.makeText(AssetsActivity.this, "Excel Create Sucessfully!", Toast.LENGTH_SHORT).show();

                    if (fOut != null) {
                        fOut.flush();
                        fOut.close();
                    }

                } catch (Exception e) {
                        e.printStackTrace();
                    Toast.makeText(AssetsActivity.this, "Excel not Create.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    nextActivity = new Intent(AssetsActivity.this, AssetDescriptionActivity.class);
                    nextActivity.putExtra("id",String.valueOf(id.get(position)));
                    nextActivity.putExtra("name", String.valueOf(name.get(position)));
                    nextActivity.putExtra("area", String.valueOf(area.get(position)));
                    nextActivity.putExtra("location", String.valueOf(location.get(position)));
                    nextActivity.putExtra("details", String.valueOf(details.get(position)));
                    nextActivity.putExtra("latitude",Double.valueOf(latitude.get(position)));
                    nextActivity.putExtra("longitude", Double.valueOf(longitude.get(position)));
                    finish();
                    startActivity(nextActivity);
            }

        });
    }

    private void showFileChooser() {

        try {
            Uri excelFile = Uri.parse(file.toString());
            Intent intent = new Intent(Intent.ACTION_VIEW,excelFile);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Please install a file manager", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void storeDataRequirement() {
        Cursor cursor = database.readAllDataRequirement();

        while (cursor.moveToNext()) {
            idRequirement.add(cursor.getString(0));
            idElements.add(cursor.getString(1));
            typeRequirement.add(cursor.getString(2));
            detailsRequirement.add(cursor.getString(3));
        }

    }

    private void storeDataElement() {
        Cursor cursor = database.readAllDataElement();

        while (cursor.moveToNext()) {
            idElement.add(cursor.getString(0));
            idAsset.add(cursor.getString(1));
            nameElement.add(cursor.getString(2));
            uniformatElement.add(cursor.getString(3));
            categoryElement.add(cursor.getString(4));
            typeElement.add(cursor.getString(5));
            yearElement.add(cursor.getString(6));
            quantityElement.add(cursor.getString(7));
            conditionElement.add(cursor.getString(8));
        }
    }

    void storeDataArrays() {
        Cursor cursor = database.readAllDataAsset();

        //ViewSub para poner Lottlie image (el GIF con el emoticon)
        myViewStub.inflate();
        if (cursor.getCount() == 0) {
            myViewStub.setVisibility(View.VISIBLE);
            assetsGroup.setVisibility(View.GONE);
            layoutGoogleMap.setVisibility(View.GONE);
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                area.add(cursor.getString(2));
                location.add(cursor.getString(3));
                latitude.add(cursor.getDouble(4));
                longitude.add(cursor.getDouble(5));
                details.add(cursor.getString(6));

                //Visibilidad de las vistas
                myViewStub.setVisibility(View.GONE);
                buttonNext.setVisibility(View.VISIBLE);
                assetsGroup.setVisibility(View.VISIBLE);
                layoutGoogleMap.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(AssetsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {

                        try {
                            lat = Double.valueOf(latitude.get(position));
                            lon = Double.valueOf(longitude.get(position));

                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    LatLng latLng = new LatLng(lat,lon);
                                    MarkerOptions options = new MarkerOptions().position(latLng)
                                            .title("I am here");
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
                                    googleMap.addMarker(options);
                                }
                            });

                            Toast.makeText(AssetsActivity.this, "Asset: " + String.valueOf(name.get(position)), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                          e.printStackTrace();
                        }
                    }
                }
            });

        } else {
            ActivityCompat.requestPermissions(AssetsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

        this.position = position;

        if (ActivityCompat.checkSelfPermission(AssetsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getLocation();

        } else {
            ActivityCompat.requestPermissions(AssetsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }
}