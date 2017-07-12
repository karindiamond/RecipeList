package com.kld2.recipelist;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private RecyclerView recipeRecyclerView;
    private RecyclerView.Adapter recipeAdapter;
    private RecyclerView.LayoutManager recipeLayoutManager;
    private List<Recipe> recipeList = new ArrayList<>();
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recycler_view);

        // keep for performance improvement if changes in content dont change layout size
//        recipeRecyclerView.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(recipeList);
        // use a linear layout manager to show items in vertical scrolling list
        recipeLayoutManager = new LinearLayoutManager(this);
        recipeRecyclerView.setLayoutManager(recipeLayoutManager);
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recipeRecyclerView.setAdapter(recipeAdapter);

        prepareRecipeData();

        fab = (FloatingActionButton) findViewById(R.id.add_recipe_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeListActivity.this, NewRecipeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void prepareRecipeData() {
        Recipe recipe = new Recipe("chicken", "www.chicken.com");
        recipeList.add(recipe);

        recipe = new Recipe("steak", "www.steak.com");
        recipeList.add(recipe);

        recipe = new Recipe("pancakes", "www.pancakes.com");
        recipeList.add(recipe);

        recipeAdapter.notifyDataSetChanged();
    }
}
