package com.example.vadim.notes;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.vadim.notes.Activities.MainActivity;
import com.example.vadim.notes.Activities.OpenTicketActivity;
import com.example.vadim.notes.Activities.OpenedCheckListActivity;
import com.example.vadim.notes.Activities.OpenedNoteActivity;
import com.example.vadim.notes.AppItems.AppItem;
import com.example.vadim.notes.AppItems.CheckList;
import com.example.vadim.notes.AppItems.Note;
import com.example.vadim.notes.AppItems.Ticket;
import com.example.vadim.notes.Services.PdfParser;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.TreeMap;

public class NotesManager
{
    private Intent OpenNoteIntent;
    private Intent OpenCheckListIntent;
    private Intent OpenTicketIntent;
    private AppItem currentNote;
    private AppListener appListener;

    private static WeakReference<NotesManager> notesManagerWeakReference;
    private WeakReference<Activity> currentActivity = null;
    private WeakReference<MainActivity> mainActivity;

    private Map<Integer, AppItem> NotesDatabase = new TreeMap<>();
    private static int noteId = 0;

    public NotesManager(MainActivity mainActivity)
    {
        notesManagerWeakReference = new WeakReference<>(this);

        this.mainActivity = new WeakReference<>(mainActivity);
        this.OpenNoteIntent = new Intent(mainActivity.getMainLayout().getContext(), OpenedNoteActivity.class);
        this.OpenCheckListIntent = new Intent(mainActivity.getMainLayout().getContext(), OpenedCheckListActivity.class);
        this.OpenTicketIntent = new Intent(mainActivity.getMainLayout().getContext(), OpenTicketActivity.class);
        this.appListener = new AppListener(this);
    }

    public static NotesManager getInstance()
    {
        return notesManagerWeakReference.get();
    }

    public void clear()
    {
        notesManagerWeakReference.clear();
        this.mainActivity.clear();
        clearCurrentActivity();
        Runtime.getRuntime().gc();
    }

    public void clearCurrentActivity()
    {
        if(currentActivity!=null){this.currentActivity.clear();}
        Runtime.getRuntime().gc();
    }

    public AppItem getCurrentNote() {return currentNote;}

    public MainActivity getMainActivity() {return mainActivity.get();}

    public void setCurrentActivity(Activity currentActivity) {this.currentActivity = new WeakReference<>(currentActivity);}

    public Activity getCurrentActivity()
    {
        if(currentActivity!=null){return currentActivity.get();}
        else return null;
    }

    public OpenedNoteActivity getOpenedNoteActivity() {return (OpenedNoteActivity)currentActivity.get();}

    public OpenedCheckListActivity getOpenedCheckListActivity() {return (OpenedCheckListActivity) currentActivity.get();}

    public OpenTicketActivity getOpenTicketActivity()
    {
        try {return (OpenTicketActivity)currentActivity.get();}
        catch(Exception e){return null;}
    }

    public AppListener getAppListener() {return appListener;}


    public void createNewNote(String noteType)
    {

        Note note = null;

        switch (noteType)
        {
            case "Note":
                note = new Note(noteId);
                break;
            case "Checklist":
                note = new CheckList(noteId);
                break;
            case "Ticket":
                note = new Ticket(noteId);
                break;
        }

        if (note != null)
        {
            generateSavedNoteScreen(note);
            buildSavedNoteView(note);
            NotesDatabase.put(note.getNoteId(), note);
            noteId++;
            String filter = mainActivity.get().getActionBarManager().getCurrentTab();
            showViewsOnly(filter);
        }
    }

    public Map<Integer, AppItem> getNotesDatabase() {return NotesDatabase;}

    protected void buildSavedNoteView(AppItem note)
    {
        Button view = new Button(NotesManager.getInstance().getMainActivity().getNotesLayout().getContext());
        ImageView icon = new ImageView(NotesManager.getInstance().getMainActivity().getNotesLayout().getContext());

        if(note instanceof CheckList)
          {
            icon.setImageResource(R.drawable.checklist);
            view.setWidth(500);
            view.setHeight(100);
            view.setTextSize(16);
          }
        else if(note instanceof Ticket)
          {
              icon.setImageResource(R.drawable.world);
              view.setWidth(500);
              view.setHeight(250);
              view.setTextSize(12);
          }
        else
            {
              icon.setImageResource(R.drawable.notes_);
              view.setWidth(500);
              view.setHeight(100);
              view.setTextSize(16);
            }

        view.setBackgroundResource(R.drawable.note_view2);
        view.getBackground().setAlpha(10);
        view.setTransformationMethod(null);
        view.setTypeface(Typeface.create("sans-serif",Typeface.NORMAL));
        view.setTextColor(Color.parseColor("#8d8d8d"));
        view.setVisibility(View.INVISIBLE);
        view.setId(note.getViewId());
        mainActivity.get().registerForContextMenu(view);
        view.setOnClickListener(appListener.new NoteViewListener(note));
        defineNoteViewPosition(view,icon,note);
        view.setVisibility(View.VISIBLE);
        icon.setVisibility(View.VISIBLE);
        view.setText(note.getTextPreview());

        mainActivity.get().getNotesLayout().addView(icon);
        mainActivity.get().getNotesLayout().addView(view);
    }

