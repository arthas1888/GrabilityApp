package com.apps.www.grabilityapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.apps.www.grabilityapp.modelos.Productos;
import java.util.ArrayList;

/**
 * Created by gustavo morales on 3/07/2016.
 * tavomorales88@gmail.com
 **/
public class ProductosDataBase extends SQLiteOpenHelper {

    private static final String D = ProductosDataBase.class.getSimpleName();
    SQLiteDatabase database;

    // database configuration
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "gravilityDb.db";
    private Context context;

    // table configuration
    private static final String TABLE_NAME = "productos";         // Table name

    public static final String SALE_TABLE_COLUMN_ID = "_id";
    public static final String SALE_TABLE_COLUMN_NOMBRE = "nombre";
    public static final String SALE_TABLE_COLUMN_DESC = "descripcion";
    public static final String SALE_TABLE_COLUMN_PRECIO = "precio";
    public static final String SALE_TABLE_COLUMN_TIPO = "tipo";
    public static final String SALE_TABLE_COLUMN_CAT = "categoria";
    public static final String SALE_TABLE_COLUMN_CAT_ID = "categoria_id";
    public static final String SALE_TABLE_COLUMN_URL_IMAGE_53 = "urlImage_53";
    public static final String SALE_TABLE_COLUMN_URL_IMAGE_75 = "urlImage_75";
    public static final String SALE_TABLE_COLUMN_URL_IMAGE_100 = "urlImage_100";


    private String [] cols = {SALE_TABLE_COLUMN_ID, SALE_TABLE_COLUMN_NOMBRE, SALE_TABLE_COLUMN_DESC,
            SALE_TABLE_COLUMN_PRECIO, SALE_TABLE_COLUMN_TIPO, SALE_TABLE_COLUMN_CAT,
            SALE_TABLE_COLUMN_CAT_ID, SALE_TABLE_COLUMN_URL_IMAGE_53, SALE_TABLE_COLUMN_URL_IMAGE_75,
            SALE_TABLE_COLUMN_URL_IMAGE_100
    };

    public ProductosDataBase(Context aContext) {
        super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
        context = aContext;
        database = getWritableDatabase();
    }

    public void add (Productos productos) {
        // we are using ContentValues to avoid sql format errors
        ContentValues contentValues = new ContentValues();
        contentValues.put(SALE_TABLE_COLUMN_NOMBRE, productos.getNombre());
        contentValues.put(SALE_TABLE_COLUMN_DESC, productos.getDescripcion());
        contentValues.put(SALE_TABLE_COLUMN_PRECIO, productos.getPrecio());
        contentValues.put(SALE_TABLE_COLUMN_TIPO, productos.getTipo());
        contentValues.put(SALE_TABLE_COLUMN_CAT, productos.getCategoria());
        contentValues.put(SALE_TABLE_COLUMN_CAT_ID, productos.getCategoriaId());
        contentValues.put(SALE_TABLE_COLUMN_URL_IMAGE_53, productos.getUrlImage53());
        contentValues.put(SALE_TABLE_COLUMN_URL_IMAGE_75, productos.getUrlImage75());
        contentValues.put(SALE_TABLE_COLUMN_URL_IMAGE_100, productos.getUrlImage100());
        database.insert(TABLE_NAME, null, contentValues);
    }

    public void add (ArrayList<Productos> productosArrayList) {
        // we are using ContentValues to avoid sql format errors
        for (Productos productos : productosArrayList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SALE_TABLE_COLUMN_NOMBRE, productos.getNombre());
            contentValues.put(SALE_TABLE_COLUMN_DESC, productos.getDescripcion());
            contentValues.put(SALE_TABLE_COLUMN_PRECIO, productos.getPrecio());
            contentValues.put(SALE_TABLE_COLUMN_TIPO, productos.getTipo());
            contentValues.put(SALE_TABLE_COLUMN_CAT, productos.getCategoria());
            contentValues.put(SALE_TABLE_COLUMN_CAT_ID, productos.getCategoriaId());
            contentValues.put(SALE_TABLE_COLUMN_URL_IMAGE_53, productos.getUrlImage53());
            contentValues.put(SALE_TABLE_COLUMN_URL_IMAGE_75, productos.getUrlImage75());
            contentValues.put(SALE_TABLE_COLUMN_URL_IMAGE_100, productos.getUrlImage100());
            database.insert(TABLE_NAME, null, contentValues);
        }
    }

    public int update(int id, Productos productos)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SALE_TABLE_COLUMN_NOMBRE, productos.getNombre());
        contentValues.put(SALE_TABLE_COLUMN_DESC, productos.getDescripcion());
        contentValues.put(SALE_TABLE_COLUMN_PRECIO, productos.getPrecio());
        contentValues.put(SALE_TABLE_COLUMN_TIPO, productos.getTipo());
        contentValues.put(SALE_TABLE_COLUMN_CAT, productos.getCategoria());
        contentValues.put(SALE_TABLE_COLUMN_CAT_ID, productos.getCategoriaId());
        contentValues.put(SALE_TABLE_COLUMN_URL_IMAGE_53, productos.getUrlImage53());
        contentValues.put(SALE_TABLE_COLUMN_URL_IMAGE_75, productos.getUrlImage75());
        contentValues.put(SALE_TABLE_COLUMN_URL_IMAGE_100, productos.getUrlImage100());
        return database.update(TABLE_NAME, contentValues, "_id =" + id, null);
    }


    public void delete(int ID)
    {
        database.delete(TABLE_NAME, "_id = " + ID, null);
    }

    public void delete()
    {
        database.delete (TABLE_NAME, null, null);
    }

    public Cursor getAll()
    {
        return database.query(TABLE_NAME, cols, null, null, null, null, null);
    }

    public Cursor getAllData () {
        String buildSQL = "SELECT * FROM " + TABLE_NAME;
        //Log.d(PublicVariables.APPTAG, "getAllData SQL: " + buildSQL);
        return database.rawQuery(buildSQL, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create your tables here
        String buildSQL = "CREATE TABLE " + TABLE_NAME + "( "
                + SALE_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + SALE_TABLE_COLUMN_NOMBRE + " TEXT, "
                + SALE_TABLE_COLUMN_DESC + " TEXT, "
                + SALE_TABLE_COLUMN_PRECIO + " TEXT, "
                + SALE_TABLE_COLUMN_TIPO + " TEXT, "
                + SALE_TABLE_COLUMN_CAT + " TEXT, "
                + SALE_TABLE_COLUMN_CAT_ID + " TEXT, "
                + SALE_TABLE_COLUMN_URL_IMAGE_53 + " TEXT, "
                + SALE_TABLE_COLUMN_URL_IMAGE_75 + " TEXT, "
                + SALE_TABLE_COLUMN_URL_IMAGE_100 + " TEXT)";
        Log.d(D, "onCreate SQL: " + buildSQL);
        sqLiteDatabase.execSQL(buildSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Database schema upgrade code goes here
        String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
        Log.d(D, "onUpgrade SQL: " + buildSQL);
        sqLiteDatabase.execSQL(buildSQL);       // drop previous table
        onCreate(sqLiteDatabase);               // create the table from the beginning
    }

}
