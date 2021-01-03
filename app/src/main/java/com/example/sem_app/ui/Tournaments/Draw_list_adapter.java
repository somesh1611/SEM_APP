package com.example.sem_app.ui.Tournaments;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Draw_list_adapter extends ArrayAdapter {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList allusers1=new ArrayList();
    String tournamentid,sportname,roundname;
    String sy,sb,sy1,sb1;
    String TAG,docid1;
    Boolean isTeam;

    private final ArrayList d;





    Activity context;



    public Draw_list_adapter(@NonNull Activity context, ArrayList p1,String tid,String s,String r,Boolean c) {
        super(context, R.layout.draw_list_item,p1);

        this.context=context;

        this.d =p1;
        this.tournamentid=tid;
        this.sportname=s;
        this.roundname=r;
        this.isTeam=c;

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
           TextView player1Label=rowView.findViewById(R.id.player1_label);
           TextView player2Label=rowView.findViewById(R.id.player2_label);
           TextView player2 = (TextView) rowView.findViewById(R.id.player2);
           TextView score1 = (TextView) rowView.findViewById(R.id.score1);
           TextView score2 = (TextView) rowView.findViewById(R.id.score2);

           if(!isTeam)
           {
               player1Label.setText("Player 1:");
               player2Label.setText("Player 2:");

           }else{
               player1Label.setText("Team 1:");
               player2Label.setText("Team 2:");
           }

           player1.setText(p1[0]);
           player2.setText(p1[1]);

      //////////////////////////////////////////////////////////////////////////////////////////////
        firebaseFirestore = FirebaseFirestore.getInstance();
       CollectionReference drawref = firebaseFirestore.collection(sportname).document(tournamentid).collection(roundname);
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

        return rowView;

    }

}
