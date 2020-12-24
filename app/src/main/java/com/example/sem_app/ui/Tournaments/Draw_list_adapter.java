package com.example.sem_app.ui.Tournaments;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class Draw_list_adapter extends ArrayAdapter {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList allusers1=new ArrayList();
    String tournamentname,sportname,roundname;
    String sy,sb,sy1,sb1;
    String TAG,id;

    private final ArrayList d;





    Activity context;



    public Draw_list_adapter(@NonNull Activity context, ArrayList p1,String t,String s,String r) {
        super(context, R.layout.draw_list_item,p1);

        this.context=context;

        this.d =p1;
        this.tournamentname=t;
        this.sportname=s;
        this.roundname=r;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.draw_list_item, null,true);

        TextView match = rowView.findViewById(R.id.match_number);
        int n=position+1;



           match.setText("Match "+n);

           String p=d.get(position).toString();
           String[]p1=p.split(",");

           TextView player1 = (TextView) rowView.findViewById(R.id.player1);
      //  player1.setText(p1.get(position).toString());

           TextView player2 = (TextView) rowView.findViewById(R.id.player2);
        //player2.setText(p2.get(position).toString());

          TextView score1 = (TextView) rowView.findViewById(R.id.score1);

          TextView score2 = (TextView) rowView.findViewById(R.id.score2);











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
                    player1.setText(sy1+" "+sb1);



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
          player2.setText(p1[1]);
          player1.setTextColor(Color.GREEN);
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

                        player2.setText(sy1 + " " + sb1);




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
      //////////////////////////////////////////////////////////////////////////////////////////////

        CollectionReference colref1 = firebaseFirestore.collection("Users");
        colref1.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            allusers1.clear();

                                            for (DocumentSnapshot Snapshot : value) {

                                                allusers1.add(Snapshot.getId());

                                            }

                                            for (Object i : allusers1) {
                                                CollectionReference collref1 = firebaseFirestore.collection("Sports Tournaments").document(i.toString()).collection("My Tournaments");
                                                Query query = collref1.whereEqualTo("Tournament Name", tournamentname);
                                                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                        if (task.isSuccessful()) {

                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                id = document.getId();

                                                               CollectionReference drawref = firebaseFirestore.collection(sportname).document(id).collection(roundname);
                                                               Query q2=drawref.whereEqualTo("Player1",p1[0]);
                                                                q2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                Map map = document.getData();
                                                                                if (map.containsKey("Winner")) {
                                                                                    String w = map.get("Winner").toString();
                                                                                    String s1 = map.get("Score1").toString();
                                                                                    String s2 = map.get("Score2").toString();


                                                                                    if (w.contentEquals(p1[0])) {
                                                                                        player1.setTextColor(Color.GREEN);
                                                                                        score1.setTextColor(Color.GREEN);
                                                                                    } else {
                                                                                        player2.setTextColor(Color.GREEN);
                                                                                        score2.setTextColor(Color.GREEN);
                                                                                    }
                                                                                    score1.setText(s1);
                                                                                    score2.setText(s2);

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


                                    //////////////////////////////////////////////////////////////////////////////////////////


        return rowView;

    }
}
