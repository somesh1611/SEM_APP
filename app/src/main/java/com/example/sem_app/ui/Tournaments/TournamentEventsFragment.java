package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class TournamentEventsFragment extends Fragment {

    ListView listview;
    String TAG;
    String[] tournamentEvents;
    String tournamentid;
    ArrayList events = new ArrayList();
    Boolean isAdmin,isOver;
    String start,end;

    Date tstart = null;
    Date tend = null;
    Date datePreviousDate = null;
    Date current = null;
    int MILLIS_IN_DAY;
    SimpleDateFormat dateFormat;



    public TournamentEventsFragment(String tid,Boolean a,String s,String e) {

        tournamentid = tid;
        isAdmin=a;
        start=s;
        end=e;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         View root = inflater.inflate(R.layout.fragment_tournament_events, container, false);
        listview = root.findViewById(R.id.tournament_events_listview);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        String today = day+"/"+month+"/"+year;

        MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            current = dateFormat.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try
        {
            tstart = dateFormat.parse(start);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            tend = dateFormat.parse(end);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String previousDate = dateFormat.format(tstart.getTime() - MILLIS_IN_DAY);

        try
        {
            datePreviousDate = dateFormat.parse(previousDate);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if((!current.after(tend)))
        {
            isOver=false;

        }else {
            isOver=true;
        }

        //Toast.makeText(getActivity(),"ee "+isOver.toString(),Toast.LENGTH_SHORT).show();


        if(isAdmin)
        {
            FirebaseFirestore.getInstance().collection("Sports Tournaments").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("My Tournaments").document(tournamentid)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    events.clear();
                    if(task.isSuccessful())
                    {

                        String sports = task.getResult().get("Sports Included").toString();

                        tournamentEvents = sports.split(",");
                        events.addAll(Arrays.asList(tournamentEvents));

                        ArrayAdapter<String> adapter = new Event_list_adapter(getActivity(),
                                events);
                        listview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }

                }
            });
        }else {
            FirebaseFirestore.getInstance().collectionGroup("My Tournaments")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    events.clear();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if(document.getId().contentEquals(tournamentid)) {

                                String sports = document.get("Sports Included").toString();
                                tournamentEvents = sports.split(",");
                                events.addAll(Arrays.asList(tournamentEvents));
                                ArrayAdapter<String> adapter = new Event_list_adapter(getActivity(),
                                        events);
                                listview.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                break;
                            }

                        }
                    }
                }
            });
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventViewFragment fragment=new EventViewFragment(tournamentid,listview.getItemAtPosition(position).toString(),isAdmin,isOver);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });

        return root;
    }
}