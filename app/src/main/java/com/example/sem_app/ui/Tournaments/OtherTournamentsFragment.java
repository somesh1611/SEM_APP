package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sem_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class OtherTournamentsFragment extends Fragment {

    ListView listView;
    ArrayList<String> other_tournaments_name =new ArrayList<>();
    ArrayList<String> getOther_tournaments_host=new ArrayList<>();
    ArrayList<String> getOther_tournaments_id=new ArrayList<>();
    ArrayList<String> getOther_tournaments_status=new ArrayList<>();
    ArrayList allusers = new ArrayList();
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private String TAG;
    Boolean admin=false;


    Date start = null;
    Date end = null;
    Date datePreviousDate = null;
    Date current = null;
    int MILLIS_IN_DAY;
    SimpleDateFormat dateFormat;



    public OtherTournamentsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_other_tournaments, container, false);
        listView = root.findViewById(R.id.other_tournaments_list);

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

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Object ID = firebaseUser.getUid();



        CollectionReference colref=firebaseFirestore.collection("Users");
        colref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allusers.clear();
               other_tournaments_name.clear();
               getOther_tournaments_host.clear();
               getOther_tournaments_id.clear();
               getOther_tournaments_status.clear();
                for(DocumentSnapshot Snapshot:value)
                {

                        allusers.add(Snapshot.getId());

                }
                if(allusers.contains(ID))
                {
                    allusers.remove(ID);
                }

                for(Object i:allusers) {

                    firebaseFirestore.collection("Sports Tournaments").document(i.toString()).collection("My Tournaments")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                    for (DocumentSnapshot Snapshot : value) {

                                        String tname = Snapshot.getString("Tournament Name");
                                        String thost = Snapshot.getString("Tournament Host");
                                        String tend = Snapshot.getString("Ending Date");
                                        String tstart = Snapshot.getString("Starting Date");
                                        String id = Snapshot.getId();

                                        try
                                        {
                                            start = dateFormat.parse(tstart);
                                        }
                                        catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }

                                        try
                                        {
                                            end = dateFormat.parse(tend);
                                        }
                                        catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }

                                        String previousDate = dateFormat.format(start.getTime() - MILLIS_IN_DAY);

                                        try
                                        {
                                            datePreviousDate = dateFormat.parse(previousDate);

                                        }
                                        catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }

                                        if(current.after(end))
                                        {
                                            getOther_tournaments_status.add("Over");

                                        }
                                        else if(current.after(datePreviousDate))
                                        {
                                            getOther_tournaments_status.add("Live");

                                        }else {
                                            getOther_tournaments_status.add("Upcoming");

                                        }

                                        other_tournaments_name.add(tname);
                                        getOther_tournaments_host.add(thost);
                                        getOther_tournaments_id.add(id);

                                    }
                                  //  Log.d(TAG, "document" + other_tournaments.size());
                                    ArrayAdapter<String> adapter = new Tournament_list_adapter(getActivity(),
                                            other_tournaments_name, getOther_tournaments_host,getOther_tournaments_id,getOther_tournaments_status);
                                    adapter.notifyDataSetChanged();
                                    listView.setAdapter(adapter);


                                }
                            });

                    ////////////////////////////////////////////////////////////////////

                }


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getActivity(),"mm "+listView.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
               TournamentViewFragment fragment=new TournamentViewFragment(listView.getItemAtPosition(position),admin);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("Back");
                transaction.commit();
            }
        });

        listView.cancelLongPress();

        return root;
    }
}