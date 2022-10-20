package br.ram.buildingcolletor.element;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import br.ram.buildingcolletor.element.custom.CustomAdapterElement;
import br.ram.buildingcolletor.element.edit.EditElementNameActivity;

public class ElementsActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private ViewStub myViewStub;
    protected View elementsGroup;
    private Intent nextActivity;
    protected Button buttonNewElement, buttonAssetList, buttonElementList, buttonNext, buttonBack, buttonDelete, buttonEdit, buttonCopy;
    protected ArrayList<String> id, idAssetList, name, uniformat, category, type, year, quantity, condition, requirements, imagesName, requirementsSize;
    protected String idAsset, lastId;
    protected String nameAsset = "";
    protected TextView title;
    protected RecyclerView recyclerView;
    protected CustomAdapterElement customAdapter;
    protected DataBaseSQL database;
    protected Cursor cursor;
    protected int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements);

        //References
        myViewStub = findViewById(R.id.emptyStateLayoutElementList);
        elementsGroup = findViewById(R.id.elementsGroup);
        buttonNewElement = findViewById(R.id.button_new_element);
        buttonAssetList = findViewById(R.id.button_assetList_element);
        buttonElementList = findViewById(R.id.button_elementList);
        buttonNext = findViewById(R.id.button_next_element);
        buttonBack = findViewById(R.id.button_back_asset_element);
        buttonDelete = findViewById(R.id.button_drop_elements);
        buttonEdit = findViewById(R.id.button_edit_elements);
        buttonCopy = findViewById(R.id.button_copy_elements);
        recyclerView = findViewById(R.id.element_rv);
        title = findViewById(R.id.element_list_text);

        //Conection with DataBase
        database = new DataBaseSQL(this);

        id = new ArrayList<>();
        idAssetList = new ArrayList<>();
        name = new ArrayList<>();
        uniformat = new ArrayList<>();
        category = new ArrayList<>();
        type = new ArrayList<>();
        year = new ArrayList<>();
        quantity = new ArrayList<>();
        condition = new ArrayList<>();
        requirements = new ArrayList<>();
        requirementsSize = new ArrayList<>();
        imagesName = new ArrayList<>();

        if(getIntent().hasExtra("idAsset")) {

            //Geting Data from Intent
            idAsset = getIntent().getStringExtra("idAsset");
            getAssetName();
            title.setText(nameAsset + "/Element List");

            storeDataArrays();
        }

        getRequirementsCount();

        //llamada del adaptador. Los datos aquí son enviados para el Adaptador y después son puestos en el recycler view
        customAdapter = new CustomAdapterElement(ElementsActivity.this,this, name, requirementsSize);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonNewElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementsActivity.this, ElementsPhotoActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonAssetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementsActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonElementList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementsActivity.this, ElementsActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementsActivity.this, ElementDescriptionActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                nextActivity.putExtra("idElement",String.valueOf(id.get(position)));
                nextActivity.putExtra("nameAsset",String.valueOf(nameAsset));
                nextActivity.putExtra("name",String.valueOf(name.get(position)));
                nextActivity.putExtra("uniformat", String.valueOf(uniformat.get(position)));
                nextActivity.putExtra("category", String.valueOf(category.get(position)));
                nextActivity.putExtra("type", String.valueOf(type.get(position)));
                nextActivity.putExtra("year", String.valueOf(year.get(position)));
                nextActivity.putExtra("quantity", String.valueOf(quantity.get(position)));
                nextActivity.putExtra("condition", String.valueOf(condition.get(position)));
                finish();
                startActivity(nextActivity);
            }

        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementsActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmDropDialog();

            }

        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementsActivity.this, EditElementNameActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                nextActivity.putExtra("idElement",id.get(position));
                nextActivity.putExtra("elementName",String.valueOf(name.get(position)));
                nextActivity.putExtra("elementUniformat", String.valueOf(uniformat.get(position)));
                nextActivity.putExtra("elementCategory", String.valueOf(category.get(position)));
                nextActivity.putExtra("elementType", String.valueOf(type.get(position)));
                nextActivity.putExtra("elementYear", String.valueOf(year.get(position)));
                nextActivity.putExtra("elementQuantity", String.valueOf(quantity.get(position)));
                nextActivity.putExtra("elementCondition", String.valueOf(condition.get(position)));
                finish();
                startActivity(nextActivity);
            }

        });

        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmCopyDialog();

            }

        });
    }

    private void confirmDropDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Element?");
        builder.setMessage("Are you sure you want to delete Element " + name.get(position) + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                name.remove(name.get(position));
                database.deleteElement(id.get(position));
                Toast.makeText(ElementsActivity.this,"Element delete Sucessfully!", Toast.LENGTH_SHORT).show();
                customAdapter.notifyItemRemoved(position);
                customAdapter.notifyItemRangeChanged(position,customAdapter.getItemCount());

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ElementsActivity.this, "Element not delete", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();

    }

    private void confirmCopyDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Copy Element?");
        builder.setMessage("Are you sure you want to copy Element " + name.get(position) + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int in) {

                database.addElement(idAsset,name.get(position),uniformat.get(position),category.get(position),type.get(position),year.get(position), quantity.get(position), condition.get(position));

                //Function to get last ID add
                storeIdElement();

                //Function to get All ImagesPath from element we select
                getImagesPath();

                for (int i=0; i<imagesName.size(); i++) {
                    database.addImageElement(lastId,imagesName.get(i));
                }

                Toast.makeText(ElementsActivity.this,"Element Copy Sucessfully!", Toast.LENGTH_SHORT).show();

                nextActivity = new Intent(ElementsActivity.this, ElementsActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                finish();
                startActivity(nextActivity);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ElementsActivity.this, "Element Not Copy", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();

    }

    public void storeDataArrays() {

        Cursor cursor = database.readAllDataElement(idAsset);

        //ViewSub para poner Lottlie image (el GIF con el emoticon)
        myViewStub.inflate();

        if (cursor.getCount() == 0) {
            myViewStub.setVisibility(View.VISIBLE);
            elementsGroup.setVisibility(View.GONE);
        } else {

            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                idAssetList.add(idAsset);
                name.add(cursor.getString(2));
                uniformat.add(cursor.getString(3));
                category.add(cursor.getString(4));
                type.add(cursor.getString(5));
                year.add(cursor.getString(6));
                quantity.add(cursor.getString(7));
                condition.add(cursor.getString(8));

                //Visibilidad de las vistas
                myViewStub.setVisibility(View.GONE);
                elementsGroup.setVisibility(View.VISIBLE);
                buttonNext.setVisibility(View.VISIBLE);
                buttonDelete.setVisibility(View.VISIBLE);
                buttonEdit.setVisibility(View.VISIBLE);
                buttonCopy.setVisibility(View.VISIBLE);
            }

        }

    }

    public void getRequirementsCount() {

        for (int i=0; i<id.size(); i++) {

            cursor = database.readAllRequirementsWithId(id.get(i));

            while (cursor.moveToNext()) {
                requirements.add(cursor.getString(0));
            }

            requirementsSize.add(String.valueOf(requirements.size()));

            requirements.removeAll(requirements);
        }

    }

    public void getAssetName() {
        Cursor cursor = database.readNameAsset(idAsset);

        cursor.moveToLast();

        nameAsset = cursor.getString(0);
    }

    public void storeIdElement() {
        Cursor cursor = database.readAllIdElement(idAsset);

        cursor.moveToLast();

        lastId = cursor.getString(0);
    }

    private void getImagesPath() {
        Cursor cursor = database.readImagePathElement(id.get(position));

        while (cursor.moveToNext()) {
            imagesName.add(cursor.getString(0));
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