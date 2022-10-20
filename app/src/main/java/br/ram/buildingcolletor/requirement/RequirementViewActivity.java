package br.ram.buildingcolletor.requirement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import br.ram.buildingcolletor.R;
import br.ram.buildingcolletor.asset.AssetsActivity;
import br.ram.buildingcolletor.database.DataBaseSQL;
import br.ram.buildingcolletor.element.ElementDescriptionActivity;
import br.ram.buildingcolletor.element.ElementsActivity;
import br.ram.buildingcolletor.requirement.edit.EditRequirementViewActivity;

public class RequirementViewActivity extends AppCompatActivity {

    //Variables
    protected Button buttonAssetsList, buttonElementsList, buttonExport,buttonEdit, buttonBack, buttonCopy, buttonDelete, buttonNewRequirement;
    private Intent nextActivity;
    protected TextView nameText, descriptionText;
    protected String idAsset, idElement,idRequirement, requirementsType, requirementsDetails;
    protected ArrayList<String> idAssets1, nameAssets, areaAsset, locationAsset, detailsAsset;
    protected ArrayList<String> idElements1, idAssets2, nameElement, uniformatElement, categoryElement, typeElement, yearElement, quantityElement, conditionElement;
    protected ArrayList<String> idRequirements, idElements2, typeRequirement, detailsRequirement;
    protected ArrayList<String> imagesName;
    protected DataBaseSQL dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_view);

        //References
        buttonAssetsList = findViewById(R.id.button_assetList_requirement_description);
        buttonElementsList = findViewById(R.id.button_elementList_requirement_description);
        buttonExport = findViewById(R.id.button_export_requirement_description);
        buttonBack = findViewById(R.id.button_back_requirement_description);
        buttonDelete = findViewById(R.id.button_drop_requirement_description);
        buttonNewRequirement = findViewById(R.id.button_new_requirement_description);
        buttonEdit = findViewById(R.id.button_edit_requirement_description);
        buttonCopy = findViewById(R.id.button_copy_requirement_description);
        nameText = findViewById(R.id.requirement_type_text_view);
        descriptionText = findViewById(R.id.requirement_details_text_view);

        //Conection DataBase
        dataBase = new DataBaseSQL(RequirementViewActivity.this);

        imagesName = new ArrayList<>();

        idAssets1 = new ArrayList<>();
        nameAssets = new ArrayList<>();
        areaAsset = new ArrayList<>();
        locationAsset = new ArrayList<>();
        detailsAsset = new ArrayList<>();

        idElements1 = new ArrayList<>();
        idAssets2 = new ArrayList<>();
        nameElement = new ArrayList<>();
        uniformatElement = new ArrayList<>();
        categoryElement = new ArrayList<>();
        typeElement = new ArrayList<>();
        yearElement = new ArrayList<>();
        quantityElement = new ArrayList<>();
        conditionElement = new ArrayList<>();

        idRequirements = new ArrayList<>();
        idElements2 = new ArrayList<>();
        typeRequirement = new ArrayList<>();
        detailsRequirement = new ArrayList<>();

        //llamada del método para coger los datos enviados del Adapter y poner en los TextView
        getAndSetIntentData();

        storeDataAsset();

        storeDataElement();

        storeDataRequirement();

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(RequirementViewActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonElementsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(RequirementViewActivity.this, ElementsActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(RequirementViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
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

                for (int i = 0; i < idAssets1.size(); i++) {
                    Row rowAsset = sheet.createRow(i + 1);

                    cell = rowAsset.createCell(0);
                    cell.setCellValue(idAssets1.get(i));

                    cell = rowAsset.createCell(1);
                    cell.setCellValue(nameAssets.get(i));

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


                for (int i = 0; i < idElements1.size(); i++) {
                    Row rowElement = sheet2.createRow(i + 1);

                    cell2 = rowElement.createCell(0);
                    cell2.setCellValue(idElements1.get(i));

                    cell2 = rowElement.createCell(1);
                    cell2.setCellValue(idAssets2.get(i));

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


                for (int i = 0; i < idRequirements.size(); i++) {
                    Row rowRequirement = sheet3.createRow(i + 1);

                    cell3 = rowRequirement.createCell(0);
                    cell3.setCellValue(idRequirements.get(i));

                    cell3 = rowRequirement.createCell(1);
                    cell3.setCellValue(idElements2.get(i));

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

                    Toast.makeText(RequirementViewActivity.this, "Excel Create Sucessfully!", Toast.LENGTH_SHORT).show();

                    if (fOut != null) {
                        fOut.flush();
                        fOut.close();
                    }

                }

                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RequirementViewActivity.this, "Excel not Create.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(RequirementViewActivity.this, RequirementsListActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                nextActivity.putExtra("idElement",idElement);
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
                nextActivity = new Intent(RequirementViewActivity.this, EditRequirementViewActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                nextActivity.putExtra("idElement",idElement);
                nextActivity.putExtra("idRequirement",idRequirement);
                nextActivity.putExtra("requirementsType",requirementsType);
                nextActivity.putExtra("requirementsDetails",requirementsDetails);
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
    private void confirmDropDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Requirement?");
        builder.setMessage("Are you sure you want to delete Requirement " + requirementsType + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dataBase.deleteRequirement(idRequirement);
                Toast.makeText(RequirementViewActivity.this,"Requirement delete Sucessfully!", Toast.LENGTH_SHORT).show();

                nextActivity = new Intent(RequirementViewActivity.this, RequirementsListActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                nextActivity.putExtra("idElement",idElement);
                finish();
                startActivity(nextActivity);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(RequirementViewActivity.this, "Requirement not delete", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();

    }

    private void confirmCopyDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Copy Requirement?");
        builder.setMessage("Are you sure you want to copy Requirement " + requirementsType + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int in) {

                dataBase.addRequirements(idElement,requirementsType,requirementsDetails);

                Toast.makeText(RequirementViewActivity.this,"Requirement Copy Sucessfully!", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(RequirementViewActivity.this, RequirementsListActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                nextActivity.putExtra("idElement",idElement);
                finish();
                startActivity(nextActivity);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(RequirementViewActivity.this, "Requirement Not Copy", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();

    }


    private void getAndSetIntentData() {
        if(getIntent().hasExtra("idAsset") && getIntent().hasExtra("idElement") && getIntent().hasExtra("idRequirement")
                && getIntent().hasExtra("requirementsType") && getIntent().hasExtra("requirementsDetails")) {

            //Geting Data from Intent
            idAsset = getIntent().getStringExtra("idAsset");
            idElement = getIntent().getStringExtra("idElement");
            idRequirement = getIntent().getStringExtra("idRequirement");
            requirementsType = getIntent().getStringExtra("requirementsType");
            requirementsDetails = getIntent().getStringExtra("requirementsDetails");

            //Setting Intent Data
            nameText.setText(Html.fromHtml(
                    "<font color='#6200EE'><b>Requirement Type: </b></font>" + requirementsType
            ));
            descriptionText.setText(Html.fromHtml(
                    "<font color='#6200EE'><b>Description: </b></font>" + requirementsDetails
            ));

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeDataAsset() {
        Cursor cursor = dataBase.readAllDataAsset();

        while (cursor.moveToNext()) {
            idAssets1.add(cursor.getString(0));
            nameAssets.add(cursor.getString(1));
            areaAsset.add(cursor.getString(2));
            locationAsset.add(cursor.getString(3));
            detailsAsset.add(cursor.getString(4));
        }
    }

    private void storeDataRequirement() {
        Cursor cursor = dataBase.readAllDataRequirement();

        while (cursor.moveToNext()) {
            idRequirements.add(cursor.getString(0));
            idElements2.add(cursor.getString(1));
            typeRequirement.add(cursor.getString(2));
            detailsRequirement.add(cursor.getString(3));
        }

    }

    private void storeDataElement() {
        Cursor cursor = dataBase.readAllDataElement();

        while (cursor.moveToNext()) {
            idElements1.add(cursor.getString(0));
            idAssets2.add(cursor.getString(1));
            nameElement.add(cursor.getString(2));
            uniformatElement.add(cursor.getString(3));
            categoryElement.add(cursor.getString(4));
            typeElement.add(cursor.getString(5));
            yearElement.add(cursor.getString(6));
            quantityElement.add(cursor.getString(7));
            conditionElement.add(cursor.getString(8));
        }
    }
}