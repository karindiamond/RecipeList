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

public class NewRecipeActivity extends AppCompatActivity {

    EditText nameText;
    EditText linkText;
    Button btn;
    Recipe recipe;
    List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("New Recipe");

        recipe = new Recipe();
        RecipeListApp appState = ((RecipeListApp)getApplicationContext());
        recipeList = appState.globalRecipeList;

        nameText = (EditText) findViewById(R.id.name_edit_text);
        nameText.setHint("enter recipe name");
        linkText = (EditText) findViewById(R.id.link_edit_text);
        linkText.setHint("enter recipe link");
        //Get keyboard to show
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btn = (Button) findViewById(R.id.save_button);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = nameText.getText().toString();
                recipe.setName(str);
                if (recipe.getName().equals("")) {
                    Toast.makeText(getApplicationContext(), "name is a required field", Toast.LENGTH_LONG).show();
                    return;
                }
                str = linkText.getText().toString();
                recipe.setLink(str);
                recipeList.add(recipe);
                finish();
            }
        });

    }

}
