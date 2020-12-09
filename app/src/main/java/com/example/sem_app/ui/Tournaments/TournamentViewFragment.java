package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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


public class TournamentViewFragment extends Fragment {
    TextView Tname,Thost,Tstart,Tend,TMname,TMnumber;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private String TAG;
    public String documentid="";
    private String tournamentname;
    String selectsports;
    Button button;
    Map map;
    ArrayList allusers = new ArrayList();
    Boolean isAdmin;



    public TournamentViewFragment()
    {

    }


    public TournamentViewFragment(Object tname,boolean a) {

        tournamentname=tname.toString();
        isAdmin=a;
    }

   /* /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TournamentViewFragment.
     */
    // TODO: Rename and change types and number of parameters
  /*  public static TournamentViewFragment newInstance(String param1, String param2) {
        TournamentViewFragment fragment = new TournamentViewFragment();
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
    } */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_tournament_view, container, false);
        Tname = root.findViewById(R.id.tournament_name_text);
        Thost = root.findViewById(R.id.host_text);
        Tstart = root.findViewById(R.id.start_date_text);
        Tend = root.findViewById(R.id.end_date_text);
        TMname = root.findViewById(R.id.manager_name_text);
        TMnumber = root.findViewById(R.id.manager_phone_text);
        button = root.findViewById(R.id.see_event_button);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String ID = firebaseUser.getUid();

        CollectionReference colref=firebaseFirestore.collection("Users");
        colref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allusers.clear();

                for(DocumentSnapshot Snapshot:value)
                {

                    allusers.add(Snapshot.getId());

                }
              /*  if(allusers.contains(ID))
                {
                    allusers.remove(ID);
                }*/

                for(Object i:allusers) {

                   CollectionReference collref = firebaseFirestore.collection("Sports Tournaments").document(i.toString()).collection("My Tournaments");
                    Query query=collref.whereEqualTo("Tournament Name", tournamentname);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    map=document.getData();
                                    String name = map.get("Tournament Name").toString();

                                    Tname.setText(name);

                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }
                    });
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    map=document.getData();

                                    String mname = map.get("Tournament Manager Name").toString();

                                    TMname.setText(mname);

                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }


                    });
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    map=document.getData();

                                    String mnumber = map.get("Tournament Manager Phone Number").toString();

                                    TMnumber.setText(mnumber);

                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }


                    });
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    map=document.getData();

                                    String host = map.get("Tournament Host").toString();

                                    Thost.setText(host);

                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }


                    });
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    map=document.getData();

                                    String sdate = map.get("Starting Date").toString();

                                    Tstart.setText(sdate);
                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }


                    });
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    map=document.getData();

                                    String edate = map.get("Ending Date").toString();

                                    Tend.setText(edate);

                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }


                    });



                    ////////////////////////////////////////////////////////////////////

                }


            }
        });



        //////////////////////////////////////////////////////////////////////////////////////////////////




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TournamentEventsFragment fragment=new TournamentEventsFragment(tournamentname,isAdmin);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();

            }
        });






        return root;
    }
}