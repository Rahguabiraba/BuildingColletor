package br.ram.buildingcolletor.requirement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import br.ram.buildingcolletor.R;
import br.ram.buildingcolletor.asset.AssetsActivity;
import br.ram.buildingcolletor.database.DataBaseSQL;
import br.ram.buildingcolletor.element.ElementTypeActivity;
import br.ram.buildingcolletor.element.ElementsActivity;

public class RequirementsActivity extends AppCompatActivity {

    protected Button buttonAssetsList, buttonElementsList, buttonBack, buttonDelete, buttonAdd, buttonTalk, buttonType, buttonDetails;
    private Intent nextActivity;
    protected AutoCompleteTextView autoCompleteTxt1;
    protected ArrayAdapter<String> adapterItems1;
    protected String[] items1;
    protected String idAsset, lastId;
    protected EditText requirementsDetailsText;
    protected ArrayList<String> idImages, idElements;
    protected String requirementType = "";
    protected String requirementDetails = "";
    protected String category = "";
    protected String quantity = "";
    protected String condition = "";
    protected String content1 = "";
    protected String content2 = "";
    protected String content3 = "";
    protected String content4 = "";
    protected String content5 = "";
    protected String elementName = "";
    private static final int RECOGNIZER_RESULT = 1;
    protected DataBaseSQL dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirements);

        buttonAssetsList = findViewById(R.id.button_assetList_requirements);
        buttonElementsList = findViewById(R.id.button_elementList_requirements);
        buttonAdd = findViewById(R.id.button_next_info_requirements);
        buttonBack = findViewById(R.id.button_back_info_requirements);
        buttonTalk = findViewById(R.id.button_talk_requirements);
        buttonDelete = findViewById(R.id.button_drop_requirements);
        buttonType = findViewById(R.id.button_type_requirements);
        buttonDetails = findViewById(R.id.button_details_requirements);
        autoCompleteTxt1 = findViewById (R.id.autocomplete1_create_uniformat);
        requirementsDetailsText = findViewById(R.id.edit_text_requirements_details);

        //Add Array de Strings into sppiner TextInputLayout (Uniformat)
        items1 = getResources().getStringArray(R.array.requirements);
        adapterItems1 = new ArrayAdapter<String>(RequirementsActivity.this, R.layout.list_item, items1);
        autoCompleteTxt1.setAdapter(adapterItems1);

        if(getIntent().hasExtra("idAsset")) {

            //Geting Data from Intent
            idAsset = getIntent().getStringExtra("idAsset");
        }

        if(getIntent().hasExtra("elementName")) {

            //Geting Data from Intent
            elementName = getIntent().getStringExtra("elementName");
        }

        if(getIntent().hasExtra("elementUniformat") && getIntent().hasExtra("elementCategory")
                && getIntent().hasExtra("elementType") && getIntent().hasExtra("elementYear") && getIntent().hasExtra("elementQuantity")
                && getIntent().hasExtra("elementUnity") && getIntent().hasExtra("elementCondition")) {

            //Geting Data from Intent
            content1 = getIntent().getStringExtra("elementUniformat");
            category = getIntent().getStringExtra("elementCategory");
            content2 = getIntent().getStringExtra("elementType");
            content3 = getIntent().getStringExtra("elementYear");
            content4 = getIntent().getStringExtra("elementQuantity");
            content5 = getIntent().getStringExtra("elementUnity");
            condition = getIntent().getStringExtra("elementCondition");
        }

        if (getIntent().hasExtra("requirementsType") && getIntent().hasExtra("requirementsDetails")) {
            //Geting Data from Intent
            requirementType = getIntent().getStringExtra("requirementsType");
            requirementDetails = getIntent().getStringExtra("requirementsDetails");
        }

        idImages = new ArrayList<>();
        idElements = new ArrayList<>();

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(RequirementsActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonElementsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(RequirementsActivity.this, ElementsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Conection DataBase
                dataBase = new DataBaseSQL(RequirementsActivity.this);

                requirementType = autoCompleteTxt1.getText().toString();
                requirementDetails = requirementsDetailsText.getText().toString();

                quantity = content4 + " " + content5;

                if (requirementType.equals("") || requirementDetails.equals("")) {

                    Toast.makeText(RequirementsActivity.this, "Please, fill all these fields", Toast.LENGTH_SHORT).show();

                } else {

                        //Add Element
                        dataBase.addElement(idAsset, elementName,content1,category,content2,content3,quantity,condition);

                        //Function to get last Element ID add
                        storeIdElement();

                        //Function to get all ID = 0
                        storeIdsZero();

                        //Add ImagesElement
                        for (int i=0; i<idElements.size(); i++) {
                            dataBase.updateAllIdImagesElement(lastId);
                        }

                        //Add Requirements
                        dataBase.addRequirements(lastId,requirementType,requirementDetails);
                    Toast.makeText(RequirementsActivity.this, "Element add Sucessfully!", Toast.LENGTH_SHORT).show();

                    nextActivity = new Intent(RequirementsActivity.this, ElementsActivity.class);
                    nextActivity.putExtra("idAsset", idAsset);
                    finish();
                    startActivity(nextActivity);
                }



            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requirementType = autoCompleteTxt1.getText().toString();
                requirementDetails = requirementsDetailsText.getText().toString();

                nextActivity = new Intent(RequirementsActivity.this, ElementTypeActivity.class);
                nextActivity.putExtra("idAsset", idAsset);
                nextActivity.putExtra("elementName", elementName);
                nextActivity.putExtra("elementUniformat", content1);
                nextActivity.putExtra("elementCategory", category);
                nextActivity.putExtra("elementType", content2);
                nextActivity.putExtra("elementYear", content3);
                nextActivity.putExtra("elementQuantity", content4);
                nextActivity.putExtra("elementUnity", content5);
                nextActivity.putExtra("elementCondition", condition);
                nextActivity.putExtra("requirementsType", requirementType);
                nextActivity.putExtra("requirementsDetails", requirementDetails);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (RequirementsActivity.this.getCurrentFocus().getId() == autoCompleteTxt1.getId()) {

                    autoCompleteTxt1.setText("");

                } else if (RequirementsActivity.this.getCurrentFocus().getId() == requirementsDetailsText.getId()) {

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

                if (RequirementsActivity.this.getCurrentFocus().getId() == requirementsDetailsText.getId()) {

                    Intent speachIntent = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    speachIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speach to text");
                    startActivityForResult(speachIntent,RECOGNIZER_RESULT);

                }
            }
        });
    }

    public void storeIdsZero() {
        Cursor cursor = dataBase.readIdWithZeroElement();
        while (cursor.moveToNext()) {
            idElements.add(cursor.getString(0));
        }
    }

    public void storeIdElement() {
        Cursor cursor = dataBase.readAllIdElements();

        cursor.moveToLast();

        lastId = cursor.getString(0);
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