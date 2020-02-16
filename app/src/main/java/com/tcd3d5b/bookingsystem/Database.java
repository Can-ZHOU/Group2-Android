package com.tcd3d5b.bookingsystem;

public class Database {
    String rDate, rTime, rId;

    public Database(){
    }

    public Database(String rDate,String rTime, String rId){
        this.rDate = rDate;
        this.rTime = rTime;
        this.rId = rId;
    }

    public String getrDate() {
        return rDate;
    }

    public String getrTime() {
        return rTime;
    }

    public String getrId() {
        return rId;
    }
}
