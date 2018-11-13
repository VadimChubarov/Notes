package com.example.vadim.notes.Services;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.widget.CompoundButtonCompat;
import android.widget.CheckBox;

public class CheckBoxCustomizer
{
    public void setCheckBoxColors(CheckBox checkBox, String checked, String unchecked)
    {
        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {Color.parseColor(checked), Color.parseColor(unchecked)};
        CompoundButtonCompat.setButtonTintList(checkBox, new ColorStateList(states, colors));
    }
}
