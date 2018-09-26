package com.kld2.recipelist;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

public class NotesFragment extends Fragment {

    private RecyclerView notesRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

        final List<Note> notes = ((RecipeActivity) requireActivity()).recipe.getNotes();

        notesRecyclerView = rootView.findViewById(R.id.notes_recycler_view);
        notesRecyclerView.setHasFixedSize(true);
        final NotesAdapter notesAdapter = new NotesAdapter(notes);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notesRecyclerView.setAdapter(notesAdapter);

        FloatingActionButton fab = rootView.findViewById(R.id.add_note_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText noteText = new EditText(getContext());
                noteText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                AlertDialog alert = new AlertDialog.Builder(requireContext())
                        .setMessage("Add Note:")
                        .setView(noteText)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (noteText.getText().toString().trim().isEmpty()) {
                                    Toast.makeText(getContext(), "Note text is required", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                notes.add(0, new Note(noteText.getText().toString(), Note.NoteType.SUBSTITUTION));
                                notesAdapter.notifyItemChanged(0);
                            }
                        })
                        .create();
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();
            }
        });

        return rootView;
    }

    private class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

        private List<Note> notes;
        private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position = notesRecyclerView.getChildLayoutPosition(view);
                final Note note = notes.get(position);
                // Create view and prefill (below) with current values
                AlertDialog alert = new AlertDialog.Builder(requireContext())
                        .setMessage("Edit note:")
                        //.setView(ingredientEntryView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //note.setText();
                                //note.setType();
                                notifyItemChanged(position);
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(requireContext())
                                        .setMessage("Are you sure you want to delete this note?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                notes.remove(position);
                                            }})
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            }
                        })
                        .create();
                //prefill fields here and set cursor position if needed
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();
                return true;
            }
        };

        // View Holder for each data item in the recyclerview
        class NoteViewHolder extends RecyclerView.ViewHolder {

            private TextView note;
            private TextView timestamp;

            NoteViewHolder(View view) {
                super(view);
                note = view.findViewById(R.id.note);
                timestamp = view.findViewById(R.id.timestamp);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        NotesAdapter(List<Note> notes) {
            this.notes = notes;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note_list_item, parent, false);
            itemView.setOnLongClickListener(onLongClickListener);
            return new NoteViewHolder(itemView);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull final NoteViewHolder holder, int position) {
            Note note = notes.get(position);
            holder.note.setText(note.getText());
            holder.timestamp.setText(SimpleDateFormat.getDateInstance().format(note.getTimestamp()));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return notes.size();
        }
    }
}
