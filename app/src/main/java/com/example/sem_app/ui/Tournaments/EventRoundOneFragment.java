package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sem_app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventRoundOneFragment extends Fragment implements DrawRecyclerAdapter.OnDrawItemClickedListner  {

    String tournamentid, sportname,TAG,roundname,mround;
    Boolean isAdmin,isSlot,isTeam;
    ArrayList drawId=new ArrayList();
    TextView round_name;
    TextView tname, sname;
    int n;
    RecyclerView mDrawList;
    DrawRecyclerAdapter mDrawAdapter;
    EventRoundOneViewModel eventRoundOneViewModel;

    public EventRoundOneFragment() {
        // Required empty public constructor
    }

    public EventRoundOneFragment(String tid, String sn,String r, Boolean a,Boolean b,Boolean c,int pid){

        tournamentid = tid;
        sportname = sn;
        roundname=r;
        isAdmin=a;
        isSlot=b;
        isTeam=c;
        n=pid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_event_round_one, container, false);
        round_name=root.findViewById(R.id.round_name_text);
        tname = root.findViewById(R.id.tournament_name);
        sname = root.findViewById(R.id.sport_name);

        mDrawList = root.findViewById(R.id.drawlist);
        mDrawList.setLayoutManager(new LinearLayoutManager(getContext()));
        mDrawList.setHasFixedSize(true);

        /////////////////////////////////////////////////////////////////////////////////////////////////

         if(n>0&&n<=2)
         {
             round_name.setText("Final");
             mround="Final";
         }
         else if(n>2&&n<=4)
         {
             round_name.setText("Semi-Final");
             mround="Semi-Final";
         }
         else if(n>4&&n<=8)
         {
             round_name.setText("Quarter-Final");
             mround="Quarter-Final";
         }
         else {
             round_name.setText(roundname);
             mround=roundname;
         }

        return root;
    }



    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventRoundOneViewModel= new ViewModelProvider(this).get(EventRoundOneViewModel.class);
        sname.setText(sportname);
        eventRoundOneViewModel.getEventDetails(tournamentid,isAdmin).observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                if(!stringObjectHashMap.isEmpty()) {
                    tname.setText(stringObjectHashMap.get("Tournament Name").toString());

                }

            }
        });
        mDrawAdapter = new DrawRecyclerAdapter(getContext(),eventRoundOneViewModel.getDrawsList(tournamentid,sportname,roundname).getValue(),this,isTeam);
        mDrawList.setAdapter(mDrawAdapter);
        eventRoundOneViewModel.getDrawsList(tournamentid,sportname,roundname).observe(getViewLifecycleOwner(), new Observer<List<Draw>>() {
            @Override
            public void onChanged(List<Draw> draws) {
                if(draws != null)
                {
                    mDrawAdapter.setData(draws);
                }
                Log.d(TAG,"huehu");
            }

        });

        eventRoundOneViewModel.getDrawId(tournamentid,sportname,roundname).observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                drawId= (ArrayList) strings;
            }
        });

    }

    @Override
    public void OnItemClicked(int position) {

        EventResultFragment fragment = new EventResultFragment(drawId.get(position).toString(), tournamentid, sportname, mround, roundname, isAdmin, position, isTeam, isSlot);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack("back");
        transaction.commit();

    }
}