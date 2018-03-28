package com.kld2.recipelist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

        final TextView recipeLink = findViewById(R.id.recipe_link);
        recipeLink.setText(recipe.getLink());
        final TextView recipeTimes = findViewById(R.id.recipe_time);
        recipeTimes.setText(recipe.getTimes());

        recipeLink.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final EditText linkText = new EditText(RecipeActivity.this);
                new AlertDialog.Builder(RecipeActivity.this)
                        .setMessage("Enter new link:")
                        .setView(linkText)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recipe.setLink(linkText.getText().toString());
                                recipeLink.setText(linkText.getText());
                            }
                        })
                        .show();
                return true;
            }
        });
        recipeTimes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                @SuppressLint("InflateParams") // we can ignore this because views in alertdialogs do not inherit from their parent layouts
                View timeEntryView = LayoutInflater.from(RecipeActivity.this).inflate(R.layout.recipe_time_entry, null);
                final EditText prepHoursText = timeEntryView.findViewById(R.id.prep_hours_edit_text);
                final EditText prepMinutesText = timeEntryView.findViewById(R.id.prep_minutes_edit_text);
                final EditText cookHoursText = timeEntryView.findViewById(R.id.cook_hours_edit_text);
                final EditText cookMinutesText = timeEntryView.findViewById(R.id.cook_minutes_edit_text);
                new AlertDialog.Builder(RecipeActivity.this)
                        .setMessage("Enter new recipe times:")
                        .setView(timeEntryView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String prepHoursInput = prepHoursText.getText().toString();
                                int prepHours = prepHoursInput.isEmpty() ? 0 : Integer.parseInt(prepHoursInput);
                                String prepMinutesInput = prepMinutesText.getText().toString();
                                int prepMinutes = prepMinutesInput.isEmpty() ? 0 : Integer.parseInt(prepMinutesInput);
                                int prepTime = (prepHours * 60) + prepMinutes;

                                String cookHoursInput = cookHoursText.getText().toString();
                                int cookHours = cookHoursInput.isEmpty() ? 0 : Integer.parseInt(cookHoursInput);
                                String cookMinutesInput = cookMinutesText.getText().toString();
                                int cookMinutes = cookMinutesInput.isEmpty() ? 0 : Integer.parseInt(cookMinutesInput);
                                int cookTime = (cookHours * 60) + cookMinutes;

                                recipe.setPrepTime(prepTime);
                                recipe.setCookTime(cookTime);
                                recipeTimes.setText(recipe.getTimes());
                            }
                        })
                        .show();
                prepHoursText.setText(String.valueOf(recipe.getPrepHours()));
                prepMinutesText.setText(String.valueOf(recipe.getPrepMinutes()));
                cookHoursText.setText(String.valueOf(recipe.getCookHours()));
                cookMinutesText.setText(String.valueOf(recipe.getCookMinutes()));
                return true;
            }
        });
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

}
