package com.something.henry.timemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class DBHelper extends SQLiteOpenHelper{

    //table subcategory
    private static final String TABLE_SUBCATEGORY="subcategory";
    private static final String SUBCATEGORY_ID="sub_id";
    private static final String SUBCATEGORY_NAME="sub_name";
    private static final String SUBCATEGORY_CATE="sub_cate";
    private static final String SUBCATEGORY_TIME="sub_time";

    //table day
    private static final String TABLE_DAY="day";
    private static final String DAY_ID="d_id";
    private static final String DAY_DATE="d_date";
    private static final String DAY_DAY="d_day";
    private static final String DAY_TIME="d_time";

    //table entry
    private static final String TABLE_DAILY_ENTRY="daily_entry";
    private static final String DAILY_ENTRY_TIME="de_time";
    private static final String ENTRY_ID="entry_id";

    //Create tables
    private static final String CREATE_TABLE_SUBCATEGORY="CREATE TABLE "
            + TABLE_SUBCATEGORY + "("
            + SUBCATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SUBCATEGORY_NAME + " TEXT, "
            + SUBCATEGORY_CATE + " TEXT, "
            + SUBCATEGORY_TIME+" REAL);";

    private static final String CREATE_TABLE_DAY="CREATE TABLE "
            + TABLE_DAY + "("
            + DAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DAY_DATE + " TEXT, "
            + DAY_DAY + " INTEGER, "
            + DAY_TIME + " REAL);" ;

    private static final String CREATE_TABLE_DAILY_ENTRY="CREATE TABLE "
            + TABLE_DAILY_ENTRY + "("
            + ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SUBCATEGORY_NAME + " TEXT, "
            + DAY_DATE + " TEXT, "
            + SUBCATEGORY_ID + " INTEGER,"
            + DAY_ID + " INTEGER,"
            + DAILY_ENTRY_TIME + " REAL);";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "TimeManager_DataBase", factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SUBCATEGORY);
        db.execSQL(CREATE_TABLE_DAY);
        db.execSQL(CREATE_TABLE_DAILY_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBCATEGORY );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAY );
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_DAILY_ENTRY + "'");
        onCreate(db);
    }

    /*
    * Subcategory
    * */
    public void addsubcategory(Subcategory subcategory){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUBCATEGORY_NAME,subcategory.getSub_name());
        values.put(SUBCATEGORY_CATE,subcategory.getSub_cate());
        values.put(SUBCATEGORY_TIME, 0.0);
        db.insert(TABLE_SUBCATEGORY, null,values);
        db.close();
    }
