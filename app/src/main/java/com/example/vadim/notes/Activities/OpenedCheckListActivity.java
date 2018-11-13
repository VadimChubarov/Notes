package com.example.vadim.notes.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.example.vadim.notes.*;
import com.example.vadim.notes.AppItems.CheckList;
import com.example.vadim.notes.Services.ContextMenuManager;
import com.example.vadim.notes.Services.SoftInputManager;

import java.util.ArrayList;

public class OpenedCheckListActivity extends Activity
{
    private RelativeLayout scrollViewLayout;
    private ContextMenuManager checkBoxesMenu;
    private EditText checkListHeader;
    private CheckList currentCheckList;
    private ArrayList<CheckList.CheckListItem> checkListItems = new ArrayList<>();
    private SoftInputManager softInputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_check_list);

        NotesManager.getInstance().setCurrentActivity(this);
        currentCheckList = (CheckList) NotesManager.getInstance().getCurrentNote();

        buildAllComponents();
        rebuildCheckListItems();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        saveCheckListData();
        NotesManager.getInstance().clearCurrentActivity();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.Button_reset_checkboxes){checkBoxesMenu.showContextMenu(menu,menuInfo);}
    }

    public void openContextMenu(int view_id){this.openContextMenu(findViewById(view_id));}


    private void saveCheckListData()
    {
        currentCheckList.setName(checkListHeader.getText().toString());
        currentCheckList.getSavesNoteView().setText(currentCheckList.getTextPreview());
        currentCheckList.getListItemsData().clear();
        for(CheckList.CheckListItem checkListItem : checkListItems)
        {currentCheckList.getListItemsData().add(checkListItem.getListItemTextView().getText().toString()+checkListItem.getCheckBoxFlag());}
    }

    private void buildAllComponents()
    {
        scrollViewLayout = findViewById(R.id.checkListScrollLayout);
        checkListHeader = findViewById(R.id.listName);

        Button addListItem = findViewById(R.id.Button_add_checkListItem);
        Button clearListItems = findViewById(R.id.Button_clear_checklist_items);
        Button exitCheckList = findViewById(R.id.Button_exit_checklist);
        Button resetChecboxes = findViewById(R.id.Button_reset_checkboxes);

        checkListHeader.setText(currentCheckList.getName());

        AppListener.NoteViewListener checkListListener = NotesManager.getInstance().getAppListener().new NoteViewListener(currentCheckList);

        addListItem.setOnClickListener(checkListListener);
        clearListItems.setOnClickListener(checkListListener);
        exitCheckList.setOnClickListener(checkListListener);
        resetChecboxes.setOnClickListener(checkListListener);

        String[]checkBoxesMenuItems = {"Reset all checkboxes","Activate all checkboxes"};
        checkBoxesMenu = new ContextMenuManager(resetChecboxes,checkBoxesMenuItems,this);

        softInputManager = new SoftInputManager();
        softInputManager.setTargetActivity(this);
        softInputManager.showSoftInput();
    }


    public void addListItem()
    {
        CheckList.CheckListItem checkListItem = currentCheckList.generateCheckListItem(this,"");
        buildListItem(checkListItem);
        checkListItem.getListItemTextView().requestFocus();
        softInputManager.showSoftInput();
        checkListItems.add(checkListItem);
    }

    public void clearCompletedListItems()
    {
        currentCheckList.getListItemsData().clear();
        boolean isChecked;

       for(int i = 0; i < checkListItems.size();i++)
       {
           isChecked = checkListItems.get(i).getListItemCheckBox().isChecked();
           if (!isChecked)
           {currentCheckList.getListItemsData().add(checkListItems.get(i).getListItemTextView().getText().toString()+0);}
       }
           checkListItems.clear();
           scrollViewLayout.removeAllViews();
           rebuildCheckListItems();
       }

    protected void rebuildCheckListItems()
    {
        for(int i =0; i < currentCheckList.getListItemsData().size(); i++)
        {
            String itemData = currentCheckList.getListItemsData().get(i);
            String itemText;
            int checkBoxFlag;
            if(itemData.length()>1)
            {
               itemText = itemData.substring(0,itemData.length()-1);
               checkBoxFlag = Character.getNumericValue(itemData.charAt(itemData.length()-1));
            }
            else{itemText = ""; checkBoxFlag =0;}

            CheckList.CheckListItem checkListItem = currentCheckList.recoverCheckListItem(this,itemText,checkBoxFlag,i);
            buildListItem(checkListItem);
            checkListItems.add(checkListItem);
        }
    }

    protected void buildListItem(CheckList.CheckListItem list_item_data)
    {
        scrollViewLayout.addView(list_item_data.getListItemCheckBox());
        scrollViewLayout.addView(list_item_data.getListItemTextView());
        defineListItemPosition(list_item_data);
        list_item_data.getListItemCheckBox().setVisibility(View.VISIBLE);
        list_item_data.getListItemTextView().setVisibility(View.VISIBLE);
    }

    public void defineListItemPosition(CheckList.CheckListItem checkListItem)
    {
        RelativeLayout.LayoutParams checkListItemParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams checkListTextParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        int previousViewId = (checkListItem.getId()-1)+10;
        int topMargin = 15;

        checkListItemParams.addRule(RelativeLayout.BELOW, previousViewId);
        checkListItemParams.setMargins(20, topMargin, 5, 5);
        checkListItem.getListItemCheckBox().setLayoutParams(checkListItemParams);

        checkListTextParams.addRule(RelativeLayout.ALIGN_BASELINE,checkListItem.getListItemCheckBox().getId());
        checkListTextParams.setMargins(150,topMargin,5,5);
        checkListItem.getListItemTextView().setLayoutParams(checkListTextParams);
    }

    public void resetAllCheckboxes()
    {
        for(CheckList.CheckListItem checkListItem : checkListItems)
        {
            if(checkListItem.getListItemCheckBox().isChecked()){checkListItem.getListItemCheckBox().toggle();}
        }
    }

     public void activateAllCheckboxes()
    {
        for(CheckList.CheckListItem checkListItem : checkListItems)
        {
            if(!checkListItem.getListItemCheckBox().isChecked()){checkListItem.getListItemCheckBox().toggle();}
        }
    }

}