    public void rebuildAllSavedNoteViews(String backUp_data)
    {
        if (backUp_data != null)
        {
           noteId = NotesDatabase.size();
           for(Map.Entry<Integer,AppItem> currentNote : NotesDatabase.entrySet())
           {
               buildSavedNoteView(currentNote.getValue());
           }
        }
    }

    protected void defineNoteViewPosition(Button view,ImageView icon, AppItem note)
    {
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout.LayoutParams IconParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);


        if (note.getNoteId() < 1) {
            buttonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            buttonParams.addRule(RelativeLayout.BELOW, (note.getViewId()-1));
        }
        buttonParams.setMargins(0, 0, 0, 0);
        view.setLayoutParams(buttonParams);

        IconParams.addRule(RelativeLayout.ALIGN_BOTTOM,note.getViewId());
        IconParams.setMargins(50,0,0,25);
        icon.setLayoutParams(IconParams);
    }

    protected int changeNoteViewPosition(AppItem note,int previousId)
    {
       Button view = mainActivity.get().findViewById(note.getViewId());

       RelativeLayout.LayoutParams newParams;
        if (note.getNoteId() > 0){
            newParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            newParams.addRule(RelativeLayout.BELOW, previousId);
            previousId = view.getId();
        }
        return previousId;
    }


    protected void generateSavedNoteScreen(AppItem note)
    {
        currentNote = note;
        if (note instanceof CheckList) {mainActivity.get().startActivity(OpenCheckListIntent);}
        else if (note instanceof Ticket)
        {if(PdfParser.getRunningTask() == null){mainActivity.get().startActivity(OpenTicketIntent);}}
        else {mainActivity.get().startActivity(OpenNoteIntent);}
    }

    public void deleteNote(int note_Id)
    {
        mainActivity.get().getNotesLayout().removeAllViews();
        int newKey;
        int newId;
        AppItem tmp;

        for (int i = 0; i < NotesDatabase.size(); i++) {
            if (i >= note_Id + 1) {
                newKey = i - 1;
                newId = newKey;
                tmp = NotesDatabase.get(i);
                NotesDatabase.put(newKey, tmp);
                NotesDatabase.get(newKey).setNoteId(newId);
                NotesDatabase.get(newKey).setViewId(newId + 10);
                buildSavedNoteView(NotesDatabase.get(newKey));
            }
        }
        NotesDatabase.remove(NotesDatabase.size() - 1);
        noteId--;
        mainActivity.get().backUpSession();
        String filter = mainActivity.get().getActionBarManager().getCurrentTab();
        showViewsOnly(filter);
    }

    public void showViewsOnly(String filter)
    {
        mainActivity.get().getNotesLayout().removeAllViews();
        int previousId = 10;
        for (int i = 0; i < NotesDatabase.size(); i++)
        {
            switch (filter) {
                case "Note":
                    if (!(NotesDatabase.get(i) instanceof CheckList) && !(NotesDatabase.get(i) instanceof Ticket)) {
                        buildSavedNoteView(NotesDatabase.get(i));
                        previousId = changeNoteViewPosition((Note)NotesDatabase.get(i),previousId);
                    }break;

                case "List":
                    if (NotesDatabase.get(i) instanceof CheckList) {
                        buildSavedNoteView(NotesDatabase.get(i));
                        previousId = changeNoteViewPosition((CheckList)NotesDatabase.get(i),previousId);
                    }break;

                case "Ticket":
                    if (NotesDatabase.get(i) instanceof Ticket) {
                        buildSavedNoteView(NotesDatabase.get(i));
                        previousId = changeNoteViewPosition((Ticket)NotesDatabase.get(i),previousId);
                    }break;

                case "All":
                    buildSavedNoteView(NotesDatabase.get(i));
                    break;
            }
        }
    }

    public void search(String query)
    {
        mainActivity.get().getNotesLayout().removeAllViews();
        int previousId = 10;
        StringBuilder noteContent;

        for (Map.Entry<Integer, AppItem> i : NotesDatabase.entrySet())
        {
            noteContent = new StringBuilder();

            if (i.getValue() instanceof CheckList)
            {
                noteContent.append(((CheckList) i.getValue()).getName());
                for (String listItemContent : ((CheckList) i.getValue()).getListItemsData())
                {noteContent.append(listItemContent.substring(0, listItemContent.length() - 1));}
            }
            else if (i.getValue() instanceof Ticket)
            {
                for (Map.Entry<String, String> ticket_data : ((Ticket) i.getValue()).getTicketData().entrySet())
                {
                    if(!ticket_data.getKey().equals("1:isInitilized"))
                    {noteContent.append(ticket_data.getValue());}
                }
            }
            else {noteContent.append(i.getValue().getText());}

            if (noteContent.toString().contains(query))
            {
                buildSavedNoteView(i.getValue());
                previousId = changeNoteViewPosition(i.getValue(),previousId);
            }
        }
    }

    public void removeAllEmptyNotes()
        {
            for (Map.Entry<Integer, AppItem> i : NotesDatabase.entrySet())
            {
                if(i.getValue().isEmpty()){deleteNote(i.getValue().getNoteId());}
            }
        }
}
