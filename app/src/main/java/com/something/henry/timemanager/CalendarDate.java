package com.something.henry.timemanager;

import android.support.annotation.NonNull;

public class CalendarDate implements Comparable<CalendarDate>{
    private int id;
    private String Day;
    private int WeekDay;
    private double date_time;

    public CalendarDate() {}

    public CalendarDate(int id, String day, int weekDay, double date_time) {
        this.id = id;
        Day = day;
        WeekDay = weekDay;
        this.date_time = date_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public int getWeekDay() {
        return WeekDay;
    }

    public void setWeekDay(int weekDay) {
        WeekDay = weekDay;
    }

    public double getDate_time() {
        return date_time;
    }

    public void setDate_time(double date_time) {
        this.date_time = date_time;
    }

    @Override
    public int compareTo(@NonNull CalendarDate o) {
        String tokens1[]=this.Day.split("\\/");
        String tokens2[]=o.getDay().split("\\/");
        if(Integer.parseInt(tokens1[0])>Integer.parseInt(tokens2[0])) return -1;
        else if(Integer.parseInt(tokens1[0])<Integer.parseInt(tokens2[0])) return 1;
        else {
            if(Integer.parseInt(tokens1[1])>Integer.parseInt(tokens2[1])) return -1;
            else if(Integer.parseInt(tokens1[1])<Integer.parseInt(tokens2[1])) return 1;
            else {
                if(Integer.parseInt(tokens1[2])>Integer.parseInt(tokens2[2])) return -1;
                else if(Integer.parseInt(tokens1[2])<Integer.parseInt(tokens2[2])) return 1;
                else return 0;
            }
        }
    }
}
