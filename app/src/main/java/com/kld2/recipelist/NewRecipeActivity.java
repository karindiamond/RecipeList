package com.kld2.recipelist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Karin on 7/12/2017.
 */

public class NewRecipeActivity extends AppCompatActivity {

    EditText nameText;
    EditText linkText;
    EditText prepHoursText;
    EditText prepMinutesText;
    EditText cookHoursText;
    EditText cookMinutesText;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("New Recipe");
        }

        nameText = findViewById(R.id.name_edit_text);
        linkText = findViewById(R.id.link_edit_text);
        prepHoursText = findViewById(R.id.prep_hours_edit_text);
        prepMinutesText = findViewById(R.id.prep_minutes_edit_text);
        cookHoursText = findViewById(R.id.cook_hours_edit_text);
        cookMinutesText = findViewById(R.id.cook_minutes_edit_text);

        //Get keyboard to show
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btn = findViewById(R.id.save_button);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = nameText.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(NewRecipeActivity.this, "name is a required field", Toast.LENGTH_LONG).show();
                    return;
                }

                String link = linkText.getText().toString();

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

                ((RecipeListApp)getApplication()).getRecipeList().add(new Recipe(name, link, prepTime, cookTime));
                finish();
            }
        });

    }

}
