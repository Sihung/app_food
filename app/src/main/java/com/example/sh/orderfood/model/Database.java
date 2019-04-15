package com.example.sh.orderfood.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


public class Database extends SQLiteOpenHelper {
    private static final String MyDATA = "OrderFoodCPC.db" ;
    private  static final int VERSION =  3;
    //private static final String POSES_TABLE="OrderDetail";

    String DBPATH,DBNAME;
    Context ctx;

    // Table Names
    private static final String TABLE_ORDERDETAIL = "OrderDetail";

    private static final String TAG = "SQLite";
    private static final String ID="ID";

    // OrderDetail Table - column nmaes
    private static final String GIA="Gia";
    private static final String NAME="TenMonAn";
    private static final String SOLUONG="SoLuong";

    // Table Create Statements
    private static final String CREATE_TABLE_ORDERDETAIL = "CREATE TABLE "
            + TABLE_ORDERDETAIL + "(" + ID + " INTEGER PRIMARY KEY," + NAME
            + " TEXT," + GIA + " INTEGER," + SOLUONG
            + " INTEGER" + ")";

    public Database(Context context) {
        super(context, MyDATA, null, VERSION);
        // DB_VERSION is an int,update it every new build
    }


    public List<MonAn> getCart(){


        Log.d("----------","day la ham get cart");
        SQLiteDatabase database = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


        String select[] =  {"TenMonAn","ID","Gia","SoLuong"};
        String sqltable = "OrderDetail";

        queryBuilder.setTables(sqltable);
        Cursor c = queryBuilder.query(database,select,null,null,null,null,null);
        final  List<MonAn> result = new ArrayList<MonAn>();
        if (c.moveToNext()){
            do {
                String sGia = c.getString(c.getColumnIndex("Gia"));
                Log.d("------------","sGia: "+sGia);
                int gia = Integer.valueOf(sGia);
                String tenMonAn = c.getString(c.getColumnIndex("TenMonAn"));
                String img_url = "img_url";
                result.add(new MonAn(tenMonAn, img_url, gia));
            }
            while (c.moveToNext());
        }
        Log.d("-------------------","result: "+result.size());
        return result;
    }


    public void addToCart(MonAn monAn) {
        Log.i(TAG, "DatabaseHelper.addMonAn"+ " - "+ monAn.getTenMonAn());

        SQLiteDatabase db = this.getWritableDatabase();

        int id = 1;
        int soluong =1;
        ContentValues values = new ContentValues();
        //values.put(ID, id);
        values.put(NAME, monAn.getTenMonAn());
        values.put(GIA, monAn.getGia());
        values.put(SOLUONG, soluong);

        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_ORDERDETAIL, null, values);


        // Đóng kết nối database.
        db.close();
    }

    public void DeleteCart(){
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("Delete from OrderDetail");
        database.execSQL(query);
    }

    public void createDataBase() {

        boolean dbExist = checkDataBase();

        SQLiteDatabase db_Read = null;

        if (!dbExist) {
            synchronized (this) {

                db_Read = this.getReadableDatabase();
                Log.e("Path 2", this.getReadableDatabase().getPath());
                db_Read.close();

                copyDataBase();
                Log.v("copyDataBase---", "Successfully");
            }
        }
    }
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DBPATH;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            Log.i("SQLite Error", "database does't exist yet.");
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() {

        try {
            InputStream myInput = ctx.getAssets().open(DBNAME);
            String outFileName = DBPATH;

            OutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024 * 3];

            int length = 0;

            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            StackTraceElement[] trace = new Exception().getStackTrace();

            StringWriter stackTrace1 = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace1));
            System.err.println(stackTrace1);

            Intent send = new Intent(Intent.ACTION_SENDTO);
            String uriText;

            uriText = "mailto:test@test.com"
                    + "&subject=Error Report"
                    + "&body="
                    + stackTrace1.toString();

            uriText = uriText.replace(" ", "%20");
            Uri uri = Uri.parse(uriText);

            send.setData(uri);
            ctx.startActivity(Intent.createChooser(send, "Send mail..."));
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_ORDERDETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERDETAIL);
        // create new tables
        onCreate(db);
    }

}
