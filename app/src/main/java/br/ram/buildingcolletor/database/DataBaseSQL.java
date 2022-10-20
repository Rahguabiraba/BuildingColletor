package br.ram.buildingcolletor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataBaseSQL extends SQLiteOpenHelper {

    protected SQLiteDatabase db;
    private Context context;

    public DataBaseSQL(Context context) {
        super(context, "assets", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE controlAssets(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, area TEXT, location TEXT, latitude DOUBLE, longitude DOUBLE, details TEXT)");
        db.execSQL("CREATE TABLE tableImages(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, idAsset INTEGER, imagePath TEXT," +
                "FOREIGN KEY (idAsset) REFERENCES controlAssets(id) " +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE)");
        db.execSQL("CREATE TABLE controlElements (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, idAsset INTEGER, name TEXT, uniformat TEXT, category TEXT, type TEXT, year TEXT, quantity TEXT, condi TEXT," +
                "FOREIGN KEY (idAsset) REFERENCES controlAssets(id) " +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE)");
        db.execSQL("CREATE TABLE tableImagesElement(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, idElement INTEGER, imagePath TEXT," +
                "FOREIGN KEY (idElement) REFERENCES controlElements(id) " +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE)");
        db.execSQL("CREATE TABLE controlRequirements(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, idElement INTEGER, name TEXT, details TEXT," +
                "FOREIGN KEY (idElement) REFERENCES controlElements(id) " +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS controlAssets");
        db.execSQL("DROP TABLE IF EXISTS tableImages");
        this.onCreate(db);
    }

    //*******Assets Code*********
    public void addAsset(String n, String a, String l, double la, double lo, String d) {
        db = this.getReadableDatabase();
        db.execSQL("INSERT INTO controlAssets (name, area, location, latitude, longitude, details) VALUES ('"+n+"', '"+a+"', '"+l+"', '"+la+"', '"+lo+"', '"+d+"')");
    }

    public Cursor readAllDataAsset() {
        String query = "SELECT * FROM controlAssets";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllIdAsset() {
        String query = "SELECT id FROM controlAssets";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void deleteAsset (String id) {
        db = this.getReadableDatabase();
        db.execSQL("DELETE FROM controlAssets WHERE id ='"+id+"'");
    }

    public void updateAsset (String id, String name, String area, String location, double latitude, double longitude, String details) {
        db = this.getReadableDatabase();
        db.execSQL("UPDATE controlAssets SET name = '"+name+"', area = '"+area+"', location = '"+location+"', latitude = '"+latitude+"', longitude = '"+longitude+"', details = '"+details+"' WHERE id = '"+id+"' ");
    }

    //******Images Code*********

    public void addImage(String idAsset, String path) {
        db = this.getReadableDatabase();
        db.execSQL("INSERT INTO tableImages (idAsset, imagePath) VALUES ('"+idAsset+"','"+path+"')");
    }

    public void deleteOneImageAsset (int id) {
        db = this.getReadableDatabase();
        db.execSQL("DELETE FROM tableImages WHERE id ='"+id+"'");
    }

    public void updateAllIdImages (String idAsset) {
        db = this.getReadableDatabase();
        db.execSQL("UPDATE tableImages SET idAsset = '"+idAsset+"' WHERE idAsset = 0");
    }

    public Cursor readImagePath(String id) {
        String query = "SELECT imagePath FROM tableImages WHERE idAsset ='"+id+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readIdWithZero() {
        String query = "SELECT idAsset FROM tableImages WHERE idAsset = 0";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //**********Elements Code*********

    public Cursor readNameAsset(String idAsset) {
        String query = "SELECT name FROM controlAssets WHERE id='"+idAsset+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataElement() {
        String query = "SELECT * FROM controlElements";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllIdElements() {
        String query = "SELECT id FROM controlElements";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataElement(String idAsset) {
        String query = "SELECT * FROM controlElements WHERE idAsset='"+idAsset+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllIdElement(String idAsset) {
        String query = "SELECT id FROM controlElements WHERE idAsset='"+idAsset+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void addElement (String idAsset, String name, String uniformat, String category, String type, String year, String quantity, String condition) {
        db = this.getReadableDatabase();
        db.execSQL("INSERT INTO controlElements (idAsset, name, uniformat, category, type, year, quantity, condi) VALUES ('"+idAsset+"', '"+name+"', '"+uniformat+"', '"+category+"', '"+type+"', '"+year+"', '"+quantity+"', '"+condition+"') ");
    }

    public void deleteElement (String id) {
        db = this.getReadableDatabase();
        db.execSQL("DELETE FROM controlElements WHERE id ='"+id+"'");
    }

    public void updateElement (String id, String name, String uniformat, String category, String type, String year, String quantity, String condition) {
        db = this.getReadableDatabase();
        db.execSQL("UPDATE controlElements SET name = '"+name+"', uniformat = '"+uniformat+"', category = '"+category+"', type = '"+type+"', year = '"+year+"', quantity = '"+quantity+"', condi = '"+condition+"' WHERE id = '"+id+"' ");
    }

    //******Images Element Code*********

    public void addImageElement(String idElement, String path) {
        db = this.getReadableDatabase();
        db.execSQL("INSERT INTO tableImagesElement (idElement, imagePath) VALUES ('"+idElement+"','"+path+"')");
    }

    public void deleteOneImageElement (int id) {
        db = this.getReadableDatabase();
        db.execSQL("DELETE FROM tableImagesElement WHERE id ='"+id+"'");
    }

    public void updateAllIdImagesElement (String idElement) {
        db = this.getReadableDatabase();
        db.execSQL("UPDATE tableImagesElement SET idElement = '"+idElement+"' WHERE idElement = 0");
    }

    public Cursor readImagePathElement (String id) {
        String query = "SELECT imagePath FROM tableImagesElement WHERE idElement ='"+id+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readIdWithZeroElement() {
        String query = "SELECT idElement FROM tableImagesElement WHERE idElement = 0";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //Requirements Code

    public void addRequirements (String idElement, String name, String details) {
        db = this.getReadableDatabase();
        db.execSQL("INSERT INTO controlRequirements (idElement, name, details) VALUES ('"+idElement+"', '"+name+"', '"+details+"') ");
    }

    public Cursor readNameElement(String idElement) {
        String query = "SELECT name FROM controlElements WHERE id='"+idElement+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataRequirement(String idElement) {
        String query = "SELECT * FROM controlRequirements WHERE idElement='"+idElement+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllRequirementsWithId(String idElement) {
        String query = "SELECT id FROM controlRequirements WHERE idElement = '"+idElement+"' ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataRequirement() {
        String query = "SELECT * FROM controlRequirements";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateRequirement (String id, String name, String details) {
        db = this.getReadableDatabase();
        db.execSQL("UPDATE controlRequirements SET name = '"+name+"', details = '"+details+"' WHERE id = '"+id+"' ");
    }

    public void deleteRequirement (String id) {
        db = this.getReadableDatabase();
        db.execSQL("DELETE FROM controlRequirements WHERE id ='"+id+"'");
    }
}
