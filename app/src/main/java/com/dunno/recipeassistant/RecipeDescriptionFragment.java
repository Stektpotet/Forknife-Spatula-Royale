package com.dunno.recipeassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecipeDescriptionFragment extends Fragment {

    public RecipeDescriptionFragment() {
        // Required empty public constructor
    }

    public static RecipeDescriptionFragment newInstance() {
        RecipeDescriptionFragment fragment = new RecipeDescriptionFragment();
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
        return inflater.inflate(R.layout.fragment_recipe_description, container, false);
    }
}
