package com.example.vadim.notes.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.*;
import com.example.vadim.notes.*;
import com.example.vadim.notes.AppItems.Ticket;
import com.example.vadim.notes.Services.DownloadsFileManager;
import com.example.vadim.notes.Services.PdfParser;
import com.example.vadim.notes.Services.PermissionManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OpenTicketActivity extends Activity
{
    private Ticket currentTicket;
    private ListView downloadsEntry;
    private PermissionManager permissionManager;
    private DownloadsFileManager downloadsFileManager;
    private ProgressDialog progressDialog;
    private Button addTicket;
    private Button closeDownloads;
    private Button eraseTicket;
    private TextView passangerText;
    private TextView dateText;
    private TextView timeText;
    private TextView trainText;
    private TextView seatText;
    private TextView carriageText;
    private TextView ticketNameText;
    private ImageView ticketDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_ticket);

        NotesManager.getInstance().setCurrentActivity(this);
        currentTicket = (Ticket) NotesManager.getInstance().getCurrentNote();

        buildAllcomponents();

        showTicketInfo(true);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

       if(PdfParser.getRunningTask()!= null
               && PdfParser.getRunningTask().isRunning()
               && !PdfParser.getRunningTask().isCancelled())
       {
           showProgressDialog(true);
       }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Button view = NotesManager.getInstance().getMainActivity().findViewById(currentTicket.getViewId());
        view.setText(currentTicket.getTextPreview());

        if(PdfParser.getRunningTask()!= null
                && PdfParser.getRunningTask().isRunning())

          {showProgressDialog(false);}

        if(!currentTicket.isInitilized() &&
                (PdfParser.getRunningTask() == null ||
                        PdfParser.getRunningTask().isCancelled()) &&
                this.isFinishing() && NotesManager.getInstance()!=null)

        {NotesManager.getInstance().deleteNote(currentTicket.getNoteId());}

        if(NotesManager.getInstance()!=null)
        {NotesManager.getInstance().clearCurrentActivity();}

        Runtime.getRuntime().gc();
    }

    protected void buildAllcomponents()
        {
            downloadsEntry = findViewById(R.id.downloads_entry_list_view);
            Button saveTicket = findViewById(R.id.Button_exit_ticket);
            eraseTicket = findViewById(R.id.Button_clear_ticket);
            addTicket = findViewById(R.id.Button_add_ticket);
            closeDownloads = findViewById(R.id.Button_close_downloads);
            passangerText = findViewById(R.id.passangerText);
            dateText = findViewById(R.id.dateText);
            timeText = findViewById(R.id.timeText);
            trainText = findViewById(R.id.trainText);
            carriageText = findViewById(R.id.carriageText);
            seatText = findViewById(R.id.seatText);
            ticketNameText = findViewById(R.id.ticketNameText);
            ticketDisplay = findViewById(R.id.TicketDisplay);

            AppListener.NoteViewListener TicketListener = NotesManager.getInstance().getAppListener().new NoteViewListener(currentTicket);

            saveTicket.setOnClickListener(TicketListener);
            eraseTicket.setOnClickListener(TicketListener);
            addTicket.setOnClickListener(TicketListener);
            closeDownloads.setOnClickListener(TicketListener);
            ticketDisplay.setOnClickListener(TicketListener);

            permissionManager = new PermissionManager(this);
            downloadsFileManager = new DownloadsFileManager(".pdf");

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Parsing ticket in progress");
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    if(PdfParser.getRunningTask()!=null){PdfParser.getRunningTask().cancel(true);}
                }
            });

            downloadsEntry.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {@Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id)
            {
                String fileName = ((TextView)itemClicked).getText().toString();
                String filePath = downloadsFileManager.getSelectedFilePath(fileName);
                currentTicket.setFilePath(filePath);

                openDowloads(false);

                if(PdfParser.getRunningTask()==null)
                {
                  PdfParser pdfParser = new PdfParser(currentTicket);
                  pdfParser.execute(filePath);
                  showProgressDialog(true);
                }
            }
            });
        }

    public void openDowloads(boolean open)
    {
        if(open && permissionManager.isReadingAccesGranted())
        {
            String[] files = downloadsFileManager.entryDownloads();
            ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,files);
            downloadsEntry.setAdapter(adapter);

            downloadsEntry.setVisibility(View.VISIBLE);
            closeDownloads.setVisibility(View.VISIBLE);
            findViewById(R.id.downloads_header).setVisibility(View.VISIBLE);
            addTicket.setVisibility(View.INVISIBLE);
        }
        else
           {
               downloadsEntry.setAdapter(null);
               downloadsEntry.setVisibility(View.INVISIBLE);
               closeDownloads.setVisibility(View.INVISIBLE);
               findViewById(R.id.downloads_header).setVisibility(View.INVISIBLE);
               addTicket.setVisibility(View.VISIBLE);
           }
    }

    public void showProgressDialog(boolean show)
    {
        if(show)
        {
            progressDialog.show();
            showButtonAddTicket(false);
            showButtonEraseTicket(false);
        }
        else{progressDialog.dismiss();}
    }

    public void showButtonAddTicket(boolean show)
    {
        if(show)
        {
            addTicket.setVisibility(View.VISIBLE);
            showButtonEraseTicket(false);
        }
        else
            {
                addTicket.setVisibility(View.INVISIBLE);
                showButtonEraseTicket(true);
            }
    }

    public void showButtonEraseTicket(boolean show)
    {
        if(show){eraseTicket.setVisibility(View.VISIBLE);}
        else{eraseTicket.setVisibility(View.INVISIBLE);}
    }

    public void showTicketInfo(boolean show)
    {
        if(show && currentTicket.isInitilized())
        {
            showButtonAddTicket(false);
            findViewById(R.id.TicketDataLayout).setVisibility(View.VISIBLE);
            passangerText.setText(currentTicket.getPassanger());
            dateText.setText(currentTicket.getDate());
            timeText.setText(currentTicket.getTime());

            String[] trainData = currentTicket.getTrainData();
            trainText.setText(trainData[0]);
            carriageText.setText(trainData[1]);
            seatText.setText(trainData[2]);
            ticketNameText.setText(currentTicket.getName());

            displayTicket(true);
        }
        else{findViewById(R.id.TicketDataLayout).setVisibility(View.INVISIBLE);}
    }

    public void clearTicketInfo()
    {
        passangerText.setText("");
        dateText.setText("");
        timeText.setText("");
        trainText.setText("");
        carriageText.setText("");
        ticketNameText.setText("");

        currentTicket.clearTicketInfo();
        currentTicket.setInitilized(false);

        showButtonAddTicket(true);
        showTicketInfo(false);
        displayTicket(false);
    }

    protected void displayTicket(boolean display)
    {
        if(display)
        {
            ticketDisplay.setVisibility(View.VISIBLE);

            PdfRenderer pdfRenderer;
            PdfRenderer.Page pdfPage;
            ParcelFileDescriptor descriptor;

            try
            {
                File file = new File(currentTicket.getFilePath());

                descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                pdfRenderer = new PdfRenderer(descriptor);

                pdfPage = pdfRenderer.openPage(0);
                Bitmap bitmap = Bitmap.createBitmap(pdfPage.getWidth(), pdfPage.getHeight(), Bitmap.Config.ARGB_8888);

                pdfPage.render(bitmap, null, new Matrix(), PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                ticketDisplay.setImageBitmap(bitmap);

                pdfPage.close();
                pdfRenderer.close();
                descriptor.close();
            }
            catch (IOException e)
               {e.printStackTrace();
                ticketDisplay.setImageBitmap(null);
                ticketDisplay.setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(this,"Ticket file missing", Toast.LENGTH_SHORT);
                toast.show();
               }
        }
        else
            {ticketDisplay.setImageBitmap(null);
             ticketDisplay.setVisibility(View.INVISIBLE);}
    }

    public  void loadDocInReader()
    {
            File file = new File(currentTicket.getFilePath());
            Context context = this.getBaseContext();
            Intent viewIntent = new Intent();

            viewIntent.setAction(Intent.ACTION_VIEW);

            Uri URI = FileProvider.getUriForFile
                    (context,
                            context.getApplicationContext().getPackageName()
                                    + ".my.package.name.provider", file);

            viewIntent.setDataAndType(URI,"application/pdf");
            viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            List<ResolveInfo> resolved = getPackageManager().queryIntentActivities(viewIntent, 0);

            if(resolved != null && resolved.size() > 0)
            {startActivity(viewIntent);}
            else
            {Toast toast = Toast.makeText(this,"Ticket file missing", Toast.LENGTH_SHORT);
             toast.show();}
    }
}
