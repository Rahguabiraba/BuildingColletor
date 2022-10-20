package br.ram.buildingcolletor.element;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ram.buildingcolletor.R;
import br.ram.buildingcolletor.asset.AssetsActivity;

public class ElementNameActivity extends AppCompatActivity {

    protected Button buttonAssetsList, buttonElementsList, buttonNext, buttonBack, buttonDelete;
    private Intent nextActivity;
    protected EditText elementName;
    protected String idAsset;
    protected String requirementsDetails = "";
    protected String requirementsType = "";
    protected String content1 = "";
    protected String elementN = "";
    protected String uniformat = "";
    protected String category = "";
    protected String type = "";
    protected String date = "";
    protected String quantity = "";
    protected String unity = "";
    protected String condition = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_name);

        //References
        buttonAssetsList = findViewById(R.id.button1_info_elements);
        buttonElementsList = findViewById(R.id.button2_info_elements);
        buttonNext = findViewById(R.id.button_next_info_elements);
        buttonBack = findViewById(R.id.button_back_info_elements);
        buttonDelete = findViewById(R.id.button_drop_info_elements);
        elementName = findViewById(R.id.element_name_info);

        if(getIntent().hasExtra("idAsset")) {

            //Geting Data from Intent
            idAsset = getIntent().getStringExtra("idAsset");
        }

        if(getIntent().hasExtra("elementName")) {

            //Geting Data from Intent
            elementN = getIntent().getStringExtra("elementName");
            elementName.setText(elementN);
        }

        if(getIntent().hasExtra("elementUniformat") && getIntent().hasExtra("elementCategory")
                && getIntent().hasExtra("elementType") && getIntent().hasExtra("elementYear") && getIntent().hasExtra("elementQuantity")
                && getIntent().hasExtra("elementUnity") && getIntent().hasExtra("elementCondition")) {

            //Geting Data from Intent
            uniformat = getIntent().getStringExtra("elementUniformat");
            category = getIntent().getStringExtra("elementCategory");
            type = getIntent().getStringExtra("elementType");
            date = getIntent().getStringExtra("elementYear");
            quantity = getIntent().getStringExtra("elementQuantity");
            unity = getIntent().getStringExtra("elementUnity");
            condition = getIntent().getStringExtra("elementCondition");
        }

        if (getIntent().hasExtra("requirementsType") && getIntent().hasExtra("requirementsDetails")) {
            //Geting Data from Intent
            requirementsType = getIntent().getStringExtra("requirementsType");
            requirementsDetails = getIntent().getStringExtra("requirementsDetails");
        }

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementNameActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonElementsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementNameActivity.this, ElementsActivity.class);
                nextActivity.putExtra("idAsset", idAsset);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                content1 = elementName.getText().toString();

                if (content1.equals("")) {
                    Toast.makeText(ElementNameActivity.this, "Please, fill all these fields", Toast.LENGTH_SHORT).show();
                } {
                    nextActivity = new Intent(ElementNameActivity.this, ElementTypeActivity.class);
                    nextActivity.putExtra("idAsset", idAsset);
                    nextActivity.putExtra("elementName", content1);
                    nextActivity.putExtra("elementUniformat", uniformat);
                    nextActivity.putExtra("elementCategory", category);
                    nextActivity.putExtra("elementType", type);
                    nextActivity.putExtra("elementYear", date);
                    nextActivity.putExtra("elementQuantity", quantity);
                    nextActivity.putExtra("elementUnity", unity);
                    nextActivity.putExtra("elementCondition", condition);
                    nextActivity.putExtra("requirementsType", requirementsType);
                    nextActivity.putExtra("requirementsDetails", requirementsDetails);
                    finish();
                    startActivity(nextActivity);
                }

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementNameActivity.this, ElementsPhotoActivity.class);
                nextActivity.putExtra("idAsset", idAsset);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elementName.setText("");
            }
        });

    }
}