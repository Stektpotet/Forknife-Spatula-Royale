package com.dunno.recipeassistant;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


// https://developer.android.com/training/data-storage/sqlite

public class IngredientDbHelper extends SQLiteOpenHelper {

    public static final String TAG = IngredientDbHelper.class.getName();
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Ingredients.db";
    private Context context;

    public static class IngredientEntry implements BaseColumns {
        public static final String TABLE_NAME = "ingredient";
        public static final String COLUMN_NAME_NAME = "name";
    }

    private static final String SQL_CREATE_ENTRIES =

            "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                    IngredientEntry._ID + " INTEGER PRIMARY KEY," +
                    IngredientEntry.COLUMN_NAME_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME;


    public IngredientDbHelper(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
        context = _context;
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void firstTimeSetupIngredientsDb() {        // Empties the ingredients db and refills it.
        SQLiteDatabase ingredientsDb = getWritableDatabase(); // Get instance of db.

        onUpgrade(ingredientsDb, 0, 0);     // Clear out db.

        // Put all ingredients into db:

        //TypedArray ingredientsArray = getResources().obtainTypedArray(R.array.ingredients);
        String[] ingredientsArray = context.getResources().getStringArray(R.array.ingredients);


        for (int i = 0; i < ingredientsArray.length; i++) {
            String ingredient = String.valueOf(ingredientsArray[i]);    // Ingredient to insert.

            ContentValues values = new ContentValues();
            values.put(IngredientDbHelper.IngredientEntry.COLUMN_NAME_NAME, ingredient);
            long newRowId = ingredientsDb.insert(   IngredientDbHelper.IngredientEntry.TABLE_NAME,
                    null,               // If no value in values, don't insert into db.
                    values);
            if (newRowId == -1) {                                                      // Debug message if failed.
                Log.d(TAG, "Inserting  " + ingredient + "  into ingredients db failed.");
            }
            else {
                Log.d(TAG, "Inserted  " + ingredient + "  into db.");
            }
        }
    }

    public Set<String> getIngredientslist() {   // Gets ingredients list from db, and sores it in shared preferences.

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                IngredientEntry._ID,
                IngredientEntry.COLUMN_NAME_NAME
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = IngredientEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = { "" };    // Get all.

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                IngredientEntry.COLUMN_NAME_NAME + " DESC";
//        Cursor cursor = db.query(
//                IngredientEntry.TABLE_NAME,   // The table to query
//                projection,             // The array of columns to return (pass null to get all)
//                selection,              // The columns for the WHERE clause
//                selectionArgs,          // The values for the WHERE clause
//                null,                   // don't group the rows
//                null,                   // don't filter by row groups
//                sortOrder               // The sort order
//        );

        Cursor  cursor = db.rawQuery("select * from " + IngredientEntry.TABLE_NAME,null);

        Set<String> items = new HashSet<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(IngredientEntry._ID));
            String ingredient = cursor.getString(cursor.getColumnIndex(IngredientEntry.COLUMN_NAME_NAME));
            items.add(ingredient);
            Log.d(TAG, "Updating ingredientsList, adding  " + ingredient + "  to Set");
        }
        cursor.close();

        return items;

    }

}
