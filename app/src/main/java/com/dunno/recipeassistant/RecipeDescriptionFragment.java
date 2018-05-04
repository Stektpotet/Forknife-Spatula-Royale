package com.dunno.recipeassistant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;

public class RecipeDescriptionFragment extends Fragment {

    private TextView mTextViewTitle;
    private TextView mTextViewHas;
    private TextView mTextViewTime;
    private TextView mTextViewDescription;
    private ImageView mImageView;

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
        View rootView = inflater.inflate(R.layout.fragment_recipe_description, container, false);
        mTextViewTitle = rootView.findViewById(R.id.fragment_recipeDescription_textView_RecipeTitle);
        mTextViewHas = rootView.findViewById(R.id.fragment_recipeDescription_textView_RecipeHas);
        mTextViewTime = rootView.findViewById(R.id.fragment_recipeDescription_textView_RecipeTime);
        mTextViewDescription = rootView.findViewById(R.id.fragment_recipeDescription_textView_RecipeDescription);
        mImageView = rootView.findViewById(R.id.fragment_recipeDescription_image);


        mTextViewTitle.setText(this.getArguments().getString(RecipeActivity.RECIPE_TITLE).replaceAll("\\\\n", "\n"));
        mTextViewTime.setText(MessageFormat.format("{0}min", this.getArguments().getInt(RecipeActivity.RECIPE_TIME)));
        mTextViewHas.setText(MessageFormat.format("{0} % in fridge", 100 * this.getArguments().getFloat(RecipeActivity.RECIPE_HAS_PERCENTAGE)));
        mTextViewDescription.setText(this.getArguments().getString(RecipeActivity.RECIPE_DESCRIPTION).replaceAll("\\\\n", "\n"));

        int resID = getResources().getIdentifier(this.getArguments().getString(RecipeActivity.RECIPE_IMAGE), "drawable", "com.dunno.recipeassistant");
        mImageView.setImageResource(resID);

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        mTextViewHas.setText(MessageFormat.format("{0} % in fridge", (int)(100 * ((RecipeActivity)getActivity()).hasPercentage)));

    }

}
