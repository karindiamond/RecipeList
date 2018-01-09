package com.kld2.recipelist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Karin on 7/12/2017.
 */

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Get recipe
        Bundle b = getIntent().getExtras();
        int recipeIndex = b.getInt("recipeIndex");

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Recipe");
    }

}
