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
    private RecipeListApp appState;

    private String fileName = "RecipeList.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Recipes");

        appState = ((RecipeListApp)getApplicationContext());

        recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recycler_view);

        // keep for performance improvement if changes in content don't change layout size
        recipeRecyclerView.setHasFixedSize(true);

        loadRecipeData();

        recipeAdapter = new RecipeAdapter(appState.globalRecipeList);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveRecipeData();
    }

    private void loadRecipeData() {
        try {
            FileInputStream fileInputStream = openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fileInputStream);
            appState.globalRecipeList = (List<Recipe>)is.readObject();
            is.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        saveRecipeData();
    }

    public void  saveRecipeData() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE); //MODE PRIVATE
            ObjectOutputStream os = new ObjectOutputStream(fileOutputStream);
            os.writeObject(appState.globalRecipeList);
            os.flush();
            os.close();
            fileOutputStream.close();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
