package com.example.vadim.notes.Activities;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.example.vadim.notes.Services.ActionBarManager;
import com.example.vadim.notes.Services.ContextMenuManager;
import com.example.vadim.notes.Database.AppDbManager;
import com.example.vadim.notes.NotesManager;
import com.example.vadim.notes.R;



public class MainActivity extends AppCompatActivity
{

    private NotesManager notesManager;
    private RelativeLayout layout;
    private RelativeLayout notesLayout;
    private ContextMenuManager itemCreationMenu;
    private ActionBarManager actionBarManager;
    private AppDbManager appDbManager;
    private String BackUpData;
    private static boolean firstLaunch = true;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildAppComponents();
        recoverSession();
        notesManager.showViewsOnly(actionBarManager.getCurrentTab());
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        backUpSession();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        backUpSession();

        notesManager.clear();
        notesManager = null;
    }

    @Override
    public void onBackPressed()
    {
        if(notesManager.getCurrentActivity()==null)
        {super.onBackPressed();}
        else
            {try{Thread.sleep(500);}catch(Exception e){}
             super.onBackPressed();}
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        int viewId = v.getId();
        super.onCreateContextMenu(menu, v, menuInfo);

        if(viewId == R.id.Button_save_note){itemCreationMenu.showContextMenu(menu,menuInfo);}
        else
            {String[] deletionMenuItems = {"Delete note"};
             ContextMenuManager itemDeletionMenu = new ContextMenuManager(v,deletionMenuItems,this);
             itemDeletionMenu.showContextMenu(menu,menuInfo);}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        actionBarManager.createSearchTool(menu,R.id.notesScroll,R.id.search,35);
        return true;
    }

    private void buildAppComponents()
    {
        layout = findViewById(R.id.mainLayout);
        notesLayout = findViewById(R.id.notesLayout);

        notesManager = new NotesManager(this);

        Button SAVE_NOTE = findViewById(R.id.Button_save_note);
        Button exitApp = findViewById(R.id.Button_exit_App);
        SAVE_NOTE.setOnClickListener(notesManager.getAppListener());
        exitApp.setOnClickListener(notesManager.getAppListener());

        String[] creationMenuItems = {"New Simple Note","New Check List","New RailwayTicket"};
        itemCreationMenu = new ContextMenuManager(SAVE_NOTE,creationMenuItems,this);

        String[] actionBarTabs = {"All","Note","List","Ticket"};
        actionBarManager = new ActionBarManager(this, notesLayout, notesManager, actionBarTabs);
        actionBarManager.createTabActionBar();

        appDbManager = new AppDbManager(layout.getContext());
    }

    protected void recoverSession()
    {
        BackUpData = appDbManager.loadAppData();
        notesManager.rebuildAllSavedNoteViews(BackUpData);
        if(firstLaunch)
        {
            notesManager.removeAllEmptyNotes();
            firstLaunch = false;
        }
    }

    public void backUpSession()
    {
        appDbManager.saveAppData(notesManager.getNotesForSave(), BackUpData);
        BackUpData = appDbManager.loadAppData();
    }

    public  RelativeLayout getMainLayout() { return layout; }

    public   RelativeLayout getNotesLayout() { return notesLayout; }

    public ActionBarManager getActionBarManager() {return actionBarManager;}
}

