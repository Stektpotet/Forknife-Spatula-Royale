package com.dunno.recipeassistant;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    private IngredientDbHelper ingredientsDbHelper;
    public SQLiteDatabase ingredientsDb;
    private TabPagerAdapter mTabPagerAdapter;
    private ProgressBar     mProgressBar;
    private LockedViewPager mViewPager;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, RecipeActivity.class);// Used for testing. Will be removed when there is a way to open it with UI.
        startActivityForResult(i, 1);

        // Ingredients db setup:
        ingredientsDbHelper = new IngredientDbHelper(getApplicationContext());               // Instantiate the connection to local db.
        ingredientsDb = ingredientsDbHelper.getWritableDatabase(); // Get instance of db.


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().clear().commit(); // Clear SP for testing / debug. TODO remove when working.

        if (!sharedPreferences.getString(getResources().getString(R.string.shared_preferences_version),  // Check if this is first time setup.
                "NoFlag").equals(getResources().getString(R.string.shared_preferences_expected_version))) {

            Log.d(TAG, "No 'first time set' flag found performing first time setup.");
            Log.d(TAG, "strings version: " + getResources().getString(R.string.shared_preferences_expected_version));
            Log.d(TAG, "SP version: " +sharedPreferences.getString(getResources().getString(R.string.shared_preferences_version),  // Check if this is first time setup.
                    "NoFlag"));

            firstTimeSetupIngredientsDb();
            // Set shared pref value to current version so next start don't do first time setup.
            sharedPreferences.edit().putString(getResources().getString(R.string.shared_preferences_version), getResources().getString(R.string.shared_preferences_expected_version));
            sharedPreferences.edit().apply();
        }
        else {
            Log.d(TAG, "Found 'first time set' flag in shared preferences.");
            Log.d(TAG, "strings version: " + getResources().getString(R.string.shared_preferences_expected_version));
            Log.d(TAG, "SP version: " +sharedPreferences.getString(getResources().getString(R.string.shared_preferences_version),  // Check if this is first time setup.
                    "NoFlag"));
        }

        setupUI();
    }

    private void firstTimeSetupIngredientsDb() {        // Empties the ingredients db and refills it.

        ingredientsDbHelper.onUpgrade(ingredientsDb, 0, 0);     // Clear out db.

        // Put all ingredients into db:

        //TypedArray ingredientsArray = getResources().obtainTypedArray(R.array.ingredients);
        String[] ingredientsArray = getResources().getStringArray(R.array.ingredients);

        //TODO REMOVE -> this is just for testing purposes
            SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            Set<String> shoppingList = new HashSet<>();
            shoppingList.addAll(Arrays.asList(ingredientsArray));
            prefEditor.putStringSet(ShoppingListFragment.PREF_SET_NAME, shoppingList);
            prefEditor.apply();
        ///////

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
                Toast.makeText(getApplicationContext(), "Inserted " + ingredient + " into db.", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Inserted  " + ingredient + "  into db.");
            }
        }
    }

    private void setupUI() {

        mViewPager = findViewById(R.id.main_viewPager);
        BottomNavigationView navigation = findViewById(R.id.main_navigationBar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mTabPagerAdapter);
        mViewPager.setCurrentItem(R.id.menu_navigation_recipes);
        navigation.setSelectedItemId(R.id.menu_navigation_recipes);
    }

    public void setActionBarTitle(String newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }
    public void setActionBarTitle(CharSequence newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int list = 0; //default to recipes
            switch (item.getItemId()) {
                case R.id.menu_navigation_shoppingList:
                    list = 2;
                    break;
                case R.id.menu_navigation_recipes:
                    list = 1;
                    break;
                case R.id.menu_navigation_fridge:
                    list = 0;
                    break;
            }

            mViewPager.setCurrentItem(list,true);
            setActionBarTitle(mViewPager.getAdapter().getPageTitle(item.getItemId()));
            return true;
        }
    };


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class TabPagerAdapter extends FragmentPagerAdapter {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(TAG, "Get the " +position + "th item!!!");
            switch (position) {
                case 2:
                    return ShoppingListFragment.newInstance(0);
                case 1:
                    return RecipeListFragment.newInstance(0);
                case 0:
                default:
                    return FridgeFragment.newInstance(0);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case R.id.menu_navigation_fridge:
                    return getBaseContext().getResources().getString(R.string.nav_fridge); //TODO: put these into @values/strings
                case R.id.menu_navigation_recipes:
                    return getBaseContext().getResources().getString(R.string.nav_recipes);
                case R.id.menu_navigation_shoppingList:
                    return getBaseContext().getResources().getString(R.string.nav_shopping);
            }
            return "How did you get here?";
        }
    }
}
