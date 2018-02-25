package com.kld2.recipelist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    private RecyclerView.LayoutManager recipeLayoutManager;
    private FloatingActionButton fab;

    private String fileName = "RecipeList.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        System.out.println("creating");

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Recipes");

        recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recycler_view);
        setSwipeToDelete();

        // keep for performance improvement if changes in content don't change layout size
        recipeRecyclerView.setHasFixedSize(true);

        loadRecipeData();

        recipeAdapter = new RecipeAdapter(RecipeListApp.globalRecipeList);

        // use a linear layout manager to show items in vertical scrolling list
        recipeLayoutManager = new LinearLayoutManager(this);
        recipeRecyclerView.setLayoutManager(recipeLayoutManager);
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recipeRecyclerView.setAdapter(recipeAdapter);

        fab = (FloatingActionButton) findViewById(R.id.add_recipe_fab);
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

    private void setSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe
                final Recipe recipe = recipeAdapter.recipeList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeListActivity.this); //alert for confirm to delete
                builder.setMessage("Are you sure you want to delete this recipe?");    //set message

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() { //when click on DELETE
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recipeAdapter.notifyItemRemoved(position);    //item removed from recylcerview
                        RecipeListApp.globalRecipeList.remove(recipe);  //then remove item
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recipeAdapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                        recipeAdapter.notifyItemRangeChanged(position, recipeAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                    }
                }).show();  //show alert dialog
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recipeRecyclerView); //set swipe to recylcerview
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveRecipeData();
    }

    private void loadRecipeData() {
        try {
            FileInputStream fileInputStream = openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fileInputStream);
            RecipeListApp.globalRecipeList = (List<Recipe>)is.readObject();
            is.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        saveRecipeData();
    }

    public void  saveRecipeData() {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE); //MODE PRIVATE
            ObjectOutputStream os = new ObjectOutputStream(fileOutputStream);
            os.writeObject(RecipeListApp.globalRecipeList);
            os.flush();
            os.close();
            fileOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        class RecipeViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private TextView name, link;

            RecipeViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.name);
                link = view.findViewById(R.id.link);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        RecipeAdapter(List<Recipe> myRecipeList) {
            recipeList = myRecipeList;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
            // create a new view
            View itemView = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_row, parent, false);
            // set the view's size, margins, paddings and layout parameter
            itemView.setOnClickListener(onClickListener);
            return new RecipeViewHolder(itemView);
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

        public View.OnClickListener getOnClickListener() {
            return onClickListener;
        }

        public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
            this.onClickListener = onItemClickListener;
        }
    }
}
