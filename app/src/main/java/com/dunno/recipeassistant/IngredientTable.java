package com.dunno.recipeassistant;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IngredientTable {

    private static final String TAG = "RecipeAssistantDB";

    //The columns we'll include in the dictionary table
    public static final String COL_NAME = "NAME";

    private static final String DATABASE_NAME = "INGREDIENTS.db";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private static final String[] DEFAULT_SEARCH_COLUMNS = {COL_NAME};

    private final DatabaseOpenHelper mDatabaseOpenHelper;
    public IngredientTable(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }


    public Cursor getIngredientMatches(String query) {
        return getIngredientMatches(query, null);
    }
    public Cursor getIngredientMatches(String query, String[] columns) {
        String selection = COL_NAME + " MATCH ?";
        String[] selectionArgs = new String[]{query + "*"};
        return query(selection, selectionArgs, columns);
    }

    public Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(), columns, selection, selectionArgs, null, null, null);
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }




    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_NAME + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);

            loadResources();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }

        private void loadResources() {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadIngredients();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        private void loadIngredients() throws IOException {
            final Resources resources = mHelperContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.ingredients);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    long id = addIngredient(line.trim());
                    if (id < 0) {
                        Log.e(TAG, "unable to add ingredient: " + line.trim());
                    }
                }
            }
        }

        public long addIngredient(String ingredient) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_NAME, ingredient);
            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }
    }
}