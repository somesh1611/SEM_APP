package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.sem_app.R;

import java.util.ArrayList;


public class ParticipatedEventsFragment extends Fragment {

    ListView listView;
    String TAG;

    ArrayList<String> participated_sports=new ArrayList<>();

   ParticipatedEventsFragment(ArrayList sp)
   {

       participated_sports.addAll(sp);

   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root = inflater.inflate(R.layout.fragment_participated_events, container, false);
        listView = root.findViewById(R.id.participated_events_list);

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, participated_sports);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        return root;
    }
}