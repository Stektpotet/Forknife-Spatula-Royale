package com.dunno.recipeassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommitRecipeActivity extends AppCompatActivity {

    public static final String TAG = CommitRecipeActivity.class.getName();

    private Button doneButton;
    private View.OnClickListener doneButtonListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    protected Set<String> mDataSet = new HashSet<>();
    protected IngredientListAdapter  mListAdapter;
    private DbHelper dbHelper;
    private int id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_recipe);

        dbHelper = new DbHelper(getApplicationContext());
        id = getIntent().getIntExtra("recipeId", -1);

        mRecyclerView = findViewById(R.id.commit_recycleView_ingredients);

        doneButton = findViewById(R.id.commit_button_done);
        View.OnClickListener doneButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
        doneButton.setOnClickListener(doneButtonListener);

        setupUI();
    }

    public void setActionBarTitle(CharSequence newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }

    private void setupUI() {
        int scrollPosition = 0;
        mLayoutManager = new LinearLayoutManager(this);
        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);


        List<Ingredient> ingredientList = dbHelper.getIngredientsInRecipe(id);
        for (int i = 0; i < ingredientList.size(); i++) {
            mDataSet.add(ingredientList.get(i).name);
        }


        mListAdapter = new IngredientListAdapter(mDataSet);
        mRecyclerView.setAdapter(mListAdapter);


        //HANDLE SWIPING OF ITEMS
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ListItemSwipeHelper(
                        0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                        new ListItemSwipeHelper.SwipeBackground("Discard", Color.RED),
                        new ListItemSwipeHelper.SwipeBackground("Add to shopping list", Color.GREEN)
                ) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        // Remove item from backing list here
                        String item = mListAdapter.getItemAt(viewHolder.getAdapterPosition());
                        if(swipeDir == ItemTouchHelper.LEFT) {
                            RemoveFromFridge(item);
                        } else { //ItemTouchHelper.RIGHT
                            MoveToShoppingList(item);
                        }
                        super.onSwiped(viewHolder, swipeDir);
                    }
                });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        setActionBarTitle("Dish completed - Well done!");
    }



    boolean MoveToShoppingList(String item) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> shoppingListDataSet = prefs.getStringSet(ShoppingListFragment.PREF_SET_NAME, new HashSet<String>());
        shoppingListDataSet.add(item);
        editor.remove(ShoppingListFragment.PREF_SET_NAME).apply();
        editor.putStringSet(ShoppingListFragment.PREF_SET_NAME, shoppingListDataSet).apply();

        mDataSet.remove(item);

        mListAdapter.updateDataSet(mDataSet); //remove the item from the visible list

        return true; //TODO look into validating this
    }


    boolean RemoveFromFridge(String item) {
        boolean removed;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> mFridgeDataSet = prefs.getStringSet(FridgeFragment.PREF_SET_NAME, new HashSet<String>());
        removed = mFridgeDataSet.remove(item);
        removed = mDataSet.remove(item);
        editor.remove(FridgeFragment.PREF_SET_NAME).apply();
        editor.putStringSet(FridgeFragment.PREF_SET_NAME, mFridgeDataSet).apply();
        mListAdapter.updateDataSet(mDataSet); //remove the item from the visible list

        return removed;
    }

}
