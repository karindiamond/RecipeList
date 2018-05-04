package com.kld2.recipelist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        //TODO as per guidelines, better to use a Toolbar https://developer.android.com/training/appbar/setting-up.html
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Recipes");
        }

        recipeRecyclerView = findViewById(R.id.recipe_recycler_view);
        setSwipeToDelete();

        // keep for performance improvement if changes in content don't change layout size
        recipeRecyclerView.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(((RecipeListApp) getApplication()).getRecipeList());

        // use a linear layout manager to show items in vertical scrolling list
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeRecyclerView.setAdapter(recipeAdapter);

        FloatingActionButton fab = findViewById(R.id.add_recipe_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeListActivity.this, NewRecipeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        recipeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((RecipeListApp) getApplication()).saveRecipeData();
    }

    private void setSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swiped
                final Recipe recipe = recipeAdapter.recipeList.get(position);

                new AlertDialog.Builder(RecipeListActivity.this)
                        .setMessage("Are you sure you want to delete this recipe?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((RecipeListApp) getApplication()).getRecipeList().remove(recipe);
                                recipeAdapter.notifyItemRemoved(position); //update view since we are on the page currently
                            }})
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recipeAdapter.notifyItemRangeChanged(position, recipeAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            }})
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                recipeAdapter.notifyItemRangeChanged(position, recipeAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            }
                        })
                        .show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recipeRecyclerView);
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

        private List<Recipe> recipeList;
        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recipeRecyclerView.getChildLayoutPosition(view);
                Recipe recipe = recipeList.get(position);
                // Open Recipe Activity
                Intent intent = new Intent(RecipeListActivity.this, RecipeActivity.class);
                intent.putExtra("recipeName", recipe.getName());
                startActivity(intent);
            }
        };

        // View Holder for each data item in the recyclerview
        class RecipeViewHolder extends RecyclerView.ViewHolder {

            private TextView name, link;

            RecipeViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.name);
                link = view.findViewById(R.id.link);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        RecipeAdapter(List<Recipe> recipeList) {
            this.recipeList = recipeList;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_item, parent, false);
            // set the view's size, margins, paddings and layout parameter
            itemView.setOnClickListener(onClickListener);
            return new RecipeViewHolder(itemView);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            Recipe recipe = recipeList.get(position);
            holder.name.setText(recipe.getName());
            holder.link.setText(recipe.getLink());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return recipeList.size();
        }
    }
}
