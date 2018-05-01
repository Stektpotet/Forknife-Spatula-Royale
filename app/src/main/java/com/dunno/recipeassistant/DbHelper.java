package com.dunno.recipeassistant;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;


// https://developer.android.com/training/data-storage/sqlite

public class DbHelper extends SQLiteAssetHelper {

    public static final String TAG = DbHelper.class.getName();
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Assistant.db";
    private Context context;

    private static final String SQL_CREATE_ENTRIES =

            "CREATE TABLE " + Ingredient.Entry.TABLE_NAME + " (" +
                    Ingredient.Entry._ID + " INTEGER PRIMARY KEY," +
                    Ingredient.Entry.COLUMN_NAME_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Ingredient.Entry.TABLE_NAME; //TODO Does not drop recipe or recipe content yet.


    public DbHelper(Context _context) {
        super(_context, DATABASE_NAME, _context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
        context = _context;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void dropTables(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public void firstTimeSetup() {


        //firstTimeSetupIngredientsDb();      // Gets ingredients from strings.xml. Bad.
    }



//    private void firstTimeSetupIngredientsDb() {        // Empties the ingredients db and refills it.
//
//        SQLiteDatabase ingredientsDb = getWritableDatabase(); // Get instance of db.
//        onUpgrade(ingredientsDb, 0, 0);     // Clear out db.
//
//        // Put all ingredients into db:
//
//        //TypedArray ingredientsArray = getResources().obtainTypedArray(R.array.ingredients);
//        String[] ingredientsArray = context.getResources().getStringArray(R.array.ingredients);
//
//
//        for (int i = 0; i < ingredientsArray.length; i++) {
//            String ingredient = String.valueOf(ingredientsArray[i]);    // com.dunno.recipeassistant.Ingredient to insert.
//
//            ContentValues values = new ContentValues();
//            values.put(DbHelper.IngredientEntry.COLUMN_NAME_NAME, ingredient);
//            long newRowId = ingredientsDb.insert(   DbHelper.IngredientEntry.TABLE_NAME,
//                    null,               // If no value in values, don't insert into db.
//                    values);
//            if (newRowId == -1) {                                                      // Debug message if failed.
//                Log.d(TAG, "Inserting  " + ingredient + "  into ingredients db failed.");
//            }
//            else {
//                Log.d(TAG, "Inserted  " + ingredient + "  into db.");
//            }
//        }
//    }

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
