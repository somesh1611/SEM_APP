package com.example.sem_app.ui.Tournaments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventResultFragment extends Fragment {

    TextView tname, sname,rname,match,score1,score2,team1,team2,team01,team02;
    EditText score01,score02;
    FloatingActionButton add_score;
    ArrayList allusers = new ArrayList();
    String tournamentname,sportname,roundname,pos,mroundname;
    String s1,id,docid,tid;
    int type;
    Boolean fwin;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String sy,sb,sy1,sb1;
    String TAG;
    Boolean isAdmin;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventResultFragment() {
        // Required empty public constructor
    }
    public EventResultFragment(String s,String tn,String sn,String mrn,String rn,Boolean a,int p) {

        s1=s;
        tournamentname = tn;
        sportname = sn;
        roundname=rn;
        mroundname=mrn;
        isAdmin=a;
        pos=String.valueOf(p+1);

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventResultFragment newInstance(String param1, String param2) {
        EventResultFragment fragment = new EventResultFragment();
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
       View root = inflater.inflate(R.layout.fragment_event_result, container, false);
        tname = root.findViewById(R.id.tName_text);
        sname = root.findViewById(R.id.sport_text);
        rname = root.findViewById(R.id.round_text);
        match = root.findViewById(R.id.match_text);
        score1 = root.findViewById(R.id.score1_text);
        score2 = root.findViewById(R.id.score2_text);
        team1 = root.findViewById(R.id.team1_text);
        team2 = root.findViewById(R.id.team2_text);
        add_score = root.findViewById(R.id.add_score);

        tname.setText(tournamentname);
        sname.setText(sportname);
        rname.setText(mroundname);
        match.setText("Match "+pos);
        //////////////////////////////////////////////////////////////////////////////////////////

        String[]p1=s1.split(",");
        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference acc_ref1=firebaseFirestore.collection("Users").document(p1[0]);
        DocumentReference acc_ref2=firebaseFirestore.collection("Users").document(p1[1]);
        acc_ref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    String name=documentSnapshot.getString("Name");
                    String year=documentSnapshot.getString("Year");
                    String branch=documentSnapshot.getString("Branch");

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
                    team1.setText(sy1+" "+sb1);



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

        if(p1[1].contentEquals("BYE"))
        {
            team2.setText(p1[1]);
        }else
        { acc_ref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    String name=documentSnapshot.getString("Name");
                    String year=documentSnapshot.getString("Year");
                    String branch=documentSnapshot.getString("Branch");

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

                    team2.setText(sy1 + " " + sb1);




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
        }
        ///////////////////////////////////////////////////////////////////////////////////////////

        CollectionReference acolref = firebaseFirestore.collection("Users");
        acolref.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                       @Override
                                       public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                           allusers.clear();

                                           for (DocumentSnapshot Snapshot : value) {

                                               allusers.add(Snapshot.getId());

                                           }
                                           for (Object i : allusers) {

                                               CollectionReference acollref = firebaseFirestore.collection("Sports Tournaments").document(i.toString()).collection("My Tournaments");
                                               Query rquery = acollref.whereEqualTo("Tournament Name", tournamentname);
                                               rquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                       if (task.isSuccessful()) {

                                                           for (QueryDocumentSnapshot document : task.getResult()) {

                                                               tid = document.getId();


                                                           CollectionReference partref = firebaseFirestore.collection(sportname).document(tid).collection(roundname);
                                                           Query q1=partref.whereEqualTo("Player1",p1[0]);
                                                              q1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                       if (task.isSuccessful()) {

                                                                           for (QueryDocumentSnapshot document : task.getResult()) {

                                                                               Map map = document.getData();
                                                                               if(map.get("Score1")==null)
                                                                               {
                                                                                   Toast.makeText(getActivity(),"Scores yet to be display!",Toast.LENGTH_SHORT).show();
                                                                               }else{
                                                                               String x1 = map.get("Score1").toString();
                                                                               String x2 = map.get("Score2").toString();
                                                                               add_score.setVisibility(View.GONE);
                                                                                   score1.setText(x1);
                                                                                   score2.setText(x2);
                                                                               }

                                                                           }

                                                                       }
                                                                   }
                                                               });

                                                           }

                                                       }
                                                   }
                                               });
                                           }
                                       }
                                   });



        //////////////////////////////////////////////////////////////////////////////////////////////
        if(!isAdmin||p1[1].contentEquals("BYE"))
        {
            add_score.setVisibility(View.GONE);
        }else{
            add_score.setVisibility(View.VISIBLE);
        }
        add_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Select Type of Score");

                builder.setSingleChoiceItems(R.array.score_type, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                Toast.makeText(getActivity(), "Points", Toast.LENGTH_SHORT).show();
                                type=1;
                                break;
                            case 1:
                                Toast.makeText(getActivity(), "Sets", Toast.LENGTH_SHORT).show();
                                type=2;
                                break;
                            default:
                                break;
                        }
                    }

                });
                builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (type) {
                            case 1:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                final View points_score = getLayoutInflater().inflate(R.layout.add_score_points, null);
                                team01 = points_score.findViewById(R.id.team1_name);
                                team02= points_score.findViewById(R.id.team2_name);
                                score01= points_score.findViewById(R.id.team1_score);
                                score02= points_score.findViewById(R.id.team2_score);
                                team01.setText(team1.getText()+": ");
                                team02.setText(team2.getText()+": ");
                                builder1.setView(points_score);
                                builder1.setTitle("Add Score");
                                builder1.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String sc1 = score01.getText().toString();
                                        String sc2 = score02.getText().toString();

                                        if (TextUtils.isEmpty(sc1) || TextUtils.isEmpty(sc2)) {
                                            Toast.makeText(getActivity(), "Please Enter the Score!", Toast.LENGTH_SHORT).show();
                                        } else {


                                            if (Integer.valueOf(sc1) > Integer.valueOf(sc2)) {
                                                fwin = true;
                                            } else {
                                                fwin = false;
                                            }

                                            firebaseAuth = FirebaseAuth.getInstance();
                                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


                                            String ID = firebaseUser.getUid();
                                            firebaseFirestore = FirebaseFirestore.getInstance();

                                            CollectionReference collref = firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments");
                                            Query query = collref.whereEqualTo("Tournament Name", tournamentname);
                                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                    if (task.isSuccessful()) {

                                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                                            id = document.getId();
                                                        }
                                                        CollectionReference scolref = firebaseFirestore.collection(sportname).document(id).collection(roundname);
                                                        String[] a = s1.split(",");
                                                        Query q = scolref.whereEqualTo("Player1", a[0]);
                                                        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                if (task.isSuccessful()) {

                                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                                        docid = document.getId();
                                                                    }
                                                                    DocumentReference docref = firebaseFirestore.collection(sportname).document(id).collection(roundname).document(docid);
                                                                    Map<String, Object> score = new HashMap<>();
                                                                    score.put("Score1", sc1);
                                                                    score.put("Score2", sc2);
                                                                    if (fwin) {
                                                                        score.put("Winner", a[0]);
                                                                    } else {
                                                                        score.put("Winner", a[1]);
                                                                    }
                                                                    docref.update(score).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(getActivity(), "Score Added Successfully", Toast.LENGTH_SHORT).show();
                                                                            add_score.setVisibility(View.GONE);
                                                                            score1.setText(score01.getText());
                                                                            score2.setText(score02.getText());
                                                                        }
                                                                    });
                                                                }


                                                            }
                                                        });


                                                    }

                                                }
                                            });
                                            //////////////////////////////////////////////////////


                                        }
                                    }
                                });
                                builder1.create();
                                builder1.show();
                                break;
                            case 2:
                                break;
                        }



                    }
                });
                //////////////////////////////////////////////////////////////////////
                builder.create();
                builder.show();

            }
        });
        return root;
    }
}