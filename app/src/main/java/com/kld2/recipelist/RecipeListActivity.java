package com.kld2.recipelist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private RecyclerView recipeRecyclerView;
    private RecyclerView.Adapter recipeAdapter;
    private RecyclerView.LayoutManager recipeLayoutManager;
    private FloatingActionButton fab;
    public List<Recipe> recipeList;

    private String fileName = "RecipeList.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Recipes");

        RecipeListApp appState = ((RecipeListApp)getApplicationContext());
        recipeList = appState.globalRecipeList;

        recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recycler_view);

        // keep for performance improvement if changes in content dont change layout size
        recipeRecyclerView.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(recipeList);
        // use a linear layout manager to show items in vertical scrolling list
        recipeLayoutManager = new LinearLayoutManager(this);
        recipeRecyclerView.setLayoutManager(recipeLayoutManager);
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recipeRecyclerView.setAdapter(recipeAdapter);

        fab = (FloatingActionButton) findViewById(R.id.add_recipe_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeListActivity.this, NewRecipeActivity.class);
                startActivity(intent);
            }
        });
        Recipe recipe = new Recipe("saving?", "www.testing.com");
        recipeList.add(recipe);
        System.out.println("RECIPE LIST BEFORE SAVE: " + recipeList.get(0));
        saveRecipeData();
        recipeList.clear();
        loadRecipeData();
        System.out.println("RECIPE LIST after load: " + recipeList.get(0).getName());
        recipeAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
//        recipeAdapter.notifyDataSetChanged();
//        saveRecipeData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveRecipeData();
    }

    private void loadRecipeData() {
        System.out.println("LOAD RECIPE DATA !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        try {
            System.out.println("Recipe list before: " + recipeList.isEmpty());
            FileInputStream fileInputStream = openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fileInputStream);
            recipeList = (List<Recipe>)is.readObject();
            is.close();
            fileInputStream.close();
            System.out.println("Recipe list: " + recipeList.get(0).getName());
            recipeAdapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//
//
//        Recipe recipe = new Recipe("chicken", "www.chicken.com");
//        recipeList.add(recipe);
//
//        recipe = new Recipe("steak", "www.steak.com");
//        recipeList.add(recipe);
//
//        recipe = new Recipe("pancakes", "www.pancakes.com");
//        recipeList.add(recipe);

        recipeAdapter.notifyDataSetChanged();
    }

    public void  saveRecipeData() {
        System.out.println("SAVE RECIPE DATA !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE); //MODE PRIVATE
            ObjectOutputStream os = new ObjectOutputStream(fileOutputStream);
            os.writeObject(recipeList);
            os.flush();
            os.close();
            fileOutputStream.close();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
