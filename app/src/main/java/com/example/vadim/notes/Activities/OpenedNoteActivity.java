package com.example.vadim.notes.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.example.vadim.notes.AppItems.Note;
import com.example.vadim.notes.AppListener;
import com.example.vadim.notes.NotesManager;
import com.example.vadim.notes.R;
import com.example.vadim.notes.Services.SoftInputManager;

public class OpenedNoteActivity extends Activity
{
    private EditText noteTextView;
    private String noteText;
    private Note currentNote;
    private SoftInputManager softInputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_note);
        NotesManager.getInstance().setCurrentActivity(this);

        currentNote = NotesManager.getInstance().getCurrentNote();
        noteText = currentNote.getText();

        buildAllComponents();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        saveNoteData();

        if(this.isFinishing() && currentNote.isEmpty())
        {NotesManager.getInstance().deleteNote(currentNote.getNoteId());}

        NotesManager.getInstance().clearCurrentActivity();
    }

    public void saveNoteData()
    {
       noteText = noteTextView.getText().toString();
       currentNote.setText(noteText);
       currentNote.getSavesNoteView().setText(currentNote.getTextPreview());
    }

    private void buildAllComponents()
    {
        RelativeLayout openNoteLayout = findViewById(R.id.OpenedNoteLayout);

        noteTextView = findViewById(R.id.NoteText);
        noteTextView.setText(noteText);

        putCursorToEnd();

        Button endButton = findViewById(R.id.Button_exit_note);
        Button eraiserButton = findViewById(R.id.Button_eraiser);

        AppListener.NoteViewListener openedNoteListener = NotesManager.getInstance().getAppListener().new NoteViewListener(currentNote);

        openNoteLayout.setOnClickListener(openedNoteListener);
        endButton.setOnClickListener(openedNoteListener);
        eraiserButton.setOnClickListener(openedNoteListener);

        softInputManager = new SoftInputManager();
        softInputManager.setTargetActivity(this);
        softInputManager.showSoftInput();
    }

    public void putCursorToEnd() {noteTextView.setSelection(noteText.length());}

    public void eraseNoteData() {noteTextView.setText("");}

    public SoftInputManager getSoftInputManager() {return softInputManager;}
}
