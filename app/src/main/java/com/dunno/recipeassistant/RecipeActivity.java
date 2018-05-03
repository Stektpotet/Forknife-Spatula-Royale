package com.dunno.recipeassistant;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class RecipeActivity extends AppCompatActivity {

    public static final String TAG = RecipeActivity.class.getName();
    private Button madeThisButton;
    private SimpleFragmentPagerAdapter mFragmentPagerAdapter;
    private ViewPager mViewPager;
    private DbHelper dbHelper;
    private Recipe recipe;
    private int id = -1;
    public float hasPercentage = 0;

    public static final String RECIPE_ID                = "id";
    public static final String RECIPE_TITLE             = "title";
    public static final String RECIPE_DESCRIPTION       = "description";
    public static final String RECIPE_HAS_PERCENTAGE    = "has";
    public static final String RECIPE_TIME              = "timeInMinutes";
    public static final String RECIPE_INSTRUCTIONS      = "instructions";
    public static final String RECIPE_IMAGE             = "image";



    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter{

        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] {
                getResources().getString(R.string.recipe_description_fragment_name),
                getResources().getString(R.string.recipe_ingredients_fragment_name),
                getResources().getString(R.string.recipe_instructions_fragment_name)};

        private Context context;
        public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;

        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putInt(RECIPE_ID, recipe.id);
            bundle.putString(RECIPE_TITLE, recipe.title);
            bundle.putInt(RECIPE_TIME, recipe.timeInMinutes);
            bundle.putFloat(RECIPE_HAS_PERCENTAGE, recipe.hasPercentage);
            bundle.putString(RECIPE_DESCRIPTION, recipe.description);
            bundle.putString(RECIPE_INSTRUCTIONS, recipe.instructions);
            bundle.putString(RECIPE_IMAGE, recipe.image);

            if (position == 1) {

                RecipeIngredientsFragment ingFrag = RecipeIngredientsFragment.newInstance();
                ingFrag.setArguments(bundle);
                return ingFrag;
            }
            else if (position == 2) {
                RecipeInstructionsFragment instFrag = RecipeInstructionsFragment.newInstance();
                instFrag.setArguments(bundle);
                return instFrag;
            }

            RecipeDescriptionFragment descFrag = RecipeDescriptionFragment.newInstance();
            descFrag.setArguments(bundle);
            return descFrag;

        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

    public void setActionBarTitle(String newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }
    public void setActionBarTitle(CharSequence newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        dbHelper = new DbHelper(getApplicationContext());

        Intent params = getIntent();
        id = params.getIntExtra("recipeId", -1);
        if (id != -1) {
            recipe = dbHelper.getRecipeById(id);
        }
        else {
            Log.e(TAG, "Attempt to get recipe with id -1, that does not exist.");
            recipe = new Recipe();
            recipe.id = -1;
        }
        hasPercentage = recipe.hasPercentage;

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.recipe_pageViewer);
        mViewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), RecipeActivity.this));

        madeThisButton = findViewById(R.id.recipe_made_button);
        View.OnClickListener madeThisButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CommitRecipeActivity.class);// Used for testing. Will be removed when there is a way to open it with UI.
                i.putExtra("recipeId", recipe.id);
                startActivityForResult(i, 1);
            }
        };
        madeThisButton.setOnClickListener(madeThisButtonListener);


        setActionBarTitle(recipe.title);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (id != -1) {
            recipe = dbHelper.getRecipeById(id);
        }
        else {
            Log.e(TAG, "Attempt to get recipe with id -1, that does not exist.");
            recipe = new Recipe();
            recipe.id = -1;
        }
        hasPercentage = recipe.hasPercentage;
        // Need to update info fragment description recieves trough bundle
    }
}
