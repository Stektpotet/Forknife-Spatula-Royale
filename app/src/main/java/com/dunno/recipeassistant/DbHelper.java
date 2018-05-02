package com.dunno.recipeassistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DbHelper extends SQLiteOpenHelper {

    public static final String TAG = DbHelper.class.getName();
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Assistant.db";
    private static String DATABASE_PATH = "";
    private Context context;
    private SQLiteDatabase dataBase;


    public DbHelper(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
        context = _context;

        dataBase = this.getWritableDatabase();
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


    // Credit to Panos on these who functions:
    // https://stackoverflow.com/questions/12910503/read-file-as-string
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



    public List<Ingredient> getIngredientslist() {   // Gets ingredients list from db, and sores it in shared preferences.

        Cursor cursor = dataBase.rawQuery("select * from " + Ingredient.Entry.TABLE_NAME,null);

        Log.d(TAG, "Get ingredientsList:");

        List items = new ArrayList<Ingredient>();
        while(cursor.moveToNext()) {

            Ingredient ingredient = new Ingredient();
            ingredient.id = cursor.getInt(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_ID));
            ingredient.name = cursor.getString(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_NAME));
            items.add(ingredient);
            Log.d(TAG, "\tAdding  " + ingredient.name);
        }
        cursor.close();

        return items;
    }

    public Ingredient getIngredientById(int id) {   // Gets ingredients list from db, and sores it in shared preferences.

        Cursor cursor = dataBase.rawQuery(  "select * from " + Ingredient.Entry.TABLE_NAME +
                                                " where _ID IS " + id,null);
        Log.d(TAG, "Get ingredient by id(" + id + "):");

        cursor.moveToNext();

        Ingredient ingredient = new Ingredient();
        ingredient.id = cursor.getInt(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_ID));
        ingredient.name = cursor.getString(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_NAME));
        Log.d(TAG, "\tGetting  " + ingredient.name);

        cursor.close();
        return ingredient;
    }

    public void removeIngredientWithId(int id) {   // Gets ingredients list from db, and sores it in shared preferences.

        dataBase.rawQuery(  "delete from " + Ingredient.Entry.TABLE_NAME +
                " where _ID = " + id,null);

        Log.d(TAG, "Removed ingredient with id(" + id + "):");
    }

    public List<Recipe> getRecipelist() {   // Gets ingredients list from db, and sores it in shared preferences.

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        Cursor cursor = dataBase.rawQuery("select * from " + Recipe.Entry.TABLE_NAME,null);

        Log.d(TAG, "Get recipeList:");

        List items = new ArrayList<Recipe>();
        while(cursor.moveToNext()) {

            Recipe recipe = new Recipe();
            recipe.id = cursor.getInt(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_ID));
            recipe.title = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Title));
            recipe.time = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Time));
            recipe.description = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Description));
            recipe.instructions = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Instructions));

            items.add(recipe);
            Log.d(TAG, "\tAdding  " + recipe.title);
        }
        cursor.close();

        return updateAllHasValues(items);
    }

    public Recipe getRecipeById(int id) {   // Gets ingredients list from db, and sores it in shared preferences.

        Cursor cursor = dataBase.rawQuery(  "select * from " + Recipe.Entry.TABLE_NAME +
                " where _ID IS " + id,null);
        Log.d(TAG, "Get recipe by id(" + id + "):");

        cursor.moveToNext();
        Recipe recipe = new Recipe();
        recipe.id = cursor.getInt(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_ID));
        recipe.title = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Title));
        recipe.time = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Time));
        recipe.description = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Description));
        recipe.instructions = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Instructions));

        Log.d(TAG, "\tGetting " + recipe.title);


        cursor.close();
        return updateHasValue(recipe);
    }

    public List<Ingredient> getIngredientsInRecipe(int id) {   // Gets ingredients list from db, and sores it in shared preferences.

        Cursor cursor = dataBase.rawQuery(  " select * from " + Ingredient.Entry.TABLE_NAME +
                                                " where _ID in (" +
                                                    " select " +RecipeContent.COLUMN_NAME_IngredientID +
                                                    " from " +  RecipeContent.TABLE_NAME +
                                                    " where " + RecipeContent.COLUMN_NAME_RecipeID + " = " + id + ")"
                                                ,null);

        Log.d(TAG, "Get ingredient in recipe(" + id + "):");

        List items = new ArrayList<Ingredient>();
        while(cursor.moveToNext()) {

            Ingredient ingredient = new Ingredient();
            ingredient.id = cursor.getInt(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_ID));
            ingredient.name = cursor.getString(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_NAME));
            items.add(ingredient);
            Log.d(TAG, "\tAdding  " + ingredient.name);
        }
        cursor.close();

        return items;
    }

    public Recipe updateHasValue(Recipe recipe) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> fridgeList = sharedPreferences.getStringSet(FridgeFragment.PREF_SET_NAME, new HashSet<String>());

        float hasPercentage = 0;

        List<Ingredient> ingredientsList = getIngredientsInRecipe(recipe.id);
        for (int j = 0; j < ingredientsList.size(); j++) {

            if (fridgeList.contains(ingredientsList.get(j).name))       // If in fridge.
            {
                hasPercentage += 1 / ingredientsList.size();
            }
        }
        recipe.hasPercentage = hasPercentage;
        return recipe;
    }

    public List<Recipe> updateAllHasValues(List<Recipe> recipeList) {   // Updates all recipes 'has' values which indicates
                                                                // how many of the required ingredients are in the fridge.
        for (int i = 0; i < recipeList.size(); i++) {

            recipeList.set(i, updateHasValue(recipeList.get(i)));
        }

        return recipeList;
    }


    public void removeRecipeWithId(int id) {   // Gets ingredients list from db, and sores it in shared preferences.

        dataBase.rawQuery("delete from " + Recipe.Entry.TABLE_NAME +
                " where _ID = " + id,null);

        Log.d(TAG, "Removed recipe with id(" + id + "):");
    }




}