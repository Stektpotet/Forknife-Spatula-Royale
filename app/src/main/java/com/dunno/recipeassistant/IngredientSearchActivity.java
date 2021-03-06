package com.dunno.recipeassistant;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class IngredientSearchActivity extends AppCompatActivity {

    public static final String TAG = IngredientSearchActivity.class.getName();

    static final int REQUEST_PICK_INGREDIENT = 1;

    public static final String KEY_RETURNED_INGREDIENT = TAG + "ingredient_key";

    private String customIngredient = "";
    Button addButton;               // Button for adding custom ingredient as string.
    ListView mSearchResultsListView;
    List<Ingredient> searchResult = new ArrayList<>();
    SearchableAdapter<Ingredient> mSearchableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_search);
        DbHelper dbHelper = new DbHelper(this);
        searchResult.addAll(dbHelper.getIngredientslist());
        setupUI();
        openOptionsMenu();

    }


    private void setupUI() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        addButton = findViewById(R.id.activity_ingredient_search_add_button);
        addButton.setVisibility(View.INVISIBLE);        // not visible until list is empty.
        View.OnClickListener addButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add string currently in search field to shoppingList.
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_RETURNED_INGREDIENT, customIngredient);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        };
        addButton.setOnClickListener(addButtonListener);

        mSearchResultsListView = findViewById(R.id.activity_ingredient_search_list);
        mSearchableAdapter = new SearchableAdapter<>(this, searchResult);
        mSearchResultsListView.setAdapter(mSearchableAdapter);
        mSearchResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_RETURNED_INGREDIENT, mSearchableAdapter.getItem(i).name);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        mSearchResultsListView.setOnHierarchyChangeListener(new AdapterView.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View view, View view1) {addButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onChildViewRemoved(View view, View view1) {
                if (mSearchResultsListView.getCount() == 0) {     // If no more items in list.
                    addButton.setVisibility(View.VISIBLE);
                }
            }
        });

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Log.d(TAG, "Attempting to go home!!!");
            onBackPressed();
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @SuppressLint("SetTextI18n")        // We need to have this space between Add and ___ .
            @Override                           // The space gets cut off from the string.xml entry.
            public boolean onQueryTextChange(String newText) {
                mSearchableAdapter.getFilter().filter(newText);
                customIngredient = newText;
                addButton.setText(getString(R.string.add_this_ingredient_button) + " " + newText);
                return true;
            }
        });

        return true;
    }
}
