package com.dunno.recipeassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ShoppingListFragment extends Fragment {

    public static final int DATASET_COUNT = 60;


    protected RecyclerView.LayoutManager    mLayoutManager;
    protected IngredientListAdapter         mListAdapter;
    protected RecyclerView                  mRecyclerView;
    protected String[]                      mDataSet;
    public ShoppingListFragment() {}

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
        //FETCH dataset FROM SQLite
        //for now - init with some placeholder data:
        initDataset();
    }

    private void initDataset() {
        mDataSet = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataSet[i] = "element #" + i;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        MainActivity activity = (MainActivity) getActivity();

        mRecyclerView = rootView.findViewById(R.id.fragment_shoppinglist_recyclerView);
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

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        super.onSaveInstanceState(savedInstanceState);
    }


}
