package com.dunno.recipeassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecipeInstructionsFragment extends Fragment {

    private TextView mTextViewInstrucitons;

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
        View rootView = inflater.inflate(R.layout.fragment_recipe_instructions, container, false);
        mTextViewInstrucitons = rootView.findViewById(R.id.fragment_recipe_instructions_textView_instructions);

        String instructions = this.getArguments().getString("instructions").replaceAll("\\\\n", "\n");
        mTextViewInstrucitons.setText(instructions);

        // Inflate the layout for this fragment
        return rootView;
    }
}



