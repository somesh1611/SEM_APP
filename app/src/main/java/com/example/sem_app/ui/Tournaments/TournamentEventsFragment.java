package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TournamentEventsFragment extends Fragment {

    ListView listview;
    String TAG;
    int count;
    Button button1;
    String[] arraySportsList={"Cricket","FootBall","VolleyBall","BasketBall","DodgeBall","Kabaddi","Kho-Kho","Badminton","Chess","Carrom"};
    String[] tournamentEvents;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private String tournamentname;
    ArrayList selectsports = new ArrayList();
    String id;
    Map map;


    public TournamentEventsFragment(String tname) {

        tournamentname = tname;

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         View root = inflater.inflate(R.layout.fragment_tournament_events, container, false);
        listview = root.findViewById(R.id.tournament_events_listview);
        button1 = root.findViewById(R.id.participate_button);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String ID = firebaseUser.getUid();


        CollectionReference collref =firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments");
        Query query=collref.whereEqualTo("Tournament Name", tournamentname);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {


                        map=document.getData();

                        String sports = map.get("Sports Included").toString();
                        tournamentEvents = sports.split(",");




                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                            android.R.layout.simple_list_item_multiple_choice, tournamentEvents);
                    adapter.notifyDataSetChanged();
                    listview.setAdapter(adapter);


                } else {

                    Log.d(TAG,"invalid");


                }
            }


        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<listview.getCount();i++){
                    if(listview.isItemChecked(i)){

                       selectsports.add(listview.getItemAtPosition(i));

                    }

                }
                CollectionReference collref =firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments");
                Query query=collref.whereEqualTo("Tournament Name", tournamentname);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {


                               id=document.getId();

                            }

                            for(Object sport:selectsports)
                            {
                                DocumentReference doc = firebaseFirestore.collection(sport.toString()).document(id);

                                DocumentReference acc_ref=firebaseFirestore.collection("Users").document(ID);
                                acc_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists())
                                        {

                                           String name =documentSnapshot.getString("Name");
                                            String year=documentSnapshot.getString("Year");
                                           String branch=documentSnapshot.getString("Branch");

                                            Map<String,Object> user = new HashMap<>();
                                            user.put("Name",name);
                                            user.put("Year",year);
                                            user.put("Branch",branch);

                                            doc.collection("participants").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                                    if(task.isSuccessful())
                                                    {
                                                        ParticipatedEventsFragment fragment=new ParticipatedEventsFragment(selectsports);
                                                        FragmentTransaction transaction=getFragmentManager().beginTransaction();
                                                        transaction.replace(R.id.nav_host_fragment,fragment);
                                                        transaction.addToBackStack("back");
                                                        transaction.commit();
                                                        Toast.makeText(getActivity(),"Participation Confirmed!",Toast.LENGTH_SHORT).show();
                                                    }else{

                                                        Log.d(TAG,"error!");
                                                    }

                                                }
                                            });



                                        }else{
                                            Log.d(TAG, "document does not exist!");
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Failed to read data!");
                                    }
                                });

                                ////////////////////////////////////////////////


                            }


                        } else {

                            Log.d(TAG,"invalid");


                        }
                    }


                });








            }
        });




        return root;
    }
}