package com.kld2.recipelist;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class RecipeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        final Recipe recipe = ((RecipeActivity)requireActivity()).recipe;

        final TextView recipeLink = rootView.findViewById(R.id.recipe_link);
        recipeLink.setText(recipe.getLink());
        final TextView recipeTimes = rootView.findViewById(R.id.recipe_time);
        recipeTimes.setText(recipe.getTimes());

        recipeLink.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final EditText linkText = new EditText(getActivity());
                new AlertDialog.Builder(requireActivity())
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
                linkText.setText(recipeLink.getText());
                return true;
            }
        });
        recipeTimes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                @SuppressLint("InflateParams") // we can ignore this because views in alertdialogs do not inherit from their parent layouts
                View timeEntryView = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_time_entry, null);
                final EditText prepHoursText = timeEntryView.findViewById(R.id.prep_hours_edit_text);
                final EditText prepMinutesText = timeEntryView.findViewById(R.id.prep_minutes_edit_text);
                final EditText cookHoursText = timeEntryView.findViewById(R.id.cook_hours_edit_text);
                final EditText cookMinutesText = timeEntryView.findViewById(R.id.cook_minutes_edit_text);
                new AlertDialog.Builder(requireActivity())
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

        return rootView;
    }
}
