package com.example.sem_app.ui.Tournaments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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


public class EventRulesFragment extends Fragment {
    String tournamentid, sportname, TAG;
    TextView tname, sname,firstIn,secondIn;
    ListView listView;
    FloatingActionButton fab_rule;
    ArrayList rules = new ArrayList();
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList allusers = new ArrayList();
    ArrayAdapter<String> adapter;
    String id,ID;
    Boolean isAdmin,isOver;
    private EditText addNewRule;
    EventRulesViewModel eventRulesViewModel;


    public EventRulesFragment(String tid, String sn,Boolean a,Boolean b) {
        tournamentid = tid;
        sportname = sn;
        isAdmin=a;
        isOver=b;

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_event_rules, container, false);

        tname = root.findViewById(R.id.tournament_name);
        sname = root.findViewById(R.id.sport_name);
        fab_rule = root.findViewById(R.id.add_rules);
        listView = root.findViewById(R.id.rules_list);

        adapter = new EventRules_list_adapter(getActivity(),rules);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        ID = firebaseUser.getUid();

        CollectionReference colref = firebaseFirestore.collection("Users");
        colref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allusers.clear();

                for (DocumentSnapshot Snapshot : value) {

                    allusers.add(Snapshot.getId());

                }
                for (Object i : allusers) {

                    FirebaseFirestore.getInstance().collection(sportname).document(tournamentid)
                            .collection("rules")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            rules.clear();
                            String basic_rule="All Official Rules of "+sportname+" are Applicable.";

                            rules.add(basic_rule);

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    rules.add(document.get("Rule").toString());
                                }
                                adapter.notifyDataSetChanged();
                                }
                        }
                    });

                }
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////////////

        CollectionReference drawref = firebaseFirestore.collection(sportname).document(tournamentid).collection("Round1");

        drawref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {

                        if((isAdmin)&&(!isOver))
                        {
                            fab_rule.setVisibility(View.VISIBLE);

                        }

                    }
                }
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(!isOver) {
                if (isAdmin) {
                    drawref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {

                                    if (position > 0) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                        builder.setTitle("Remove This Rule?");

                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String current_rule = (String) listView.getItemAtPosition(position);
                                                CollectionReference ruleref1 = firebaseFirestore.collection(sportname).document(tournamentid).collection("rules");
                                                Query query1 = ruleref1.whereEqualTo("Rule", current_rule);
                                                query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                String rule_id = document.getId();
                                                                DocumentReference docref = firebaseFirestore.collection(sportname).document(tournamentid).collection("rules").document(rule_id);
                                                                docref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        rules.remove(listView.getItemAtPosition(position));
                                                                        adapter.notifyDataSetChanged();
                                                                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                            }
                                                        }


                                                    }
                                                });

                                                /////////////////////////////////////////////////////////////////////////////////////////


                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                            }
                                        });


                                        builder.create();
                                        builder.show();

                                    } else {
                                        Toast.makeText(getActivity(), "Built In Rule,Cannot be Deleted!", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }


                        }
                    });

                }


            }
                return true;
        }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////

       fab_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                final View rule_instructions = getLayoutInflater().inflate(R.layout.rules_instructions, null);
                firstIn = rule_instructions.findViewById(R.id.first_instruction);
                secondIn = rule_instructions.findViewById(R.id.second_instruction);

                String add = "1)You Can Write your own Choice Rule and Add it to the List as well.";
                String delete = "2)You can Delete Newly Added Rule by long Pressed on That Rule.";

                firstIn.setText(add);
                secondIn.setText(delete);

                builder1.setView(rule_instructions);
                builder1.setTitle("Instructions");
                builder1.setIcon(R.drawable.ic_baseline_info_24);

                builder1.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog.Builder newbuilder = new AlertDialog.Builder(getActivity());

                        final View addRule = getLayoutInflater().inflate(R.layout.add_event_rule, null);
                        addNewRule = addRule.findViewById(R.id.add_rule_edittext);

                        newbuilder.setView(addRule);
                        newbuilder.setTitle("New Rule");

                        newbuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String r = addNewRule.getText().toString();

                                if (TextUtils.isEmpty(r)) {
                                    Toast.makeText(getActivity(), "Empty Rule!", Toast.LENGTH_SHORT).show();
                                } else {


                                    DocumentReference doc = firebaseFirestore.collection(sportname).document(tournamentid);


                                    Map<String, Object> newRule = new HashMap<>();
                                    newRule.put("Rule", r);

                                    doc.collection("rules").add(newRule).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {

                                            if (task.isSuccessful()) {


                                                rules.add(r);
                                                adapter.notifyDataSetChanged();

                                                Toast.makeText(getActivity(), "New Rule Added Successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();


                                                Log.d(TAG, "error!");
                                            }

                                        }
                                    });


                                }
                            }
                        });

                        newbuilder.create();
                        newbuilder.show();


                    }


                });

                builder1.create();
                builder1.show();

                ///////////////////////////////////////////////////////////////////////////////////////

                ///////////////////////////////////////////////////////////////////////////////////////////////
            }
        });
        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventRulesViewModel = new ViewModelProvider(this).get(EventRulesViewModel.class);
        sname.setText(sportname);
        eventRulesViewModel.getEventDetails(tournamentid,isAdmin).observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                if(!stringObjectHashMap.isEmpty()) {

                    tname.setText(stringObjectHashMap.get("Tournament Name").toString());

                }

            }
        });

    }
}