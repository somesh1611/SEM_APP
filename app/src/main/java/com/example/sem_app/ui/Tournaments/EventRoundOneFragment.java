package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventRoundOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventRoundOneFragment extends Fragment {

    String tournamentname, sportname,TAG,roundname,mround;
    Boolean isAdmin,isSlot,isTeam;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList allusers = new ArrayList();
    ArrayList draw=new ArrayList();
    TextView round_name;
    ListView drawlist;
    ArrayAdapter adapter;
    TextView tname, sname;
    String id;
    int n;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventRoundOneFragment() {
        // Required empty public constructor
    }

    public EventRoundOneFragment(String tn, String sn,String r, Boolean a,Boolean b,Boolean c){

        tournamentname = tn;
        sportname = sn;
        roundname=r;
        isAdmin=a;
        isSlot=b;
        isTeam=c;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventRoundOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventRoundOneFragment newInstance(String param1, String param2) {
        EventRoundOneFragment fragment = new EventRoundOneFragment();
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
        View root=inflater.inflate(R.layout.fragment_event_round_one, container, false);
        round_name=root.findViewById(R.id.round_name_text);
        tname = root.findViewById(R.id.tournament_name);
        sname = root.findViewById(R.id.sport_name);

        drawlist=root.findViewById(R.id.drawlist);


        tname.setText(tournamentname);
        sname.setText(sportname);

        adapter=new Draw_list_adapter(getActivity(),draw,tournamentname,sportname,roundname,isTeam);
        adapter.notifyDataSetChanged();
        drawlist.setAdapter(adapter);


        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        String ID = firebaseUser.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        /////////////////////////////////////////////////////////////////////////////////////////////////

        CollectionReference colref = firebaseFirestore.collection("Users");
        colref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allusers.clear();

                for (DocumentSnapshot Snapshot : value) {

                    allusers.add(Snapshot.getId());

                }

                for (Object i : allusers) {
                    CollectionReference collref = firebaseFirestore.collection("Sports Tournaments").document(i.toString()).collection("My Tournaments");
                    Query query = collref.whereEqualTo("Tournament Name", tournamentname);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            draw.clear();

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    id = document.getId();

                                    CollectionReference drawref = firebaseFirestore.collection(sportname).document(id).collection(roundname);
                                    drawref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            if (task.isSuccessful()) {

                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    Map map = document.getData();
                                                    String p1= (String) map.get("Player1");

                                                    String p2= (String) map.get("Player2");
                                                    draw.add(p1+","+p2);
                                                    //////////////////////////////////////////////////////////////////////////////////////////

                                                }
                                                adapter.notifyDataSetChanged();
                                                n=(draw.size())*2;
                                                Log.d(TAG,"de"+n);

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


                                            } else {
                                                Toast.makeText(getActivity(),"Draws Yet to be Displayed!",Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });

                                }


                            } else {

                                Log.d(TAG, "invalid");


                            }
                        }

                    });

                }
            }
        });

        drawlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EventResultFragment fragment=new EventResultFragment(drawlist.getItemAtPosition(position).toString(),tournamentname,sportname,mround,roundname,isAdmin,position,isTeam);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });

        return root;
    }

}