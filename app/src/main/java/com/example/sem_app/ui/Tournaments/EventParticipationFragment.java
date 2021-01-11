package com.example.sem_app.ui.Tournaments;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

    String tournamentid, sportname, TAG;
    TextView tname, sname;
    ListView listView;
    Button join;
    FloatingActionButton fab_slot,fab_slot_view;
    ArrayList participant = new ArrayList();
    ArrayList participant_id = new ArrayList();
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList allusers = new ArrayList();
    ArrayList allusers1 = new ArrayList();
    ArrayAdapter<String> adapter;
    String id,tid,tid1;
    int type;
    String sy,sb, sy1,sb1;
    Boolean isAdmin,isTeam,isOver;
    EventParticipationViewModel eventParticipationViewModel;



    public EventParticipationFragment(String tid, String sn,Boolean a,Boolean b,Boolean c) {

        tournamentid = tid;
        sportname = sn;
        isAdmin=a;
        isTeam=b;
        isOver=c;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_event_participation, container, false);
        tname = root.findViewById(R.id.tournament_name);
        sname = root.findViewById(R.id.sport_name);
        join = root.findViewById(R.id.join);
        fab_slot = root.findViewById(R.id.create_slot);
        fab_slot_view = root.findViewById(R.id.view_slot);
        listView = root.findViewById(R.id.participant_list);



       // tname.setText(tournamentname);
       // sname.setText(sportname);

        adapter = new EventParticipants_list_adapter(getActivity(),
                 participant);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        firebaseFirestore = FirebaseFirestore.getInstance();


        /////////////////////////////////////////////////////////////////////////////////////////////

        CollectionReference colref = firebaseFirestore.collection("Users");
        colref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allusers.clear();

                for (DocumentSnapshot Snapshot : value) {

                    allusers.add(Snapshot.getId());

                }
              /*  if(allusers.contains(ID))
                {
                    allusers.remove(ID);
                }*/

                for (Object i : allusers) {


                    CollectionReference partref = firebaseFirestore.collection(sportname).document(tournamentid).collection("participants");
                    partref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            participant.clear();
                            participant_id.clear();

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Map map = document.getData();
                                    String n= (String) map.get("Name");
                                    String b= (String) map.get("Branch");
                                    String y= (String) map.get("Year");


                                                    switch (y){
                                                        case "First Year":
                                                            sy="FE";
                                                            break;
                                                        case "Second Year":
                                                            sy="SE";
                                                            break;
                                                        case "Third Year":
                                                            sy="TE";
                                                            break;
                                                        case "Fourth Year":
                                                            sy="BE";
                                                            break;
                                                        default:
                                                            break;
                                                    }

                                                    switch (b){
                                                        case "Computer Engineering":
                                                            sb="COMP";
                                                            break;
                                                        case "IT Engineering":
                                                            sb="IT";
                                                            break;
                                                        case "ENTC Engineering":
                                                            sb="ENTC";
                                                            break;
                                                        case "Civil Engineering":
                                                            sb="CIVIL";
                                                            break;
                                                        case "Mechanical Engineering":
                                                            sb="MECH";
                                                            break;
                                                        case "Electrical Engineering":
                                                            sb="ELE";
                                                            break;
                                                        case "Printing Engineering":
                                                            sb="PRINTING";
                                                            break;
                                                        default:
                                                            break;
                                                    }

                                                    participant.add(n+":"+sy+" "+sb);
                                                    participant_id.add(map.get("uid"));

                                }

                                                adapter.notifyDataSetChanged();

                                            } else {
                                                Log.d(TAG, "error!");
                                            }

                        }
                    });
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String ID = firebaseUser.getUid();


        ///////////////////////////////////////////////////////////////////////////////////////////////////

      listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(!isOver) {
                    if (isAdmin) {
                        firebaseFirestore.collection(sportname).document(tournamentid).collection("Round1")
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    if (task.getResult().isEmpty()) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                        builder.setTitle("Remove This Participant?");

                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // String current_part = (String) listView.getItemAtPosition(position);
                                                String pid = (String) participant_id.get(position);
                                                CollectionReference ruleref1 = firebaseFirestore.collection(sportname).document(tournamentid).collection("participants");
                                                Query query1 = ruleref1.whereEqualTo("uid", pid);
                                                query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                String part_id = document.getId();
                                                                DocumentReference docref = firebaseFirestore.collection(sportname).document(tournamentid).collection("participants").document(part_id);
                                                                docref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        participant_id.remove(participant_id.get(position));
                                                                        participant.remove(listView.getItemAtPosition(position));
                                                                        adapter.notifyDataSetChanged();
                                                                        Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                });

                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });

                                        builder.create();
                                        builder.show();

                                    }
                                }
                            }
                        });


                    }
                }

                return true;
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        if(isAdmin)
        {

                        CollectionReference dref = firebaseFirestore.collection(sportname).document(tournamentid).collection("Round1");
                        dref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    if(!task.getResult().isEmpty())
                                    {

                                        fab_slot_view.setVisibility(View.VISIBLE);

                                    }else {
                                        if(!isOver) {
                                            join.setVisibility(View.VISIBLE);
                                            fab_slot.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }


                                }
                        });


        }
        else{
            ///////////////////////////////////////////////////////////////////////////////////////////////
            CollectionReference drawref = firebaseFirestore.collection(sportname).document(tournamentid).collection("Round1");

            drawref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        if(!task.getResult().isEmpty())
                        {

                            fab_slot_view.setVisibility(View.VISIBLE);

                        }else {
                            if(!isOver) {

                                join.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });
        }

        ////////////////////////////////////////////////////////////////////////////////////////
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                DocumentReference acc_ref = firebaseFirestore.collection("Users").document(ID);
                acc_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            String name = documentSnapshot.getString("Name");
                            String year = documentSnapshot.getString("Year");
                            String branch = documentSnapshot.getString("Branch");

                            switch (year){
                                case "First Year":
                                    sy1="FE";
                                    break;
                                case "Second Year":
                                    sy1="SE";
                                    break;
                                case "Third Year":
                                    sy1="TE";
                                    break;
                                case "Fourth Year":
                                    sy1="BE";
                                    break;
                                default:
                                    break;
                            }

                            switch (branch){
                                case "Computer Engineering":
                                    sb1="COMP";
                                    break;
                                case "IT Engineering":
                                    sb1="IT";
                                    break;
                                case "ENTC Engineering":
                                    sb1="ENTC";
                                    break;
                                case "Civil Engineering":
                                    sb1="CIVIL";
                                    break;
                                case "Mechanical Engineering":
                                    sb1="MECH";
                                    break;
                                case "Electrical Engineering":
                                    sb1="ELE";
                                    break;
                                case "Printing Engineering":
                                    sb1="PRINTING";
                                    break;
                                default:
                                    break;
                            }



                            if(participant_id.contains(ID))
                            {
                                Toast.makeText(getActivity(), "Your Already Participated!", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {

                                Map<String, Object> user = new HashMap<>();
                                user.put("Name", name);
                                user.put("Year", year);
                                user.put("Branch", branch);
                                user.put("uid",ID);

                                DocumentReference doc = firebaseFirestore.collection(sportname).document(tournamentid);
                                doc.collection("participants").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        if (task.isSuccessful()) {

                                            participant.add(name+":"+sy1+" "+sb1);
                                            participant_id.add(ID);
                                            adapter.notifyDataSetChanged();

                                            Toast.makeText(getActivity(), "Participation Confirmed!", Toast.LENGTH_SHORT).show();
                                        } else {

                                            Log.d(TAG, "error!");
                                        }

                                    }
                                });
                            }
                            ////////////////////////////////////////////////////////////////////////
                        }
                    }
                });

            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////


         fab_slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Confirm Participant List")
                        .setMessage("Are you sure?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                builder.setTitle("Select Type of Event");

                                builder.setSingleChoiceItems(R.array.event_type, 2, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        switch (which) {
                                            case 0:
                                                Toast.makeText(getActivity(), "Knockout", Toast.LENGTH_SHORT).show();
                                                type=1;
                                                break;
                                            case 1:
                                                Toast.makeText(getActivity(), "Knockout + League", Toast.LENGTH_SHORT).show();
                                                type=2;
                                                break;
                                            default:
                                                break;
                                        }
                                    }


                                });
                               builder.setPositiveButton("Create Draw", new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        switch (type) {
                                            case 1:
                                                Boolean is_slot=false;
                                                EventKnockoutDrawFragment fragment = new EventKnockoutDrawFragment(tournamentid, sportname, isAdmin,is_slot, participant, participant_id,isTeam);
                                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                transaction.replace(R.id.nav_host_fragment, fragment);
                                                transaction.addToBackStack("back");
                                                transaction.commit();
                                                break;
                                            case 2:
                                                Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
                                                break;
                                            default:
                                                break;
                                        }



                                    }
                                });

                                builder.create();
                                builder.show();

                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

                /////////////////////////////////////////////////////////////////////////////////////////


            }


        });

        fab_slot_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean is_slot=true;
                EventKnockoutDrawFragment fragment = new EventKnockoutDrawFragment(tournamentid, sportname, isAdmin,is_slot,participant_id,isTeam);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                transaction.addToBackStack("back");
                transaction.commit();

            }
        });
        return root;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventParticipationViewModel= new ViewModelProvider(this).get(EventParticipationViewModel.class);
        sname.setText(sportname);
        eventParticipationViewModel.getEventDetails(tournamentid,isAdmin).observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                if(!stringObjectHashMap.isEmpty()) {

                    tname.setText(stringObjectHashMap.get("Tournament Name").toString());

                }

            }
        });

    }

}