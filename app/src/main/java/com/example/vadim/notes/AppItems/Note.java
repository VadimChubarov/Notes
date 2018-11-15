package com.example.vadim.notes.AppItems;
public class Note implements AppItem
{
    private String text="";
    int noteId;
    int viewId;
    String itemType = "Note";

    public Note(int noteId)
    {
      this.noteId = noteId;
      this.viewId = 10+noteId;
    }

    public int getViewId() {return viewId;}

    public void setViewId(int viewId) {this.viewId = viewId;}

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

    @Override
    public String getItemType() {
        return itemType;
    }
}
