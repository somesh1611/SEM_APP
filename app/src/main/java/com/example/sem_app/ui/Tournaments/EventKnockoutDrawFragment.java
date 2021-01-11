package com.example.sem_app.ui.Tournaments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventKnockoutDrawFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventKnockoutDrawFragment extends Fragment {
    String tournamentid, sportname, TAG;
    Boolean isAdmin;
    ArrayList participant=new ArrayList();
    ArrayList participant_id=new ArrayList();
    Button b1,b2,b3,b4,b5,b6,b7;
    String sy,sb, sy1,sb1;
    int var;
    int count = 0;
    TextView tname, sname;
    String bye;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList<Integer> arrayList = new ArrayList<Integer>();
    ArrayList players_draw=new ArrayList();
    String docid1;
    String player1,player2;
    Boolean isSlot,isTeam;
    EventKnockoutDrawViewModel eventKnockoutDrawViewModel;
    int n;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventKnockoutDrawFragment() {
        // Required empty public constructor
    }
    public EventKnockoutDrawFragment(String tid, String sn1,Boolean b,Boolean c,ArrayList p1,Boolean d) {

        tournamentid = tid;
        sportname = sn1;
        isAdmin=b;
        isSlot=c;
        participant_id=p1;
        isTeam=d;

    }

    public EventKnockoutDrawFragment(String tid, String sn,Boolean a,Boolean c,ArrayList p,ArrayList p_id,Boolean d){

        tournamentid = tid;
        sportname = sn;
        isAdmin=a;
        isSlot=c;
        participant=p;
        participant_id=p_id;
        isTeam=d;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventKnockoutDrawFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventKnockoutDrawFragment newInstance(String param1, String param2) {
        EventKnockoutDrawFragment fragment = new EventKnockoutDrawFragment();
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
        View root = inflater.inflate(R.layout.fragment_event_knockout_draw, container, false);

        tname = root.findViewById(R.id.tournament_name);
        sname = root.findViewById(R.id.sport_name);
      // tname.setText(tournamentname);
       // sname.setText(sportname);
        b1 = root.findViewById(R.id.round1_button);
        b2 = root.findViewById(R.id.round2_button);
        b3 = root.findViewById(R.id.round3_button);
        b4 = root.findViewById(R.id.round4_button);
        b5 = root.findViewById(R.id.round5_button);
        b6 = root.findViewById(R.id.round6_button);
        b7 = root.findViewById(R.id.round7_button);

        b1.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        b3.setVisibility(View.GONE);
        b4.setVisibility(View.GONE);
        b5.setVisibility(View.GONE);
        b6.setVisibility(View.GONE);
        b7.setVisibility(View.GONE);

        arrayList.add(2);
        arrayList.add(4);
        arrayList.add(8);
        arrayList.add(16);
        arrayList.add(32);
        arrayList.add(64);
        arrayList.add(128);


        count=0;
        if (!participant_id.isEmpty()) {

            n = participant_id.size();
            int b = n;


            while (n > 0) {
                n = n / 2;
                count++;
            }

            if (arrayList.contains((b))) {
                count = count - 1;

            }

        } else {
            count = var;
        }

        switch (count) {
            case 1: {
                b1.setVisibility(View.VISIBLE);
                var = 1;

            }
            break;

            case 2: {
                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                var = 2;

            }
            break;

            case 3: {
                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
                var = 3;
            }
            break;

            case 4: {
                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
                b4.setVisibility(View.VISIBLE);
                var = 4;

            }
            break;

            case 5: {
                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
                b4.setVisibility(View.VISIBLE);
                b5.setVisibility(View.VISIBLE);
                var = 5;

            }
            break;

            case 6: {
                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
                b4.setVisibility(View.VISIBLE);
                b5.setVisibility(View.VISIBLE);
                b6.setVisibility(View.VISIBLE);
                var = 6;

            }
            break;

            case 7: {
                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
                b4.setVisibility(View.VISIBLE);
                b5.setVisibility(View.VISIBLE);
                b6.setVisibility(View.VISIBLE);
                b7.setVisibility(View.VISIBLE);
                var = 7;

            }
            break;
            default: {
                Toast.makeText(getActivity(), "Not a Single Participation!", Toast.LENGTH_SHORT).show();
            }
            break;
        }

        if(isSlot)
        {
           var=count;
        }else {


            firebaseFirestore = FirebaseFirestore.getInstance();

            players_draw = DrawMaker(participant);

            for (Object d : players_draw) {
                String p = d.toString();

                DocumentReference doc = firebaseFirestore.collection(sportname).document(tournamentid);
                String[] pp = p.split(",");
                Map<String, Object> draw = new HashMap<>();
                if(isTeam)
                {
                    String[]p1=pp[0].split(":");
                    draw.put("Player1", p1[1]);
                    if(!pp[1].contentEquals("BYE"))
                    {
                        String[]p2=pp[1].split(":");
                        draw.put("Player2", p2[1]);
                    }else {
                        draw.put("Player2", pp[1]);
                    }

                }else {

                    draw.put("Player1", pp[0]);
                    draw.put("Player2", pp[1]);
                }


                doc.collection("Round1").add(draw).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(getActivity(), "Round 1 Draws Listed!", Toast.LENGTH_SHORT).show();
                        } else {

                            Log.d(TAG, "error!");
                        }

                    }
                });
            }

        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String round="Round1";
                EventRoundOneFragment fragment=new EventRoundOneFragment(tournamentid,sportname,round,isAdmin,isSlot,isTeam,participant_id.size());
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String round="Round2";
                EventRoundTwoFragment fragment=new EventRoundTwoFragment(tournamentid,sportname,round,isAdmin,isTeam);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String round="Round3";
                EventRoundTwoFragment fragment=new EventRoundTwoFragment(tournamentid,sportname,round,isAdmin,isTeam);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String round="Round4";
                EventRoundTwoFragment fragment=new EventRoundTwoFragment(tournamentid,sportname,round,isAdmin,isTeam);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String round="Round5";
                EventRoundTwoFragment fragment=new EventRoundTwoFragment(tournamentid,sportname,round,isAdmin,isTeam);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String round="Round6";
                EventRoundTwoFragment fragment=new EventRoundTwoFragment(tournamentid,sportname,round,isAdmin,isTeam);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String round="Round7";
                EventRoundTwoFragment fragment=new EventRoundTwoFragment(tournamentid,sportname,round,isAdmin,isTeam);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventKnockoutDrawViewModel= new ViewModelProvider(this).get(EventKnockoutDrawViewModel.class);
        sname.setText(sportname);
        eventKnockoutDrawViewModel.getEventDetails(tournamentid,isAdmin).observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                if(!stringObjectHashMap.isEmpty()) {

                    tname.setText(stringObjectHashMap.get("Tournament Name").toString());
                }

            }
        });

    }

}