package com.example.sem_app.ui.Tournaments;

public class Tournament {


    String name;
    String host;
    String start;
    String end;
    String sports;

    public Tournament() {

    }

    public Tournament(String name,String host,String start,String end,String sports,String mName,String mNumber) {
        this.name = name;
        this.host = host;
        this.start = start;
        this.end = end;
        this.sports = sports;
        this.mName=mName;
        this.mNumber = mNumber;
    }

    public String getmNumber() {
        return mNumber;
    }

    public void setmNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    String mName;

    public String getmName() {

        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    String mNumber;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }





}
