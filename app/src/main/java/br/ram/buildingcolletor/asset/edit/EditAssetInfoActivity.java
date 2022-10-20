package br.ram.buildingcolletor.asset.edit;

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
import br.ram.buildingcolletor.asset.AssetsActivity;

public class EditAssetInfoActivity extends AppCompatActivity {

    protected Button buttonAssetsList, buttonNext, buttonDelete, buttonSize, buttonName, buttonUnit;
    private Intent nextActivity;
    protected AutoCompleteTextView autoCompleteTxt1;
    protected ArrayAdapter<String> adapterItems1;
    protected String[] items1;
    protected EditText assetName, assetArea;
    protected String[] partes;
    protected String id, name, area, location, details;
    protected double lat, lon;
    protected String content1 = "";
    protected String content2 = "";
    protected String content3 = "";
    protected String content4 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_asset_info);

        //References
        buttonAssetsList = findViewById(R.id.button1_info_assets_edit);
        buttonDelete = findViewById(R.id.button_drop_info_assets_edit);
        buttonName = findViewById(R.id.button_name_info_assets_edit);
        buttonSize = findViewById(R.id.button_size_info_assets_edit);
        buttonUnit  = findViewById(R.id.button_unit_info_assets_edit);
        buttonNext = findViewById(R.id.button_next_info_assets_edit);
        assetName = findViewById(R.id.asset_name_info_edit);
        assetArea = findViewById(R.id.asset_area_info_edit);
        autoCompleteTxt1 = findViewById (R.id.autocomplete1_info_edit);

        getAndSetIntentData();

        //Add Array de Strings into sppiner TextInputLayout (Area Type)
        items1 = getResources().getStringArray(R.array.area_types);
        adapterItems1 = new ArrayAdapter<String>(EditAssetInfoActivity.this, R.layout.list_item, items1);
        autoCompleteTxt1.setAdapter(adapterItems1);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (EditAssetInfoActivity.this.getCurrentFocus().getId() == assetName.getId()) {

                    assetName.setText("");

                } else if (EditAssetInfoActivity.this.getCurrentFocus().getId() == assetArea.getId()) {

                    assetArea.setText("");

                } else if (EditAssetInfoActivity.this.getCurrentFocus().getId() == autoCompleteTxt1.getId()) {

                    autoCompleteTxt1.setText("");

                }
            }
        });

        autoCompleteTxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hide Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);
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

                //Show DropDown List
                autoCompleteTxt1.showDropDown();
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
                    Toast.makeText(EditAssetInfoActivity.this, "Please, fill all these fields", Toast.LENGTH_SHORT).show();
                } else {
                    nextActivity = new Intent(EditAssetInfoActivity.this, EditAssetLocationActivity.class);
                    nextActivity.putExtra("id", id);
                    nextActivity.putExtra("name", content1);
                    nextActivity.putExtra("area", content4);
                    nextActivity.putExtra("location", location);
                    nextActivity.putExtra("latitude",lat);
                    nextActivity.putExtra("longitude", lon);
                    nextActivity.putExtra("details", details);
                    finish();
                    startActivity(nextActivity);
                }
            }
        });

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(EditAssetInfoActivity.this, AssetsActivity.class);
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
            details = getIntent().getStringExtra("details");
            lat = getIntent().getExtras().getDouble("latitude");
            lon = getIntent().getExtras().getDouble("longitude");

            //Setting Intent Data
            assetName.setText(name);

            partes = area.split(" ");
            assetArea.setText(partes[0]);
            autoCompleteTxt1.setText(partes[1]);

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

}