package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class OtherTournamentsFragment extends Fragment {

    ListView listView;
    ArrayList<String> other_tournaments =new ArrayList<>();

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private String TAG;



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



        CollectionReference colref=firebaseFirestore.collection("Sports Tournaments");
       colref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {

               if (task.isSuccessful()) {
                   firebaseAuth= FirebaseAuth.getInstance();
                   FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                   String ID = firebaseUser.getUid();
                   for (QueryDocumentSnapshot document : task.getResult()) {

                       String id=document.getId();
                       if(id!=ID)
                       {
                          // other_tournaments.add(document.getString("Tournament Name"));
                           firebaseFirestore.collection("Sports Tournaments").document(id).collection("My Tournaments")
                                   .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                       @Override
                                       public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                           other_tournaments.clear();
                                           for(DocumentSnapshot Snapshot:value)
                                           {
                                               other_tournaments.add(Snapshot.getString("Tournament Name"));



                                           }
                                           Log.d(TAG, "document"+ other_tournaments.size());
                                           ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                                                   android.R.layout.simple_list_item_1, other_tournaments);
                                           adapter.notifyDataSetChanged();
                                           listView.setAdapter(adapter);


                                       }
                                   });
                       }else {
                           Log.d(TAG, "error");
                       }
                      // Log.d(TAG, document.getId() + " => " + document.getData());
                   }

               } else {
                   Log.d(TAG, "Error getting documents: ", task.getException());
               }
           }
       });
       /* colref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot documentSnapshot:value)
                {
                    if(documentSnapshot.contains(ID))
                    {
                        Log.d(TAG,"this is your,proceed further!");
                    }else{
                        other_tournaments.add(documentSnapshot.getString("Tournament Name"));
                    }
                }

                Log.d(TAG, "document"+ other_tournaments.size());
                ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_list_item_1, other_tournaments);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

            }
        }); */
        return root;
    }
}