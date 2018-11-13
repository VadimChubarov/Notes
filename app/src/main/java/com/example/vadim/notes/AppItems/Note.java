package com.example.vadim.notes.AppItems;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.example.vadim.notes.NotesManager;
import com.example.vadim.notes.R;


public class Note
{
    private String text="";
    int noteId;
    Button savedNoteView;
    ImageView icon;

    public Note(int noteId)
    {
      this.noteId = noteId;
      this.savedNoteView = new Button(NotesManager.getInstance().getMainActivity().getNotesLayout().getContext());
      this.icon = new ImageView(NotesManager.getInstance().getMainActivity().getNotesLayout().getContext());
      savedNoteView.setWidth(500);
      savedNoteView.setHeight(100);
      savedNoteView.setBackgroundResource(R.drawable.note_view2);
      savedNoteView.getBackground().setAlpha(10);
      savedNoteView.setTransformationMethod(null);
      savedNoteView.setTypeface(Typeface.create("sans-serif",Typeface.NORMAL));
      savedNoteView.setTextSize(16);
      savedNoteView.setTextColor(Color.parseColor("#8d8d8d"));
      savedNoteView.setVisibility(View.INVISIBLE);
      savedNoteView.setId(10+noteId);
      icon.setImageResource(R.drawable.notes_);
    }

    public String getTextPreview()
    {
        String previewText = this.text;
        if(this.text.length()>20){previewText = this.text.substring(0,20)+ "......";}
        return previewText;
    }

    public boolean isEmpty()
    {
      if(text.length()==0){return true;}
      return false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNoteId() { return noteId; }

    public void setNoteId(int noteId) { this.noteId = noteId; }

    public Button getSavesNoteView() { return this.savedNoteView; }

    public ImageView getIcon() {return icon;}

}
