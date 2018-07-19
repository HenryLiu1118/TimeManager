package com.something.henry.timemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DayAdapter  extends ArrayAdapter<CalendarDate> {
    private java.util.List<CalendarDate> calendardates;
    private Context context;


    public DayAdapter(@NonNull Context context, int resource, @NonNull List<CalendarDate> objects) {
        super(context, resource, objects);
        this.context=context;
        this.calendardates=objects;
    }

    @Override
    public int getCount() {
        return calendardates.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.listview_item_day,parent,false);

        TextView tv_day=(TextView) view.findViewById(R.id.tv_day);
        Spinner spin_day=(Spinner) view.findViewById(R.id.spin_day);
        CalendarDate cd=calendardates.get(position);
        tv_day.setText(cd.getDay()+" / "+WeekDay(cd.getWeekDay())+" / "+cd.getDate_time()+" hours");

        DBHelper dbHelper=new DBHelper(context,null,null,1);
        ArrayList<Subcategory> subcategories=dbHelper.getSubCategoryForDay(cd.getId());
        ArrayList<String> SubCategoryName=new ArrayList<>();
        for(Subcategory sb: subcategories){
            SubCategoryName.add(sb.getSub_name()+" "+sb.getSub_time()+" hours");
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,SubCategoryName);
        spin_day.setAdapter(arrayAdapter);

        return view;
    }

    public String WeekDay(int i){
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
