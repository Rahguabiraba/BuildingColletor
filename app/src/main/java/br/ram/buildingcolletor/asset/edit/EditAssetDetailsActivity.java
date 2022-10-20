package br.ram.buildingcolletor.asset.edit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import br.ram.buildingcolletor.R;
import br.ram.buildingcolletor.asset.AssetsActivity;
import br.ram.buildingcolletor.database.DataBaseSQL;

public class EditAssetDetailsActivity extends AppCompatActivity {

    protected Button buttonNext, buttonBack, buttonAssetsList, buttonDelete, buttonText, buttonTalk;
    private Intent nextActivity;
    protected EditText assetDetails;
    protected String content1 = "";
    protected DataBaseSQL dataBase;
    protected String id, name, area, location, aDetails;
    protected double lat;
    protected double lon;
    private static final int RECOGNIZER_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_asset_details);

        buttonNext = findViewById(R.id.button_next_details_edit);
        buttonBack = findViewById(R.id.button_back_details_edit);
        buttonDelete = findViewById(R.id.button_drop_assets_details_edit);
        buttonText = findViewById(R.id.button_text_assets_details_edit);
        buttonTalk = findViewById(R.id.button_talk_assets_details_edit);
        buttonAssetsList = findViewById(R.id.button1_assetList_details_edit);
        assetDetails = findViewById(R.id.edit_text_asset_details_edit);

        //Conection DataBase
        dataBase = new DataBaseSQL(EditAssetDetailsActivity.this);

        getAndSetIntentData();

        buttonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assetDetails.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(assetDetails, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        buttonTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent speachIntent = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speachIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speach to text");
                startActivityForResult(speachIntent,RECOGNIZER_RESULT);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assetDetails.setText("");
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                content1 = assetDetails.getText().toString().trim();

                if (content1.equals("")) {
                    Toast.makeText(EditAssetDetailsActivity.this, "Please fill this field!", Toast.LENGTH_SHORT).show();
                } else {

                    dataBase.updateAsset(id,name,area,location,lat, lon, content1);
                    Toast.makeText(EditAssetDetailsActivity.this, "Asset Update Sucessfully!", Toast.LENGTH_SHORT).show();

                    nextActivity = new Intent(EditAssetDetailsActivity.this, AssetsActivity.class);
                    finish();
                    startActivity(nextActivity);
                }

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(EditAssetDetailsActivity.this, EditAssetLocationActivity.class);
                nextActivity.putExtra("id",id);
                nextActivity.putExtra("name",name);
                nextActivity.putExtra("area",area);
                nextActivity.putExtra("location",location);
                nextActivity.putExtra("latitude",lat);
                nextActivity.putExtra("longitude",lon);
                nextActivity.putExtra("details", assetDetails.getText().toString());
                finish();
                startActivity(nextActivity);
            }
        });

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(EditAssetDetailsActivity.this, AssetsActivity.class);
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
            location = getIntent().getStringExtra("location");
            aDetails = getIntent().getStringExtra("details");
            lat = getIntent().getExtras().getDouble("latitude");
            lon = getIntent().getExtras().getDouble("longitude");

            assetDetails.setText(aDetails);

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            assetDetails.setText(matches.get(0).toString());
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

}