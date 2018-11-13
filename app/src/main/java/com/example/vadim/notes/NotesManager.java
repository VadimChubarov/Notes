package com.example.vadim.notes;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import com.example.vadim.notes.Activities.MainActivity;
import com.example.vadim.notes.Activities.OpenTicketActivity;
import com.example.vadim.notes.Activities.OpenedCheckListActivity;
import com.example.vadim.notes.Activities.OpenedNoteActivity;
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
    private Note currentNote;
    private AppListener appListener;

    private static WeakReference<NotesManager> notesManagerWeakReference;
    private WeakReference<Activity> currentActivity = null;
    private WeakReference<MainActivity> mainActivity;

    private Map<Integer, Note> NotesDatabase = new TreeMap<>();
    private StringBuilder NotesForSave;
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


    public Note getCurrentNote() {return currentNote;}

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


    public void createNewNote(String noteType) {

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

    public String getNotesForSave() {
        prepareNotesForSave();
        return NotesForSave.toString();
    }

    protected void prepareNotesForSave()
    {
        NotesForSave = new StringBuilder();
        for (Map.Entry<Integer, Note> i : NotesDatabase.entrySet())
        {
            if (i.getValue() instanceof CheckList) {
                NotesForSave.append(((CheckList) i.getValue()).getName());
                NotesForSave.append("!%!");

                for (String list_item : ((CheckList) i.getValue()).getListItemsData()) {
                    NotesForSave.append(list_item);
                    NotesForSave.append("!%!");
                }
                NotesForSave.append("!#!");}

             else if (i.getValue() instanceof Ticket)
            {
                for (Map.Entry<String, String> data : ((Ticket) i.getValue()).getTicketData().entrySet())
                {
                  NotesForSave.append(data.getValue());
                  NotesForSave.append("!*!");
                }
                NotesForSave.append("!#!");}

            else{
                NotesForSave.append(i.getValue().getText());
                NotesForSave.append("!#!");}
        }
        NotesForSave.append(noteId);
    }

    protected void buildSavedNoteView(Note note)
    {
        mainActivity.get().registerForContextMenu(note.getSavesNoteView());
        note.getSavesNoteView().setOnClickListener(appListener.new NoteViewListener(note));
        defineNoteViewPosition(note);
        note.getSavesNoteView().setVisibility(View.VISIBLE);
        note.getIcon().setVisibility(View.VISIBLE);
        note.getSavesNoteView().setText(note.getTextPreview());
    }

    public void rebuildAllSavedNoteViews(String backUp_data)
    {
        if (backUp_data != null)
        {
            String notes_data[] = backUp_data.split("!#!");
            noteId = Integer.parseInt(notes_data[notes_data.length-1]);

            for (int i = 0; i < noteId; i++)
            {
                if (notes_data[i].contains("!%!"))
                {
                    CheckList current_checklist = new CheckList(i);
                    String[] list_items_data = notes_data[i].split("!%!");
                    current_checklist.setName(list_items_data[0]);

                    for (String list_item : list_items_data)
                    {current_checklist.getListItemsData().add(list_item);}

                    current_checklist.getListItemsData().remove(0);
                    NotesDatabase.put(i, current_checklist);
                    buildSavedNoteView(current_checklist);
                }

                else if(notes_data[i].contains("!*!"))
                {
                   Ticket current_ticket = new Ticket(i);
                   String[] ticket_data = notes_data[i].split("!\\*!");

                   current_ticket.setAllData(ticket_data);

                   NotesDatabase.put(i,current_ticket);
                   buildSavedNoteView(current_ticket);
                }
                else
                    {
                    Note current_note = new Note(i);
                    current_note.setText(notes_data[i]);

                    NotesDatabase.put(i, current_note);
                    buildSavedNoteView(current_note);
                }
            }
        }
    }

    protected void defineNoteViewPosition(Note note)
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
            buttonParams.addRule(RelativeLayout.BELOW, (NotesDatabase.get(note.getNoteId() - 1).getSavesNoteView().getId()));
        }
        buttonParams.setMargins(0, 0, 0, 0);
        note.getSavesNoteView().setLayoutParams(buttonParams);

        IconParams.addRule(RelativeLayout.ALIGN_BOTTOM,note.getSavesNoteView().getId());
        IconParams.setMargins(50,0,0,25);
        note.getIcon().setLayoutParams(IconParams);
    }

    protected int changeNoteViewPosition(Note note,int previousId)
    {
        RelativeLayout.LayoutParams newParams;
        if (note.getNoteId() > 0){
            newParams = (RelativeLayout.LayoutParams) note.getSavesNoteView().getLayoutParams();
            newParams.addRule(RelativeLayout.BELOW, previousId);
            previousId = note.getSavesNoteView().getId();
        }
        return previousId;
    }


    protected void generateSavedNoteScreen(Note note)
    {
        currentNote = note;
        if (note instanceof CheckList) {mainActivity.get().startActivity(OpenCheckListIntent);}
        else if (note instanceof Ticket)
        {if(PdfParser.getRunningTask() == null){mainActivity.get().startActivity(OpenTicketIntent);}}
        else {mainActivity.get().startActivity(OpenNoteIntent);}
    }

    public void deleteNote(int note_Id) {
        mainActivity.get().getNotesLayout().removeAllViews();
        int newKey;
        int newId;
        Note tmp;

        for (int i = 0; i < NotesDatabase.size(); i++) {
            if (i >= note_Id + 1) {
                newKey = i - 1;
                newId = newKey;
                tmp = NotesDatabase.get(i);
                NotesDatabase.put(newKey, tmp);
                NotesDatabase.get(newKey).setNoteId(newId);
                NotesDatabase.get(newKey).getSavesNoteView().setId(newId + 10);
                defineNoteViewPosition(NotesDatabase.get(newKey));
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
        for (int i = 0; i < NotesDatabase.size(); i++) {
            switch (filter) {
                case "Note":
                    if (!(NotesDatabase.get(i) instanceof CheckList) && !(NotesDatabase.get(i) instanceof Ticket)) {
                        buildSavedNoteView(NotesDatabase.get(i));
                        previousId = changeNoteViewPosition(NotesDatabase.get(i),previousId);
                        mainActivity.get().getNotesLayout().addView(NotesDatabase.get(i).getIcon());
                        mainActivity.get().getNotesLayout().addView(NotesDatabase.get(i).getSavesNoteView());
                    }break;

                case "List":
                    if (NotesDatabase.get(i) instanceof CheckList) {
                        buildSavedNoteView(NotesDatabase.get(i));
                        previousId = changeNoteViewPosition(NotesDatabase.get(i),previousId);
                        mainActivity.get().getNotesLayout().addView(NotesDatabase.get(i).getIcon());
                        mainActivity.get().getNotesLayout().addView(NotesDatabase.get(i).getSavesNoteView());
                    }break;

                case "Ticket":
                    if (NotesDatabase.get(i) instanceof Ticket) {
                        buildSavedNoteView(NotesDatabase.get(i));
                        previousId = changeNoteViewPosition(NotesDatabase.get(i),previousId);
                        mainActivity.get().getNotesLayout().addView(NotesDatabase.get(i).getIcon());
                        mainActivity.get().getNotesLayout().addView(NotesDatabase.get(i).getSavesNoteView());
                    }break;

                case "All":
                    buildSavedNoteView(NotesDatabase.get(i));
                    mainActivity.get().getNotesLayout().addView(NotesDatabase.get(i).getIcon());
                    mainActivity.get().getNotesLayout().addView(NotesDatabase.get(i).getSavesNoteView());
                    break;
            }
        }
    }

    public void search(String query)
    {
        mainActivity.get().getNotesLayout().removeAllViews();
        int previousId = 10;
        StringBuilder noteContent;

        for (Map.Entry<Integer, Note> i : NotesDatabase.entrySet())
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
                mainActivity.get().getNotesLayout().addView(i.getValue().getIcon());
                mainActivity.get().getNotesLayout().addView(i.getValue().getSavesNoteView());
            }
        }
    }

    public void removeAllEmptyNotes()
        {
            for (Map.Entry<Integer, Note> i : NotesDatabase.entrySet())
            {
                if(i.getValue().isEmpty()){deleteNote(i.getValue().getNoteId());}
            }
        }
}
