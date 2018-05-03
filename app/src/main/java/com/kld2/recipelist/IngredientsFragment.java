package com.kld2.recipelist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class IngredientsFragment extends Fragment {

    private RecyclerView ingredientsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        final List<Ingredient> ingredients = ((RecipeActivity) requireActivity()).recipe.getIngredients();
        ingredientsRecyclerView = rootView.findViewById(R.id.ingredients_recycler_view);
        ingredientsRecyclerView.setHasFixedSize(true);
        final IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        FloatingActionButton fab = rootView.findViewById(R.id.add_ingredient_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("InflateParams") // we can ignore this because views in alertdialogs do not inherit from their parent layouts
                View ingredientEntryView = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_ingredient_entry, null);
                final EditText quantityNumeratorText = ingredientEntryView.findViewById(R.id.quantity_numerator_text);
                final EditText quantityDenominatorText = ingredientEntryView.findViewById(R.id.quantity_denominator_text);
                final EditText unitText = ingredientEntryView.findViewById(R.id.unit_text);
                final EditText nameText = ingredientEntryView.findViewById(R.id.name_text);
                final EditText noteText = ingredientEntryView.findViewById(R.id.note_text);
                new AlertDialog.Builder(requireContext())
                        .setMessage("Add ingredient:")
                        .setView(ingredientEntryView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int quantityNumerator = quantityToInt(quantityNumeratorText);
                                int quantityDenominator = quantityToInt(quantityDenominatorText);
                                if (validateIngredient(quantityNumerator, quantityDenominator, unitText, nameText, noteText)) {
                                    ingredients.add(new Ingredient(quantityNumerator, quantityDenominator, unitText.getText().toString(), nameText.getText().toString(), noteText.getText().toString()));
                                    // TODO why is this not needed here: ingredientsAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .show();
            }
        });

        return rootView;
    }

    private int quantityToInt(EditText intText) {
        return intText.getText().toString().isEmpty() ? 1 : Integer.parseInt(intText.getText().toString());
    }

    private boolean validateIngredient(int quantityNumerator, int quantityDenominator, EditText unitText, EditText nameText, EditText noteText) {
        if (quantityNumerator < 1 || quantityDenominator < 1) {
            Toast.makeText(getContext(), "Invalid quantity", Toast.LENGTH_LONG).show();
            return false;
        }
        if (nameText.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Ingredient name is required", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

        private List<Ingredient> ingredientList;
        private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position = ingredientsRecyclerView.getChildLayoutPosition(view);
                final Ingredient ingredient = ingredientList.get(position);
                @SuppressLint("InflateParams") // we can ignore this because views in alertdialogs do not inherit from their parent layouts
                View ingredientEntryView = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_ingredient_entry, null);
                final EditText quantityNumeratorText = ingredientEntryView.findViewById(R.id.quantity_numerator_text);
                final EditText quantityDenominatorText = ingredientEntryView.findViewById(R.id.quantity_denominator_text);
                final EditText unitText = ingredientEntryView.findViewById(R.id.unit_text);
                final EditText nameText = ingredientEntryView.findViewById(R.id.name_text);
                final EditText noteText = ingredientEntryView.findViewById(R.id.note_text);
                new AlertDialog.Builder(requireContext())
                        .setMessage("Edit ingredient:")
                        .setView(ingredientEntryView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int quantityNumerator = quantityToInt(quantityNumeratorText);
                                int quantityDenominator = quantityToInt(quantityDenominatorText);
                                if (validateIngredient(quantityNumerator, quantityDenominator, unitText, nameText, noteText)) {
                                    ingredient.setQuantityNumerator(quantityNumerator);
                                    ingredient.setQuantityDenominator(quantityDenominator);
                                    ingredient.setUnit(unitText.getText().toString());
                                    ingredient.setName(nameText.getText().toString());
                                    ingredient.setNote(noteText.getText().toString());
                                    notifyItemChanged(position);
                                }
                            }
                        })
                        .show();
                quantityNumeratorText.setText(String.valueOf(ingredient.getQuantityNumerator()));
                quantityDenominatorText.setText(String.valueOf(ingredient.getQuantityDenominator()));
                unitText.setText(ingredient.getUnit());
                nameText.setText(ingredient.getName());
                noteText.setText(ingredient.getNote());
                return true;
            }
        };

        // View Holder for each data item in the recyclerview
        class IngredientViewHolder extends RecyclerView.ViewHolder {

            private TextView ingredient;

            IngredientViewHolder(View view) {
                super(view);
                ingredient = view.findViewById(R.id.ingredient);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        IngredientsAdapter(List<Ingredient> ingredientList) {
            this.ingredientList = ingredientList;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public IngredientsAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_list_item, parent, false);
            // set the view's size, margins, paddings and layout parameter
            itemView.setOnLongClickListener(onLongClickListener);
            return new IngredientsAdapter.IngredientViewHolder(itemView);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull IngredientsAdapter.IngredientViewHolder holder, int position) {
            Ingredient ingredient = ingredientList.get(position);
            holder.ingredient.setText(ingredient.getIngredientString());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return ingredientList.size();
        }
    }
}
