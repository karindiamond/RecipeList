package com.kld2.recipelist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DirectionIngredientsDialogFragment extends DialogFragment {

    public static final String TAG = "DIRECTION_INGREDIENTS_DIALOG";

    public static DirectionIngredientsDialogFragment newInstance(List<Ingredient> ingredients) {
        DirectionIngredientsDialogFragment fragment = new DirectionIngredientsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("ingredients", (ArrayList<Ingredient>) ingredients);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final List<Ingredient> ingredients = (List<Ingredient>) getArguments().getSerializable("ingredients");

        View rootView = requireActivity().getLayoutInflater().inflate(R.layout.direction_ingredients, null);
        RecyclerView recyclerView = rootView.findViewById(R.id.direction_ingredients_recycler_view);
        final DirectionIngredientsAdapter adapter = new DirectionIngredientsAdapter(ingredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        Button addButton = rootView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Ingredient> recipeIngredients = new ArrayList<>(((RecipeActivity) requireActivity()).recipe.getIngredients());
                recipeIngredients.removeAll(ingredients);

                final PopupMenu popupMenu = new PopupMenu(getContext(), v);
                Menu menu = popupMenu.getMenu();
                for (int i = 0; i < recipeIngredients.size(); i++) {
                    menu.add(0, i, i, recipeIngredients.get(i).getIngredientString());
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int position = item.getItemId();
                        ingredients.add(recipeIngredients.get(position));
                        adapter.notifyItemChanged(position);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swiped
                final Ingredient ingredient = adapter.ingredients.get(position);

                new AlertDialog.Builder(requireContext())
                        .setMessage("Are you sure you want to remove this ingredient?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ingredients.remove(ingredient);
                                adapter.notifyItemRemoved(position); //update view since we are on the page currently
                            }})
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyItemRangeChanged(position, adapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            }})
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                adapter.notifyItemRangeChanged(position, adapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            }
                        })
                        .show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return new AlertDialog.Builder(requireContext()).setView(rootView).create();
    }

    private class DirectionIngredientsAdapter extends RecyclerView.Adapter<DirectionIngredientsAdapter.DirectionIngredientsViewHolder> {

        private List<Ingredient> ingredients;

        // View Holder for each data item in the recyclerview
        class DirectionIngredientsViewHolder extends RecyclerView.ViewHolder {

            private TextView ingredient;

            DirectionIngredientsViewHolder(View view) {
                super(view);
                ingredient = view.findViewById(R.id.ingredient);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        DirectionIngredientsAdapter(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
        }

        @NonNull
        @Override
        public DirectionIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.direction_ingredient_list_item, parent, false);
            return new DirectionIngredientsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final DirectionIngredientsViewHolder holder, int position) {
            holder.ingredient.setText(ingredients.get(position).getIngredientString());
        }

        @Override
        public int getItemCount() {
            return ingredients.size();
        }
    }
}
