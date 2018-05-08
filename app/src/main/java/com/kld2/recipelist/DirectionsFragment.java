package com.kld2.recipelist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DirectionsFragment extends Fragment {

    private RecyclerView directionsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_directions, container, false);

        final List<Direction> directions = ((RecipeActivity) requireActivity()).recipe.getDirections();

        directionsRecyclerView = rootView.findViewById(R.id.directions_recycler_view);
        directionsRecyclerView.setHasFixedSize(true);
        final DirectionsFragment.DirectionsAdapter directionsAdapter = new DirectionsFragment.DirectionsAdapter(directions);
        directionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        directionsRecyclerView.setAdapter(directionsAdapter);

        FloatingActionButton fab = rootView.findViewById(R.id.add_direction_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText directionText = new EditText(getContext());
                directionText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                AlertDialog alert = new AlertDialog.Builder(requireContext())
                        .setMessage("Add direction:")
                        .setView(directionText)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (directionText.getText().toString().trim().isEmpty()) {
                                    Toast.makeText(getContext(), "Direction text is required", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                directions.add(new Direction(directionText.getText().toString(), new ArrayList<Ingredient>()));
                            }
                        })
                        .create();
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });

        return rootView;
    }

    private class DirectionsAdapter extends RecyclerView.Adapter<DirectionsFragment.DirectionsAdapter.DirectionViewHolder> {

        private List<Direction> directionList;
        private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position = directionsRecyclerView.getChildLayoutPosition(view);
                final Direction direction = directionList.get(position);
                final EditText directionText = new EditText(getContext());
                directionText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                AlertDialog alert = new AlertDialog.Builder(requireContext())
                        .setMessage("Edit direction:")
                        .setView(directionText)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (directionText.getText().toString().trim().isEmpty()) {
                                    Toast.makeText(getContext(), "Direction text is required", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                direction.setText(directionText.getText().toString());
                                notifyItemChanged(position);
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(requireContext())
                                        .setMessage("Are you sure you want to delete this direction?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                directionList.remove(position);
                                                notifyItemRemoved(position);
                                            }})
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            }
                        })
                        .create();
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                directionText.setText(direction.getText());
                directionText.setSelection(directionText.getText().length());
                alert.show();
                return true;
            }
        };

        // View Holder for each data item in the recyclerview
        class DirectionViewHolder extends RecyclerView.ViewHolder {

            private TextView direction;

            DirectionViewHolder(View view) {
                super(view);
                direction = view.findViewById(R.id.direction);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        DirectionsAdapter(List<Direction> directionList) {
            this.directionList = directionList;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public DirectionsFragment.DirectionsAdapter.DirectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.direction_list_item, parent, false);
            // set the view's size, margins, paddings and layout parameter
            itemView.setOnLongClickListener(onLongClickListener);
            return new DirectionsFragment.DirectionsAdapter.DirectionViewHolder(itemView);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull final DirectionsFragment.DirectionsAdapter.DirectionViewHolder holder, int position) {
            Direction direction = directionList.get(position);
            String directionText = (position + 1) + ". " + direction.getText();
            holder.direction.setText(directionText);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return directionList.size();
        }
    }
}
