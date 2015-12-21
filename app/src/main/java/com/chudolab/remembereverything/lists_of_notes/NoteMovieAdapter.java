package com.chudolab.remembereverything.lists_of_notes;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.chudolab.remembereverything.R;
import com.chudolab.remembereverything.Singleton;
import com.chudolab.remembereverything.one_note_show.SimpleNoteActivity;
import com.chudolab.remembereverything.one_note_show.TaskNoteActivity;
import com.chudolab.remembereverything.one_note_show.ToDoNoteActivity;
import com.chudolab.remembereverything.type_of_notes.Note;
import com.chudolab.remembereverything.type_of_notes.ToDoNote;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ASUS on 10.11.2015.
 */
public class NoteMovieAdapter extends RecyclerView.Adapter<NoteMovieAdapter.ViewHolder> {
    private Context context;
    private int idOfNoteView;
    private int idOfNoteName;
    private int idOfNoteSubject;
    private int idOfNoteText;
    public ArrayList<Note> listOfNotes;
    private int idOfDoingList;
    private String name;
    Class noteActivity;
    String subject;


    public Context getContext() {
        return context;
    }


    public NoteMovieAdapter(Context context,String tipeOfNote, ArrayList<Note> listOfNotes, int idOfNoteView, int idOfNoteName, int idOfNoteSubject, int idOfNoteText) {
        this.context = context;
        this.idOfNoteView = idOfNoteView;
        this.idOfNoteName = idOfNoteName;
        this.idOfNoteSubject = idOfNoteSubject;
        this.idOfNoteText = idOfNoteText;
        this.listOfNotes = listOfNotes;
        this.idOfDoingList=0;
        name=tipeOfNote;
        if(name.equals("Simple")){
            noteActivity= SimpleNoteActivity.class;
        }else if(name.equals("Task")){
            noteActivity= TaskNoteActivity.class;
        }else {
            noteActivity= ToDoNoteActivity.class;
        }
    }
    public NoteMovieAdapter(Context context,String tipeOfNote, int idOfNoteView, int idOfNoteName, int idOfNoteSubject, int idOfNoteText) {
        this.context = context;
        this.idOfNoteView = idOfNoteView;
        this.idOfNoteName = idOfNoteName;
        this.idOfNoteSubject = idOfNoteSubject;
        this.idOfNoteText = idOfNoteText;
        this.listOfNotes = new ArrayList<Note>();
        this.idOfDoingList=0;
        name=tipeOfNote;
        if(name.equals("Simple")){
            noteActivity= SimpleNoteActivity.class;
        }else if(name.equals("Task")){
            noteActivity= TaskNoteActivity.class;
        }else {
            noteActivity= ToDoNoteActivity.class;
        }
    }

    public NoteMovieAdapter(Context context,String tipeOfNote, ArrayList<Note> listOfNotes, int idOfNoteView, int idOfNoteName, int idOfDoingList) {
        this.context = context;
        this.idOfNoteView = idOfNoteView;
        this.idOfNoteName = idOfNoteName;
        this.listOfNotes = listOfNotes;
        this.idOfDoingList = idOfDoingList;
        name=tipeOfNote;
        if(name.equals("Simple")){
            noteActivity= SimpleNoteActivity.class;
        }else if(name.equals("Task")){
            noteActivity= TaskNoteActivity.class;
        }else {
            noteActivity= ToDoNoteActivity.class;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(idOfNoteView, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindMovie(listOfNotes.get(position));

    }

    @Override
    public int getItemCount() {
        return listOfNotes.size();
    }

    public int getPosition(){
        return getPosition();
    }

    public void remove(int position) {
        Note note =listOfNotes.get(position);
        ParseUser user = ParseUser.getCurrentUser();

        if(name.equals("Simple")){
            ParseObject object = ParseObject.createWithoutData("SimpleNotes", note.getObjectId());
            object.deleteInBackground();
            Singleton.getInstance().getSimpleNotes().remove(note);
            notifyItemRemoved(position);
        }else if(name.equals("ToDo")){
            ParseObject object = ParseObject.createWithoutData("ToDoNotes", note.getObjectId());
            object.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {

                }
            });
            Singleton.getInstance().getToDoNotes().remove(note);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }else if(name.equals("Task")){
            ParseObject object = ParseObject.createWithoutData("TaskNotes", note.getObjectId());
            object.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {

                }
            });
            Singleton.getInstance().getTaskNotes().remove(note);
            notifyItemRemoved(position);
        }

    }

    public void add(Note note){
        listOfNotes.add(note);
    }

    public void swap(int firstPosition, int secondPosition){
        Collections.swap(listOfNotes, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView tvNoteName;
        public final TextView tvText;
        public final TextView tvNoteSubject;
        public final ListView lvDoing;

        //public final ImageView doing;

        public ViewHolder(final View view){
            super(view);
            if (idOfDoingList==0) {
                tvNoteName = (TextView) view.findViewById(idOfNoteName);

                tvText = (TextView) view.findViewById(idOfNoteText);
                lvDoing=null;
                tvNoteSubject = (TextView) view.findViewById(idOfNoteSubject);
            }
            else {
                tvNoteSubject = (TextView) view.findViewById(R.id.tvToDoSubject);
                tvText=null;
                tvNoteName = (TextView) view.findViewById(idOfNoteName);
                lvDoing = (ListView) view.findViewById(R.id.lvDoing);
            }
            //edit!!!
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                  Intent intent = new Intent(context, noteActivity);
//                    intent.putExtra("position",getAdapterPosition());
//                   context.startActivity(intent);
//
//                }});

        }


        public void bindMovie(Note note){
            if (idOfDoingList == 0) {
                tvNoteName.setText("Name: "+note.getName());
                tvText.setText("Text: "+note.getText());
                tvNoteSubject.setText("Subject: "+note.getSubject());
            }else{
                tvNoteName.setText("Name: " + note.getName());
                tvNoteSubject.setText("Subject: "+note.getSubject());
                for (int i= 0; i<((ToDoNote)note).getDoing().size();i++){
                    CheckBox cb = new CheckBox(getContext());

                }

               // lvDoing.setAdapter(new ToDoAdapter(context, R.layout.doing_for_list, ((ToDoNote) note).getDoing()));
//                lvDoing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        view.findViewById(R.id.)
//                    }
//                });
            }

        }
    }
}