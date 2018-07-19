package com.something.henry.timemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class List extends AppCompatActivity {

    private ListView listView;
    private EditText et_subcategory;
    DBHelper dbHelper;
    private SubcategoryAdapter subcategoryAdapter;
    private String SubName="";
    ArrayList<Subcategory> Sub,SubSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView=findViewById(R.id.LV);
        et_subcategory=findViewById(R.id.et_subcategory);
        dbHelper=new DBHelper(this,null,null,1);

        ArrayList<Subcategory> SubCategoryList=dbHelper.getAllSubCategory();
        String Name="";
        if(getIntent().getExtras()!=null){
            int Cate=getIntent().getIntExtra("Category",0);
            if(Cate==1) Name="School";
            else if(Cate==2) Name="Skill";
            else Name="Intern";
        }
        Sub=new ArrayList<>();
        for(Subcategory sb: SubCategoryList){
            if(sb.getSub_cate().equals(Name)) Sub.add(sb);
        }
        if(Sub.isEmpty()){
            Toast.makeText(getApplicationContext(),"This Subcategory is empty", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        SubSearch=new ArrayList<>(Sub);
        subcategoryAdapter=new SubcategoryAdapter(this,R.layout.listview_item_subcategory,SubSearch);
        listView.setAdapter(subcategoryAdapter);

        et_subcategory.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                SubName=s.toString();
                if(SubName.length()!=0){
                    SubSearch.clear();//have to clear and add !!Important
                    for(Subcategory SBsearch:Sub){
                        if(SBsearch.getSub_name().toLowerCase().indexOf(SubName.toLowerCase())>=0) SubSearch.add(SBsearch);
                    }
                } else {
                    SubSearch.clear();
                    SubSearch.addAll(Sub);
                }
                subcategoryAdapter.notifyDataSetChanged();
            }
        });
    }
}
