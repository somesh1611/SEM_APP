package com.example.sem_app.ui.Tournaments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sem_app.R;

import java.util.ArrayList;

public class Event_list_adapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList maintitle;
    public Event_list_adapter(Activity context, ArrayList maintitle) {
        super(context, R.layout.event_list_item,maintitle);


        this.context = context;
        this.maintitle = maintitle;



    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.event_list_item, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);


        titleText.setText(maintitle.get(position).toString());

        return rowView;

    };
}
