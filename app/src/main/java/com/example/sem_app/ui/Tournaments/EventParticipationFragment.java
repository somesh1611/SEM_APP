package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EventParticipationFragment extends Fragment {

    String tournamentname,sportname,TAG;
    TextView tname,sname;
    ListView listView;
    Button join;
    FloatingActionButton fab_slot;
    ArrayList participant=new ArrayList();
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList allusers = new ArrayList();
    ArrayAdapter<String> adapter;
    String id;



    public EventParticipationFragment(String tn, String sn) {

        tournamentname=tn;
        sportname=sn;

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_event_participation, container, false);
        tname = root.findViewById(R.id.tournament_name);
        sname = root.findViewById(R.id.sport_name);
        join = root.findViewById(R.id.join);
        fab_slot= root.findViewById(R.id.create_slot);
        listView = root.findViewById(R.id.participant_list);

        tname.setText(tournamentname);
        sname.setText(sportname);

         adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, participant);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        firebaseFirestore=FirebaseFirestore.getInstance();


        /////////////////////////////////////////////////////////////////////////////////////////////

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
                    participant.clear();
                    CollectionReference collref = firebaseFirestore.collection("Sports Tournaments").document(i.toString()).collection("My Tournaments");
                    Query query=collref.whereEqualTo("Tournament Name", tournamentname);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    id = document.getId();

                                    CollectionReference partref = firebaseFirestore.collection(sportname).document(id).collection("participants");
                                    partref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            if (task.isSuccessful()) {

                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    Map map=document.getData();
                                                    participant.add(map.get("Name"));

                                                }
                                              //  ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                                                       // android.R.layout.simple_list_item_1, participant);
                                                adapter.notifyDataSetChanged();
                                              // listView.setAdapter(adapter);
                                            }else{
                                                Log.d(TAG,"error!");
                                            }

                                        }
                                    });

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

        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String ID = firebaseUser.getUid();

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference doc = firebaseFirestore.collection(sportname).document(id);

                DocumentReference acc_ref = firebaseFirestore.collection("Users").document(ID);
                acc_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            String name = documentSnapshot.getString("Name");
                            String year = documentSnapshot.getString("Year");
                            String branch = documentSnapshot.getString("Branch");

                            Map<String, Object> user = new HashMap<>();
                            user.put("Name", name);
                            user.put("Year", year);
                            user.put("Branch", branch);

                            doc.collection("participants").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                    if (task.isSuccessful()) {

                                        participant.add(name);
                                        adapter.notifyDataSetChanged();
                                        join.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Participation Confirmed!", Toast.LENGTH_SHORT).show();
                                    } else {

                                        Log.d(TAG, "error!");
                                    }

                                }
                            });
                        }
                    }
                });

            }
        });





        return root;
    }
}