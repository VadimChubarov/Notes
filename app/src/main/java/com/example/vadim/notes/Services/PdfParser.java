package com.example.vadim.notes.Services;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.vadim.notes.AppItems.Ticket;
import com.example.vadim.notes.NotesManager;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

public class PdfParser extends AsyncTask<String,Void,String>
{
    private String extractedText;
    private Ticket ticket;
    private static PdfParser runningTask;

    public PdfParser(Ticket ticket)
    {
       this.ticket = ticket;
    }


    public boolean isRunning()
    {
        if(this.getStatus()==(Status.RUNNING)) {return true;}
        else{return false;}
    }

    public static PdfParser getRunningTask() {return runningTask;}

    @Override
    protected String doInBackground(String... filePath)
    {
        extractText(filePath[0]);
        return extractedText;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        NotesManager.getInstance().getOpenTicketActivity().showProgressDialog(true);
        NotesManager.getInstance().getOpenTicketActivity().showButtonAddTicket(false);
        NotesManager.getInstance().getOpenTicketActivity().showButtonEraseTicket(false);
        runningTask = this;
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);

        runningTask = null;
        processExtractedData();
        NotesManager.getInstance().getOpenTicketActivity().showProgressDialog(false);
        NotesManager.getInstance().getOpenTicketActivity().showButtonEraseTicket(true);
        NotesManager.getInstance().getOpenTicketActivity().showTicketInfo(true);

    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);

        runningTask = null;
        extractedText = null;
        ticket.setInitilized(false);

        if (NotesManager.getInstance() != null)
        {
            NotesManager.getInstance().getOpenTicketActivity().clearTicketInfo();
            Toast toast = Toast.makeText(NotesManager.getInstance().getMainActivity(), "Ticket parsing cancelled", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public  void extractText(String filePath)
    {
        extractedText = null;

        if(filePath.endsWith(".pdf"))
        {
            PdfReader pdfReader;
            PdfDocument pdf;
            try
            {
                pdfReader = new PdfReader(filePath);
                pdf = new PdfDocument(pdfReader);

                extractedText = PdfTextExtractor.getTextFromPage(pdf.getPage(1), new LocationTextExtractionStrategy());

                pdf.close();
                pdfReader.close();
            }
            catch (Exception e)
            {
                Toast toast = Toast.makeText(NotesManager.getInstance().getOpenTicketActivity(),"PROBLEM WITH READING FILE", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void processExtractedData()
    {
        if(extractedText!=null && extractedText.contains("УКРЗАЛІЗНИЦЯ"))
        {
            String dataList [] = extractedText.split("\n");

            for(int i = 0; i < dataList.length; i++)
            {

                if(dataList[i].contains("Прізвище, Ім’я") && dataList[i].contains("Поїзд"))
                {
                    String data[] = dataList[i].split("(Прізвище, Ім’я)|(Поїзд)");
                    ticket.setPassenger(data[1]);
                    ticket.setTrainData(data[2]);
                    ticket.setTrainData("-");
                }
                if(dataList[i].contains("Дата/час відпр."))
                {
                    String data[] = dataList[i].split("(Дата/час відпр.)|( )");
                    ticket.setDepartureDate(data[1]);
                    ticket.setDepartureTime(data[2]);
                }
                if(dataList[i].contains("Дата/час приб."))
                {
                    String data[] = dataList[i].split("(Дата/час приб.)|( )");
                    ticket.setArrivalDate(data[2]);
                    ticket.setArrivalTime(data[3]);
                }
                if(dataList[i].contains("Вагон")||dataList[i].contains("Місце"))
                {
                    String data[] = dataList[i].split(" ");
                    for(String item : data)
                    {
                        if(item.matches("\\d{1,3}"))
                        {
                            ticket.setTrainData(item);
                            ticket.setTrainData("-");
                        }
                        if(item.matches("[A-Я-I]+\\-[А-Я-I]+"))
                        {
                            ticket.setName(item);
                        }
                    }
                }
            }
         ticket.setInitilized(true);
        }
    }
}
