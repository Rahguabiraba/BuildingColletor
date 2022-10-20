package br.ram.buildingcolletor.requirement.edit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import br.ram.buildingcolletor.element.ElementTypeActivity;
import br.ram.buildingcolletor.element.ElementsActivity;
import br.ram.buildingcolletor.R;
import br.ram.buildingcolletor.requirement.RequirementsListActivity;
import br.ram.buildingcolletor.asset.AssetsActivity;
import br.ram.buildingcolletor.database.DataBaseSQL;

public class EditRequirementViewActivity extends AppCompatActivity {

    protected Button buttonAssetsList, buttonElementsList, buttonEdit, buttonCancel, buttonDelete, buttonType, buttonDetails, buttonTalk;
    private Intent nextActivity;
    protected AutoCompleteTextView autoCompleteTxt1;
    protected ArrayAdapter<String> adapterItems1;
    protected String[] items1;
    protected String idAsset, idElement,idRequirement, requirementsType, requirementsDetails;
    protected EditText requirementsDetailsText;
    private static final int RECOGNIZER_RESULT = 1;
    protected String requirementType = "";
    protected String requirementDetails = "";
    protected DataBaseSQL dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_requirement_view);

        buttonAssetsList = findViewById(R.id.button_assetList_requirements_new_edit);
        buttonElementsList = findViewById(R.id.button_elementList_requirements_new_edit);
        buttonEdit = findViewById(R.id.button_next_info_requirements_new_edit);
        buttonCancel = findViewById(R.id.button_back_info_requirements_new_edit);
        buttonDelete = findViewById(R.id.button_drop_requirements_new_edit);
        buttonType = findViewById(R.id.button_type_requirements_new_edit);
        buttonDetails = findViewById(R.id.button_description_requirements_new_edit);
        buttonTalk = findViewById(R.id.button_talk_requirements_new_edit);
        autoCompleteTxt1 = findViewById (R.id.autocomplete1_create_requirement_type_new_edit);
        requirementsDetailsText = findViewById(R.id.edit_text_requirements_details_new_edit);

        //Add Array de Strings into sppiner TextInputLayout (Uniformat)
        items1 = getResources().getStringArray(R.array.requirements);
        adapterItems1 = new ArrayAdapter<String>(EditRequirementViewActivity.this, R.layout.list_item, items1);
        autoCompleteTxt1.setAdapter(adapterItems1);

        getAndSetIntentData();

        //Conection DataBase
        dataBase = new DataBaseSQL(EditRequirementViewActivity.this);

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(EditRequirementViewActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonElementsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(EditRequirementViewActivity.this, ElementsActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requirementType = autoCompleteTxt1.getText().toString();
                requirementDetails = requirementsDetailsText.getText().toString();

                if (requirementType.equals("") || requirementDetails.equals("")) {

                    Toast.makeText(EditRequirementViewActivity.this, "Please, fill all these fields", Toast.LENGTH_SHORT).show();

                } else {

                    //Add Requirements
                    dataBase.updateRequirement(idRequirement,requirementType,requirementDetails);
                    Toast.makeText(EditRequirementViewActivity.this, "Requirement Update Sucessfully!", Toast.LENGTH_SHORT).show();

                    nextActivity = new Intent(EditRequirementViewActivity.this, RequirementsListActivity.class);
                    nextActivity.putExtra("idAsset", idAsset);
                    nextActivity.putExtra("idElement",idElement);
                    finish();
                    startActivity(nextActivity);
                }



            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(EditRequirementViewActivity.this, ElementTypeActivity.class);
                nextActivity.putExtra("idAsset", idAsset);
                nextActivity.putExtra("idElement", idElement);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (EditRequirementViewActivity.this.getCurrentFocus().getId() == autoCompleteTxt1.getId()) {

                    autoCompleteTxt1.setText("");

                } else if (EditRequirementViewActivity.this.getCurrentFocus().getId() == requirementsDetailsText.getId()) {

                    requirementsDetailsText.setText("");

                }
            }
        });

        buttonType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                autoCompleteTxt1.requestFocus();

                //Hide Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                //Show DropDown List
                autoCompleteTxt1.showDropDown();
            }
        });

        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requirementsDetailsText.requestFocus();

                //Show Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(requirementsDetailsText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        buttonTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (EditRequirementViewActivity.this.getCurrentFocus().getId() == requirementsDetailsText.getId()) {

                    Intent speachIntent = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    speachIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speach to text");
                    startActivityForResult(speachIntent,RECOGNIZER_RESULT);

                }
            }
        });

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

            //Setting Data
            autoCompleteTxt1.setText(requirementsType);
            requirementsDetailsText.setText(requirementsDetails);

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            requirementsDetailsText.setText(matches.get(0).toString());
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}