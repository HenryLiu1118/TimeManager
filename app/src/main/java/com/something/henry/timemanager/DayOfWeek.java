package com.something.henry.timemanager;

public class DayOfWeek {
    private double hours;
    public int num;
    private String WeekDay;

    public DayOfWeek() {
        this.hours=0;
        this.num=0;
        this.WeekDay="";
    }

    public double getAve() {
        if(num==0) return 0;
        return hours/num;
    }


    public String getWeekDay() {
        return WeekDay;
    }

    public void setWeekDay(int weekDay) {
        WeekDay = WeekDayTrans(weekDay);
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours += hours;
    }

    public String WeekDayTrans(int i){
        if(i==0) return "Sunday";
        else if(i==1) return "Monday";
        else if(i==2) return "Tuesday";
        else if(i==3) return "Wednessday";
        else if(i==4) return "Thursday";
        else if(i==5) return "Friday";
        else if(i==6) return "Saturday";
        else return "";
    }
}
