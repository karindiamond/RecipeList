package com.kld2.recipelist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Karin on 7/12/2017.
 */

public class RecipeActivity extends AppCompatActivity {

    Recipe recipe = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Get recipe
        Bundle b = getIntent().getExtras();
        String recipeName = b.getString("recipeName");

        for (Recipe r: RecipeListApp.globalRecipeList) {
            if (r.getName().equals(recipeName)) {
                recipe = r;
            }
        }
        if (recipe == null) {
            //TODO how handle this case?
            Toast.makeText(getApplicationContext(), "No recipe with that name found", Toast.LENGTH_LONG).show();
            return;
        }
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(recipe.getName());
        TextView recipeLink = (TextView) findViewById(R.id.recipe_link);
        recipeLink.setText(recipe.getLink());
        TextView recipeTimes = (TextView) findViewById(R.id.recipe_time);
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
//                        recipeAdapter.notifyItemRemoved(position);    //item removed from recylcerview
                        RecipeListApp.globalRecipeList.remove(recipe);  //then remove item
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
