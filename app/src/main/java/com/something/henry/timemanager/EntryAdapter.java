package com.something.henry.timemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EntryAdapter extends ArrayAdapter<DailyEntry> {
    private List<DailyEntry> dailyEntries;
    private Context context;

    public EntryAdapter(@NonNull Context context, int resource, @NonNull List<DailyEntry> objects) {
        super(context, resource, objects);
        this.dailyEntries = objects;
        this.context=context;
    }

    @Override
    public int getCount() {
        return dailyEntries.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.listview_item_entry, parent,false);

        TextView Title=(TextView) view.findViewById(R.id.Title);
        final DailyEntry de=dailyEntries.get(position);
        Title.setText("Date: "+de.getDay()+" Category: "+de.getSub_name());
        TextView Hours=(TextView) view.findViewById(R.id.Hours);
        Hours.setText("Hours Spending in this Entry: "+de.getTime());

        Button Delete=(Button) view.findViewById(R.id.btn_delete);
        final EditText et_modify=(EditText) view.findViewById(R.id.et_modify);
        Button Modify=(Button) view.findViewById(R.id.btn_modify);

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailyEntries.remove(position);
                DBHelper dbHelper=new DBHelper(context,null,null,1);
                dbHelper.deleteentry(de);
                notifyDataSetChanged();
            }
        });

        Modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TimeChange=et_modify.getText().toString();
                if(TextUtils.isEmpty(TimeChange)){
                    Toast.makeText(getContext(),"Empty Input", Toast.LENGTH_LONG).show();
                    return;
                }

                DBHelper dbHelper=new DBHelper(context,null,null,1);
                dbHelper.modifyentry(de,Double.parseDouble(TimeChange));
                de.setTime(Double.parseDouble(TimeChange));
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
