package com.dunno.recipeassistant;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Assert;

import java.util.HashSet;
import java.util.Set;

public class FridgeFragment extends Fragment {

    public static final String TAG = FridgeFragment.class.getName();

    public static final int DATASET_COUNT = 60;

    public static final String PREF_SET_NAME = "FRIDGE_LIST";


    protected RecyclerView.LayoutManager    mLayoutManager;
    protected IngredientListAdapter         mListAdapter;
    protected RecyclerView                  mRecyclerView;
    protected Set<String>                   mDataSet = new HashSet<>();
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
       
       
    }

    protected FloatingActionButton  mAddIngredientButton;
    protected TextView              mEmptyListStatusText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fridgelist, container, false);
        MainActivity activity = (MainActivity) getActivity();


        if(savedInstanceState != null) {

        }


        setupUI(rootView);

        return rootView;
    }

    private void setupUI(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.fragment_fridgelist_recyclerView);
        int scrollPosition = 0;
        mLayoutManager = new LinearLayoutManager(getActivity());
        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);

        mDataSet = PreferenceManager.getDefaultSharedPreferences(getActivity()).getStringSet(PREF_SET_NAME, new HashSet<String>());

        mListAdapter = new IngredientListAdapter(mDataSet);
        mRecyclerView.setAdapter(mListAdapter);


        mEmptyListStatusText = rootView.findViewById(R.id.fragment_fridgelist_txt_empty);
        mEmptyListStatusText.setVisibility((mDataSet.size() > 0) ? View.GONE : View.VISIBLE);

        mAddIngredientButton = rootView.findViewById(R.id.fragment_fridgelist_btn_add);
        mAddIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(getActivity(), IngredientSearchActivity.class).setAction(Intent.ACTION_SEARCH);
                startActivityForResult(searchIntent, IngredientSearchActivity.REQUEST_PICK_INGREDIENT);
            }
        });
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
        super.onSaveInstanceState(savedInstanceState);
    }


}
