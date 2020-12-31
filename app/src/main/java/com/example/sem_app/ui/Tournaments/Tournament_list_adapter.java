package com.example.sem_app.ui.Tournaments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sem_app.R;

import java.util.ArrayList;

public class Tournament_list_adapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList maintitle;
    private final ArrayList subtitle;
    private final ArrayList tid;



    public Tournament_list_adapter(Activity context, ArrayList maintitle, ArrayList subtitle,ArrayList id) {
        super(context,R.layout.tournament_list_item,id);


        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
        this.tid = id;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.tournament_list_item, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);

        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(maintitle.get(position).toString());

        subtitleText.setText(subtitle.get(position).toString());

        return rowView;

    };

}
