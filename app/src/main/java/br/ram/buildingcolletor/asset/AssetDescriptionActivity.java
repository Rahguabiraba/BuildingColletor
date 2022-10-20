package br.ram.buildingcolletor.asset;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import br.ram.buildingcolletor.database.DataBaseSQL;
import br.ram.buildingcolletor.asset.edit.EditAssetInfoActivity;
import br.ram.buildingcolletor.element.ElementsActivity;
import br.ram.buildingcolletor.R;

public class AssetDescriptionActivity extends AppCompatActivity {

    //Variables
    protected Button buttonAssetsList, buttonElementsList, buttonExport, buttonNext,buttonEdit, buttonBack, buttonCopy, buttonBackImage, buttonNextImage, buttonDelete, buttonNewAsset;
    private Intent nextActivity;
    protected TextView nameText, descriptionText, sizeText, locationText;
    protected String id, name, area, location, details, lastId;
    protected double lat, lon;
    protected ArrayList<String> imagesName, idImages;
    protected ArrayList<String> idAsset, nameAsset, areaAsset, locationAsset, detailsAsset;
    protected ArrayList<String> idElement, idAssets, nameElement, uniformatElement, categoryElement, typeElement, yearElement, quantityElement, conditionElement;
    protected ArrayList<String> idRequirement, idElements, typeRequirement, detailsRequirement;
    private ImageSwitcher imageCamera;
    private ArrayList<Uri> imageUris;
    private Bitmap image;
    private int position = 0;
    private static final int RQS_OPEN_DOCUMENT = 1;
    protected DataBaseSQL dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_description);

        //References
        buttonAssetsList = findViewById(R.id.button1_assetList_description);
        buttonElementsList = findViewById(R.id.button2_elementList_description);
        buttonNext = findViewById(R.id.button_next_description);
        buttonBack = findViewById(R.id.button_back_description);
        buttonDelete = findViewById(R.id.button_drop_assets_description);
        buttonNewAsset = findViewById(R.id.button_new_assets_description);
        buttonBackImage = findViewById(R.id.button_back_image_asset);
        buttonNextImage = findViewById(R.id.button_next_image_asset);
        buttonEdit = findViewById(R.id.button_edit_assets_description);
        buttonCopy = findViewById(R.id.button_copy_assets_description);
        buttonExport = findViewById(R.id.button_export_assets_description);
        nameText = findViewById(R.id.asset_description_title);
        descriptionText = findViewById(R.id.asset_description);
        sizeText = findViewById(R.id.asset_size_text_description);
        locationText = findViewById(R.id.asset_location_text_description);
        imageCamera = findViewById(R.id.image_camera_description);

        //Conection DataBase
        dataBase = new DataBaseSQL(AssetDescriptionActivity.this);

        //llamada del método para coger los datos enviados del Adapter y poner en los TextView
        getAndSetIntentData();

        imageUris = new ArrayList<>();
        imagesName = new ArrayList<>();
        idImages = new ArrayList<>();

        idAssets = new ArrayList<>();
        nameAsset = new ArrayList<>();
        areaAsset = new ArrayList<>();
        locationAsset = new ArrayList<>();
        detailsAsset = new ArrayList<>();

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

        getImagesPath();

        storeDataAsset();

        storeDataElement();

        storeDataRequirement();

        for (int i = 0; i<imagesName.size(); i++) {
            image = readFileFromInternalStorage(imagesName.get(i));
            String imagePath = MediaStore.Images.Media.insertImage(getContentResolver(),image,null,null);
            try {
                Uri uri = Uri.parse(imagePath);
                imageUris.add(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        imageCamera.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });

        try {
            imageCamera.setImageURI(imageUris.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Click handle, show previous image
        buttonBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    position--;
                    imageCamera.setImageURI(imageUris.get(position));
                } else {
                    Toast.makeText(AssetDescriptionActivity.this, "No Previous Images...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Click handle, show next image
        buttonNextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < imageUris.size() - 1) {
                    position++;
                    imageCamera.setImageURI(imageUris.get(position));
                } else {
                    Toast.makeText(AssetDescriptionActivity.this, "No More Images...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(AssetDescriptionActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(AssetDescriptionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

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

                sheet.setColumnWidth(0, (20 * 100));
                sheet.setColumnWidth(1, (30 * 200));
                sheet.setColumnWidth(2, (30 * 200));
                sheet.setColumnWidth(3, (30 * 200));
                sheet.setColumnWidth(4, (30 * 200));

                for (int i = 0; i < idAssets.size(); i++) {
                    Row rowAsset = sheet.createRow(i + 1);

                    cell = rowAsset.createCell(0);
                    cell.setCellValue(idAssets.get(i));

                    cell = rowAsset.createCell(1);
                    cell.setCellValue(nameAsset.get(i));

                    cell = rowAsset.createCell(2);
                    cell.setCellValue(areaAsset.get(i));

                    cell = rowAsset.createCell(3);
                    cell.setCellValue(locationAsset.get(i));

                    cell = rowAsset.createCell(4);
                    cell.setCellValue(detailsAsset.get(i));

                    sheet.setColumnWidth(0, (20 * 100));
                    sheet.setColumnWidth(1, (30 * 200));
                    sheet.setColumnWidth(2, (30 * 200));
                    sheet.setColumnWidth(3, (30 * 200));
                    sheet.setColumnWidth(4, (30 * 200));
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

                sheet2.setColumnWidth(0, (20 * 100));
                sheet2.setColumnWidth(1, (20 * 100));
                sheet2.setColumnWidth(2, (30 * 200));
                sheet2.setColumnWidth(3, (30 * 200));
                sheet2.setColumnWidth(4, (30 * 200));
                sheet2.setColumnWidth(5, (30 * 200));
                sheet2.setColumnWidth(6, (30 * 200));
                sheet2.setColumnWidth(7, (30 * 200));
                sheet2.setColumnWidth(8, (30 * 200));


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

                    sheet2.setColumnWidth(0, (20 * 100));
                    sheet2.setColumnWidth(1, (20 * 100));
                    sheet2.setColumnWidth(2, (30 * 200));
                    sheet2.setColumnWidth(3, (30 * 200));
                    sheet2.setColumnWidth(4, (30 * 200));
                    sheet2.setColumnWidth(5, (30 * 200));
                    sheet2.setColumnWidth(6, (30 * 200));
                    sheet2.setColumnWidth(7, (30 * 200));
                    sheet2.setColumnWidth(8, (30 * 200));
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

                sheet3.setColumnWidth(0, (20 * 100));
                sheet3.setColumnWidth(1, (30 * 200));
                sheet3.setColumnWidth(2, (30 * 200));
                sheet3.setColumnWidth(3, (30 * 200));


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


                    sheet3.setColumnWidth(0, (20 * 100));
                    sheet3.setColumnWidth(1, (30 * 200));
                    sheet3.setColumnWidth(2, (30 * 200));
                    sheet3.setColumnWidth(3, (30 * 200));
                }


                try {

                    String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/Buildings/Archives";

                    File fileDir = new File(path);
                    if (!fileDir.exists())
                        fileDir.mkdir();


                    String mPath = path + "/Buildings_" + new Date().getTime() + ".xls";

                    File file = new File(mPath);
                    FileOutputStream fOut = new FileOutputStream(file);

                    wb.write(fOut);

                    Toast.makeText(AssetDescriptionActivity.this, "Excel Create Sucessfully!", Toast.LENGTH_SHORT).show();

                    if (fOut != null) {
                        fOut.flush();
                        fOut.close();
                    }

                }

                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AssetDescriptionActivity.this, "Excel not Create.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonElementsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(AssetDescriptionActivity.this, ElementsActivity.class);
                nextActivity.putExtra("idAsset",id);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonNewAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(AssetDescriptionActivity.this, AssetsPhotoActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmDropDialog();

            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextActivity = new Intent(AssetDescriptionActivity.this, EditAssetInfoActivity.class);
                nextActivity.putExtra("id", id);
                nextActivity.putExtra("name", name);
                nextActivity.putExtra("area", area);
                nextActivity.putExtra("location", location);
                nextActivity.putExtra("details", details);
                nextActivity.putExtra("latitude",lat);
                nextActivity.putExtra("longitude", lon);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmCopyDialog();

            }
        });

    }

    private void storeDataAsset() {
        Cursor cursor = dataBase.readAllDataAsset();

        while (cursor.moveToNext()) {
            idAssets.add(cursor.getString(0));
            nameAsset.add(cursor.getString(1));
            areaAsset.add(cursor.getString(2));
            locationAsset.add(cursor.getString(3));
            detailsAsset.add(cursor.getString(4));
        }
    }

    private void storeDataRequirement() {
        Cursor cursor = dataBase.readAllDataRequirement();

        while (cursor.moveToNext()) {
            idRequirement.add(cursor.getString(0));
            idElements.add(cursor.getString(1));
            typeRequirement.add(cursor.getString(2));
            detailsRequirement.add(cursor.getString(3));
        }

    }

    private void storeDataElement() {
        Cursor cursor = dataBase.readAllDataElement();

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

    private void confirmDropDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Asset?");
        builder.setMessage("Are you sure you want to delete Asset " + name + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dataBase.deleteAsset(id);
                Toast.makeText(AssetDescriptionActivity.this,"Asset delete Sucessfully!", Toast.LENGTH_SHORT).show();

                nextActivity = new Intent(AssetDescriptionActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AssetDescriptionActivity.this, "Asset not delete", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();

    }

    private void confirmCopyDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Copy Asset?");
        builder.setMessage("Are you sure you want to copy Asset " + name + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int in) {

                dataBase.addAsset(name,area,location,lat, lon, details);
                Toast.makeText(AssetDescriptionActivity.this,"Asset Copy Sucessfully!", Toast.LENGTH_SHORT).show();

                //Function to get last ID add
                storeIdAsset();

                for (int i=0; i<imagesName.size(); i++) {
                    dataBase.addImage(lastId,imagesName.get(i));
                }


                nextActivity = new Intent(AssetDescriptionActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AssetDescriptionActivity.this, "Asset Not Copy", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();

    }

    private void getAndSetIntentData() {
        if(getIntent().hasExtra("name") && getIntent().hasExtra("id") && getIntent().hasExtra("area")
                && getIntent().hasExtra("location") && getIntent().hasExtra("details")
        && getIntent().hasExtra("latitude") && getIntent().hasExtra("longitude")) {

            //Geting Data from Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            area = getIntent().getStringExtra("area");
            location = getIntent().getStringExtra("location");
            details = getIntent().getStringExtra("details");
            lat = getIntent().getExtras().getDouble("latitude");
            lon = getIntent().getExtras().getDouble("longitude");

            //Setting Intent Data
            nameText.setText("Asset " + name);
            descriptionText.setText(Html.fromHtml(
                    "<font color='#6200EE'><b>Description: </b></font>" + details
            ));
            sizeText.setText(Html.fromHtml(
                    "<font color='#6200EE'><b>Size: </b></font>" + area
            ));
            locationText.setText(Html.fromHtml(
                    "<font color='#6200EE'><b>Location: </b></font>" + location
            ));

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getImagesPath() {
        Cursor cursor = dataBase.readImagePath(id);

        while (cursor.moveToNext()) {
            imagesName.add(cursor.getString(0));
        }
    }

    public void storeIdAsset() {
        Cursor cursor = dataBase.readAllIdAsset();

        cursor.moveToLast();

        lastId = cursor.getString(0);
    }

    private Bitmap readFileFromInternalStorage(String imageName){

            try {
                String imPath = Environment.getExternalStorageDirectory().toString() + "/DCIM/ImagesAsset" + imageName;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap mustOpen = BitmapFactory.decodeFile(imPath, options);

                return mustOpen;

            } catch (Exception e) {
                e.printStackTrace();
            }

        return null;
    }
}