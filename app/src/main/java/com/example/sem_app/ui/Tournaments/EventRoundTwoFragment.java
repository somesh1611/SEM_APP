package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventRoundTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventRoundTwoFragment extends Fragment implements DrawRecyclerAdapter.OnDrawItemClickedListner{

    String tournamentid, sportname,TAG,roundname,mround,preround;
    FloatingActionButton make_draw;
    Boolean isAdmin,isSlot,isTeam;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList allusers = new ArrayList();
    ArrayList players1 = new ArrayList();
    ArrayList winners = new ArrayList();
    ArrayList draw=new ArrayList();
    ArrayList predraw=new ArrayList();
    ArrayList postdraw=new ArrayList();
    TextView round_name;
    ArrayAdapter adapter;
    TextView tname, sname;
    String id,id1,id2;
    String bye;
    ArrayList drawId;
    RecyclerView mDrawList;
    DrawRecyclerAdapter mDrawAdapter;
    EventRoundTwoViewModel eventRoundTwoViewModel;
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

    public EventRoundTwoFragment(String tid, String sn,String r, Boolean a,Boolean c){

        tournamentid = tid;
        sportname = sn;
        roundname=r;
        isAdmin=a;
        isTeam=c;
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
        mDrawList=root.findViewById(R.id.drawlist);

        mDrawList = root.findViewById(R.id.drawlist);
        mDrawList.setLayoutManager(new LinearLayoutManager(getContext()));
        mDrawList.setHasFixedSize(true);

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


        firebaseFirestore = FirebaseFirestore.getInstance();



       make_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CollectionReference drawref = firebaseFirestore.collection(sportname).document(tournamentid).collection(preround);
                drawref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        winners.clear();
                        players1.clear();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map map = document.getData();
                                if (map.containsKey("Player1")) {
                                    players1.add(map.get("Player1").toString());
                                }
                                if (map.containsKey("Winner")) {
                                    winners.add(map.get("Winner").toString());
                                }
                            }

                            if ((winners.size() == players1.size()) && (winners.size() >= 1)) {
                                postdraw = DrawMaker(winners);
                                for (Object d : postdraw) {
                                    String p = d.toString();
                                    DocumentReference doc = firebaseFirestore.collection(sportname).document(tournamentid);
                                    String[] pp = p.split(",");
                                    Map<String, Object> draw = new HashMap<>();
                                    draw.put("Player1", pp[0]);
                                    draw.put("Player2", pp[1]);

                                    doc.collection(roundname).add(draw).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {

                                            if (task.isSuccessful()) {
                                                // getFragmentManager().beginTransaction().detach(EventRoundTwoFragment.this).attach(EventRoundTwoFragment.this).commit();
                                                Toast.makeText(getActivity(), roundname + " Draws Listed!", Toast.LENGTH_SHORT).show();
                                                make_draw.setVisibility(View.GONE);
                                               getFragmentManager().beginTransaction().detach(EventRoundTwoFragment.this).attach(EventRoundTwoFragment.this).commit();

                                            } else {
                                                Log.d(TAG, "error!");
                                            }

                                        }
                                    });
                                }
                            }else {
                                Toast.makeText(getActivity(), preround+" Yet to be Completed!", Toast.LENGTH_SHORT).show();
                            }
                                            //////////////////////////////////////////////////////////////////////////////////////
                        }
                    }
                });

                ////////////////////////////////////////////////////////////////////////////////////////
            }
        });


        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventRoundTwoViewModel= new ViewModelProvider(this).get(EventRoundTwoViewModel.class);
        sname.setText(sportname);
        eventRoundTwoViewModel.getEventDetails(tournamentid,isAdmin).observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                if(!stringObjectHashMap.isEmpty()) {
                    tname.setText(stringObjectHashMap.get("Tournament Name").toString());

                }

            }
        });

        mDrawAdapter = new DrawRecyclerAdapter(getContext(),eventRoundTwoViewModel.getDrawsList(tournamentid,sportname,roundname).getValue(),this,isTeam);
        mDrawList.setAdapter(mDrawAdapter);
        eventRoundTwoViewModel.getDrawsList(tournamentid,sportname,roundname).observe(getViewLifecycleOwner(), new Observer<List<Draw>>() {
            @Override
            public void onChanged(List<Draw> draws) {
                if(draws != null)
                {
                    if(draws.size()==0)
                    {
                        make_draw.setVisibility(View.VISIBLE);
                    }
                    else if(draws.size()>0) {

                        mDrawAdapter.setData(draws);
                    }

                }
            }

        });

        eventRoundTwoViewModel.getDrawId(tournamentid,sportname,roundname).observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                drawId= (ArrayList) strings;
                if(drawId!=null) {
                    n = drawId.size();
                    if(n>0&&n<=1)
                    {
                        round_name.setText("Final");
                        mround="Final";
                    }
                    else if(n>1&&n<=2)
                    {
                        round_name.setText("Semi-Final");
                        mround="Semi-Final";
                    }
                    else if(n>2&&n<=4)
                    {
                        round_name.setText("Quarter-Final");
                        mround="Quarter-Final";
                    }
                    else {
                        round_name.setText(roundname);
                        mround=roundname;
                    }
                }
            }
        });

    }

    @Override
    public void OnItemClicked(int position) {
        EventResultFragment fragment = new EventResultFragment(drawId.get(position).toString(), tournamentid, sportname, mround, roundname, isAdmin, position, isTeam, isSlot);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack("back");
        transaction.commit();
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