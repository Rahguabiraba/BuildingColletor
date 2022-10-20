package br.ram.buildingcolletor.element.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ram.buildingcolletor.element.ElementsActivity;
import br.ram.buildingcolletor.R;
import br.ram.buildingcolletor.asset.AssetsActivity;

public class EditElementNameActivity extends AppCompatActivity {

    protected Button buttonAssetsList, buttonElementsList, buttonNext, buttonDelete;
    private Intent nextActivity;
    protected EditText elementName;
    protected String idAsset, idElement, quantity, unity;
    protected String[] partes;
    protected String requirementsDetails = "";
    protected String requirementsType = "";
    protected String content1 = "";
    protected String elementN = "";
    protected String uniformat = "";
    protected String category = "";
    protected String type = "";
    protected String date = "";
    protected String content2 = "";
    protected String condition = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_element_name);

        //References
        buttonAssetsList = findViewById(R.id.button1_info_elements_edit);
        buttonElementsList = findViewById(R.id.button2_info_elements_edit);
        buttonNext = findViewById(R.id.button_next_info_elements_edit);
        buttonDelete = findViewById(R.id.button_drop_info_elements_edit);
        elementName = findViewById(R.id.element_name_info_edit);

        getAndSetIntentData();

        getRequirementData();

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(EditElementNameActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonElementsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(EditElementNameActivity.this, ElementsActivity.class);
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
                    Toast.makeText(EditElementNameActivity.this, "Please, fill all these fields", Toast.LENGTH_SHORT).show();
                } {
                    nextActivity = new Intent(EditElementNameActivity.this, EditElementTypeActivity.class);
                    nextActivity.putExtra("idAsset", idAsset);
                    nextActivity.putExtra("idElement",idElement);
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

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elementName.setText("");
            }
        });
    }

    private void getRequirementData() {

        if (getIntent().hasExtra("requirementsType") && getIntent().hasExtra("requirementsDetails")) {
            requirementsType = getIntent().getStringExtra("requirementsType");
            requirementsDetails = getIntent().getStringExtra("requirementsDetails");
        }
    }

    private void getAndSetIntentData() {

        if(getIntent().hasExtra("idAsset") && getIntent().hasExtra("idElement") && getIntent().hasExtra("elementName") && getIntent().hasExtra("elementUniformat")
                && getIntent().hasExtra("elementCategory") && getIntent().hasExtra("elementType") && getIntent().hasExtra("elementYear")
                && getIntent().hasExtra("elementQuantity") && getIntent().hasExtra("elementCondition")) {

            //Geting Data from Intent
            idAsset = getIntent().getStringExtra("idAsset");
            idElement = getIntent().getStringExtra("idElement");
            elementN = getIntent().getStringExtra("elementName");
            uniformat = getIntent().getStringExtra("elementUniformat");
            category = getIntent().getStringExtra("elementCategory");
            type = getIntent().getStringExtra("elementType");
            date = getIntent().getStringExtra("elementYear");
            content2 = getIntent().getStringExtra("elementQuantity");
            condition = getIntent().getStringExtra("elementCondition");

            partes = content2.split(" ");
            quantity = partes[0];
            unity = partes[1];

            //Setting Intent Data
            elementName.setText(elementN);

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }
}