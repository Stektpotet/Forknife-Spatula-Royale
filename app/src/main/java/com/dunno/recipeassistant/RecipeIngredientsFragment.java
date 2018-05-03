package com.dunno.recipeassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeIngredientsFragment extends Fragment {

    public static final String TAG = RecipeIngredientsFragment.class.getName();
    public static final String STATUS_ADDED = "Added";
    public static final String STATUS_HAS = "Has";
    public static final String STATUS_NEED = "Need";

    private int recipeID = 0;
    private List<Ingredient> ingredients;



    private Button addAllButton;


    public RecipeIngredientsFragment() { }

    public static RecipeIngredientsFragment newInstance() {
        RecipeIngredientsFragment fragment = new RecipeIngredientsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    RecipeIngredientAdapter mListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        setupUI(rootView);

        recipeID = getArguments().getInt(RecipeActivity.RECIPE_ID);
        DbHelper dbHelper = new DbHelper(getContext());
        ingredients = dbHelper.getIngredientsInRecipe(recipeID);
        mListAdapter.addAll(ingredients);

        addAllButton = rootView.findViewById(R.id.fragment_recipeIngredients_button_addAll);
        View.OnClickListener addAllButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListAdapter.clear();
                mListAdapter.addAll(ingredients);
                for (int i = 0; i < mListAdapter.getCount(); i++) {
                    mListAdapter.MoveToShoppingList(mListAdapter.getItem(i));
                }
            }
        };
        addAllButton.setOnClickListener(addAllButtonListener);

        return rootView;
    }

    private ListView mIngredientList;

    void setupUI(View rootView) {
        mIngredientList = rootView.findViewById(R.id.fragment_recipeIngredients_listView);
        mListAdapter = new RecipeIngredientAdapter(getContext(),  R.layout.fragment_recipe_ingredients);
        mIngredientList.setAdapter(mListAdapter);
    }


    public class RecipeIngredientAdapter extends ArrayAdapter<Ingredient> {

        public boolean MoveToShoppingList(Ingredient item) {
            Log.d(TAG, "Moving " + item.name + " to shopping list!");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = prefs.edit();
            Set<String> shoppingListDataSet = prefs.getStringSet(ShoppingListFragment.PREF_SET_NAME, new HashSet<String>());
            shoppingListDataSet.add(item.name);
            editor.remove(ShoppingListFragment.PREF_SET_NAME).apply();
            editor.putStringSet(ShoppingListFragment.PREF_SET_NAME, shoppingListDataSet).apply();
            return true; //TODO look into validating this
        }

        public RecipeIngredientAdapter(@NonNull Context context, int resource) {
            super(context,0, resource);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Get the data item for this position
            Ingredient item = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_ingredients_item, parent, false);
            }
            // Lookup view for data population
            TextView title =  convertView.findViewById(R.id.recipe_ingredients_item_txt_title);
            final TextView status = convertView.findViewById(R.id.recipe_ingredients_item_txt_status);
            final Button  addBtn = convertView.findViewById(R.id.recipe_ingredients_item_btn_add);
            // Populate the data into the template view using the data object
            title.setText(item.name);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            Set<String> fridgeList = sharedPreferences.getStringSet(FridgeFragment.PREF_SET_NAME, new HashSet<String>());
            Set<String> shoppingList = sharedPreferences.getStringSet(ShoppingListFragment.PREF_SET_NAME, new HashSet<String>());

            if (shoppingList.contains(item.name)) {

                status.setText(STATUS_ADDED);
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorStatusAdd));
                addBtn.setEnabled(false);
            }
            else if (fridgeList.contains(item.name)) {

                status.setText(STATUS_HAS);
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorStatusHas));
            }

            else {
                status.setText(STATUS_NEED);
                status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorStatusNeed));

            }


            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MoveToShoppingList(getItem(position));
                    status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorStatusAdd));
                    addBtn.setEnabled(false);
                    status.setText(STATUS_ADDED);
                }
            });

            // Return the completed view to render on screen
            return convertView;
        }
    }


}
