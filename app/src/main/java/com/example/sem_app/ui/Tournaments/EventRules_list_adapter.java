package com.example.sem_app.ui.Tournaments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sem_app.R;

import java.util.ArrayList;

public class EventRules_list_adapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList rule;
    public EventRules_list_adapter(Activity context, ArrayList rule) {
        super(context, R.layout.event_rules_list_item,rule);


        this.context = context;
        this.rule = rule;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.event_rules_list_item, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.rule_text);

        titleText.setText(rule.get(position).toString());

        return rowView;

    }


}
