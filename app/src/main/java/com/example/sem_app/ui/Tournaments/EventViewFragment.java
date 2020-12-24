package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Map;


public class EventViewFragment extends Fragment {
    String tournamentname,sportname;
    TextView tname,sname,wname;
    ArrayList allusers=new ArrayList();
    ArrayList arrayList=new ArrayList();
    String id;
    String wsy1,wsb1;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Button part,rule;
    Boolean isAdmin;



    public EventViewFragment(String tn, String sn,Boolean a) {
        tournamentname=tn;
        sportname=sn;
        isAdmin=a;

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root = inflater.inflate(R.layout.fragment_event_view, container, false);
       tname = root.findViewById(R.id.tournament_name_textview);
        sname = root.findViewById(R.id.sport_name_text);
        wname = root.findViewById(R.id.winner_name_text);
        part = root.findViewById(R.id.part);
       rule = root.findViewById(R.id.rule);
       tname.setText(tournamentname);
       sname.setText(sportname);

        arrayList.add(2);
        arrayList.add(4);
        arrayList.add(8);
        arrayList.add(16);
        arrayList.add(32);
        arrayList.add(64);
        arrayList.add(128);

        firebaseFirestore=FirebaseFirestore.getInstance();

       //////////////////////////////////////////////////////////////////////////////////////
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

                                                       if (task.isSuccessful()) {

                                                           for (QueryDocumentSnapshot document : task.getResult()) {

                                                               String tid = document.getId();

                                                               CollectionReference dref = firebaseFirestore.collection(sportname).document(tid).collection("Round1");
                                                               dref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                       if (task.isSuccessful()) {

                                                                           if(!task.getResult().isEmpty()) {

                                                                               int count=0;
                                                                               int n =(task.getResult().size())*2;
                                                                               int a=n;
                                                                               while (n > 0) {
                                                                                   n = n / 2;
                                                                                   count++;
                                                                               }

                                                                               if (arrayList.contains((a))) {
                                                                                   count = count - 1;
                                                                               }
                                                                               CollectionReference ref = firebaseFirestore.collection(sportname).document(tid).collection("Round"+count);
                                                                               ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                   @Override
                                                                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                                       if (task.isSuccessful()) {

                                                                                           for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                               Map map = document.getData();
                                                                                               if (map.containsKey("Winner")) {
                                                                                                   String w = map.get("Winner").toString();
                                                                                                   DocumentReference wdref = firebaseFirestore.collection("Users").document(w);
                                                                                                   wdref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                                       @Override
                                                                                                       public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                                           if (documentSnapshot.exists()) {
                                                                                                               String name = documentSnapshot.getString("Name");
                                                                                                               String wyear = documentSnapshot.getString("Year");
                                                                                                               String wbranch = documentSnapshot.getString("Branch");

                                                                                                               switch (wyear) {
                                                                                                                   case "First Year":
                                                                                                                       wsy1 = "FE";
                                                                                                                       break;
                                                                                                                   case "Second Year":
                                                                                                                       wsy1 = "SE";
                                                                                                                       break;
                                                                                                                   case "Third Year":
                                                                                                                       wsy1 = "TE";
                                                                                                                       break;
                                                                                                                   case "Fourth Year":
                                                                                                                       wsy1 = "BE";
                                                                                                                       break;
                                                                                                                   default:
                                                                                                                       break;
                                                                                                               }

                                                                                                               switch (wbranch) {
                                                                                                                   case "Computer Engineering":
                                                                                                                       wsb1 = "COMP";
                                                                                                                       break;
                                                                                                                   case "IT Engineering":
                                                                                                                       wsb1 = "IT";
                                                                                                                       break;
                                                                                                                   case "ENTC Engineering":
                                                                                                                       wsb1 = "ENTC";
                                                                                                                       break;
                                                                                                                   case "Civil Engineering":
                                                                                                                       wsb1 = "CIVIL";
                                                                                                                       break;
                                                                                                                   case "Mechanical Engineering":
                                                                                                                       wsb1 = "MECH";
                                                                                                                       break;
                                                                                                                   case "Electrical Engineering":
                                                                                                                       wsb1 = "ELE";
                                                                                                                       break;
                                                                                                                   case "Printing Engineering":
                                                                                                                       wsb1 = "PRINTING";
                                                                                                                       break;
                                                                                                                   default:
                                                                                                                       break;
                                                                                                               }
                                                                                                               wname.setVisibility(View.VISIBLE);
                                                                                                               wname.setTextColor(getResources().getColor(R.color.color_pink));
                                                                                                               wname.setText("Winner : " + wsy1 + " " + wsb1);

                                                                                                           }
                                                                                                       }
                                                                                                   });
                                                                                               }
                                                                                           }
                                                                                       }



                                                                                   }
                                                                               });


                                                                           }
                                                                       }else{

                                                                           wname.setVisibility(View.GONE);
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

                                                    /////////////////////////////////////////////////////////////////////////////////////////////



       part.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               EventParticipationFragment fragment=new EventParticipationFragment(tournamentname,sportname,isAdmin);
               FragmentTransaction transaction=getFragmentManager().beginTransaction();
               transaction.replace(R.id.nav_host_fragment,fragment);
               transaction.addToBackStack("back");
               transaction.commit();
           }
       });
       rule.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               EventRulesFragment fragment=new EventRulesFragment(tournamentname,sportname,isAdmin);
               FragmentTransaction transaction=getFragmentManager().beginTransaction();
               transaction.replace(R.id.nav_host_fragment,fragment);
               transaction.addToBackStack("back");
               transaction.commit();
           }
       });
        return root;
    }
}