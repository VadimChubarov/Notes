package com.example.vadim.notes.Services;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import com.example.vadim.notes.NotesManager;
import com.example.vadim.notes.R;

public class ActionBarManager
{
    private Activity targetActivity;
    private Object managerClass;
    private ActionBar actionBar;
    private RelativeLayout targetLayout;
    private String[] tabs;
    private String currentTab;
    private SearchManager searchManager;

   public ActionBarManager(AppCompatActivity targetActivity, RelativeLayout targetLayout, Object managerClass, String [] tabs)
   {
        this.targetActivity = targetActivity;
        this.managerClass = managerClass;
        this.actionBar = targetActivity.getSupportActionBar();
        this.targetLayout = targetLayout;
        this.tabs = tabs;
   }


   public void createTabActionBar()
   {
       actionBar.setDisplayShowTitleEnabled(false);
       actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
       ActionBar.TabListener tabListener = new ActionBar.TabListener()
       {
           NotesManager notesManager = (NotesManager) managerClass;
           public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
           {
               for(int i = 0; i < tabs.length;i++)
               {if (tab.getText().equals(tabs[i]))
               {
                   notesManager.showViewsOnly(tabs[i]);currentTab = tabs[i];}
               }


                try{searchManager.showSearcBar(false);}catch(Exception e){}
           }
           public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft){}
           public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft){}
           {
               try{searchManager.showSearcBar(false);}catch(Exception e){}
           }
       };
       for (String tab : tabs)
       {
           ActionBar.Tab currentTab = actionBar.newTab();
           currentTab.setText(tab).setTabListener(tabListener);
           actionBar.addTab(currentTab);
       }
   }
    public String getCurrentTab() {return currentTab;}


    public void createSearchTool (Menu menu, int scrollViewId, int searchViewId, int sensitivity)
    {
       searchManager = new SearchManager(menu,scrollViewId,searchViewId,sensitivity);
    }

    public class SearchManager
    {

        MenuInflater inflater;
        SearchView searchView;
        Menu menu;
        boolean menuCreated;
        int sensitivity;
        int scrolViewId;
        int searchViewId;

       @SuppressLint("NewApi")
       public SearchManager(Menu menu, int scrollViewId, int searchViewId, final int sensitivity)
       {
           this.sensitivity = sensitivity;
           this.menu = menu;
           this.scrolViewId = scrollViewId;
           this.searchViewId = searchViewId;

           inflater = targetActivity.getMenuInflater();
           ScrollView scrollView = targetActivity.findViewById(scrolViewId);

               scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener()
               {
                   boolean direction_DOWN;
                   boolean direction_UP;
                   @Override
                   public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
                   {
                       if (scrollY > oldScrollY && !direction_DOWN){direction_UP = true;}
                       else if (scrollY < (oldScrollY-sensitivity) && !direction_UP){direction_DOWN = true;}
                       else{direction_DOWN = false; direction_UP =false;}

                       if (direction_UP && menuCreated && !searchView.hasFocus()){showSearcBar(false);}
                       else if (direction_DOWN  && !menuCreated){showSearcBar(true);}
                   }
               });
           }

       public void showSearcBar(boolean show)
    {
        if(show)
        {
            inflater.inflate(R.menu.options_menu, menu);
            searchView = (SearchView) menu.findItem(searchViewId).getActionView();
            menuCreated = true;

            final NotesManager notesManager = (NotesManager) managerClass;
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
                @Override
                public boolean onQueryTextSubmit(String query) {return false;}
                @Override
                public boolean onQueryTextChange(String newText){notesManager.search(newText);return false;}
            });
        }
        else{menu.clear();menuCreated = false;}}
    }
}
