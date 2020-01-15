package com.example.smartagriculture;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Feed> {
    private List<Feed> feedList;
    Context mainContext;
    public CustomAdapter(List<Feed> feedlist,Context mainContext){
       super(mainContext,R.layout.dth_view,feedlist);
        this.mainContext=mainContext;
       this.feedList=feedlist;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(mainContext);
        View view=inflater.inflate(R.layout.dth_view,null);
        TextView textView=view.findViewById(R.id.temperaturev);
        TextView textView1=view.findViewById(R.id.humidityv);
        Feed feed=feedList.get(position);
        textView.setText(feed.getTemperature());
       textView1.setText(feed.getHumidity());
        return view;
    }
}