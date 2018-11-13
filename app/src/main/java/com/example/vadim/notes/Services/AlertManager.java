package com.example.vadim.notes.Services;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.example.vadim.notes.Activities.OpenTicketActivity;
import com.example.vadim.notes.Activities.OpenedCheckListActivity;
import com.example.vadim.notes.Activities.OpenedNoteActivity;
import com.example.vadim.notes.NotesManager;


public class AlertManager
{
    private String alertQuestion;
    private AlertDialog alertDialog;

    public enum Alerts
    {
        CANCEL,
        DELETE,
        ERASE,
        EXIT,
        CLOSE,
    }

    public AlertManager (String alertQuestion)
    {
        this.alertQuestion = alertQuestion;
    }


    public void createDialog(Alerts positive, final Alerts negative, final Alerts action, final Activity targetActivity)
    {
        final AlertDialog.Builder alertWindow = new AlertDialog.Builder(targetActivity);
        alertWindow.setMessage(alertQuestion);
        alertWindow.setPositiveButton(positive.toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(action==Alerts.EXIT){targetActivity.finish();}
                if((action==Alerts.ERASE))
                {
                    if(targetActivity instanceof OpenedNoteActivity){
                        NotesManager.getInstance().getOpenedNoteActivity().eraseNoteData();}
                    if(targetActivity instanceof OpenedCheckListActivity){NotesManager.getInstance().getOpenedCheckListActivity().clearCompletedListItems();}
                    if(targetActivity instanceof OpenTicketActivity) {NotesManager.getInstance().getOpenTicketActivity().clearTicketInfo();}
                }
            }
        });
        alertWindow.setNegativeButton(negative.toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        alertDialog = alertWindow.create();
    }

    public void runAlertDialog() {alertDialog.show();}
}
