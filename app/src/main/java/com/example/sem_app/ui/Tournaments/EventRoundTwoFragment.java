package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventRoundTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventRoundTwoFragment extends Fragment {

    String tournamentname, sportname,TAG,roundname,mround,preround;
    FloatingActionButton make_draw;
    Boolean isAdmin,isSlot;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList allusers = new ArrayList();
    ArrayList players1 = new ArrayList();
    ArrayList winners = new ArrayList();
    ArrayList draw=new ArrayList();
    ArrayList predraw=new ArrayList();
    ArrayList postdraw=new ArrayList();
    TextView round_name;
    ListView drawlist;
    ArrayAdapter adapter;
    TextView tname, sname;
    String id,id1,id2;
    String bye;
    int n;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventRoundTwoFragment() {
        // Required empty public constructor
    }

    public EventRoundTwoFragment(String tn, String sn,String r, Boolean a){

        tournamentname = tn;
        sportname = sn;
        roundname=r;
        isAdmin=a;


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventRoundTwoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventRoundTwoFragment newInstance(String param1, String param2) {
        EventRoundTwoFragment fragment = new EventRoundTwoFragment();
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
        View root=inflater.inflate(R.layout.fragment_event_round_two, container, false);
        round_name=root.findViewById(R.id.round_name_text);
        tname = root.findViewById(R.id.tournament_name);
        sname = root.findViewById(R.id.sport_name);
        make_draw=root.findViewById(R.id.make_draw_fab);


        drawlist=root.findViewById(R.id.drawlist);


        tname.setText(tournamentname);
        sname.setText(sportname);
        round_name.setText(roundname);

        switch (roundname)
        {
            case "Round2":
                preround="Round1";
                break;
            case "Round3":
                preround="Round2";
                break;
            case "Round4":
                preround="Round3";
                break;
            case "Round5":
                preround="Round4";
                break;
            case "Round6":
                preround="Round5";
                break;
            case "Round7":
                preround="Round6";
                break;
        }


        adapter=new Draw_list_adapter(getActivity(),draw,tournamentname,sportname,roundname);
        adapter.notifyDataSetChanged();
        drawlist.setAdapter(adapter);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String Id = firebaseUser.getUid();
        CollectionReference ref1 = firebaseFirestore.collection("Sports Tournaments").document(Id).collection("My Tournaments");
        Query query2 = ref1.whereEqualTo("Tournament Name", tournamentname);
        query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                winners.clear();
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        id2 = document.getId();
                        CollectionReference drawref = firebaseFirestore.collection(sportname).document(id2).collection(preround);
                        drawref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        Map map = document.getData();
                                        if(map.containsKey("Player1"))
                                        {
                                            players1.add(map.get("Player1").toString());
                                        }
                                        if(map.containsKey("Winner")) {
                                            winners.add(map.get("Winner").toString());
                                        }

                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////
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

                                                           CollectionReference dref = firebaseFirestore.collection(sportname).document(tid).collection(roundname);
                                                           dref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                   if (task.isSuccessful()) {
                                                                       draw.clear();

                                                                       if (!task.getResult().isEmpty()) {

                                                                           make_draw.setVisibility(View.GONE);

                                                                           for (QueryDocumentSnapshot document : task.getResult()) {

                                                                               Map map = document.getData();
                                                                               String p1= (String) map.get("Player1");
                                                                               String p2= (String) map.get("Player2");
                                                                               draw.add(p1+","+p2);

                                                                               //////////////////////////////////////////////////////////////////////////////////////////

                                                                           }
                                                                           adapter.notifyDataSetChanged();
                                                                           n=(draw.size())*2;
                                                                           Log.d(TAG,"de"+n);

                                                                           if(n>0&&n<=2)
                                                                           {
                                                                               round_name.setText("Final");
                                                                               mround="Final";

                                                                           }
                                                                           else if(n>2&&n<=4)
                                                                           {
                                                                               round_name.setText("Semi-Final");
                                                                               mround="Semi-Final";
                                                                           }
                                                                           else if(n>4&&n<=8)
                                                                           {
                                                                               round_name.setText("Quarter-Final");
                                                                               mround="Quarter-Final";
                                                                           }
                                                                           else {
                                                                               round_name.setText(roundname);
                                                                               mround=roundname;
                                                                           }


                                                                       }else {
                                                                           if((isAdmin)&&(winners.size()==players1.size()))
                                                                           {
                                                                               make_draw.setVisibility(View.VISIBLE);
                                                                           }

                                                                           Toast.makeText(getActivity(),"Draws Yet to be Displayed!",Toast.LENGTH_SHORT).show();

                                                                       }
                                                                   }


                                                               }
                                                           });
                                                           }


                                                       }/*end*/


                                                   }
                                               });
                                           }
                                       }
                                   });

        make_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth= FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String ID = firebaseUser.getUid();
                CollectionReference collref1 = firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments");
                Query query = collref1.whereEqualTo("Tournament Name", tournamentname);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        predraw.clear();
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {


                                id = document.getId();
                                CollectionReference drawref = firebaseFirestore.collection(sportname).document(id).collection(preround);
                                drawref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                Map map = document.getData();
                                                predraw.add(map.get("Winner").toString());

                                            }
                                            postdraw = DrawMaker(predraw);
                                            for (Object d : postdraw) {
                                                String p = d.toString();
                                                DocumentReference doc = firebaseFirestore.collection(sportname).document(id);
                                                String[] pp = p.split(",");
                                                Map<String, Object> draw = new HashMap<>();
                                                draw.put("Player1", pp[0]);
                                                draw.put("Player2", pp[1]);

                                                doc.collection(roundname).add(draw).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                                        if (task.isSuccessful()) {
                                                            // getFragmentManager().beginTransaction().detach(EventRoundTwoFragment.this).attach(EventRoundTwoFragment.this).commit();
                                                            Toast.makeText(getActivity(), roundname+"Draws Listed!", Toast.LENGTH_SHORT).show();
                                                            make_draw.setVisibility(View.GONE);
                                                            getFragmentManager().beginTransaction().detach(EventRoundTwoFragment.this).attach(EventRoundTwoFragment.this).commit();

                                                        } else {
                                                            Log.d(TAG, "error!");
                                                        }

                                                    }
                                                });
                                            }
                                            //////////////////////////////////////////////////////////////////////////////////////
                                        }
                                    }
                                });
                            }
                        }
                    }

                });
                ////////////////////////////////////////////////////////////////////////////////////////
            }
        });

        drawlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EventResultFragment fragment=new EventResultFragment(drawlist.getItemAtPosition(position).toString(),tournamentname,sportname,mround,roundname,isAdmin,position);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });
        return root;
    }

    public ArrayList DrawMaker(ArrayList p)
    {
        ArrayList draw =p;
        int var=0;

        if(draw.size() %2 !=0) {
            Random rd =new Random();
            int randomnumber1 = rd.nextInt(draw.size());
            bye= draw.get(randomnumber1).toString();
            draw.remove(randomnumber1);
            var=1;
        }
        ArrayList draw1 =  new ArrayList();

        while(draw.size()>0)
        {
            Random rd =new Random();
            int randomnumber = rd.nextInt(draw.size());
            draw1.add(draw.get(randomnumber));
            draw.remove(randomnumber);
        }
        ArrayList returner1;
        int j=0;
        if(var==1)
        {
            ArrayList returner =  new ArrayList();
            for (int k = 1; k < draw1.size()/2+1; k++) {
                returner.add(draw1.get(j).toString()+","+draw1.get(j+1).toString());
                j = j + 2;
            }
            returner.add(bye+","+"BYE");
            returner1=returner;

        }
        else
        {
            ArrayList<String> returner =  new ArrayList<String>();
            for (int k = 0; k < draw1.size()/2; k++) {
                returner.add(draw1.get(j).toString()+","+draw1.get(j+1).toString());
                j = j + 2;
            }
            returner1=returner;
        }

        return returner1;
    }

}