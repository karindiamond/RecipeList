package com.kld2.recipelist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Karin on 7/12/2017.
 */

public class RecipeActivity extends AppCompatActivity {

    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Get recipe
        final String recipeName = getIntent().getStringExtra("recipeName");
        for (Recipe recipe: ((RecipeListApp)getApplication()).getRecipeList()) {
            if (recipe.getName().equals(recipeName)) {
                this.recipe = recipe;
                break;
            }
        }

        if (recipe == null) {
            Toast.makeText(this, "No recipe with that name found", Toast.LENGTH_LONG).show();
            finish();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipe.getName());
        }

        RecipePagerAdapter recipePagerAdapter = new RecipePagerAdapter(getSupportFragmentManager());
        ViewPager recipeViewPager = findViewById(R.id.pager);
        recipeViewPager.setAdapter(recipePagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                // Delete item was selected
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to delete this recipe?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((RecipeListApp)getApplication()).getRecipeList().remove(recipe);
                                finish();
                            }})
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        // TODO only save this specific recipe somehow?
        super.onPause();
        ((RecipeListApp) getApplication()).saveRecipeData();
    }

    private class RecipePagerAdapter extends FragmentPagerAdapter {

        RecipePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new RecipeFragment();
                case 1:
                    return new IngredientsFragment();
                case 2:
                    return new DirectionsFragment();
                case 3:
                    return new NotesFragment();
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Ingredients";
                case 2:
                    return "Directions";
                case 3:
                    return "Notes";
                default:
                    return null;
            }
        }
    }

}
