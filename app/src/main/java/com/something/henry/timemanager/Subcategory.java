package com.something.henry.timemanager;

import android.support.annotation.NonNull;

public class Subcategory implements Comparable<Subcategory> {
    private int id;
    private String sub_name;
    private String sub_cate;
    private double sub_time;

    public Subcategory(){}
    public Subcategory(int id, String sub_name, String sub_cate, double sub_time) {
        this.id=id;
        this.sub_name = sub_name;
        this.sub_cate = sub_cate;
        this.sub_time = sub_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_cate() {
        return sub_cate;
    }

    public void setSub_cate(String sub_cate) {
        this.sub_cate = sub_cate;
    }

    public double getSub_time() {
        return sub_time;
    }

    public void setSub_time(double sub_time) {
        this.sub_time = sub_time;
    }

    @Override
    public int compareTo(@NonNull Subcategory o) {
        return (int)(o.getSub_time()-this.sub_time);
    }
}
