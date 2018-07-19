package com.something.henry.timemanager;

public class DailyEntry {
    private int Eid;
    private int Day_id;
    private String Day;
    private double time;
    private int Sub_id;
    private String sub_name;

    public DailyEntry(int eid,int day_id, String day, double time, int sub_id, String sub_name) {
        Eid=eid;
        Day_id = day_id;
        Day = day;
        this.time = time;
        Sub_id = sub_id;
        this.sub_name = sub_name;
    }

    public int getEid() {
        return Eid;
    }

    public void setEid(int eid) {
        Eid = eid;
    }

    public int getDay_id() {
        return Day_id;
    }

    public void setDay_id(int day_id) {
        Day_id = day_id;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getSub_id() {
        return Sub_id;
    }

    public void setSub_id(int sub_id) {
        Sub_id = sub_id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }
}
