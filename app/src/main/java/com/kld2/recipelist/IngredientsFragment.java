package com.kld2.recipelist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class IngredientsFragment extends Fragment {

    private RecyclerView ingredientsRecyclerView;
    private ItemTouchHelper itemTouchHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        final List<Ingredient> ingredients = ((RecipeActivity) requireActivity()).recipe.getIngredients();
        ingredientsRecyclerView = rootView.findViewById(R.id.ingredients_recycler_view);
        ingredientsRecyclerView.setHasFixedSize(true);
        final IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        FloatingActionButton fab = rootView.findViewById(R.id.add_ingredient_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("InflateParams") // we can ignore this because views in alertdialogs do not inherit from their parent layouts
                View ingredientEntryView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_ingredient_entry, null);
                final EditText quantityNumeratorText = ingredientEntryView.findViewById(R.id.quantity_numerator_text);
                final EditText quantityDenominatorText = ingredientEntryView.findViewById(R.id.quantity_denominator_text);
                final EditText unitText = ingredientEntryView.findViewById(R.id.unit_text);
                final EditText nameText = ingredientEntryView.findViewById(R.id.name_text);
                final EditText noteText = ingredientEntryView.findViewById(R.id.note_text);
                AlertDialog alert = new AlertDialog.Builder(requireContext())
                        .setMessage("Add ingredient:")
                        .setView(ingredientEntryView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int quantityNumerator = quantityToInt(quantityNumeratorText);
                                int quantityDenominator = quantityToInt(quantityDenominatorText);
                                if (validateIngredient(quantityNumerator, quantityDenominator, nameText)) {
                                    ingredients.add(new Ingredient(quantityNumerator, quantityDenominator, unitText.getText().toString(), nameText.getText().toString(), noteText.getText().toString()));
                                    // TODO why is this not needed here: ingredientsAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .create();
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();
            }
        });

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.UP | ItemTouchHelper.DOWN);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(ingredients, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(ingredients, i, i - 1);
                    }
                }
                ingredientsAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }
        });
        itemTouchHelper.attachToRecyclerView(ingredientsRecyclerView);

        return rootView;
    }

    private int quantityToInt(EditText intText) {
        return intText.getText().toString().isEmpty() ? 1 : Integer.parseInt(intText.getText().toString());
    }

    private boolean validateIngredient(int quantityNumerator, int quantityDenominator, EditText nameText) {
        if (quantityNumerator < 1 || quantityDenominator < 1) {
            Toast.makeText(getContext(), "Invalid quantity", Toast.LENGTH_LONG).show();
            return false;
        }
        if (nameText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Ingredient name is required", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

        private List<Ingredient> ingredients;
        private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position = ingredientsRecyclerView.getChildLayoutPosition(view);
                final Ingredient ingredient = ingredients.get(position);
                @SuppressLint("InflateParams") // we can ignore this because views in alertdialogs do not inherit from their parent layouts
                View ingredientEntryView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_ingredient_entry, null);
                final EditText quantityNumeratorText = ingredientEntryView.findViewById(R.id.quantity_numerator_text);
                final EditText quantityDenominatorText = ingredientEntryView.findViewById(R.id.quantity_denominator_text);
                final EditText unitText = ingredientEntryView.findViewById(R.id.unit_text);
                final EditText nameText = ingredientEntryView.findViewById(R.id.name_text);
                final EditText noteText = ingredientEntryView.findViewById(R.id.note_text);
                AlertDialog alert = new AlertDialog.Builder(requireContext())
                        .setMessage("Edit ingredient:")
                        .setView(ingredientEntryView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int quantityNumerator = quantityToInt(quantityNumeratorText);
                                int quantityDenominator = quantityToInt(quantityDenominatorText);
                                if (validateIngredient(quantityNumerator, quantityDenominator, nameText)) {
                                    ingredient.setQuantityNumerator(quantityNumerator);
                                    ingredient.setQuantityDenominator(quantityDenominator);
                                    ingredient.setUnit(unitText.getText().toString());
                                    ingredient.setName(nameText.getText().toString());
                                    ingredient.setNote(noteText.getText().toString());
                                    notifyItemChanged(position);
                                }
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(requireContext())
                                        .setMessage("Are you sure you want to delete this ingredient?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Ingredient removedIngredient = ingredients.remove(position);
                                                notifyItemRemoved(position);

                                                // also delete ingredient from any direction ingredient lists that include it
                                                for (Direction direction : ((RecipeActivity) requireActivity()).recipe.getDirections()) {
                                                    direction.getIngredients().remove(removedIngredient);
                                                }
                                            }})
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            }
                        })
                        .create();
                quantityNumeratorText.setText(String.valueOf(ingredient.getQuantityNumerator()));
                quantityDenominatorText.setText(String.valueOf(ingredient.getQuantityDenominator()));
                unitText.setText(ingredient.getUnit());
                nameText.setText(ingredient.getName());
                noteText.setText(ingredient.getNote());
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                quantityNumeratorText.setSelection(quantityNumeratorText.getText().length());
                alert.show();
                return true;
            }
        };

        // View Holder for each data item in the recyclerview
        class IngredientViewHolder extends RecyclerView.ViewHolder {

            private TextView ingredient;
            private ImageView handleView;

            IngredientViewHolder(View view) {
                super(view);
                ingredient = view.findViewById(R.id.ingredient);
                handleView = view.findViewById(R.id.handle);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        IngredientsAdapter(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_list_item, parent, false);
            itemView.setOnLongClickListener(onLongClickListener);
            return new IngredientViewHolder(itemView);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onBindViewHolder(@NonNull final IngredientViewHolder holder, int position) {
            Ingredient ingredient = ingredients.get(position);
            holder.ingredient.setText(ingredient.getIngredientString());
            holder.handleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        itemTouchHelper.startDrag(holder);
                    }
                    return true;
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return ingredients.size();
        }
    }
}
