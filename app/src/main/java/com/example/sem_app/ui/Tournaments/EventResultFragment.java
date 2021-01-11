package com.example.sem_app.ui.Tournaments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EventResultFragment extends Fragment {

    TextView tname, sname, rname, match, score1, score2, team1, team2, team01, team02, tie, iteam1, iteam2;
    TextView ws1t1, ws1t2, ws2t1, ws2t2, ws3t1, ws3t2, ws4t1, ws4t2, ws5t1, ws5t2;
    TextView steam1, steam2, set1, set2, set3, set4, set5;
    EditText t1s1, t1s2, t1s3, t1s4, t1s5, t2s1, t2s2, t2s3, t2s4, t2s5;
    EditText score01, score02;
    FloatingActionButton add_score, add_bye_result;
    String tournamentid, sportname, roundname, pos, mroundname;
    String s1, id, docid;
    String s1t1, s2t1, s3t1, s4t1, s5t1, s1t2, s2t2, s3t2, s4t2, s5t2;
    int type, stype;
    Boolean fwin;
    FirebaseFirestore firebaseFirestore;
    String TAG;
    Boolean isAdmin, isTeam, isSlot;


    EventResultViewModel eventResultViewModel;

    public EventResultFragment() {
        // Required empty public constructor
    }

    public EventResultFragment(String s, String tid, String sn, String mrn, String rn, Boolean a, int p, Boolean b, Boolean c) {

        s1 = s;
        tournamentid = tid;
        sportname = sn;
        roundname = rn;
        mroundname = mrn;
        isAdmin = a;
        pos = String.valueOf(p + 1);
        isTeam = b;
        isSlot = c;

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
        iteam1 = root.findViewById(R.id.team1_1_text);
        iteam2 = root.findViewById(R.id.team2_1_text);
        add_score = root.findViewById(R.id.add_score);
        add_bye_result = root.findViewById(R.id.add_bye_result);
        ws1t1 = root.findViewById(R.id.set1_1);
        ws2t1 = root.findViewById(R.id.set1_2);
        ws3t1 = root.findViewById(R.id.set1_3);
        ws4t1 = root.findViewById(R.id.set1_4);
        ws5t1 = root.findViewById(R.id.set1_5);
        ws1t2 = root.findViewById(R.id.set2_1);
        ws2t2 = root.findViewById(R.id.set2_2);
        ws3t2 = root.findViewById(R.id.set2_3);
        ws4t2 = root.findViewById(R.id.set2_4);
        ws5t2 = root.findViewById(R.id.set2_5);
        tie = root.findViewById(R.id.tie_breaker_text);


        firebaseFirestore = FirebaseFirestore.getInstance();


        ///////////////////////////////////////////////////////////////////////////////////////////

        add_bye_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setByeResult(s1);

            }
        });

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
                                type = 1;
                                break;
                            case 1:
                                Toast.makeText(getActivity(), "Sets", Toast.LENGTH_SHORT).show();
                                type = 2;
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
                                team02 = points_score.findViewById(R.id.team2_name);
                                score01 = points_score.findViewById(R.id.team1_score);
                                score02 = points_score.findViewById(R.id.team2_score);
                                team01.setText(team1.getText() + ": ");
                                team02.setText(team2.getText() + ": ");
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
                                            if (Integer.valueOf(sc1) == Integer.valueOf(sc2)) {
                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                                                builder2.setTitle("Tie Match");
                                                builder2.setMessage("Who won the Tie-Breaker?");
                                                builder2.setPositiveButton(team1.getText().toString(), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Map<String, Object> score = new HashMap<>();
                                                        score.put("Score1", sc1);
                                                        score.put("Score2", sc2);

                                                        if (isTeam) {
                                                            score.put("Tie", "Tie-Breaker Won by " + team1.getText().toString());
                                                            score.put("Winner", team1.getText().toString());
                                                        } else {
                                                            score.put("Tie", "Tie-Breaker Won by " + team1.getText().toString() + ":" + iteam1.getText().toString());
                                                            score.put("Winner", team1.getText().toString() + ":" + iteam1.getText().toString());
                                                        }


                                                        DocumentReference docref = firebaseFirestore.collection(sportname).document(tournamentid)
                                                                .collection(roundname).document(s1);


                                                        docref.update(score).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getActivity(), "Score Added Successfully", Toast.LENGTH_SHORT).show();
                                                                add_score.setVisibility(View.GONE);
                                                                score1.setText(score01.getText());
                                                                score2.setText(score02.getText());
                                                                tie.setText("Tie-Breaker Won by " + team1.getText().toString());
                                                            }
                                                        });


                                                        ///////////////////////////////////////////////////////////////////////


                                                    }
                                                }).setNegativeButton(team2.getText().toString(), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ///////////////////////////////////////////////////////////////////////////////////////

                                                        ///////////////////////////////////////////////////////////////////////
                                                        Map<String, Object> score = new HashMap<>();
                                                        score.put("Score1", sc1);
                                                        score.put("Score2", sc2);

                                                        if (isTeam) {
                                                            score.put("Tie", "Tie-Breaker Won by " + team2.getText().toString());
                                                            score.put("Winner", team2.getText().toString());
                                                        } else {
                                                            score.put("Tie", "Tie-Breaker Won by " + team2.getText().toString() + ":" + iteam2.getText().toString());
                                                            score.put("Winner", team2.getText().toString() + ":" + iteam2.getText().toString());
                                                        }


                                                        DocumentReference docref = firebaseFirestore.collection(sportname).document(tournamentid).collection(roundname).document(s1);


                                                        docref.update(score).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getActivity(), "Score Added Successfully", Toast.LENGTH_SHORT).show();
                                                                add_score.setVisibility(View.GONE);
                                                                score1.setText(score01.getText());
                                                                score2.setText(score02.getText());
                                                                tie.setText("Tie-Breaker Won by " + team2.getText().toString());
                                                            }
                                                        });


                                                    }
                                                });
                                                builder2.create();
                                                builder2.show();


                                            } else {

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                                if (Integer.valueOf(sc1) > Integer.valueOf(sc2)) {
                                                    fwin = true;
                                                } else {
                                                    fwin = false;
                                                }


                                                DocumentReference docref = firebaseFirestore.collection(sportname).document(tournamentid).collection(roundname).document(s1);
                                                Map<String, Object> score = new HashMap<>();
                                                score.put("Score1", sc1);
                                                score.put("Score2", sc2);

                                                if (fwin) {
                                                    if (isTeam) {
                                                        score.put("Winner", team1.getText().toString());
                                                    } else {
                                                        score.put("Winner", team1.getText().toString() + ":" + iteam1.getText().toString());
                                                    }
                                                } else {
                                                    if (isTeam) {
                                                        score.put("Winner", team2.getText().toString());
                                                    } else {
                                                        score.put("Winner", team2.getText().toString() + ":" + iteam2.getText().toString());
                                                    }
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


                                                ////////////////////////////////////////////////////////////////////////////////////////////////////////////


                                            }
                                        }
                                    }
                                });
                                builder1.create();
                                builder1.show();
                                break;
                            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
                            case 2:

                            AlertDialog.Builder sbuilder1 = new AlertDialog.Builder(getActivity());

                                sbuilder1.setTitle("Select Number of Sets Played");

                                sbuilder1.setSingleChoiceItems(R.array.Sets, 5, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        switch (which) {
                                            case 0:
                                                Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                                                stype=1;
                                                break;
                                            case 1:
                                                Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                                                stype=2;
                                                break;
                                            case 2:
                                                Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
                                                stype=3;
                                                break;
                                            case 3:
                                                Toast.makeText(getActivity(), "4", Toast.LENGTH_SHORT).show();
                                                stype=4;
                                                break;
                                            case 4:
                                                Toast.makeText(getActivity(), "5", Toast.LENGTH_SHORT).show();
                                                stype=5;
                                                break;
                                            default:
                                                break;
                                        }
                                    }

                                });
                                ////////////////////////////////////////////////////////////////////////

                                sbuilder1.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                AlertDialog.Builder sbuilder = new AlertDialog.Builder(getActivity());
                                final View sets_score = getLayoutInflater().inflate(R.layout.add_score_sets, null);
                                steam1 = sets_score.findViewById(R.id.steam1_name);
                                steam2 = sets_score.findViewById(R.id.steam2_name);
                                set1 = sets_score.findViewById(R.id.set1_textview);
                                set2 = sets_score.findViewById(R.id.set2_textview);
                                set3 = sets_score.findViewById(R.id.set3_textview);
                                set4 = sets_score.findViewById(R.id.set4_textview);
                                set5 = sets_score.findViewById(R.id.set5_textview);
                                t1s1 = sets_score.findViewById(R.id.set1Team1);
                                t1s2 = sets_score.findViewById(R.id.set2Team1);
                                t1s3 = sets_score.findViewById(R.id.set3Team1);
                                t1s4 = sets_score.findViewById(R.id.set4Team1);
                                t1s5 = sets_score.findViewById(R.id.set5Team1);

                                t2s1 = sets_score.findViewById(R.id.set1Team2);
                                t2s2 = sets_score.findViewById(R.id.set2Team2);
                                t2s3 = sets_score.findViewById(R.id.set3Team2);
                                t2s4 = sets_score.findViewById(R.id.set4Team2);
                                t2s5 = sets_score.findViewById(R.id.set5Team2);

                                steam1.setText(team1.getText()+": ");
                                steam2.setText(team2.getText()+": ");
                                sbuilder.setView(sets_score);
                                sbuilder.setTitle("Add Score");

                                switch (stype)
                                {
                                    case 1:
                                        set1.setVisibility(View.VISIBLE);
                                        t1s1.setVisibility(View.VISIBLE);
                                        t2s1.setVisibility(View.VISIBLE);
                                        break;
                                    case 2:
                                        set1.setVisibility(View.VISIBLE);
                                        t1s1.setVisibility(View.VISIBLE);
                                        t2s1.setVisibility(View.VISIBLE);
                                        set2.setVisibility(View.VISIBLE);
                                        t1s2.setVisibility(View.VISIBLE);
                                        t2s2.setVisibility(View.VISIBLE);
                                        break;
                                    case 3:
                                        set1.setVisibility(View.VISIBLE);
                                        t1s1.setVisibility(View.VISIBLE);
                                        t2s1.setVisibility(View.VISIBLE);
                                        set2.setVisibility(View.VISIBLE);
                                        t1s2.setVisibility(View.VISIBLE);
                                        t2s2.setVisibility(View.VISIBLE);
                                        set3.setVisibility(View.VISIBLE);
                                        t1s3.setVisibility(View.VISIBLE);
                                        t2s3.setVisibility(View.VISIBLE);
                                        break;
                                    case 4:
                                        set1.setVisibility(View.VISIBLE);
                                        t1s1.setVisibility(View.VISIBLE);
                                        t2s1.setVisibility(View.VISIBLE);
                                        set2.setVisibility(View.VISIBLE);
                                        t1s2.setVisibility(View.VISIBLE);
                                        t2s2.setVisibility(View.VISIBLE);
                                        set3.setVisibility(View.VISIBLE);
                                        t1s3.setVisibility(View.VISIBLE);
                                        t2s3.setVisibility(View.VISIBLE);
                                        set4.setVisibility(View.VISIBLE);
                                        t1s4.setVisibility(View.VISIBLE);
                                        t2s4.setVisibility(View.VISIBLE);
                                        break;
                                    case 5:
                                        set1.setVisibility(View.VISIBLE);
                                        t1s1.setVisibility(View.VISIBLE);
                                        t2s1.setVisibility(View.VISIBLE);
                                        set2.setVisibility(View.VISIBLE);
                                        t1s2.setVisibility(View.VISIBLE);
                                        t2s2.setVisibility(View.VISIBLE);
                                        set3.setVisibility(View.VISIBLE);
                                        t1s3.setVisibility(View.VISIBLE);
                                        t2s3.setVisibility(View.VISIBLE);
                                        set4.setVisibility(View.VISIBLE);
                                        t1s4.setVisibility(View.VISIBLE);
                                        t2s4.setVisibility(View.VISIBLE);
                                        set5.setVisibility(View.VISIBLE);
                                        t1s5.setVisibility(View.VISIBLE);
                                        t2s5.setVisibility(View.VISIBLE);
                                        break;
                                    default:
                                        break;
                                }


                                sbuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ////////////////////////////////////////////////////////////

                                        switch (stype)
                                        {
                                            case 1:
                                                s1t1=t1s1.getText().toString();
                                                s1t2=t2s1.getText().toString();

                                                if(TextUtils.isEmpty(s1t1)||TextUtils.isEmpty(s1t2))
                                                {
                                                    Toast.makeText(getActivity(),"Sets score Should not be Empty!",Toast.LENGTH_SHORT).show();
                                                }else{
                                                    setsScore(s1t1,s1t2,"-","-","-","-","-","-","-","-",s1);
                                                }
                                                break;
                                            case 2:
                                                s1t1=t1s1.getText().toString();
                                                s1t2=t2s1.getText().toString();
                                                s2t1=t1s2.getText().toString();
                                                s2t2=t2s2.getText().toString();
                                                if(TextUtils.isEmpty(s1t1)||TextUtils.isEmpty(s1t2)||TextUtils.isEmpty(s2t1)||TextUtils.isEmpty(s2t2))
                                                {
                                                    Toast.makeText(getActivity(),"Sets score Should not be Empty!",Toast.LENGTH_SHORT).show();

                                                }else{
                                                    setsScore(s1t1,s1t2,s2t1,s2t2,"-","-","-","-","-","-",s1);
                                                }


                                                break;
                                            case 3:
                                                s1t1=t1s1.getText().toString();
                                                s1t2=t2s1.getText().toString();
                                                s2t1=t1s2.getText().toString();
                                                s2t2=t2s2.getText().toString();
                                                s3t1=t1s3.getText().toString();
                                                s3t2=t2s3.getText().toString();
                                                if(TextUtils.isEmpty(s1t1)||TextUtils.isEmpty(s1t2)||TextUtils.isEmpty(s2t1)||TextUtils.isEmpty(s2t2)||TextUtils.isEmpty(s3t1)||TextUtils.isEmpty(s3t2))
                                                {
                                                    Toast.makeText(getActivity(),"Sets score Should not be Empty!",Toast.LENGTH_SHORT).show();

                                                }else{
                                                    setsScore(s1t1,s1t2,s2t1,s2t2,s3t1,s3t2,"-","-","-","-",s1);

                                                }

                                                break;
                                            case 4:
                                                s1t1=t1s1.getText().toString();
                                                s1t2=t2s1.getText().toString();
                                                s2t1=t1s2.getText().toString();
                                                s2t2=t2s2.getText().toString();
                                                s3t1=t1s3.getText().toString();
                                                s3t2=t2s3.getText().toString();
                                                s4t1=t1s4.getText().toString();
                                                s4t2=t2s4.getText().toString();
                                                if(TextUtils.isEmpty(s1t1)||TextUtils.isEmpty(s1t2)||TextUtils.isEmpty(s2t1)||TextUtils.isEmpty(s2t2)||TextUtils.isEmpty(s3t1)||TextUtils.isEmpty(s3t2)||TextUtils.isEmpty(s4t1)||TextUtils.isEmpty(s4t2))
                                                {
                                                    Toast.makeText(getActivity(),"Sets score Should not be Empty!",Toast.LENGTH_SHORT).show();

                                                }else{
                                                    setsScore(s1t1,s1t2,s2t1,s2t2,s3t1,s3t2,s4t1,s4t2,"-","-",s1);

                                                }
                                                break;
                                            case 5:
                                                s1t1=t1s1.getText().toString();
                                                s1t2=t2s1.getText().toString();
                                                s2t1=t1s2.getText().toString();
                                                s2t2=t2s2.getText().toString();
                                                s3t1=t1s3.getText().toString();
                                                s3t2=t2s3.getText().toString();
                                                s4t1=t1s4.getText().toString();
                                                s4t2=t2s4.getText().toString();
                                                s5t2=t2s5.getText().toString();
                                                s5t1=t1s5.getText().toString();
                                                if(TextUtils.isEmpty(s1t1)||TextUtils.isEmpty(s1t2)||TextUtils.isEmpty(s2t1)||TextUtils.isEmpty(s2t2)||TextUtils.isEmpty(s3t1)||TextUtils.isEmpty(s3t2)||TextUtils.isEmpty(s4t1)||TextUtils.isEmpty(s4t2)||TextUtils.isEmpty(s5t1)||TextUtils.isEmpty(s5t2))
                                                {
                                                    Toast.makeText(getActivity(),"Sets score Should not be Empty!",Toast.LENGTH_SHORT).show();

                                                }else{
                                                    setsScore(s1t1,s1t2,s2t1,s2t2,s3t1,s3t2,s4t1,s4t2,s5t1,s5t2,s1);
                                                }
                                                break;
                                            default:
                                                break;
                                        }

                                        /////////////////////////////////////////////////////////////
                                    }
                                });
                                sbuilder.create();
                                sbuilder.show();
                                    }
                                });
                                /////////////////////////////////////////////////////////////////////////////////
                                sbuilder1.create();
                                sbuilder1.show();
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

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventResultViewModel = new ViewModelProvider(this).get(EventResultViewModel.class);
        sname.setText(sportname);
        rname.setText(mroundname);
        match.setText("Match " + pos);


        eventResultViewModel.getEventDetails(tournamentid,isAdmin).observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                if (!stringObjectHashMap.isEmpty()) {

                    tname.setText(stringObjectHashMap.get("Tournament Name").toString());

                }

            }
        });


        eventResultViewModel.getResultDetails(tournamentid, sportname, roundname, s1).observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {

                if (stringObjectHashMap.containsKey("Player1")) {

                    if (isTeam) {
                        team1.setText(stringObjectHashMap.get("Player1").toString());
                        team2.setText(stringObjectHashMap.get("Player2").toString());
                    } else {
                        String[] ip1 = stringObjectHashMap.get("Player1").toString().split(":");
                        team1.setText(ip1[0]);
                        iteam1.setText(ip1[1]);
                        if (stringObjectHashMap.get("Player2").toString().contentEquals("BYE")) {
                            team2.setText(stringObjectHashMap.get("Player2").toString());
                        } else {
                            String[] ip2 = stringObjectHashMap.get("Player2").toString().split(":");
                            team2.setText(ip2[0]);
                            iteam2.setText(ip2[1]);
                        }
                    }
                }
                if (stringObjectHashMap.containsKey("Tie")) {

                    tie.setTextColor(Color.GREEN);
                    tie.setText(stringObjectHashMap.get("Tie").toString());

                    score1.setText(stringObjectHashMap.get("Score1").toString());
                    score2.setText(stringObjectHashMap.get("Score2").toString());

                } else if (stringObjectHashMap.containsKey("Winner")) {
                    if (stringObjectHashMap.get("Winner").toString().contentEquals(stringObjectHashMap.get("Player1").toString())) {
                        team1.setTextColor(Color.GREEN);
                        score1.setTextColor(Color.GREEN);
                    } else {
                        team2.setTextColor(Color.GREEN);
                        score2.setTextColor(Color.GREEN);
                    }

                    score1.setText(stringObjectHashMap.get("Score1").toString());
                    score2.setText(stringObjectHashMap.get("Score2").toString());


                    if (stringObjectHashMap.containsKey("SET1")) {
                        String z1 = stringObjectHashMap.get("SET1").toString();
                        String[] zz = z1.split("-");
                        if (Integer.valueOf(zz[0]) > Integer.valueOf(zz[1])) {

                            ws1t1.setText(z1);
                        } else {

                            ws1t2.setText(z1);
                        }
                    }
                    if (stringObjectHashMap.containsKey("SET2")) {
                        String z2 = stringObjectHashMap.get("SET2").toString();
                        String[] zz = z2.split("-");
                        if (Integer.valueOf(zz[0]) > Integer.valueOf(zz[1])) {

                            ws2t1.setText(z2);

                        } else {

                            ws2t2.setText(z2);
                        }
                    }
                    if (stringObjectHashMap.containsKey("SET3")) {
                        String z3 = stringObjectHashMap.get("SET3").toString();
                        String[] zz = z3.split("-");
                        if (Integer.valueOf(zz[0]) > Integer.valueOf(zz[1])) {

                            ws3t1.setText(z3);

                        } else {

                            ws3t2.setText(z3);

                        }
                    }
                    if (stringObjectHashMap.containsKey("SET4")) {
                        String z4 = stringObjectHashMap.get("SET4").toString();
                        String[] zz = z4.split("-");
                        if (Integer.valueOf(zz[0]) > Integer.valueOf(zz[1])) {

                            ws4t1.setText(z4);
                        } else {

                            ws4t2.setText(z4);

                        }
                    }
                    if (stringObjectHashMap.containsKey("SET5")) {
                        String z5 = stringObjectHashMap.get("SET5").toString();
                        String[] zz = z5.split("-");
                        if (Integer.valueOf(zz[0]) > Integer.valueOf(zz[1])) {
                            ws5t1.setText(z5);
                        } else {
                            ws5t2.setText(z5);
                        }
                    }
                    ////////////////////////////////////////////////////////////

                } else {
                    if (isAdmin) {
                        if (stringObjectHashMap.get("Player2").toString().contentEquals("BYE")) {
                            add_bye_result.setVisibility(View.VISIBLE);
                        } else {
                            add_score.setVisibility(View.VISIBLE);
                        }

                    }
                    Toast.makeText(getActivity(), "Scores Yet to be Displayed!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void setsScore(String s1t1Score, String s1t2Score, String s2t1Score, String s2t2Score, String s3t1Score, String s3t2Score, String s4t1Score, String s4t2Score, String s5t1Score, String s5t2Score, String p1) {

        String st11 = s1t1Score;
        String st12 = s1t2Score;
        String st21 = s2t1Score;
        String st22 = s2t2Score;
        String st31 = s3t1Score;
        String st32 = s3t2Score;
        String st41 = s4t1Score;
        String st42 = s4t2Score;
        String st51 = s5t1Score;
        String st52 = s5t2Score;
        String d1 = p1;


        int i1, i2;

        Map smap = new HashMap();

        i1 = i2 = 0;

        if (st11 != "-") {
            smap.put("SET1", st11 + "-" + st12);
            if (Integer.valueOf(st11) > Integer.valueOf(st12)) {
                i1++;
            } else {
                i2++;
            }

        }
        if (st21 != "-") {
            smap.put("SET2", st21 + "-" + st22);
            if (Integer.valueOf(st21) > Integer.valueOf(st22)) {
                i1++;
            } else {
                i2++;
            }

        }
        if (st31 != "-") {
            smap.put("SET3", st31 + "-" + st32);
            if (Integer.valueOf(st31) > Integer.valueOf(st32)) {
                i1++;
            } else {
                i2++;
            }

        }
        if (st41 != "-") {
            smap.put("SET4", st41 + "-" + st42);
            if (Integer.valueOf(st41) > Integer.valueOf(st42)) {
                i1++;
            } else {
                i2++;
            }

        }
        if (st51 != "-") {
            smap.put("SET5", st51 + "-" + st52);
            if (Integer.valueOf(st51) > Integer.valueOf(st52)) {
                i1++;
            } else {
                i2++;
            }

        }

        if (i1 > i2) {
            if(isTeam) {
                smap.put("Winner", team1.getText().toString());
            }else {
                smap.put("Winner", team1.getText().toString()+":"+iteam1.getText().toString());
            }
        } else {
            if(isTeam) {
                smap.put("Winner", team2.getText().toString());
            }else {
                smap.put("Winner", team2.getText().toString()+":"+iteam2.getText().toString());
            }
        }

        smap.put("Score1", i1);
        smap.put("Score2", i2);


        firebaseFirestore = FirebaseFirestore.getInstance();

        int finalI = i1;
        int finalI1 = i2;


        DocumentReference sdocref = firebaseFirestore.collection(sportname).document(tournamentid).collection(roundname).document(d1);

        sdocref.update(smap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Score Added Successfully", Toast.LENGTH_SHORT).show();
                add_score.setVisibility(View.GONE);
                score1.setText(String.valueOf(finalI));
                score2.setText(String.valueOf(finalI1));
            }
        });

    }





    public void setByeResult(String p1)
    {
        firebaseFirestore=FirebaseFirestore.getInstance();


                    DocumentReference doref = firebaseFirestore.collection(sportname).document(tournamentid).collection(roundname).document(p1);
                    Map<String, Object> score = new HashMap<>();
                    score.put("Score1", "+");
                    score.put("Score2", "-");
                    if(isTeam) {
                        score.put("Winner", team1.getText().toString());
                    }else {
                        score.put("Winner", team1.getText().toString()+":"+iteam1.getText().toString());
                    }

                    doref.update(score).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            add_bye_result.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Score Added Successfully", Toast.LENGTH_SHORT).show();
                           // team1.setTextColor(Color.GREEN);
                            score1.setText("+");
                            score2.setText("-");
                        }
                    });

    }
}