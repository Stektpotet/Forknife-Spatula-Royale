package com.dunno.recipeassistant;


import android.app.Activity;


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
            bundle.putString("title", recipe.title);
            bundle.putString("time", recipe.time);
            bundle.putString("description", recipe.description);
            bundle.putString("instructions", recipe.instructions);

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        dbHelper = new DbHelper(getApplicationContext());

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
                startActivityForResult(i, 1);
            }
        };
        madeThisButton.setOnClickListener(madeThisButtonListener);


        Intent params = getIntent();
        int id = params.getIntExtra("recipeId", -1);
        if (id != -1) {
           recipe = dbHelper.getRecipeById(id);
        }
        else {
            Log.e(TAG, "Attempt to get recipe with id -1, that does not exist.");
            recipe = new Recipe();
            recipe.id = -1;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
