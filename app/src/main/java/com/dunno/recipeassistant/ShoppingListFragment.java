package com.dunno.recipeassistant;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class ShoppingListFragment extends Fragment {

    public static final int DATASET_COUNT = 60;

    public static final String PREF_SET_NAME = "SHOPPING_LIST";

    private TextView             mEmptyListStatusText;
    private FloatingActionButton mAddIngredientButton;

    protected RecyclerView.LayoutManager    mLayoutManager;
    protected IngredientListAdapter         mListAdapter;
    protected RecyclerView                  mRecyclerView;
    protected Set<String>                   mDataSet = new HashSet<>();
    public ShoppingListFragment() {}

    MenuItem mMenuItemClearAll;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        mMenuItemClearAll = menu.add("Discard All");
        MenuItem mMenuClearPrefs = menu.add("Clear Prefs");
        mMenuClearPrefs.setEnabled(true);
        mMenuItemClearAll.setEnabled(mDataSet.size() > 0);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Discard All")) {

        }
        if (item.getTitle().equals("Clear Prefs")) {
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply();
        }
        return super.onOptionsItemSelected(item);
    }

    public static ShoppingListFragment newInstance(int entry) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        //SET ARGS
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        MainActivity activity = (MainActivity) getActivity();


        if (savedInstanceState != null) {

        }
        setupUI(rootView);
        return rootView;
    }

    private void setupUI(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.fragment_shoppinglist_recyclerView);
        int scrollPosition = 0;
        mLayoutManager = new LinearLayoutManager(getActivity());
        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);

        mDataSet = PreferenceManager.getDefaultSharedPreferences(getActivity()).getStringSet(PREF_SET_NAME,  new HashSet<String>());

        mListAdapter = new IngredientListAdapter(mDataSet);
        mRecyclerView.setAdapter(mListAdapter);


        mEmptyListStatusText = rootView.findViewById(R.id.fragment_shoppinglist_txt_empty);
        mEmptyListStatusText.setVisibility((mDataSet.size() > 0) ? View.GONE : View.VISIBLE);

        //HANDLE NEW ITEM-BUTTON
        mAddIngredientButton = rootView.findViewById(R.id.fragment_shoppinglist_btn_add);
        mAddIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(getActivity(), IngredientSearchActivity.class).setAction(Intent.ACTION_SEARCH);
                startActivityForResult(searchIntent, IngredientSearchActivity.REQUEST_PICK_INGREDIENT);
            }
        });


        //HANDLE SWIPING OF ITEMS
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ListItemSwipeHelper(
                        0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                        new ListItemSwipeHelper.SwipeBackground("Add to Fridge", Color.GREEN),
                        new ListItemSwipeHelper.SwipeBackground("Discard", Color.RED)
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
                    MoveToFridge(item);
                } else { //ItemTouchHelper.RIGHT
                    RemoveFromShoppingList(item);
                }
                super.onSwiped(viewHolder, swipeDir);
            }
        });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            String ingredient = data.getStringExtra(IngredientSearchActivity.KEY_RETURNED_INGREDIENT);
            if( !AddItemToShoppingList(ingredient)) {
                Toast.makeText(getContext(), ingredient + " was already in your shopping list", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //returns false if already existing
    boolean AddItemToShoppingList(String newIngredient) {
        boolean added;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        mDataSet = prefs.getStringSet(PREF_SET_NAME, mDataSet);
        added = mDataSet.add(newIngredient);
        editor.remove(PREF_SET_NAME).apply();
        editor = prefs.edit();
        editor.putStringSet(PREF_SET_NAME, mDataSet);
        editor.apply();
        mListAdapter.updateDataSet(mDataSet);

        ((MainActivity)getContext()).updatePagerTabs(); //notify the pageradapter that the fragments need to be updated

        return added;
    }

    //returns false if already existing
    boolean MoveToFridge(String item) {
        boolean added;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> FridgeDataSet = prefs.getStringSet(FridgeFragment.PREF_SET_NAME, new HashSet<String>());
        added = FridgeDataSet.add(item);
        editor.remove(FridgeFragment.PREF_SET_NAME).apply();
        editor.putStringSet(FridgeFragment.PREF_SET_NAME, FridgeDataSet).apply();
        mDataSet = prefs.getStringSet(PREF_SET_NAME, mDataSet);
        added = mDataSet.remove(item);
        editor.remove(PREF_SET_NAME).apply();
        editor.putStringSet(PREF_SET_NAME, mDataSet).apply();
        mListAdapter.updateDataSet(mDataSet); //remove the item from the visible list

        ((MainActivity)getContext()).updatePagerTabs(); //notify the pageradapter that the fragments need to be updated

        return added;
    }

    boolean RemoveFromShoppingList(String item) {
        boolean removed;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        mDataSet = prefs.getStringSet(PREF_SET_NAME, mDataSet);
        removed = mDataSet.remove(item);
        editor.remove(PREF_SET_NAME).apply();
        editor.putStringSet(PREF_SET_NAME, mDataSet).apply();
        mListAdapter.updateDataSet(mDataSet); //remove the item from the visible list

        ((MainActivity)getContext()).updatePagerTabs(); //notify the pageradapter that the fragments need to be updated

        return removed;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        super.onSaveInstanceState(savedInstanceState);
    }


}
