package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sem_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;


public class ParticipatedEventsFragment extends Fragment {

    ListView listView;
    String TAG;
    FloatingActionButton more;
    String tournamentName;
    ArrayAdapter adapter;
    ArrayList<String> participated_sports=new ArrayList<>();
   ArrayList<String> sports=new ArrayList();
    ArrayList not_participated_sports=new ArrayList();
    ArrayList allusers = new ArrayList();
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String id;
    String[]s1=new String[10];

   ParticipatedEventsFragment(ArrayList e,ArrayList sp,String tname)
   {
       sports.addAll(e);
       participated_sports.addAll(sp);
       tournamentName=tname;

   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root = inflater.inflate(R.layout.fragment_participated_events, container, false);
        listView = root.findViewById(R.id.participated_events_list);
        more = root.findViewById(R.id.add_more_participation);

         adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, participated_sports);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        for(Iterator<String>i=sports.iterator();i.hasNext();)
        {
            if(participated_sports.contains(i.next()))
            {
                i.remove();
            }
        }

        if(!sports.isEmpty()) {

           // sports.toArray(s1);
            for (int j = 0; j < sports.size(); j++) {
                s1[j] = sports.get(j);
            }
        }



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
            }
        });






        /*   s1= (String[]) sports.toArray(new String[sports.size()]); */

       /*    more.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {



                                    }
                                });
          AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Sports");
        not_participated_sports.clear();

                builder.setMultiChoiceItems(s1, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        String[]sports1=getResources().getStringArray(R.array.sportsList);

                        if(isChecked)
                        {
                            not_participated_sports.add(sports1[which]);
                        }
                        else if(not_participated_sports.contains(sports1[which])) {
                            not_participated_sports.remove(sports1[which]);
                        }

                    }


                });
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(not_participated_sports.isEmpty())
                        {
                            Toast toast = new Toast(getActivity());
                            toast.makeText(getActivity(),"Select Atleast One Event to Participate!",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        else{
                           /* for (Iterator i1 = not_participated_sports.iterator();i1.hasNext();)
                            {
                                if(participated_sports.contains(i1.hasNext()))
                                {
                                    not_participated_sports.remove(i1);
                                }
                            }*/

                          /*  for(Object i:not_participated_sports)
                            {
                                if(participated_sports.contains(i))
                                {
                                    not_participated_sports.remove(i);
                                }
                            }*/



                         /*   colref.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    allusers.clear();

                                    for (DocumentSnapshot Snapshot : value) {

                                        allusers.add(Snapshot.getId());

                                    }
                                    for (Object i1 : allusers) {
                                        //events.clear();
                                        CollectionReference collref = firebaseFirestore.collection("Sports Tournaments").document(i1.toString()).collection("My Tournaments");
                                        Query query = collref.whereEqualTo("Tournament Name", tournamentName);
                                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                if (task.isSuccessful()) {

                                                    for (QueryDocumentSnapshot document : task.getResult()) {


                                                        id = document.getId();


                                                        for (Object sport : not_participated_sports) {
                                                            DocumentReference doc = firebaseFirestore.collection(sport.toString()).document(id);

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
                                                                                  //  ParticipatedEventsFragment fragment = new ParticipatedEventsFragment(selectsports,tournamentname);
                                                                                    //FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                                    //transaction.replace(R.id.nav_host_fragment, fragment);
                                                                                   // transaction.addToBackStack("back");
                                                                                   // transaction.commit();
                                                                                    adapter.addAll(not_participated_sports);
                                                                                    Toast.makeText(getActivity(), "Participation Confirmed!", Toast.LENGTH_SHORT).show();
                                                                                } else {

                                                                                    Log.d(TAG, "error!");
                                                                                }

                                                                            }
                                                                        });


                                                                    } else {
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


                                                        }////////////

                                                    }


                                                } else {

                                                    Log.d(TAG, "invalid");


                                                }
                                            }
                                        });


                                        ////////////////////////////////////////////////////////////////////

                                    }


                                }

                            });


                        }


                //////////////////////////////////////////////////////////////////////////////////////////////////
                    }
                });
          builder.create();
        builder.show();


            }
        });*/


        return root;
    }
}