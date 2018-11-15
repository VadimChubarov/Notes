package com.example.vadim.notes.AppItems;

import java.util.Map;
import java.util.TreeMap;

public class Ticket extends Note implements AppItem
{
    private StringBuilder name;
    private StringBuilder trainData;
    private Map<String, String> ticketData = new TreeMap<>();
    private String[] keys;

    public Ticket(int noteId)
    {
        super(noteId);
        name = new StringBuilder();
        trainData = new StringBuilder();
        this.itemType = "Ticket";

        keys = new String []{
                "1:isInitilized",
                "2:name",
                "3:passenger",
                "4:departureDate",
                "5:arrivalDate",
                "6:departureTime",
                "7:arrivalTime",
                "8:trainData",
                "9:filePath"};

        for(String key: keys)
        {
            if(key.equals("1:isInitilized")){ticketData.put(key,"false");}
            else{ticketData.put(key,"----");}
        }

    }

    public void clearTicketInfo()
    {
       name = new StringBuilder();
       trainData = new StringBuilder();

       for(Map.Entry<String,String> data : ticketData.entrySet())
       {
           if(data.getKey().equals("1:isInitilized")){data.setValue("false");}
           else{data.setValue("----");}
       }
    }

    @Override
    public boolean isEmpty()
    {
        if(!this.isInitilized()){return true;}
        else{return false;}
    }

    public void setAllData(String[] ticket_data)
    {
        for(int i = 0; i < ticket_data.length; i++)
        {
            ticketData.put(keys[i],ticket_data[i]);
        }
    }

    public void setPassenger(String passenger) {this.ticketData.put("3:passenger",passenger);}

    public void setDepartureDate(String departureDate) {this.ticketData.put("4:departureDate",departureDate);}

    public void setArrivalDate(String arrivalDate) {this.ticketData.put("5:arrivalDate",arrivalDate);}

    public void setDepartureTime(String departureTime) {this.ticketData.put("6:departureTime",departureTime);}

    public void setArrivalTime(String arrivalTime) {this.ticketData.put("7:arrivalTime",arrivalTime);}

    public void setTrainData(String trainData)
    {
        this.trainData.append(trainData);
        this.ticketData.put("8:trainData",this.trainData.toString());
    }

    public void setFilePath(String filePath) {this.ticketData.put("9:filePath",filePath);}

    public void setName(String name)
    {
        this.name.append(name); this.name.append("     ");;
        this.ticketData.put("2:name",this.name.toString());
    }

    @Override
    public String getTextPreview()
    {
        return  ticketData.get("2:name")+
                "\n"+ticketData.get("3:passenger")+
                "\n"+ticketData.get("4:departureDate")+
                "   "+ticketData.get("6:departureTime");
    }

    public String getPassanger() {return ticketData.get("3:passenger");}

    public String getDate() {return ticketData.get("4:departureDate")+" ------ "+ticketData.get("5:arrivalDate");}

    public String getTime() {return ticketData.get("6:departureTime")+" ------ "+ticketData.get("7:arrivalTime");}

    public String[] getTrainData()
    {
        return ticketData.get("8:trainData").split("-");
    }

    public String getName() {return ticketData.get("2:name");}

    public String getFilePath() {return ticketData.get("9:filePath");}

    public boolean isInitilized()
    {
       if(ticketData.get("1:isInitilized").equals("true")){return true;}
       else{return false;}
    }

    public void setInitilized(boolean initilized)
    {
        if(initilized){ticketData.put("1:isInitilized","true");}
        else{ticketData.put("1:isInitilized","false");}
    }

    public Map<String, String> getTicketData() {return ticketData;}

    @Override
    public String getItemType() {
        return itemType;
    }
}