/*
    public void deletesubcategory(String Name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_SUBCATEGORY + " WHERE "+ SUBCATEGORY_NAME + "=\'" + Name + "\';");
        db.close();
    }
*/
    public void modifysubcategory(Subcategory sub){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="UPDATE "+TABLE_SUBCATEGORY + " SET " + SUBCATEGORY_TIME + "=\'" + sub.getSub_time() + "\', " + SUBCATEGORY_NAME + "=\'" + sub.getSub_name() + "\' WHERE "+ SUBCATEGORY_ID + "=\'" + sub.getId() + "\';";
        db.execSQL(sql);

        //change the entry table
        sql="UPDATE "+TABLE_DAILY_ENTRY + " SET " + SUBCATEGORY_NAME + "=\'" + sub.getSub_name() + "\' WHERE "+ SUBCATEGORY_ID + "=\'" + sub.getId() + "\';";
        db.execSQL(sql);
        db.close();
    }

    public ArrayList<Subcategory> getAllSubCategory(){
        ArrayList<Subcategory> AllSubCategory = new ArrayList<> ();
        String selectQuery = "SELECT  * FROM " + TABLE_SUBCATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                if(c.getString(c.getColumnIndex(SUBCATEGORY_NAME))!=null){
                    Subcategory tempsub=new Subcategory();
                    tempsub.setId(c.getInt(c.getColumnIndex(SUBCATEGORY_ID)));
                    tempsub.setSub_name(c.getString(c.getColumnIndex(SUBCATEGORY_NAME)));
                    tempsub.setSub_cate(c.getString(c.getColumnIndex(SUBCATEGORY_CATE)));
                    tempsub.setSub_time(c.getDouble(c.getColumnIndex(SUBCATEGORY_TIME)));
                    AllSubCategory.add(tempsub);
                }
            }while(c.moveToNext());
        }
        db.close();
        Collections.sort(AllSubCategory);
        return AllSubCategory;
    }

    /*
    * Day
    * */
    public int adddate(CalendarDate date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DAY_DATE,date.getDay());
        values.put(DAY_DAY,date.getWeekDay());
        values.put(DAY_TIME,date.getDate_time());
        long NewId=db.insert(TABLE_DAY, null,values);
        db.close();
        return (int)NewId;
    }


    public void modifydate(CalendarDate date){
        SQLiteDatabase db = this.getWritableDatabase();
        //change the subcategory name
        String sql="UPDATE "+TABLE_DAY + " SET " + DAY_TIME + "=\'" + date.getDate_time() + "\' WHERE "+ DAY_ID + "=\'" + date.getId() + "\';";
        db.execSQL(sql);
        db.close();
    }

    public ArrayList<CalendarDate> getAllDay(){
        ArrayList<CalendarDate> AllDay=new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DAY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                if(c.getString(c.getColumnIndex(DAY_DATE))!=null){
                    CalendarDate tempday=new CalendarDate();
                    tempday.setId(c.getInt(c.getColumnIndex(DAY_ID)));
                    tempday.setDay(c.getString(c.getColumnIndex(DAY_DATE)));
                    tempday.setWeekDay(c.getInt(c.getColumnIndex(DAY_DAY)));
                    tempday.setDate_time(c.getDouble(c.getColumnIndex(DAY_TIME)));
                    AllDay.add(tempday);
                }
            }while(c.moveToNext());
        }
        db.close();
        Collections.sort(AllDay);
        return AllDay;
    }

    /*
    * Daily Entry
    * */
    public void addentry(Subcategory subcategory, CalendarDate date,double time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUBCATEGORY_NAME,subcategory.getSub_name());
        values.put(DAY_DATE,date.getDay());
        values.put(DAILY_ENTRY_TIME,time);
        values.put(SUBCATEGORY_ID,subcategory.getId());
        values.put(DAY_ID,date.getId());
        db.insert(TABLE_DAILY_ENTRY,null,values);
        db.close();
    }

    public void deleteentry(DailyEntry de){
        SQLiteDatabase db = this.getWritableDatabase();
        //update entry
        db.execSQL("DELETE FROM "+TABLE_DAILY_ENTRY + " WHERE "+ ENTRY_ID + "=\'" + de.getEid()  + "\';");
        //update subcategory
        double delete_time=getSubCategoryById(de.getSub_id()).getSub_time()-de.getTime();
        db.execSQL("UPDATE "+TABLE_SUBCATEGORY + " SET " + SUBCATEGORY_TIME + "=\'" + delete_time + "\' WHERE "+ SUBCATEGORY_ID + "=\'" + de.getSub_id() + "\';");
        //update say
        delete_time=getCalendarById(de.getDay_id()).getDate_time()-de.getTime();
        db.execSQL("UPDATE "+TABLE_DAY + " SET " + DAY_TIME + "=\'" + delete_time + "\' WHERE "+ DAY_ID + "=\'" + de.getDay_id() + "\';");
        db.close();
    }

    public void modifyentry(DailyEntry de, double Time){
        SQLiteDatabase db = this.getWritableDatabase();
        //update entry
        db.execSQL("UPDATE "+TABLE_DAILY_ENTRY + " SET " + DAILY_ENTRY_TIME + "=\'" + Time + "\' WHERE "+ ENTRY_ID + "=\'" + de.getEid()  + "\';");
        //update subcategory
        double change_time=getSubCategoryById(de.getSub_id()).getSub_time()+Time-de.getTime();
        db.execSQL("UPDATE "+TABLE_SUBCATEGORY + " SET " + SUBCATEGORY_TIME + "=\'" + change_time + "\' WHERE "+ SUBCATEGORY_ID + "=\'" + de.getSub_id() + "\';");
        //update say
        change_time=getCalendarById(de.getDay_id()).getDate_time()+Time-de.getTime();
        db.execSQL("UPDATE "+TABLE_DAY + " SET " + DAY_TIME + "=\'" + change_time + "\' WHERE "+ DAY_ID + "=\'" + de.getDay_id() + "\';");
        db.close();
    }
    private Subcategory getSubCategoryById(int id){
        Subcategory sb=new Subcategory();
        String selectQuery = "SELECT  * FROM " + TABLE_SUBCATEGORY+" WHERE "+SUBCATEGORY_ID+" =\'"+id+ "\';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        sb.setSub_time(c.getDouble(c.getColumnIndex(SUBCATEGORY_TIME)));
        return sb;
    }//wrong date
    private CalendarDate getCalendarById(int id){
        CalendarDate cd=new CalendarDate();
        String selectQuery = "SELECT  * FROM " + TABLE_DAY+" WHERE "+DAY_ID+" =\'"+id+ "\';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        cd.setDate_time(c.getDouble(c.getColumnIndex(DAY_TIME)));
        cd.setId(c.getInt(c.getColumnIndex(DAY_ID)));
        cd.setDay(c.getString(c.getColumnIndex(DAY_DATE)));
        return cd;
    }

    public ArrayList<Subcategory> getSubCategoryForDay(int id){
        ArrayList<Subcategory> AllSubCategory = new ArrayList<> ();
        ArrayList<Integer> AllSubCategoryID = new ArrayList<> ();//Used to check duplicate item
        String selectQuery = "SELECT  * FROM " + TABLE_DAILY_ENTRY+" WHERE "+DAY_ID+" =\'"+id+ "\';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                if(c.getString(c.getColumnIndex(DAY_DATE))!=null){
                    if(AllSubCategoryID.contains(c.getInt(c.getColumnIndex(SUBCATEGORY_ID)))){
                        int index=AllSubCategoryID.indexOf(c.getInt(c.getColumnIndex(SUBCATEGORY_ID)));
                        Subcategory subdup=AllSubCategory.get(index);
                        double t=c.getDouble(c.getColumnIndex(DAILY_ENTRY_TIME));
                        subdup.setSub_time(subdup.getSub_time()+t);
                        continue;
                    }

                    Subcategory tempsub=new Subcategory();
                    tempsub.setId(c.getInt(c.getColumnIndex(SUBCATEGORY_ID)));
                    tempsub.setSub_name(c.getString(c.getColumnIndex(SUBCATEGORY_NAME)));
                    tempsub.setSub_cate("");
                    tempsub.setSub_time(c.getDouble(c.getColumnIndex(DAILY_ENTRY_TIME)));

                    AllSubCategory.add(tempsub);
                    AllSubCategoryID.add(c.getInt(c.getColumnIndex(SUBCATEGORY_ID)));
                }
            }while(c.moveToNext());
        }
        db.close();
        return AllSubCategory;
    }

    public ArrayList<DailyEntry> getDailyEntryList(){
        ArrayList<DailyEntry> AllEntry=new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DAILY_ENTRY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                if(c.getString(c.getColumnIndex(DAY_DATE))!=null){
                    int Eid=c.getInt(c.getColumnIndex(ENTRY_ID));
                    int DayId=c.getInt(c.getColumnIndex(DAY_ID));
                    int SubId=c.getInt(c.getColumnIndex(SUBCATEGORY_ID));
                    String DayName=c.getString(c.getColumnIndex(DAY_DATE));
                    String SubName=c.getString(c.getColumnIndex(SUBCATEGORY_NAME));
                    double EntryTime=c.getDouble(c.getColumnIndex(DAILY_ENTRY_TIME));
                    DailyEntry de=new DailyEntry(Eid,DayId,DayName,EntryTime,SubId,SubName);
                    AllEntry.add(de);
                }
            } while(c.moveToNext());
        }
        db.close();
        Collections.reverse(AllEntry);
        return AllEntry;
    }
}
