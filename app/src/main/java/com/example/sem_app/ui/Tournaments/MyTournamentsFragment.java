package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sem_app.R;

import java.util.ArrayList;
import java.util.List;

public class MyTournamentsFragment extends Fragment implements TournamentRecyclerAdapter.OnTournamentItemClickedListner {

    ArrayList tournamentId = new ArrayList();
    Boolean admin=true;
    private String TAG;
    RecyclerView mTournamentList;
    TournamentRecyclerAdapter mTournamentAdapter;
    MyTournamentsViewModel myTournamentsViewModel;

    public MyTournamentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_tournaments, container, false);

        mTournamentList = root.findViewById(R.id.my_tournaments_list);
        mTournamentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTournamentList.setHasFixedSize(true);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myTournamentsViewModel= new ViewModelProvider(this).get(MyTournamentsViewModel.class);
        mTournamentAdapter= new TournamentRecyclerAdapter(myTournamentsViewModel.getTournamentsList().getValue(),this,getContext());
        mTournamentList.setAdapter(mTournamentAdapter);
        myTournamentsViewModel.getTournamentsList().observe(getViewLifecycleOwner(), new Observer<List<Tournament>>() {
            @Override
            public void onChanged(List<Tournament> tournaments) {

                if(tournaments != null)
                {
                    mTournamentAdapter.setData(tournaments);
                }

            }
        });

        myTournamentsViewModel.getTournamentId().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                tournamentId= (ArrayList) strings;
            }
        });

    }

    @Override
    public void OnItemClicked(int position) {
        TournamentViewFragment fragment=new TournamentViewFragment(tournamentId.get(position).toString(),admin);
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment,fragment);
        transaction.addToBackStack("Back");
        transaction.commit();
    }
}