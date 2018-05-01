package com.dunno.recipeassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static final String TAG = DbHelper.class.getName();
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Assistant.db";
    private static String DATABASE_PATH = "";
    private Context context;
    private SQLiteDatabase dataBase;



    private static final String SQL_CREATE_ENTRIES =

            "CREATE TABLE " + Ingredient.Entry.TABLE_NAME + " (" +
                    Ingredient.Entry._ID + " INTEGER PRIMARY KEY," +
                    Ingredient.Entry.COLUMN_NAME_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Ingredient.Entry.TABLE_NAME; //TODO Does not drop recipe or recipe content yet.


    public DbHelper(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
        context = _context;
    }

    public void firstTimeSetup() {

        Log.d(TAG, "Attempting to read DB file: " + DATABASE_NAME);
        try {

            String queriesText = getStringFromFile();
            SQLiteDatabase db = this.getWritableDatabase();
            Log.d(TAG, queriesText);
            String[] queries = queriesText.split(";");
            for (int i = 0; i < queries.length -1; i++)
            {
                Log.d(TAG, queries[i]);
                db.execSQL(queries[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public String getStringFromFile () throws Exception {
        InputStream fin = context.getAssets().open(DATABASE_NAME);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        onCreate(db);
//        db.execSQL(SQL_DELETE_ENTRIES);
    }



}



// Thrid attempt.:
//###########################################################
// Russian tutorial.

/*
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DbHelper extends SQLiteOpenHelper {
    public static final String TAG = DbHelper.class.getName();
    private static String DB_NAME = "Assistant.sql";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        Log.d(TAG, "Attempting to open db:");
        this.getReadableDatabase();
        Log.d(TAG, "Done.");
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        Log.d(TAG, "CheckDataBase: " + (dbFile.exists() ? "EXIST" : "NOT EXIST"));
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        Log.d(TAG, "Attempting to copy.");
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }
}
*/

        // Russian tutorial.
//###########################################################
        // original:
/*
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


// https://developer.android.com/training/data-storage/sqlite

public class DbHelper extends SQLiteOpenHelper {

    public static final String TAG = DbHelper.class.getName();
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Assistant.db";
    private static String DATABASE_PATH = "";
    private Context context;
    private SQLiteDatabase mDataBase;

    private static final String SQL_CREATE_ENTRIES =

            "CREATE TABLE " + Ingredient.Entry.TABLE_NAME + " (" +
                    Ingredient.Entry._ID + " INTEGER PRIMARY KEY," +
                    Ingredient.Entry.COLUMN_NAME_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Ingredient.Entry.TABLE_NAME; //TODO Does not drop recipe or recipe content yet.


    public DbHelper(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";

        context = _context;

        ensureDataBasePresent();
        Log.d(TAG, "Attempting to open db:");
        this.getReadableDatabase();
        Log.d(TAG, "Done.");

    }


    private boolean checkDataBase() {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void ensureDataBasePresent() {
        if (!checkDataBase()) {
            this.getReadableDatabase();     // Why getDB in the process of setting it up?
            this.close();                   // Unsure what this is.
            try {
                copyDBFile();
                Log.d(TAG, "Nice message: the DB copied successfully.");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = context.getAssets().open(DATABASE_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DATABASE_PATH + DATABASE_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        onCreate(db);
//        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public void dropTables(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }


    public List<Ingredient> getIngredientslist(SQLiteDatabase db) {   // Gets ingredients list from db, and sores it in shared preferences.

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Ingredient.Entry._ID,
                Ingredient.Entry.COLUMN_NAME_NAME
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = Ingredient.Entry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = { "" };    // Get all.

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Ingredient.Entry.COLUMN_NAME_NAME + " DESC";

        Cursor  cursor = db.rawQuery("select * from " + Ingredient.Entry.TABLE_NAME,null);

        List items = new ArrayList<Ingredient>();
        while(cursor.moveToNext()) {

            Ingredient ingredient = new Ingredient();
            ingredient.id = cursor.getInt(cursor.getColumnIndex(Ingredient.Entry._ID));
            ingredient.name = cursor.getString(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_NAME));
            items.add(ingredient);
            Log.d(TAG, "Updating ingredientsList, adding  " + ingredient + "  to Set");
        }
        cursor.close();

        return items;

    }

}
*/
