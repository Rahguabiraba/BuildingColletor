package br.ram.buildingcolletor.asset;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import br.ram.buildingcolletor.database.DataBaseSQL;
import br.ram.buildingcolletor.R;

public class AssetsDetailsActivity extends AppCompatActivity {

    protected Button buttonNext, buttonBack, buttonAssetsList, buttonDelete, buttonText, buttonTalk;
    private Intent nextActivity;
    protected EditText assetDetails;
    protected String content1 = "";
    protected DataBaseSQL dataBase;
    protected String assetName, assetArea, assetLocation, aDetails, lastId;
    protected ArrayList<String> idImages, idAssets;
    protected double lat;
    protected double lon;
    private static final int RECOGNIZER_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_details);

        buttonNext = findViewById(R.id.button_next_details);
        buttonBack = findViewById(R.id.button_back_details);
        buttonDelete = findViewById(R.id.button_drop_assets_details);
        buttonText = findViewById(R.id.button_text_assets_details);
        buttonTalk = findViewById(R.id.button_talk_assets_details);
        buttonAssetsList = findViewById(R.id.button1_assetList_details);
        assetDetails = findViewById(R.id.edit_text_asset_details);

        //Conection DataBase
        dataBase = new DataBaseSQL(AssetsDetailsActivity.this);
        idImages = new ArrayList<>();
        idAssets = new ArrayList<>();

        if(getIntent().hasExtra("name") && getIntent().hasExtra("area") && getIntent().hasExtra("location")
                ) {

            //Geting Data from Intent
            assetName = getIntent().getStringExtra("name");
            assetArea = getIntent().getStringExtra("area");
            assetLocation = getIntent().getStringExtra("location");

        }

        if(getIntent().hasExtra("details")) {

            //Geting Data from Intent
            aDetails = getIntent().getStringExtra("details");
            assetDetails.setText(aDetails);

        }

        if (getIntent().hasExtra("latitude")) {

            lat = getIntent().getExtras().getDouble("latitude");

        }

        if (getIntent().hasExtra("longitude")) {

            lon = getIntent().getExtras().getDouble("longitude");

        }

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
                    Toast.makeText(AssetsDetailsActivity.this, "Please fill this field!", Toast.LENGTH_SHORT).show();
                } else {

                    dataBase.addAsset(assetName,assetArea,assetLocation,lat, lon, content1);
                    Toast.makeText(AssetsDetailsActivity.this, "Asset add Sucessfully!", Toast.LENGTH_SHORT).show();

                    //Function to get last ID add
                    storeIdAsset();

                    //Function to get all ID = 0
                    storeIdsOne();

                    for (int i=0; i<idAssets.size(); i++) {
                        dataBase.updateAllIdImages(lastId);
                    }

                    nextActivity = new Intent(AssetsDetailsActivity.this, AssetsActivity.class);
                    finish();
                    startActivity(nextActivity);
                }

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(AssetsDetailsActivity.this, AssetsLocationActivity.class);
                nextActivity.putExtra("name",assetName);
                nextActivity.putExtra("area",assetArea);
                nextActivity.putExtra("location",assetLocation);
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
                nextActivity = new Intent(AssetsDetailsActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            assetDetails.setText(matches.get(0).toString());
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public void storeIdsOne() {
        Cursor cursor = dataBase.readIdWithZero();
        while (cursor.moveToNext()) {
            idAssets.add(cursor.getString(0));
        }
    }

    public void storeIdAsset() {
        Cursor cursor = dataBase.readAllIdAsset();

        cursor.moveToLast();

        lastId = cursor.getString(0);
    }
}