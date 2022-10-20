package br.ram.buildingcolletor.element;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.florent37.runtimepermission.RuntimePermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import br.ram.buildingcolletor.ImageAdapter;
import br.ram.buildingcolletor.R;
import br.ram.buildingcolletor.RecyclerViewClickListener;
import br.ram.buildingcolletor.asset.AssetsActivity;
import br.ram.buildingcolletor.database.DataBaseSQL;

public class ElementsPhotoActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private Button buttonCaptureImage, buttonAssetsList, buttonElementList, buttonNext, buttonBack, buttonDelete;
    protected String idAsset;
    private Intent nextActivity;
    protected RecyclerView recyclerView;
    protected ImageAdapter adapter;
    protected ArrayList<Bitmap> images;
    private static final int READ_PERMISSION = 101;
    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 200;
    protected Bitmap imageCamera;
    protected DataBaseSQL dataBase;
    protected ArrayList<String> id, pathList;
    protected int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements_photo);

        //Reference
        buttonCaptureImage = findViewById(R.id.button_capture_image_element);
        buttonNext = findViewById(R.id.button_next_photo_element);
        buttonBack = findViewById(R.id.button_back_photo_element);
        buttonDelete = findViewById(R.id.button_drop_image_element);
        buttonElementList = findViewById(R.id.button2_photo_element);
        buttonAssetsList = findViewById(R.id.button1_photo_element);
        recyclerView = findViewById(R.id.recyclerView_Gallery_Images_Elements);

        dataBase = new DataBaseSQL(this);

        images = new ArrayList<>();
        id = new ArrayList<>();
        pathList = new ArrayList<>();

        if(getIntent().hasExtra("idAsset")) {

            //Geting Data from Intent
            idAsset = getIntent().getStringExtra("idAsset");

        }

        adapter = new ImageAdapter(ElementsPhotoActivity.this,this,images);
        recyclerView.setLayoutManager(new GridLayoutManager(ElementsPhotoActivity.this,1));
        recyclerView.setAdapter(adapter);

        //Button Capture Image
        buttonCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ElementsPhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ElementsPhotoActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_PERMISSION);

                    return;
                }

                getImage();
            }
        });

        //Delete Image from ImageView
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (images.size() == 0) {
                    Toast.makeText(ElementsPhotoActivity.this, "Please add images.", Toast.LENGTH_SHORT).show();
                } else {

                    confirmDropDialog();

                }
            }
        });

        //Next Activity
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If no images, so we have to add images
                if (images.size() == 0) {
                    Toast.makeText(ElementsPhotoActivity.this, "Please add images", Toast.LENGTH_SHORT).show();
                } else {

                    if (pathList != null) {
                        //Bucle for put idAsset like 1 and the path
                        for (int i=0; i<pathList.size(); i++) {
                            String idAsset = "0";
                            dataBase.addImageElement(idAsset,pathList.get(i));
                        }
                        Toast.makeText(ElementsPhotoActivity.this, "Images add Sucessfully!", Toast.LENGTH_SHORT).show();
                    }

                    nextActivity = new Intent(ElementsPhotoActivity.this, ElementNameActivity.class);
                    nextActivity.putExtra("idAsset",idAsset);
                    finish();
                    startActivity(nextActivity);
                }


            }
        });

        //Previous Activity
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementsPhotoActivity.this, ElementsActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonAssetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementsPhotoActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        });

        buttonElementList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity = new Intent(ElementsPhotoActivity.this, ElementsActivity.class);
                nextActivity.putExtra("idAsset",idAsset);
                finish();
                startActivity(nextActivity);
            }
        });

    }

    private void getImage() {
        final CharSequence[] items;
        try{
            items = new CharSequence[]{"Take Photo","Choose Image","Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(ElementsPhotoActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Select Image");
            builder.setItems(items, (dialogInterface, i) -> {
                if(items[i].equals("Take Photo")){
                    RuntimePermission.askPermission(this)
                            .request(Manifest.permission.CAMERA)
                            .onAccepted(result -> {
                                takePicture();
                            })
                            .onDenied(result -> {
                                new android.app.AlertDialog.Builder(this)
                                        .setMessage("Please accept our permissions")
                                        .setPositiveButton("yes", (dialog1, which) -> result.askAgain()) // ask again
                                        .setNegativeButton("no", (dialog1, which) -> dialog1.dismiss())
                                        .show();
                            })
                            .ask();
                }else if(items[i].equals("Choose Image")){
                    RuntimePermission.askPermission(this)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .onAccepted(result -> {
                                onClickGallery();
                            })
                            .onDenied(result -> {
                                new android.app.AlertDialog.Builder(this)
                                        .setMessage("Please accept our permissions")
                                        .setPositiveButton("yes", (dialog1, which) -> result.askAgain()) // ask again
                                        .setNegativeButton("no", (dialog1, which) -> dialog1.dismiss())
                                        .show();

                            })
                            .ask();
                }else{
                    dialogInterface.dismiss();
                }

            });
            builder.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageCamera);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(takePictureIntent, REQUEST_CAMERA);
    }

    private void onClickGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){

            if(data.getClipData() != null) {

                int countOfImages = data.getClipData().getItemCount();

                for(int i=0; i <countOfImages; i++) {

                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    try {
                        //Transform Uri to Bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),imageUri);
                        images.add(bitmap);
                        SaveImage(bitmap);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                adapter.notifyDataSetChanged();

            } else {

                Uri imageUri = data.getData();
                try {
                    //Transform Uri to Bitmap
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),imageUri);
                    images.add(bitmap);
                    SaveImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            adapter.notifyDataSetChanged();

        }
        else if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK){

            if(data.getClipData() != null) {

                int countOfImages = data.getClipData().getItemCount();

                for(int i=0; i <countOfImages; i++) {

                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    try {
                        //Transform Uri to Bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),imageUri);

                        images.add(bitmap);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                adapter.notifyDataSetChanged();

            } else {

                Uri imageUri = data.getData();
                try {
                    //Transform Uri to Bitmap
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),imageUri);

                    images.add(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();

        }
        else {
            Toast.makeText(this, "You haven't pick any image", Toast.LENGTH_LONG).show();
        }
    }

    private boolean CheckPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
            return false;
        }
        return true;
    }

    private void confirmDropDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Image?");
        builder.setMessage("Are you sure you want to delete image?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                images.remove(images.get(position));
                dataBase.deleteOneImageElement(position);
                Toast.makeText(ElementsPhotoActivity.this,"Image delete Sucessfully!", Toast.LENGTH_SHORT).show();
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position,adapter.getItemCount());

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ElementsPhotoActivity.this, "Image not delete", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();

    }

    private File SaveImage(Bitmap image) {
        if (!CheckPermission())
            return null;

        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/ImagesElement";
            File fileDir = new File(path);
            if (!fileDir.exists())
                fileDir.mkdir();

            String imageName = "/Element_" + new Date().getTime() + ".png";

            pathList.add(imageName);

            String mPath = path + "/Element_" + new Date().getTime() + ".png";

            Bitmap bitmap = image;
            File file = new File(mPath);
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();

            return file;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

        this.position = position;

    }
}