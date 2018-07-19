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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SubcategoryAdapter extends ArrayAdapter<Subcategory>{

    private List<Subcategory> subcategories;
    private Context context;
    DBHelper dbHelper;

    public SubcategoryAdapter(@NonNull Context context, int resource, @NonNull List<Subcategory> objects) {
        super(context, resource, objects);
        this.context=context;
        this.subcategories=objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.listview_item_subcategory,parent,false);

        TextView Title=(TextView) view.findViewById(R.id.Title);
        final Subcategory sb=subcategories.get(position);
        Title.setText(sb.getSub_name()+" ("+sb.getSub_cate()+")");
        TextView Hours=(TextView) view.findViewById(R.id.Hours);
        Hours.setText("Hours Spending in Studying: "+sb.getSub_time());

        //Button Delete=(Button) view.findViewById(R.id.btn_delete);
        final EditText et_modify=(EditText) view.findViewById(R.id.et_modify);
        Button Modify=(Button) view.findViewById(R.id.btn_modify);
/*
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subcategories.remove(position);
                DBHelper dbHelper=new DBHelper(context,null,null,1);
                dbHelper.deletesubcategory(sb.getSub_name());
                notifyDataSetChanged();
            }
        });
*/
        Modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SubName=et_modify.getText().toString();
                if(TextUtils.isEmpty(SubName)){
                    Toast.makeText(getContext(),"Please type your Category Name", Toast.LENGTH_LONG).show();
                    return;
                }
                //Check Duplicate
                dbHelper=new DBHelper(context,null,null,1);
                ArrayList<Subcategory> SubCategoryList=dbHelper.getAllSubCategory();
                ArrayList<String> SubCategoryName=new ArrayList<>();
                for(Subcategory sb1: SubCategoryList){
                    SubCategoryName.add(sb1.getSub_name().toLowerCase());
                }
                boolean ChangeName=sb.getSub_name().toLowerCase().equals(SubName.toLowerCase());
                if(!ChangeName&&SubCategoryName.contains(SubName.toLowerCase())){
                    Toast.makeText(getContext(),"Duplicate Category Name", Toast.LENGTH_LONG).show();
                    return;
                }
                sb.setSub_name(SubName);
                DBHelper dbHelper=new DBHelper(context,null,null,1);
                dbHelper.modifysubcategory(sb);
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
