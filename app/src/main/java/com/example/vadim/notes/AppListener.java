package com.example.vadim.notes;
import android.view.View;
import com.example.vadim.notes.AppItems.Note;
import com.example.vadim.notes.Services.AlertManager;

public class AppListener  implements View.OnClickListener

{
    private NotesManager notesManager;
    private AlertManager exitAlert;
    private AlertManager eraseNoteAlert;
    private AlertManager eraseChecklistAlert;
    private AlertManager eraseTicketAlert;

    public AppListener(NotesManager notesManager)
    {
        this.notesManager = notesManager;
        this.exitAlert = new AlertManager("Close Notes application?");
        this.eraseNoteAlert = new AlertManager("Erase note text?");
        this.eraseChecklistAlert = new AlertManager("Erase completed list items?");
        this.eraseTicketAlert = new AlertManager("Erase ticket data?");
        this.exitAlert.createDialog(AlertManager.Alerts.CLOSE, AlertManager.Alerts.CANCEL, AlertManager.Alerts.EXIT,
                notesManager.getMainActivity());
    }
    @Override
    public void onClick(View v)
    {
        if (v.getId()== R.id.Button_save_note) {notesManager.createNewNote("Note");}
        if (v.getId()==R.id.Button_exit_App) {exitAlert.runAlertDialog();}
    }


    public class NoteViewListener implements View.OnClickListener
    {
        private Note note;

        public NoteViewListener(Note note)
        {
            this.note = note;
        }
        @Override
        public void onClick(View v)
        {
              switch (v.getId())
              {
                  case R.id.Button_add_checkListItem : notesManager.getOpenedCheckListActivity().
                          addListItem(); break;

                  case R.id.Button_clear_checklist_items : eraseChecklistAlert.createDialog
                          (AlertManager.Alerts.ERASE, AlertManager.Alerts.CANCEL, AlertManager.Alerts.ERASE,
                          notesManager.getOpenedCheckListActivity());
                      eraseChecklistAlert.runAlertDialog(); break;

                  case R.id.Button_reset_checkboxes : notesManager.getOpenedCheckListActivity().openContextMenu(R.id.Button_reset_checkboxes); break;

                  case R.id.Button_exit_checklist : notesManager.getOpenedCheckListActivity().finish(); break;

                  case R.id.Button_exit_note : notesManager.getOpenedNoteActivity().saveNoteData();
                      notesManager.getOpenedNoteActivity().finish(); break;

                  case R.id.Button_eraiser : eraseNoteAlert.createDialog
                          (AlertManager.Alerts.ERASE, AlertManager.Alerts.CANCEL, AlertManager.Alerts.ERASE,
                      notesManager.getOpenedNoteActivity());
                      eraseNoteAlert.runAlertDialog(); break;

                  case R.id.OpenedNoteLayout : notesManager.getOpenedNoteActivity().saveNoteData();
                      notesManager.getOpenedNoteActivity().getSoftInputManager().showSoftInput();
                      notesManager.getOpenedNoteActivity().putCursorToEnd(); break;

                  case R.id.Button_add_ticket : notesManager.getOpenTicketActivity().openDowloads(true); break;

                  case R.id.Button_clear_ticket: eraseTicketAlert.createDialog(AlertManager.Alerts.ERASE, AlertManager.Alerts.CANCEL, AlertManager.Alerts.ERASE,
                          notesManager.getOpenTicketActivity());
                      eraseTicketAlert.runAlertDialog(); break;

                  case R.id.Button_exit_ticket: notesManager.getOpenTicketActivity().finish(); break;

                  case R.id.Button_close_downloads: notesManager.getOpenTicketActivity().openDowloads(false); break;

                  case R.id.TicketDisplay : notesManager.getOpenTicketActivity().loadDocInReader(); break;

                  default: notesManager.generateSavedNoteScreen(note); break;
              }
        }
    }
}
