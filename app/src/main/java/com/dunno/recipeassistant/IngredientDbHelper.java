package com.dunno.recipeassistant;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


// https://developer.android.com/training/data-storage/sqlite

public class IngredientDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Ingredients.db";


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


    public IngredientDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
