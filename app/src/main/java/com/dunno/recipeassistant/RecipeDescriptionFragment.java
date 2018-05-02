package com.dunno.recipeassistant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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


        mTextViewTitle.setText(this.getArguments().getString("title").replaceAll("\\\\n", "\n"));
        mTextViewTime.setText(this.getArguments().getString("time").replaceAll("\\\\n", "\n"));
        mTextViewHas.setText((int)(100 * this.getArguments().getFloat("has")) + " % in fridge");
        mTextViewDescription.setText(this.getArguments().getString("description").replaceAll("\\\\n", "\n"));

        int resID = getResources().getIdentifier(this.getArguments().getString("image"), "drawable", "com.dunno.recipeassistant");
        mImageView.setImageResource(resID);

        // Inflate the layout for this fragment
        return rootView;
    }
}
