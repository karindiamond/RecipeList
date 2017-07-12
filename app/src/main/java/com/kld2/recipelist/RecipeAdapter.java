package com.kld2.recipelist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Karin on 7/5/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
        private List<Recipe> recipeList;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class RecipeViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView name, link;
            public RecipeViewHolder(TextView view) {
                super(view);
                name = view.findViewById(R.id.name);
                link = view.findViewById(R.id.link);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public RecipeAdapter(List<Recipe> myRecipeList) {
            recipeList = myRecipeList;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            TextView itemView = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_row, parent, false);
            // set the view's size, margins, paddings and layout parameter

            RecipeViewHolder viewHolder = new RecipeViewHolder(itemView);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecipeViewHolder holder, int position) {
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
