package com.something.henry.timemanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Spinner spinner1, spinner2;
    Button btn_date;
    TextView tv_sum,tv_School,tv_Skill,tv_Intern,tv_Ave,tv_WeekDayAve;
    Calendar calendar;
    int d, m, y;
    Date theday,Today;
    EditText et_sub,et_record;
    DBHelper dbHelper;
    ArrayList<Subcategory> SubCategoryList;
    ArrayList<CalendarDate> calendarDates;
    DecimalFormat df = new DecimalFormat("###.##");
    int NumOfDays=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setview();
        UpdateList();
        setSpinner1();
        setSpinner2();
        setcurrentdate();
        setcategory();
    }

    public void setview(){
        spinner1=(Spinner) findViewById(R.id.sp1);//for rtecord
        spinner2=(Spinner) findViewById(R.id.sp2);//for create subcategory

        tv_sum=(TextView)findViewById(R.id.tv_sum);
        tv_School=(TextView)findViewById(R.id.tv_School);
        tv_Skill=(TextView)findViewById(R.id.tv_Skill);
        tv_Intern=(TextView)findViewById(R.id.tv_Intern);
        tv_Ave=(TextView)findViewById(R.id.tv_Ave);
        tv_WeekDayAve=(TextView)findViewById(R.id.tv_WeekDayAve);

        btn_date=(Button) findViewById(R.id.btn_date);
        dbHelper=new DBHelper(this,null,null,1);
        et_sub=(EditText) findViewById(R.id.et_sub);
        et_record=(EditText) findViewById(R.id.et_record);
    }

    public void UpdateList(){
        SubCategoryList=dbHelper.getAllSubCategory();
        calendarDates=dbHelper.getAllDay();
    }

    public void setSpinner1(){
        SubCategoryList=dbHelper.getAllSubCategory();
        if(SubCategoryList.size()==0) return;
        Collections.sort(SubCategoryList, new Comparator<Subcategory>() {
            @Override
            public int compare(Subcategory o1, Subcategory o2) {
                if(!o1.getSub_cate().equals(o2.getSub_cate()))
                    return o1.getSub_cate().compareTo(o2.getSub_cate());
                else return o1.getSub_name().compareTo(o2.getSub_name());
            }
        });
        ArrayList<String> SubCategoryName=new ArrayList<>();
        for(Subcategory sb: SubCategoryList){
            SubCategoryName.add(sb.getSub_name());
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,SubCategoryName);
        spinner1.setAdapter(arrayAdapter);
    }

    public void setSpinner2(){
        ArrayList<String> category=new ArrayList<>();
        category.add("School");
        category.add("Skill");
        category.add("Intern");
        ArrayAdapter<String> adaptercategory=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,category);
        spinner2.setAdapter(adaptercategory);
    }

    public void setcurrentdate(){
        calendar = Calendar.getInstance();
        d=calendar.get(Calendar.DAY_OF_MONTH);
        m=calendar.get(Calendar.MONTH);
        y=calendar.get(Calendar.YEAR);
        Today=new Date(y-1900,m,d);
        theday=new Date(y-1900,m,d);
        btn_date.setText((m+1)+"/"+d+"/"+y);
    }

    public void setcategory(){
        double School=0,Skill=0,Intern=0;
        for(Subcategory sb:SubCategoryList){
            if(sb.getSub_cate().equals("School")) School+=sb.getSub_time();
            else if(sb.getSub_cate().equals("Skill")) Skill+=sb.getSub_time();
            else if(sb.getSub_cate().equals("Intern")) Intern+=sb.getSub_time();
        }
        tv_School.setText("Total "+df.format(School)+" hours for School activity");
        tv_Skill.setText("Total "+df.format(Skill)+" hours for Skill activity");
        tv_Intern.setText("Total "+df.format(Intern)+" hours for Intern activity");
        double Sum=School+Skill+Intern;
        double Ave=0;
        //Info about weekday
        String WeekDay_str="";
        DayOfWeek[] WeekDay=getWeekDay();
        if(calendarDates.size()>0) Ave=Sum/NumOfDays;
        if(Sum>0) for(DayOfWeek dow: WeekDay){ WeekDay_str+="You spent average "+df.format(dow.getAve())+" hours on "+dow.getWeekDay()+"\n"; }
        if(WeekDay[0].getAve()>0) WeekDay_str="Your Study on "+WeekDay[0].getWeekDay()+" is the most efficient, and "+WeekDay[6].getWeekDay()+" is the least efficient.\n\n"+WeekDay_str;
        tv_WeekDayAve.setText(WeekDay_str);
        tv_sum.setText("You've studied "+df.format(Sum)+" hours totaly.");
        if(Sum>0) tv_Ave.setText("The Average hours you spend per day is "+df.format(Ave)+" hours.");
    }
    private static Date getDateByStr(String s1){
        String tokens1[]=s1.split("\\/");
        int m1=Integer.parseInt(tokens1[0])-1,d1=Integer.parseInt(tokens1[1]),y1=Integer.parseInt(tokens1[2]);
        return new Date(y1-1900,m1,d1);
    }

    public void RecordHours(View view){
        String RecordHours=et_record.getText().toString();
        String datestr=btn_date.getText().toString();
        if(TextUtils.isEmpty(RecordHours)){
            Toast.makeText(getApplicationContext(),"Empty input", Toast.LENGTH_LONG).show();
            return;
        }
        if(SubCategoryList.isEmpty()){
            Toast.makeText(getApplicationContext(),"Set your Subcategory first", Toast.LENGTH_LONG).show();
            return;
        }
        //check dulicate
        boolean containsday=false;
        CalendarDate CD=new CalendarDate();//Used for get the specific Calendar object if possible
        for(CalendarDate cd: calendarDates){
            if(cd.getDay().equals(datestr)){
                CD=cd;
                containsday=true;
                break;
            }
        }

        if(!containsday){
            CD=new CalendarDate(0,datestr,getDay(theday),Double.parseDouble(RecordHours));
            int NewId=dbHelper.adddate(CD);
            CD.setId(NewId);
        } else {
            CD.setDate_time(CD.getDate_time()+Double.parseDouble(RecordHours));
            dbHelper.modifydate(CD);
        }

        //Get the specific Subcategory object
        String SubCategoryName = spinner1.getSelectedItem().toString();
        if(TextUtils.isEmpty(SubCategoryName)){
            Toast.makeText(getApplicationContext(),"Please Select a Subcategory", Toast.LENGTH_LONG).show();
            return;
        }
        Subcategory SB=new Subcategory();
        for(Subcategory sb: SubCategoryList){
            if(sb.getSub_name().equals(SubCategoryName)){
                SB=sb;
                break;
            }
        }

        dbHelper.addentry(SB,CD,Double.parseDouble(RecordHours));
        SB.setSub_time(SB.getSub_time()+Double.parseDouble(RecordHours));
        dbHelper.modifysubcategory(SB);
        et_record.setText("");
        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
        UpdateList();
        setcategory();
    }

    public void SetChosenDate(View view){
        DatePickerDialog pickerDialog=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                btn_date.setText((month+1)+"/"+dayOfMonth+"/"+year);
                theday=new Date(year-1900,month,dayOfMonth);
            }
        }, y, m, d);
        pickerDialog.show();
    }

    public void CreateSubcategory(View view){
        String SubName=et_sub.getText().toString();
        if(TextUtils.isEmpty(SubName)){
            Toast.makeText(getApplicationContext(),"Please type your Category Name", Toast.LENGTH_LONG).show();
            return;
        }
        //Check Duplicate
        ArrayList<String> SubCategoryName=new ArrayList<>();
        for(Subcategory sb: SubCategoryList){
            SubCategoryName.add(sb.getSub_name().toLowerCase());
        }
        if(SubCategoryName.contains(SubName.toLowerCase())){
            Toast.makeText(getApplicationContext(),"Duplicate Category Name", Toast.LENGTH_LONG).show();
            return;
        }
        //upodate database
        String CategoryName = spinner2.getSelectedItem().toString();
        if(TextUtils.isEmpty(CategoryName)){
            Toast.makeText(getApplicationContext(),"Please Select a Category", Toast.LENGTH_LONG).show();
            return;
        }
        Subcategory subcategory=new Subcategory(0,SubName,CategoryName,0.0);
        dbHelper.addsubcategory(subcategory);
        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
        et_sub.setText("");
        setSpinner1();
    }

    public void CheckSchool(View view){
        startActivity(new Intent(MainActivity.this,List.class).putExtra("Category",1));
    }

    public void CheckSkill(View view){
        startActivity(new Intent(MainActivity.this,List.class).putExtra("Category",2));
    }

    public void CheckIntern(View view){
        startActivity(new Intent(MainActivity.this,List.class).putExtra("Category",3));
    }

    public void CheckDaily(View view){
        startActivity(new Intent(MainActivity.this,CalendarActivity.class));
    }

    public void CheckRecord(View view){
        startActivity(new Intent(MainActivity.this,ListEntry.class));
    }

    public int getDay(Date date) {
        return date.getDay();
    }

    private DayOfWeek[] getWeekDay(){
        NumOfDays=0;
        DayOfWeek WeekDay[]=new DayOfWeek[7];
        for(int i=0;i<7;i++){
            WeekDay[i]=new DayOfWeek();
            WeekDay[i].setWeekDay(i);
        }

        Calendar c1 = Calendar.getInstance();
        c1.setTime(Today);
        Calendar c2 = Calendar.getInstance();

        if(calendarDates.size()>0) c2.setTime(getDateByStr(calendarDates.get(calendarDates.size()-1).getDay()));
        else c2.setTime(Today);
        while(c1.after(c2)||c1.equals(c2)) {
            if(c2.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){
                WeekDay[2].num++;
            }
            else if(c2.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY){
                WeekDay[3].num++;
            }
            else if(c2.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY){
                WeekDay[4].num++;
            }
            else if(c2.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY){
                WeekDay[5].num++;
            }
            else if(c2.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
                WeekDay[6].num++;
            }
            else if(c2.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
                WeekDay[0].num++;
            }
            else if(c2.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                WeekDay[1].num++;
            }
            c2.add(Calendar.DATE,1);
            NumOfDays++;
        }

        for(CalendarDate cd: calendarDates){ WeekDay[cd.getWeekDay()].setHours(cd.getDate_time()); }
        Arrays.sort(WeekDay, new Comparator<DayOfWeek>() {
            @Override
            public int compare(DayOfWeek o1, DayOfWeek o2) {
                if(o1.getAve()>o2.getAve()) return -1;
                else return 1;
            }
        });
        return WeekDay;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateList();
        setSpinner1();
        setcurrentdate();
        setcategory();
    }
}
