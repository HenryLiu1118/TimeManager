package com.something.henry.timemanager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListEntry extends AppCompatActivity {

    private ListView listView;
    DBHelper dbHelper;
    EntryAdapter entryAdapter;//need to be global
    ArrayList<DailyEntry> dailyEntries, subdailyEntry;
    public int index=0;
    public boolean isLoading=false;
    public Handler mHandler;
    public View ftView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_entry);

        listView=findViewById(R.id.LV);
        dbHelper=new DBHelper(this,null,null,1);
        dailyEntries= dbHelper.getDailyEntryList();
        if(dailyEntries.isEmpty()){
            Toast.makeText(getApplicationContext(),"This List is empty", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        subdailyEntry=new ArrayList<DailyEntry>();
        addlist();

        LayoutInflater li=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView=li.inflate(R.layout.foot_view,null);
        mHandler=new MyHandler();

        entryAdapter=new EntryAdapter(this,R.layout.listview_item_entry,subdailyEntry);
        listView.setAdapter(entryAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view.getLastVisiblePosition()==totalItemCount-1&&entryAdapter.getCount()>=10&&isLoading==false){
                    isLoading=true;
                    Thread thread=new ThreadGetMoreData();
                    thread.start();
                    entryAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void addlist(){
        for(int i=0;i<10;i++){
            if(index>=dailyEntries.size()) break;
            subdailyEntry.add(dailyEntries.get(index));
            index++;
        }
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
