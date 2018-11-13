package com.example.vadim.notes.Database;
import android.provider.BaseColumns;

public final class AppDbStruct
{
    private AppDbStruct() {
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_SAVED_NOTES = "title";
        public static final String COLUMN_NOTE_DATA = "subtitle";
    }
}
