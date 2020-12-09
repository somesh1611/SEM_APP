package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sem_app.R;


public class EventViewFragment extends Fragment {
    String tournamentname,sportname;
    TextView tname,sname;
    Button part,rule;



    public EventViewFragment(String tn, String sn) {
        tournamentname=tn;
        sportname=sn;

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root = inflater.inflate(R.layout.fragment_event_view, container, false);
       tname = root.findViewById(R.id.tournament_name_textview);
        sname = root.findViewById(R.id.sport_name_text);
        part = root.findViewById(R.id.part);
       rule = root.findViewById(R.id.rule);
       tname.setText(tournamentname);
       sname.setText(sportname);
       part.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               EventParticipationFragment fragment=new EventParticipationFragment(tournamentname,sportname);
               FragmentTransaction transaction=getFragmentManager().beginTransaction();
               transaction.replace(R.id.nav_host_fragment,fragment);
               transaction.addToBackStack("back");
               transaction.commit();
           }
       });
        return root;
    }
}