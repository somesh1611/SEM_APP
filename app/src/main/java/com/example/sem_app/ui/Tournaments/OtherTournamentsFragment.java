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

import java.util.ArrayList;


public class OtherTournamentsFragment extends Fragment {

    ListView listView;
    ArrayList<String> other_tournaments_name =new ArrayList<>();
    ArrayList<String> getOther_tournaments_host=new ArrayList<>();
    ArrayList<String> getOther_tournaments_id=new ArrayList<>();
    ArrayList allusers = new ArrayList();
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private String TAG;
    Boolean admin=false;



    public OtherTournamentsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_other_tournaments, container, false);
        listView = root.findViewById(R.id.other_tournaments_list);
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
                                        String id = Snapshot.getId();


                                        //  String tsdate=(Snapshot.getString("Starting Date"));

                                        other_tournaments_name.add(tname);
                                        getOther_tournaments_host.add(thost);
                                        getOther_tournaments_id.add(id);
                                        //other_tournaments.add(Snapshot.getString("Tournament Name"));

                                        //my_tournaments.add(Snapshot.getString("Starting Date"));

                                    }
                                  //  Log.d(TAG, "document" + other_tournaments.size());
                                    ArrayAdapter<String> adapter = new Tournament_list_adapter(getActivity(),
                                            other_tournaments_name, getOther_tournaments_host,getOther_tournaments_id);
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