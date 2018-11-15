package com.example.vadim.notes.AppItems;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.InputFilter;
import android.view.View;
import android.widget.*;
import com.example.vadim.notes.Services.CheckBoxCustomizer;

import java.util.ArrayList;

public class CheckList extends Note implements AppItem
{
    private String name;
    private ArrayList<String> listItemsData;

    public CheckList(int checkListId)
    {
      super(checkListId);
      this.listItemsData = new ArrayList<String>();
      this.name = "New Checklist";
      this.itemType = "CheckList";
    }

    @Override
    public boolean isEmpty() {return false;}

    public String getTextPreview()
    {
        String previewText = this.name;
        if(this.name.length()>20){previewText = this.name.substring(0,20)+ "......";}
        return previewText;
    }

    public CheckListItem generateCheckListItem(Context context,String text)
    {
        CheckListItem checkListItem = new CheckListItem(context);
        checkListItem.getListItemTextView().setText(text);
        listItemsData.add(checkListItem.getListItemTextView().getText().toString()+checkListItem.getCheckBoxFlag());
        checkListItem.setId(listItemsData.size()-1);

        return checkListItem;
    }

    public CheckListItem recoverCheckListItem(Context context,String text,int checkBoxFlag,int id)
    {
        CheckListItem checkListItem = new CheckListItem(context);
        checkListItem.getListItemTextView().setText(text);
        checkListItem.setCheckBoxFlag(checkBoxFlag);
        if(checkBoxFlag==1){checkListItem.getListItemCheckBox().setChecked(true);}
        checkListItem.setId(id);

        return checkListItem;
    }

    public ArrayList<String> getListItemsData() {return listItemsData;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    @Override
    public String getItemType() {
        return itemType;
    }

    public class CheckListItem
    {
        private int id;
        private EditText listItemTextView;
        private CheckBox listItemCheckBox;
        private int checkBoxFlag;

        public CheckListItem(Context context)
        {
           this.listItemTextView = new EditText(context);
           this.listItemCheckBox = new CheckBox(context);
           this.checkBoxFlag = 0;
           this.listItemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
           {@Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
               {if(isChecked) {CheckListItem.this.checkBoxFlag = 1;}
               else{CheckListItem.this.checkBoxFlag = 0;}}
           });
           CheckBoxCustomizer checkBoxCustomizer = new CheckBoxCustomizer();
           checkBoxCustomizer.setCheckBoxColors(listItemCheckBox,"#70d333","#bdbdbd");

           this.listItemTextView.setWidth(500);
           this.listItemTextView.setHeight(100);
           this.listItemTextView.setTextSize(16);
           this.listItemTextView.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(35)});
           this.listItemTextView.getBackground().setColorFilter(Color.parseColor("#00000000"), PorterDuff.Mode.SRC_IN);
           this.listItemTextView.setPadding(0,0,0,0);
           this.listItemTextView.setSingleLine(true);
           this.listItemTextView.setVisibility(View.INVISIBLE);
           this.listItemCheckBox.setVisibility(View.INVISIBLE);
        }

        public TextView getListItemTextView() {return listItemTextView;}

        public CheckBox getListItemCheckBox() {return listItemCheckBox;}

        public void setId(int id)
        {
            this.id = id;
            this.getListItemCheckBox().setId(id+10);
        }
        public int getId() {return id;}

        public int getCheckBoxFlag() {return checkBoxFlag;}

        public void setCheckBoxFlag(int checkBoxFlag) {this.checkBoxFlag = checkBoxFlag;}

    }
}
