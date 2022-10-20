package br.ram.buildingcolletor.element;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import br.ram.buildingcolletor.R;
import br.ram.buildingcolletor.requirement.RequirementsActivity;
import br.ram.buildingcolletor.asset.AssetsActivity;
import br.ram.buildingcolletor.database.DataBaseSQL;

public class ElementTypeActivity extends AppCompatActivity {

    protected Button buttonAssetsList, buttonElementsList, buttonNext, buttonBack, buttonDelete, buttonUp, buttonDown, buttonSelect;
    private Intent nextActivity;
    protected AutoCompleteTextView autoCompleteTxt1, autoCompleteTxt2, autoCompleteTxt3, autoCompleteTxt4, autoCompleteTxt5;
    protected ArrayAdapter<String> adapterItems1, adapterItems2, adapterItems3, adapterItems4, adapterItems5;
    protected String[] items1, items2, items3, items4, items5;
    protected EditText elementQuantity;
    protected TextView date;
    protected ArrayList<String> idImages, idElements;
    protected String idAsset, lastId;
    protected String requirementsDetails = "";
    protected String requirementsType = "";
    protected String category = "";
    protected String quantity = "";
    protected String condition = "";
    protected String content1 = "";
    protected String content2 = "";
    protected String content3 = "";
    protected String content4 = "";
    protected String content5 = "";
    protected String elementName = "";
    protected DataBaseSQL database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_type);

        //References
        buttonAssetsList = findViewById(R.id.button_assetsList_type);
        buttonElementsList = findViewById(R.id.button_elementList_type);
        buttonNext = findViewById(R.id.button_next_type_elements);
        buttonBack = findViewById(R.id.button_back_type_elements);
        buttonDelete = findViewById(R.id.button_drop_type_elements);
        buttonUp = findViewById(R.id.button_up_type_elements);
        buttonDown = findViewById(R.id.button_down_type_elements);
        buttonSelect = findViewById(R.id.button_select_type_elements);
        elementQuantity = findViewById(R.id.element_quantity_info);
        date = findViewById(R.id.date_create);
        autoCompleteTxt1 = findViewById (R.id.autocomplete1_create_uniformat);
        autoCompleteTxt2 = findViewById (R.id.autocomplete1_create_category);
        autoCompleteTxt3 = findViewById (R.id.autocomplete1_create_type);
        autoCompleteTxt4 = findViewById (R.id.autocomplete1_create_unity);
        autoCompleteTxt5 = findViewById (R.id.autocomplete1_create_condition);

        //Add Array de Strings into sppiner TextInputLayout (Uniformat)
        items1 = getResources().getStringArray(R.array.uniformat);
        adapterItems1 = new ArrayAdapter<String>(ElementTypeActivity.this, R.layout.list_item, items1);
        autoCompleteTxt1.setAdapter(adapterItems1);

        //Add Array de Strings into sppiner TextInputLayout (Category)
        items2 = getResources().getStringArray(R.array.category);
        adapterItems2 = new ArrayAdapter<String>(ElementTypeActivity.this, R.layout.list_item, items2);
        autoCompleteTxt2.setAdapter(adapterItems2);

        autoCompleteTxt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                category = autoCompleteTxt2.getText().toString();

                if (category.equals("Arch")) {

                    autoCompleteTxt3.setText("");

                    //Add Array de Strings into sppiner TextInputLayout (Type Arch)
                    items3 = getResources().getStringArray(R.array.element_type_arch);
                    adapterItems3 = new ArrayAdapter<String>(ElementTypeActivity.this, R.layout.list_item, items3);
                    autoCompleteTxt3.setAdapter(adapterItems3);
                }

                if (category.equals("Mech")) {

                    autoCompleteTxt3.setText("");

                    //Add Array de Strings into sppiner TextInputLayout (Type Mech)
                    items3 = getResources().getStringArray(R.array.element_type_mech);
                    adapterItems3 = new ArrayAdapter<String>(ElementTypeActivity.this, R.layout.list_item, items3);
                    autoCompleteTxt3.setAdapter(adapterItems3);

                }

                if (category.equals("Electric")) {

                    autoCompleteTxt3.setText("");

                    //Add Array de Strings into sppiner TextInputLayout (Type Eletric)
                    items3 = getResources().getStringArray(R.array.element_type_eletric);
                    adapterItems3 = new ArrayAdapter<String>(ElementTypeActivity.this, R.layout.list_item, items3);
                    autoCompleteTxt3.setAdapter(adapterItems3);

                }

                if (category.equals("Site Development")) {

                    autoCompleteTxt3.setText("");

                    //Add Array de Strings into sppiner TextInputLayout (Type Site)
                    items3 = getResources().getStringArray(R.array.element_type_site);
                    adapterItems3 = new ArrayAdapter<String>(ElementTypeActivity.this, R.layout.list_item, items3);
                    autoCompleteTxt3.setAdapter(adapterItems3);

                }
            }
        });

        //Add Array de Strings into sppiner TextInputLayout (Unity)
        items4 = getResources().getStringArray(R.array.unity);
        adapterItems4 = new ArrayAdapter<String>(ElementTypeActivity.this, R.layout.list_item, items4);
        autoCompleteTxt4.setAdapter(adapterItems4);

        //Add Array de Strings into sppiner TextInputLayout (Condition)
        items5 = getResources().getStringArray(R.array.condition);
        adapterItems5 = new ArrayAdapter<String>(ElementTypeActivity.this, R.layout.list_item, items5);
        autoCompleteTxt5.setAdapter(adapterItems5);

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

            autoCompleteTxt1.setText(content1);
            autoCompleteTxt2.setText(category);
            autoCompleteTxt3.setText(content2);
            date.setText(content3);
            elementQuantity.setText(content4);
            autoCompleteTxt4.setText(content5);
            autoCompleteTxt5.setText(condition);
        }

        if (getIntent().hasExtra("requirementsType") && getIntent().hasExtra("requirementsDetails")) {
            //Geting Data from Intent
            requirementsType = getIntent().getStringExtra("requirementsType");
            requirementsDetails = getIntent().getStringExtra("requirementsDetails");
        }

        idImages = new ArrayList<>();
        idElements = new ArrayList<>();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Hide Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(date.getWindowToken(), 0);

                final Calendar today = Calendar.getInstance();

                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ElementTypeActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        date.setText(String.valueOf(selectedYear));
                    }
                },today.get(Calendar.YEAR),today.get(Calendar.MONTH));

                builder.setActivatedMonth(Calendar.AUGUST)
                        .setMinYear(1990)
                        .setActivatedYear(today.get(Calendar.YEAR))
                        .setMaxYear(2099)
                        .setTitle("Select Year")
                        .showYearOnly()
                        .build().show();
            }
        });

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementTypeActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonElementsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementTypeActivity.this, ElementsActivity.class);
                nextActivity.putExtra("idAsset", idAsset);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Conection DataBase
                database = new DataBaseSQL(ElementTypeActivity.this);

                content1 = autoCompleteTxt1.getText().toString();
                content2 = autoCompleteTxt3.getText().toString();
                content3 = date.getText().toString();
                content4 = elementQuantity.getText().toString();
                content5 = autoCompleteTxt4.getText().toString();
                condition = autoCompleteTxt5.getText().toString();

                quantity = content4 + " " + content5;

                if (content1.equals("") || category.equals("") || content2.equals("") || content4.equals("") || condition.equals("") || content5.equals("")) {

                    Toast.makeText(ElementTypeActivity.this, "Please, fill all these fields", Toast.LENGTH_SHORT).show();

                } else {

                    if (condition.equals("Excelent") || condition.equals("Good")) {

                        database.addElement(idAsset, elementName,content1,category,content2,content3,quantity,condition);
                        Toast.makeText(ElementTypeActivity.this, "Element add Sucessfully!", Toast.LENGTH_SHORT).show();

                        //Function to get last Element ID add
                        storeIdElement();

                        //Function to get all ID = 0
                        storeIdsZero();

                        for (int i=0; i<idElements.size(); i++) {
                            database.updateAllIdImagesElement(lastId);
                        }

                        nextActivity = new Intent(ElementTypeActivity.this, ElementsActivity.class);
                        nextActivity.putExtra("idAsset", idAsset);
                        finish();
                        startActivity(nextActivity);

                    } else {

                        nextActivity = new Intent(ElementTypeActivity.this, RequirementsActivity.class);
                        nextActivity.putExtra("idAsset", idAsset);
                        nextActivity.putExtra("elementName", elementName);
                        nextActivity.putExtra("elementUniformat", content1);
                        nextActivity.putExtra("elementCategory", category);
                        nextActivity.putExtra("elementType", content2);
                        nextActivity.putExtra("elementYear", content3);
                        nextActivity.putExtra("elementQuantity", content4);
                        nextActivity.putExtra("elementUnity", content5);
                        nextActivity.putExtra("elementCondition", condition);
                        nextActivity.putExtra("requirementsType", requirementsType);
                        nextActivity.putExtra("requirementsDetails", requirementsDetails);
                        finish();
                        startActivity(nextActivity);

                    }
                }


            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementTypeActivity.this, ElementNameActivity.class);
                nextActivity.putExtra("idAsset", idAsset);
                nextActivity.putExtra("elementName", elementName);
                nextActivity.putExtra("elementUniformat", content1);
                nextActivity.putExtra("elementCategory", category);
                nextActivity.putExtra("elementType", content2);
                nextActivity.putExtra("elementYear", content3);
                nextActivity.putExtra("elementQuantity", content4);
                nextActivity.putExtra("elementUnity", content5);
                nextActivity.putExtra("elementCondition", condition);
                nextActivity.putExtra("requirementsType", requirementsType);
                nextActivity.putExtra("requirementsDetails", requirementsDetails);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt1.getId()) {

                    autoCompleteTxt1.setText("");

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt2.getId()) {

                    autoCompleteTxt2.setText("");

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt3.getId()) {

                    autoCompleteTxt3.setText("");

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == date.getId()) {

                    date.setText("");

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == elementQuantity.getId()) {

                    elementQuantity.setText("");

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt4.getId()) {

                    autoCompleteTxt4.setText("");

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt5.getId()) {

                    autoCompleteTxt5.setText("");

                }
            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt1.getId()) {

                    autoCompleteTxt1.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt2.getId()) {

                    autoCompleteTxt1.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt3.getId()) {

                    autoCompleteTxt2.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == date.getId()) {

                    autoCompleteTxt3.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == elementQuantity.getId()) {

                    date.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt4.getId()) {

                    elementQuantity.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt5.getId()) {

                    autoCompleteTxt4.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                }
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt1.getId()) {

                    autoCompleteTxt2.requestFocus();

                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt2.getId()) {

                    autoCompleteTxt3.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt3.getId()) {

                    date.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == date.getId()) {

                    elementQuantity.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == elementQuantity.getId()) {

                    autoCompleteTxt4.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt4.getId()) {

                    autoCompleteTxt5.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt5.getId()) {

                    autoCompleteTxt5.requestFocus();
                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                }
            }
        });

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt1.getId()) {

                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt1.getWindowToken(), 0);

                    //Show DropDown List
                    autoCompleteTxt1.showDropDown();

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt2.getId()) {

                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt2.getWindowToken(), 0);

                    //Show DropDown List
                    autoCompleteTxt2.showDropDown();

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt3.getId()) {

                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt3.getWindowToken(), 0);

                    //Show DropDown List
                    autoCompleteTxt3.showDropDown();

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == date.getId()) {

                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt3.getWindowToken(), 0);

                    final Calendar today = Calendar.getInstance();

                    MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ElementTypeActivity.this,
                            new MonthPickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(int selectedMonth, int selectedYear) {
                                    date.setText(String.valueOf(selectedYear));
                                }
                            },today.get(Calendar.YEAR),today.get(Calendar.MONTH));

                    builder.setActivatedMonth(Calendar.AUGUST)
                            .setMinYear(1990)
                            .setActivatedYear(today.get(Calendar.YEAR))
                            .setMaxYear(2099)
                            .setTitle("Select Year")
                            .showYearOnly()
                            .build().show();

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == elementQuantity.getId()) {

                    //Show Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(elementQuantity, InputMethodManager.SHOW_IMPLICIT);

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt4.getId()) {

                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt4.getWindowToken(), 0);

                    //Show DropDown List
                    autoCompleteTxt4.showDropDown();

                } else if (ElementTypeActivity.this.getCurrentFocus().getId() == autoCompleteTxt5.getId()) {

                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTxt5.getWindowToken(), 0);

                    //Show DropDown List
                    autoCompleteTxt5.showDropDown();

                }
            }
        });

    }

    public void storeIdsZero() {
        Cursor cursor = database.readIdWithZeroElement();
        while (cursor.moveToNext()) {
            idElements.add(cursor.getString(0));
        }
    }

    public void storeIdElement() {
        Cursor cursor = database.readAllIdElements();

        cursor.moveToLast();

        lastId = cursor.getString(0);
    }

}