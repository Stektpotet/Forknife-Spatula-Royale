package com.dunno.recipeassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecipeInstructionsFragment extends Fragment {

    public RecipeInstructionsFragment() {
        // Required empty public constructor
    }

    public static RecipeInstructionsFragment newInstance() {
        RecipeInstructionsFragment fragment = new RecipeInstructionsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_instructions, container, false);
    }


}
