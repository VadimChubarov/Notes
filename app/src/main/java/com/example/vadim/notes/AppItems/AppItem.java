package com.example.vadim.notes.AppItems;

public interface AppItem
{
    String getItemType();

    int getViewId();

    void setViewId(int viewId);

    String getTextPreview();

    boolean isEmpty();

    String getText();

    void setText(String text);

    int getNoteId();

    void setNoteId(int noteId);
}
