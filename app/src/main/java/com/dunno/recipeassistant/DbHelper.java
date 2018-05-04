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
            ingredient.unit = cursor.getString(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_UNIT));
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
        ingredient.unit = cursor.getString(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_UNIT));
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
            recipe.timeInMinutes = cursor.getInt(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Time));
            recipe.description = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Description));
            recipe.instructions = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Instructions));
            recipe.image = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Image));

            items.add(recipe);
            Log.d(TAG, "\tAdding  " + recipe.title);
        }
        cursor.close();

        return updateAllHasValues(items);
    }

    public final static String TAG_TABLE_NAME = "tag";
    public final static String TAG_COLUMN_NAME_ID = "_ID";
    public final static String TAG_COLUMN_NAME_Name = "name";

    public Set<RecipeTag> getRecipeTags(int id) {   // Gets tags of a recipe from db

        //select tag._ID, tag.name from tag INNER JOIN recipeTag ON tag._ID = recipeTag.tagId WHERE recipeId = id;
        Cursor cursor = dataBase.rawQuery(  "select * from " + TAG_TABLE_NAME +
                " inner join " + RecipeTag.Entry.TABLE_NAME + " on " + TAG_COLUMN_NAME_ID +
                " = " + RecipeTag.Entry.COLUMN_NAME_recipeId + " where " + RecipeTag.Entry.COLUMN_NAME_recipeId + " = " + id,null);
        Log.d(TAG, "Get recipe tags by id(" + id + "):");
        Set<RecipeTag> tags = new HashSet<>();
        while(cursor.moveToNext()) {
            int tagID = cursor.getInt(cursor.getColumnIndex(TAG_COLUMN_NAME_ID));
            String name = cursor.getString(cursor.getColumnIndex(TAG_COLUMN_NAME_Name));
            RecipeTag tag = new RecipeTag(tagID, name);
            tags.add(tag);
            Log.d(TAG, "\tAdding  " + tag.getText());
        }
        cursor.close();
        return tags;
    }

    public Set<RecipeTag> getActiveTags() { //gets tags that are used by recipes
        String query = "select * from " +TAG_TABLE_NAME + " where " + TAG_COLUMN_NAME_ID + " in (select distinct " +
                        RecipeTag.Entry.COLUMN_NAME_tagId + " from " + RecipeTag.Entry.TABLE_NAME + ")";
        Cursor cursor = dataBase.rawQuery(query,null);

        Set<RecipeTag> tags = new HashSet<>();
        while(cursor.moveToNext()) {
            int tagID = cursor.getInt(cursor.getColumnIndex(TAG_COLUMN_NAME_ID));
            String name = cursor.getString(cursor.getColumnIndex(TAG_COLUMN_NAME_Name));
            RecipeTag tag = new RecipeTag(tagID, name);
            tags.add(tag);
            Log.d(TAG, "\tAdding  " + tag.getText());
        }
        cursor.close();
        return tags;
    }

    public Set<Recipe> getRecipesWithTags(RecipeTag... tags) {   // Gets tags of a recipe from db
        if(tags.length < 1) {
            Log.e(TAG, "DON'T DO THAT YOU FUCKER");
            return null;
        }
        String query = "select distinct "+Recipe.Entry.TABLE_NAME+".* " + "from "+ Recipe.Entry.TABLE_NAME + " inner join " +
                RecipeTag.Entry.TABLE_NAME + " on " + Recipe.Entry.COLUMN_NAME_ID + " = " +
                RecipeTag.Entry.COLUMN_NAME_recipeId + " where " + RecipeTag.Entry.COLUMN_NAME_tagId + " = " + tags[0].getId();
        Log.d(TAG, "Get recipe by tag id(" + tags[0].getId() + "):");

        for(int i = 1; i < tags.length; i++) {
            query = query + " or "+ RecipeTag.Entry.COLUMN_NAME_tagId + " = " + tags[i].getId();
            Log.d(TAG, "Get recipe by tag id(" + tags[i].getId() + "):");
        }

        Cursor cursor = dataBase.rawQuery(query,null);

        Set<Recipe> recipes = new HashSet<>();
        while(cursor.moveToNext()) {
            Recipe recipe = new Recipe();
            recipe.id = cursor.getInt(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_ID));
            recipe.title = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Title));
            recipe.timeInMinutes = cursor.getInt(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Time));
            recipe.description = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Description));
            recipe.instructions = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Instructions));
            recipe.image = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Image));
            recipes.add(recipe);
            Log.d(TAG, "\tAdding  " + recipe.title);
        }
        cursor.close();
        return recipes;
    }

    public Recipe getRecipeById(int id) {   // Gets ingredients list from db, and sores it in shared preferences.

        Cursor cursor = dataBase.rawQuery(  "select * from " + Recipe.Entry.TABLE_NAME +
                " where _ID IS " + id,null);
        Log.d(TAG, "Get recipe by id(" + id + "):");

        cursor.moveToNext();
        Recipe recipe = new Recipe();
        recipe.id = cursor.getInt(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_ID));
        recipe.title = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Title));
        recipe.timeInMinutes = cursor.getInt(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Time));
        recipe.description = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Description));
        recipe.instructions = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Instructions));
        recipe.image = cursor.getString(cursor.getColumnIndex(Recipe.Entry.COLUMN_NAME_Image));

        Log.d(TAG, "\tGetting " + recipe.title);


        cursor.close();
        return updateHasValue(recipe);
    }

    public List<Ingredient> getIngredientsInRecipe(int id) {   // Gets ingredients list from db, and sores it in shared preferences.

        Log.d("TAG", "SQL For getting ingredients from recipe" +
                " \nselect " +Ingredient.Entry.COLUMN_NAME_ID +
                "\n, " + Ingredient.Entry.COLUMN_NAME_NAME +
                "\n, " + Ingredient.Entry.COLUMN_NAME_UNIT +
                "\n, cont." + RecipeContent.COLUMN_NAME_AMOUNT +
                "\n from " + Ingredient.Entry.TABLE_NAME +
                "\n inner join " + RecipeContent.TABLE_NAME + " cont " +
                "\n on cont." + RecipeContent.COLUMN_NAME_IngredientID +
                " = " + Ingredient.Entry.COLUMN_NAME_ID +
                "\n where " + RecipeContent.COLUMN_NAME_RecipeID +
                " = " + id);

        Cursor cursor = dataBase.rawQuery(  " select " +Ingredient.Entry.COLUMN_NAME_ID +
                                                ", " + Ingredient.Entry.COLUMN_NAME_NAME +
                                                ", " + Ingredient.Entry.COLUMN_NAME_UNIT +
                                                ", cont." + RecipeContent.COLUMN_NAME_AMOUNT +
                                                " from " + Ingredient.Entry.TABLE_NAME +
                                                " inner join " + RecipeContent.TABLE_NAME + " cont " +
                                                " on cont." + RecipeContent.COLUMN_NAME_IngredientID +
                                                " = " + Ingredient.Entry.COLUMN_NAME_ID +
                                                " where cont." + RecipeContent.COLUMN_NAME_RecipeID +
                                                " = " + id
                                                ,null);

        Log.d(TAG, "Get ingredient in recipe(" + id + "):");

        List items = new ArrayList<Ingredient>();
        while(cursor.moveToNext()) {

            Ingredient ingredient = new Ingredient();
            ingredient.id = cursor.getInt(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_ID));
            ingredient.name = cursor.getString(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_NAME));
            ingredient.unit = cursor.getString(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_UNIT));
            ingredient.amount = cursor.getFloat(cursor.getColumnIndex(Ingredient.Entry.COLUMN_NAME_UNIT) +1);
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
                hasPercentage += 1.0f / (float)ingredientsList.size();
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