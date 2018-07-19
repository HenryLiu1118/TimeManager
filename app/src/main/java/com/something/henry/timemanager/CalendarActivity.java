package com.something.henry.timemanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView=(CalendarView)findViewById(R.id.CalendarTable);
        dbHelper=new DBHelper(this,null,null,1);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selecteddate=(month+1)+"/"+dayOfMonth+"/"+year;
                setspinner(selecteddate);
            }
        });
        setcurrentdate();
    }

    public void setspinner(String selecteddate){
        TextView tv_Spinner=(TextView)findViewById(R.id.tv_Spinner);
        tv_Spinner.setText(selecteddate);
        TextView tv_scroll=(TextView)findViewById(R.id.tv_scroll);
        String tv_scroll_str="";

        ArrayList<CalendarDate> calendarDates=dbHelper.getAllDay();
        CalendarDate cd=new CalendarDate();
        boolean found=false;
        for(CalendarDate CD:calendarDates){
            if(CD.getDay().equals(selecteddate)){
                found=true;
                cd=CD;
                break;
            }
        }
        if(!found||cd.getDate_time()<=0){
            tv_scroll_str="There's no activity on "+selecteddate;
            tv_scroll.setText(tv_scroll_str);
            return;
        }
        tv_Spinner.setText(selecteddate+" Total "+cd.getDate_time()+" hours");
        ArrayList<Subcategory> subcategories=dbHelper.getSubCategoryForDay(cd.getId());
        for(Subcategory sb: subcategories){
            tv_scroll_str+=sb.getSub_name()+" "+sb.getSub_time()+" hours";
            tv_scroll_str+="\n";
        }
        tv_scroll.setText(tv_scroll_str);
    }

    public void setcurrentdate(){
        Calendar calendar = Calendar.getInstance();
        int d=calendar.get(Calendar.DAY_OF_MONTH);
        int m=calendar.get(Calendar.MONTH);
        int y=calendar.get(Calendar.YEAR);
        String selecteddate=(m+1)+"/"+d+"/"+y;
        setspinner(selecteddate);
    }

    public void CheckDaily(View view){
        startActivity(new Intent(CalendarActivity.this,ListDay.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
