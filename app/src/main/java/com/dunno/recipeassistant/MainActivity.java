package com.dunno.recipeassistant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    private TabPagerAdapter mTabPagerAdapter;
    private LockedViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Ingredients db setup:
        DbHelper dbHelper = new DbHelper(getApplicationContext());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.getString(getResources().getString(R.string.shared_preferences_version),  // Check if this is first timeInMinutes setup.
                "NoFlag").equals(getResources().getString(R.string.shared_preferences_expected_version))) {



            Log.d(TAG, "No 'first timeInMinutes set' flag found performing first timeInMinutes setup.");
            Log.d(TAG, "strings version: " + getResources().getString(R.string.shared_preferences_expected_version));
            Log.d(TAG, "SP version: " + sharedPreferences.getString(getResources().getString(R.string.shared_preferences_version),  // Check if this is first timeInMinutes setup.
                    "NoFlag"));
            dbHelper.firstTimeSetup();  // Load db from file.

            // Set shared pref value to current version so next start don't do first timeInMinutes setup.
            sharedPreferences.edit().putString(getResources().getString(R.string.shared_preferences_version), getResources().getString(R.string.shared_preferences_expected_version)).apply();

        }
        else {

            Log.d(TAG, "Found 'first timeInMinutes set' flag in shared preferences.");
            Log.d(TAG, "strings version: " + getResources().getString(R.string.shared_preferences_expected_version));
            Log.d(TAG, "SP version: " + sharedPreferences.getString(getResources().getString(R.string.shared_preferences_version),  // Check if this is first timeInMinutes setup.
                    "NoFlag"));

        }

        setupUI();
    }

    void updatePagerTabs() {
        mTabPagerAdapter.notifyDataSetChanged();
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
                case R.id.menu_navigation_recipes:
                    list = 0;
                    break;
                case R.id.menu_navigation_fridge:
                    list = 1;
                    break;
                case R.id.menu_navigation_shoppingList:
                    list = 2;
                    break;
            }

            mViewPager.setCurrentItem(list,true);
            setActionBarTitle(mViewPager.getAdapter().getPageTitle(item.getItemId()));
            return true;
        }
    };


    @Override
    protected void onPostResume() {
        super.onPostResume();

        updatePagerTabs();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class TabPagerAdapter extends FragmentPagerAdapter {

        TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(TAG, "Get the " +position + "th item!!!");
            switch (position) {
                default:
                case 0:
                    return RecipeListFragment.newInstance();
                case 1:
                    return FridgeFragment.newInstance();
                case 2:
                    return ShoppingListFragment.newInstance();
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case R.id.menu_navigation_fridge:
                    return getBaseContext().getResources().getString(R.string.nav_fridge);
                case R.id.menu_navigation_recipes:
                    return getBaseContext().getResources().getString(R.string.nav_recipes);
                case R.id.menu_navigation_shoppingList:
                    return getBaseContext().getResources().getString(R.string.nav_shopping);
            }
            return "How did you get here?";
        }
    }
}
