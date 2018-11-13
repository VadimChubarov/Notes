package com.example.vadim.notes.Services;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import com.example.vadim.notes.NotesManager;
import com.example.vadim.notes.R;


public class ContextMenuManager
{
    private NoteContextViewListener[] contextViewListeners;
    private String[] menuItems;
    private View viewForContextMenu;

    public ContextMenuManager(View viewForContextMenu, String[] menuItems, Activity targetActivity)
    {
        targetActivity.registerForContextMenu(viewForContextMenu);

        this.viewForContextMenu = viewForContextMenu;
        this.menuItems = menuItems;
        this.contextViewListeners = new NoteContextViewListener[menuItems.length];

        for (int i = 0; i < contextViewListeners.length; i++)
        {
            contextViewListeners[i] = new NoteContextViewListener();
        }
    }

    public void showContextMenu(ContextMenu menu, ContextMenu.ContextMenuInfo menuInfo)
    {
        for (int i = 0; i < menuItems.length; i++)
        {
            menu.add(0, i, 0, menuItems[i]);
            menu.getItem(i).setOnMenuItemClickListener(contextViewListeners[i]);
        }
    }

    private class NoteContextViewListener implements android.view.MenuItem.OnMenuItemClickListener
    {
        @Override
        public boolean onMenuItemClick(MenuItem item)
        {
            if (viewForContextMenu.getId() == R.id.Button_save_note)
               {
                switch (item.getItemId())
                {
                    case 0: NotesManager.getInstance().createNewNote("Note");break;
                    case 1: NotesManager.getInstance().createNewNote("Checklist");break;
                    case 2: NotesManager.getInstance().createNewNote("Ticket"); break;}
                }

            else if (viewForContextMenu.getId() == R.id.Button_reset_checkboxes) {
                switch (item.getItemId())
                {
                    case 0: NotesManager.getInstance().getOpenedCheckListActivity().resetAllCheckboxes(); break;
                    case 1: NotesManager.getInstance().getOpenedCheckListActivity().activateAllCheckboxes();break;
                }
            }
            else
                {
                    int noteId = (int) viewForContextMenu.getId() - 10;
                    NotesManager.getInstance().deleteNote(noteId);
                }
         return false;
        }
    }
}