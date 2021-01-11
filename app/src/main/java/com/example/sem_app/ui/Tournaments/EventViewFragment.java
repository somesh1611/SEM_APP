package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sem_app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class EventViewFragment extends Fragment {
    String tournamentid,sportname;
    TextView tname,sname,wname;
    ArrayList team_sports=new ArrayList();
    Button part,rule;
    Boolean isAdmin,isTeam,isOver;
    EventViewViewModel eventViewViewModel;



    public EventViewFragment(String tid, String sn,Boolean a,Boolean b) {
        tournamentid=tid;
        sportname=sn;
        isAdmin=a;
        isOver=b;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root = inflater.inflate(R.layout.fragment_event_view, container, false);
       tname = root.findViewById(R.id.tournament_name_textview);
        sname = root.findViewById(R.id.sport_name_text);
        wname = root.findViewById(R.id.winner_name_text);
        part = root.findViewById(R.id.part);
       rule = root.findViewById(R.id.rule);

       //team_sports.addAll(Collections.singleton(R.array.sportsList));
        team_sports.addAll(Arrays.asList(getResources().getStringArray(R.array.team_sports)));
       if(team_sports.contains(sportname))
       {
           isTeam=true;
           //Toast.makeText(getActivity(),"Team",Toast.LENGTH_SHORT).show();
       }else {
           isTeam=false;
           //Toast.makeText(getActivity(),"Single",Toast.LENGTH_SHORT).show();
       }

        //////////////////////////////////////////////////////////////////////////////////////////

      part.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               EventParticipationFragment fragment=new EventParticipationFragment(tournamentid,sportname,isAdmin,isTeam,isOver);
               FragmentTransaction transaction=getFragmentManager().beginTransaction();
               transaction.replace(R.id.nav_host_fragment,fragment);
               transaction.addToBackStack("back");
               transaction.commit();
           }
       });
       rule.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               EventRulesFragment fragment=new EventRulesFragment(tournamentid,sportname,isAdmin,isOver);
               FragmentTransaction transaction=getFragmentManager().beginTransaction();
               transaction.replace(R.id.nav_host_fragment,fragment);
               transaction.addToBackStack("back");
               transaction.commit();
           }
       });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventViewViewModel = new ViewModelProvider(this).get(EventViewViewModel.class);
        sname.setText(sportname);
        eventViewViewModel.getEventDetails(tournamentid,isAdmin).observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                if(!stringObjectHashMap.isEmpty()) {

                    tname.setText(stringObjectHashMap.get("Tournament Name").toString());

                }

            }
        });

        eventViewViewModel.getWinner(tournamentid,sportname,isTeam).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty())
                {
                    wname.setVisibility(View.VISIBLE);
                    wname.setTextColor(getResources().getColor(R.color.color_pink));
                    wname.setText("WINNER - "+s);
                }
            }
        });

    }
}