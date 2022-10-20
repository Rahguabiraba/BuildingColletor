package br.ram.buildingcolletor.requirement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.ram.buildingcolletor.R;
import br.ram.buildingcolletor.RecyclerViewClickListener;
import br.ram.buildingcolletor.asset.AssetsActivity;
import br.ram.buildingcolletor.database.DataBaseSQL;
import br.ram.buildingcolletor.element.ElementsActivity;
import br.ram.buildingcolletor.requirement.custom.CustomAdapterRequirement;

public class RequirementsListActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private ViewStub myViewStub;
    protected View requirementsGroup;
    private Intent nextActivity;
    protected Button buttonNewRequirement, buttonAssetList, buttonElementList, buttonNext, buttonBack;
    protected ArrayList<String> id, idElementList, name, details;
    protected String idAsset, idElement;
    protected String nameElement = "";
    protected TextView title;
    protected RecyclerView recyclerView;
    protected CustomAdapterRequirement customAdapter;
    protected DataBaseSQL dataBase;
    protected int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirements_list);

        //References
        myViewStub = findViewById(R.id.emptyStateLayoutRequirementList);
        requirementsGroup = findViewById(R.id.requirementGroup);
        buttonNewRequirement = findViewById(R.id.button_new_requirement);
        buttonAssetList = findViewById(R.id.button_assetList_requirement);
        buttonElementList = findViewById(R.id.button_requirement_elementList);
        buttonNext = findViewById(R.id.button_next_requirement);
        buttonBack = findViewById(R.id.button_back_asset_requirement);
        recyclerView = findViewById(R.id.requirement_rv);
        title = findViewById(R.id.requirement_list_text);

        //Conection with DataBase
        dataBase = new DataBaseSQL(this);

        id = new ArrayList<>();
        idElementList = new ArrayList<>();
        name = new ArrayList<>();
        details = new ArrayList<>();

        getAndSetIntentData();

        storeDataArrays();

        //Call adapter
        customAdapter = new CustomAdapterRequirement(RequirementsListActivity.this,this, name);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonNewRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(RequirementsListActivity.this, RequirementNewActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                nextActivity.putExtra("idElement",idElement);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonAssetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(RequirementsListActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonElementList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(RequirementsListActivity.this, ElementsActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(RequirementsListActivity.this, RequirementViewActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                nextActivity.putExtra("idElement",idElement);
                nextActivity.putExtra("idRequirement",String.valueOf(id.get(position)));
                nextActivity.putExtra("requirementsType",String.valueOf(name.get(position)));
                nextActivity.putExtra("requirementsDetails", String.valueOf(details.get(position)));
                startActivity(nextActivity);
            }

        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(RequirementsListActivity.this, ElementsActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                finish();
                startActivity(nextActivity);
            }
        });
    }

    private void getAndSetIntentData() {

        if(getIntent().hasExtra("idAsset") && getIntent().hasExtra("idElement")) {

            //Geting Data from Intent
            idAsset = getIntent().getStringExtra("idAsset");
            idElement = getIntent().getStringExtra("idElement");
            getElementName();

            //Setting Intent Data
            title.setText(nameElement + " /Requirement List");

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getElementName() {
        Cursor cursor = dataBase.readNameElement(idElement);

        cursor.moveToLast();

        nameElement = cursor.getString(0);
    }

    public void storeDataArrays() {

        Cursor cursor = dataBase.readAllDataRequirement(idElement);

        //ViewSub para poner Lottlie image (el GIF con el emoticon)
        myViewStub.inflate();

        if (cursor.getCount() == 0) {
            myViewStub.setVisibility(View.VISIBLE);
            requirementsGroup.setVisibility(View.GONE);
        } else {

            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                idElementList.add(idElement);
                name.add(cursor.getString(2));
                details.add(cursor.getString(3));

                //Visibilidad de las vistas
                myViewStub.setVisibility(View.GONE);
                requirementsGroup.setVisibility(View.VISIBLE);
                buttonNext.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        this.position = position;

        showToast();
    }

    private void showToast() {
        Toast.makeText(this, "Element: " + name.get(position), Toast.LENGTH_SHORT).show();
    }
}