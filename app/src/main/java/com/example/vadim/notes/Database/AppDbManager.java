package com.example.vadim.notes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class AppDbManager extends SQLiteOpenHelper
    {
        private SQLiteDatabase AppDataBase;
        private ContentValues values;


        protected static final int DATABASE_VERSION = 1;
        protected static final String DATABASE_NAME = "NoteAppDB.db";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + AppDbStruct.FeedEntry.TABLE_NAME + " (" +
                        AppDbStruct.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        AppDbStruct.FeedEntry.COLUMN_SAVED_NOTES + " TEXT," +
                        AppDbStruct.FeedEntry.COLUMN_NOTE_DATA + " TEXT)";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + AppDbStruct.FeedEntry.TABLE_NAME;

        public AppDbManager(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }


        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            onUpgrade(db, oldVersion, newVersion);
        }

        public void saveAppData(String newData, String existedData)
        {
            AppDataBase = this.getWritableDatabase();
            values = new ContentValues();

            values.put(AppDbStruct.FeedEntry.COLUMN_NOTE_DATA, newData);
            String selection = AppDbStruct.FeedEntry.COLUMN_NOTE_DATA + " LIKE ?";
            String[] selectionArgs = {existedData};

            int count = AppDataBase.update(
                    AppDbStruct.FeedEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            if (count==0)
            {
                values.put(AppDbStruct.FeedEntry.COLUMN_SAVED_NOTES, "AppNotesDataStorage");
                values.put(AppDbStruct.FeedEntry.COLUMN_NOTE_DATA,newData);
                AppDataBase.insert(AppDbStruct.FeedEntry.TABLE_NAME, null, values);
            }
            values.clear();
            AppDataBase.close();
            AppDataBase = null;
        }

        public String loadAppData()
        {
            String BackUp_data = null;
            AppDataBase = this.getReadableDatabase();

            String[] projection = {
                    BaseColumns._ID,
                    AppDbStruct.FeedEntry.COLUMN_SAVED_NOTES,
                    AppDbStruct.FeedEntry.COLUMN_NOTE_DATA
            };
            String selection = AppDbStruct.FeedEntry.COLUMN_SAVED_NOTES + " = ?";
            String sortOrder = AppDbStruct.FeedEntry.COLUMN_NOTE_DATA+ " DESC";
            String[] selectionArgs = { "AppNotesDataStorage" };

            Cursor cursor = AppDataBase.query(
                    AppDbStruct.FeedEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
            while(cursor.moveToNext()) { BackUp_data = cursor.getString(cursor.getColumnIndexOrThrow(AppDbStruct.FeedEntry.COLUMN_NOTE_DATA));}
            cursor.close();
            AppDataBase.close();
            AppDataBase = null;
            return BackUp_data;
        }
    }

