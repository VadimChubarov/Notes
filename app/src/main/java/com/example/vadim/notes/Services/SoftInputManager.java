package com.example.vadim.notes.Services;
import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;


public class SoftInputManager
{
    protected Activity targetActivity;

    public void showSoftInput()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) targetActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);}
    }

    public void setTargetActivity(Activity targetActivity) {this.targetActivity = targetActivity;}
}
