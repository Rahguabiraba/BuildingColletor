package br.ram.buildingcolletor.asset;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ram.buildingcolletor.R;

public class AssetsInfoActivity extends AppCompatActivity {

    protected Button buttonAssetsList, buttonNext, buttonBack, buttonDelete, buttonSize, buttonName, buttonUnit;
    private Intent nextActivity;
    protected AutoCompleteTextView autoCompleteTxt1;
    protected ArrayAdapter<String> adapterItems1;
    protected String[] items1;
    protected EditText assetName, assetArea;
    protected String[] partes;
    protected String aName, aArea, assetLocation, aDetails;
    protected double lat, lon;
    protected String content1 = "";
    protected String content2 = "";
    protected String content3 = "";
    protected String content4 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_info);

        //References
        buttonAssetsList = findViewById(R.id.button1_info_assets);
        buttonDelete = findViewById(R.id.button_drop_info_assets);
        buttonName = findViewById(R.id.button_name_info_assets);
        buttonSize = findViewById(R.id.button_size_info_assets);
        buttonUnit  = findViewById(R.id.button_unit_info_assets);
        buttonNext = findViewById(R.id.button_next_info_assets);
        buttonBack = findViewById(R.id.button_back_info_assets);
        assetName = findViewById(R.id.asset_name_info);
        assetArea = findViewById(R.id.asset_area_info);
        autoCompleteTxt1 = findViewById (R.id.autocomplete1_info);


        if(getIntent().hasExtra("name") && getIntent().hasExtra("area")) {

            //Geting Data from AssetLocation if you click on Back Button
            aName = getIntent().getStringExtra("name");
            aArea = getIntent().getStringExtra("area");

            assetName.setText(aName);

            partes = aArea.split(" ");
            assetArea.setText(partes[0]);
            autoCompleteTxt1.setText(partes[1]);

        }

        if(getIntent().hasExtra("location")) {

            //Geting Data from Intent
            assetLocation = getIntent().getStringExtra("location");
        }

        if(getIntent().hasExtra("details")) {

            //Geting Data from Intent
            aDetails = getIntent().getStringExtra("details");
        }

        if(getIntent().hasExtra("latitude") && getIntent().hasExtra("longitude")) {

            //Geting Data from Intent
            lat = getIntent().getExtras().getDouble("latitude");
            lon = getIntent().getExtras().getDouble("longitude");
        }

            //Add Array de Strings into sppiner TextInputLayout (Area Type)
        items1 = getResources().getStringArray(R.array.area_types);
        adapterItems1 = new ArrayAdapter<String>(AssetsInfoActivity.this, R.layout.list_item, items1);
        autoCompleteTxt1.setAdapter(adapterItems1);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AssetsInfoActivity.this.getCurrentFocus().getId() == assetName.getId()) {

                    assetName.setText("");

                } else if (AssetsInfoActivity.this.getCurrentFocus().getId() == assetArea.getId()) {

                    assetArea.setText("");

                } else if (AssetsInfoActivity.this.getCurrentFocus().getId() == autoCompleteTxt1.getId()) {

                    autoCompleteTxt1.setText("");

                }
            }
        });

        buttonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assetArea.clearFocus();
                autoCompleteTxt1.clearFocus();
                assetName.requestFocus();

                //Show Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(assetName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        buttonSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assetName.clearFocus();
                autoCompleteTxt1.clearFocus();
                assetArea.requestFocus();

                //Show Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(assetArea, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        buttonUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assetName.clearFocus();
                assetArea.clearFocus();
                autoCompleteTxt1.requestFocus();

                //Hide Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                //Show DropDown List
                autoCompleteTxt1.showDropDown();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                content1 = assetName.getText().toString().trim();
                content2 = assetArea.getText().toString().trim();
                content3 = autoCompleteTxt1.getText().toString().trim();

                content4 = content2 + " " + content3;

                if (content1.equals("") || content2.equals("") || content4.equals("")) {
                    Toast.makeText(AssetsInfoActivity.this, "Please, fill all these fields", Toast.LENGTH_SHORT).show();
                } else {
                    nextActivity = new Intent(AssetsInfoActivity.this, AssetsLocationActivity.class);
                    nextActivity.putExtra("name", content1);
                    nextActivity.putExtra("area", content4);
                    nextActivity.putExtra("location", assetLocation);
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
                nextActivity = new Intent(AssetsInfoActivity.this, AssetsPhotoActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(AssetsInfoActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });
    }
}