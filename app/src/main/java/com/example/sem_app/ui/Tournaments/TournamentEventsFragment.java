package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.Arrays;
import java.util.Map;


public class TournamentEventsFragment extends Fragment {

    ListView listview;
    String TAG;
    Button button1;
    //String[] arraySportsList={"Cricket","FootBall","VolleyBall","BasketBall","DodgeBall","Kabaddi","Kho-Kho","Badminton","Chess","Carrom"};
    String[] tournamentEvents;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String tournamentname;
    ArrayList selectsports = new ArrayList();
    String id;
    Map map;
    ArrayList allusers = new ArrayList();
    ArrayList events = new ArrayList();
    Boolean isAdmin;



    public TournamentEventsFragment(String tname,Boolean a) {

        tournamentname = tname;
        isAdmin=a;


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         View root = inflater.inflate(R.layout.fragment_tournament_events, container, false);
        listview = root.findViewById(R.id.tournament_events_listview);
      //  button1 = root.findViewById(R.id.participate_button);

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
                    events.clear();
                    CollectionReference collref = firebaseFirestore.collection("Sports Tournaments").document(i.toString()).collection("My Tournaments");
                    Query query=collref.whereEqualTo("Tournament Name", tournamentname);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    map=document.getData();
                                    String sports = map.get("Sports Included").toString();
                                   tournamentEvents = sports.split(",");
                                    events.addAll(Arrays.asList(tournamentEvents));

                                }
                                ArrayAdapter<String> adapter = new Event_list_adapter(getActivity(),
                                         events);
                                adapter.notifyDataSetChanged();
                                listview.setAdapter(adapter);
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }
                    });
                }


            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventViewFragment fragment=new EventViewFragment(tournamentname,listview.getItemAtPosition(position).toString(),isAdmin);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });

        return root;
    }
}