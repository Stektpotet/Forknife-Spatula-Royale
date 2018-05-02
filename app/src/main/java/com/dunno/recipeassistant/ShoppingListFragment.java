package com.dunno.recipeassistant;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

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
        mMenuItemClearAll.setEnabled(mDataSet.size() > 0);
        super.onCreateOptionsMenu(menu, inflater);
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
               return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Remove item from backing list here
                mListAdapter.notifyDataSetChanged();
            }

        });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            String ingredient = data.getStringExtra(IngredientSearchActivity.KEY_RETURNED_INGREDIENT);
            if( !AddItemToFridge(ingredient)) {
                Toast.makeText(getContext(), ingredient + " was already added to your fridge", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //returns false if already existing
    boolean AddItemToFridge(String newIngredient) {
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
        return added;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        super.onSaveInstanceState(savedInstanceState);
    }

}
