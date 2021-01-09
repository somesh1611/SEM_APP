package com.example.sem_app.ui.Tournaments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tournament {

    String name;
    String host;
    String start;
    String end;


    public Tournament() {

    }

    public Tournament(String name,String host,String start,String end) {
        this.name = name;
        this.host = host;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getStatus()
    {
        String status="";

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        String today = day+"/"+month+"/"+year;

        Date tstart = null;
        Date tend = null;
        Date datePreviousDate = null;
        Date current = null;


        int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            current = dateFormat.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try
        {
            tstart = dateFormat.parse(start);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            tend = dateFormat.parse(end);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String previousDate = dateFormat.format(tstart.getTime() - MILLIS_IN_DAY);

        try
        {
            datePreviousDate = dateFormat.parse(previousDate);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if(current.after(tend))
        {
            status="Over";

        }
        else if(current.after(datePreviousDate))
        {
            status="Live";

        }else {
            status="Upcoming";
        }

        return status;

    }




}
