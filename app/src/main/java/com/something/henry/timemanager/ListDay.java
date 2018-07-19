package com.something.henry.timemanager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListDay extends AppCompatActivity {

    private ListView listView;
    DBHelper dbHelper;
    private DayAdapter dayAdapter;
    ArrayList<CalendarDate> DayList,DaySearch,SubDaySearch;
    TextView tv_listname;
    public int index=0;
    public boolean isLoading=false;
    public View ftView;
    public Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_day);

        listView=findViewById(R.id.LV);
        dbHelper=new DBHelper(this,null,null,1);
        tv_listname=findViewById(R.id.tv_listname);


        DayList=dbHelper.getAllDay();
        for(int i=DayList.size()-1;i>=0;i--){
            if(DayList.get(i).getDate_time()<=0) DayList.remove(i);
        }
        if(DayList.isEmpty()){
            Toast.makeText(getApplicationContext(),"This List is empty", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        //Default
        DaySearch=new ArrayList<>(DayList);
        SubDaySearch=new ArrayList<>();
        addlist();

        LayoutInflater li=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView=li.inflate(R.layout.foot_view,null);
        mHandler=new MyHandler();

        dayAdapter=new DayAdapter(this,R.layout.listview_item_day,SubDaySearch);
        listView.setAdapter(dayAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view.getLastVisiblePosition()==totalItemCount-1&&dayAdapter.getCount()>=10&&isLoading==false){
                    isLoading=true;
                    Thread thread=new ThreadGetMoreData();
                    thread.start();
                    dayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void WeekDaySelectClick(View view){
        Button bt= findViewById(view.getId());
        int weekdayid=WeekDayId(bt.getText().toString());
        DaySearch.clear();
        for(CalendarDate CDsearch:DayList){
            if(CDsearch.getWeekDay()==weekdayid) DaySearch.add(CDsearch);
        }
        SubDaySearch.clear();
        index=0;
        addlist();
        dayAdapter.notifyDataSetChanged();
        tv_listname.setText("Activity List of "+getWeekDayName(bt.getText().toString()));
    }

    public void AllWeekDaySelectClick(View view){
        DaySearch.clear();
        DaySearch.addAll(DayList);
        SubDaySearch.clear();
        index=0;
        addlist();
        tv_listname.setText("Activity List of All day");
        dayAdapter.notifyDataSetChanged();
    }

    public void addlist(){
        for(int i=0;i<10;i++){
            if(index>=DaySearch.size()) break;
            SubDaySearch.add(DaySearch.get(index));
            index++;
        }
    }

    public String getWeekDayName(String s){
        if(s.equals("M")) return "Monday";
        else if(s.equals("Tu")) return "Tuesday";
        else if(s.equals("W")) return "Wednesday";
        else if(s.equals("Th")) return "Thursday";
        else if(s.equals("F")) return "Friday";
        else if(s.equals("Sa")) return "Saturday";
        else return "Sunday";
    }

    public int WeekDayId(String s){
        if(s.equals("M")) return 1;
        else if(s.equals("Tu")) return 2;
        else if(s.equals("W")) return 3;
        else if(s.equals("Th")) return 4;
        else if(s.equals("F")) return 5;
        else if(s.equals("Sa")) return 6;
        else return 0;
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    listView.addFooterView(ftView);
                    break;
                case 1:
                    addlist();
                    listView.removeFooterView(ftView);
                    isLoading=false;
                    break;
                default:
                    break;
            }
        }
    }

    public class ThreadGetMoreData extends Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace();}
            mHandler.sendEmptyMessage(1);
        }
    }
}
