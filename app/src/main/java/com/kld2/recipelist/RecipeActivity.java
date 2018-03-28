package com.kld2.recipelist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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
        String recipeName = getIntent().getStringExtra("recipeName");
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

        TextView recipeLink = findViewById(R.id.recipe_link);
        recipeLink.setText(recipe.getLink());
        TextView recipeTimes = findViewById(R.id.recipe_time);
        recipeTimes.setText(recipe.getTimes());
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
                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeActivity.this); //alert for confirm to delete
                builder.setMessage("Are you sure you want to delete this recipe?");    //set message

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() { //when click on DELETE
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((RecipeListApp)getApplication()).getRecipeList().remove(recipe);
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();  //show alert dialog
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
