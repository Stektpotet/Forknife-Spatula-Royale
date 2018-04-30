package com.dunno.recipeassistant;

import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.Set;

public class FridgeFragment extends Fragment {

    public static final int DATASET_COUNT = 60;

    public static final String PREF_SET_NAME = "FRIDGE_LIST";

    protected TextView mEmptyListStatusText;

    protected RecyclerView.LayoutManager    mLayoutManager;
    protected IngredientListAdapter         mListAdapter;
    protected RecyclerView                  mRecyclerView;
    protected String[]                      mDataSet = {};
    public FridgeFragment() {}

    public static FridgeFragment newInstance(int entry) {
        FridgeFragment fragment = new FridgeFragment();
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
        Set<String> storedFridgeList =  PreferenceManager.getDefaultSharedPreferences(getActivity()).getStringSet(PREF_SET_NAME, null);
        if(storedFridgeList != null) {
            mDataSet = storedFridgeList.toArray(new String[storedFridgeList.size()]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fridgelist, container, false);
        MainActivity activity = (MainActivity) getActivity();

        mRecyclerView = rootView.findViewById(R.id.fragment_fridgelist_recyclerView);
        mEmptyListStatusText = rootView.findViewById(R.id.fragment_fridgelist_txt_empty);

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

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        super.onSaveInstanceState(savedInstanceState);
    }


}
