package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sem_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyTournamentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTournamentsFragment extends Fragment {

    ListView listView;
    ArrayList<String> my_tournaments_name =new ArrayList<>();
    ArrayList<String> my_tournaments_host =new ArrayList<>();
    Boolean admin=true;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private String TAG;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyTournamentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyTournamentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyTournamentsFragment newInstance(String param1, String param2) {
        MyTournamentsFragment fragment = new MyTournamentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_my_tournaments, container, false);

        listView = root.findViewById(R.id.my_tournaments_list);
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.tournament_list_header, listView, false);
        TextView list_header=header.findViewById(R.id.tournament_list_header_textview);
        list_header.setText("MY TOURNAMENTS");
        listView.addHeaderView(header,null,false);




        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String ID = firebaseUser.getUid();


        firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        my_tournaments_name.clear();
                        my_tournaments_host.clear();
                        for(DocumentSnapshot Snapshot:value)
                        {
                            String tname = Snapshot.getString("Tournament Name");
                            String thost = Snapshot.getString("Tournament Host");

                            //String tsdate=(Snapshot.getString("Starting Date"));

                            my_tournaments_name.add(tname);
                            my_tournaments_host.add(thost);

                            //my_tournaments.add(Snapshot.getString("Starting Date"));

                        }
                       // Log.d(TAG, "document"+ my_tournaments.size());
                        ArrayAdapter<String> adapter = new Tournament_list_adapter(getActivity(),
                                my_tournaments_name, my_tournaments_host);

                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);


                    }
                });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TournamentViewFragment fragment=new TournamentViewFragment(listView.getItemAtPosition(position),admin);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("Back");
                transaction.commit();
            }
        });

        return root;
    }

}