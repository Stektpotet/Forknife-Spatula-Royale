package com.dunno.recipeassistant;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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

import java.util.Set;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ShoppingListFragment extends Fragment {

    public static final int DATASET_COUNT = 60;

    public static final String PREF_SET_NAME = "SHOPPING_LIST";

    private TextView mEmptyListStatusText;

    protected RecyclerView.LayoutManager    mLayoutManager;
    protected IngredientListAdapter         mListAdapter;
    protected RecyclerView                  mRecyclerView;
    protected String[]                      mDataSet = {};
    public ShoppingListFragment() {}

    MenuItem mMenuItemClearAll;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        mMenuItemClearAll = menu.add("Clear All");
        mMenuItemClearAll.setEnabled(mDataSet.length > 0);
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
        //FETCH dataset FROM SQLite
        //for now - init with some placeholder data:
        initDataset();
    }

    private void initDataset() {

        Set<String> storedShoppingList =  PreferenceManager.getDefaultSharedPreferences(getActivity()).getStringSet(PREF_SET_NAME, null);
        if(storedShoppingList != null) {
            mDataSet = storedShoppingList.toArray(new String[storedShoppingList.size()]);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        MainActivity activity = (MainActivity) getActivity();

        mRecyclerView = rootView.findViewById(R.id.fragment_shoppinglist_recyclerView);

        mEmptyListStatusText = rootView.findViewById(R.id.fragment_shoppinglist_txt_empty);

        mEmptyListStatusText.setVisibility((mDataSet.length > 0) ? View.GONE : View.VISIBLE);


        if(savedInstanceState != null) {

        }

        int scrollPosition = 0;
        mLayoutManager = new LinearLayoutManager(getActivity());
        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);

        mListAdapter = new IngredientListAdapter(mDataSet);
        mRecyclerView.setAdapter(mListAdapter);

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

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        super.onSaveInstanceState(savedInstanceState);
    }

}
