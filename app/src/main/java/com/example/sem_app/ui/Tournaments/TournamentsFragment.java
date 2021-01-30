package com.example.sem_app.ui.Tournaments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TournamentsFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = "TAG";
    private AlertDialog.Builder dialogBuilder;
    EditText newTournamentName,newTournamentHost,newTournamentStartDate,newTournamentEndDate,tournamentManagerName1,tournamentManagerNumber1;
    private TextView startDateDisplay;
    private TextView endDateDisplay;
    private ImageButton startPickDate;
    private ImageButton endPickDate;
    private boolean startorend;
    ArrayList<String> userSports=new ArrayList<String>();
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Button b1,b2;
    private TournamentsViewModel tournamentsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tournamentsViewModel =
                new ViewModelProvider(this).get(TournamentsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tournaments, container, false);
        b1=root.findViewById(R.id.my_tournament_button);
        b2=root.findViewById(R.id.other_tournament_button);
        FloatingActionButton fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    dialogBuilder=new AlertDialog.Builder(getActivity());
                    final View newTournament=getLayoutInflater().inflate(R.layout.new_tournament,null);
                    newTournamentName=newTournament.findViewById(R.id.tournamentName);
                    newTournamentHost=newTournament.findViewById(R.id.tournamenthost);
                    newTournamentStartDate=newTournament.findViewById(R.id.start_date);
                    newTournamentEndDate=newTournament.findViewById(R.id.end_date);



                    dialogBuilder.setView(newTournament);
                    dialogBuilder.setTitle("New Tournament");
                    //dialog=dialogBuilder.create();

                    //dialog.setTitle("New Tournament");
                    //dialog.show();


                    dialogBuilder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(TextUtils.isEmpty(newTournamentName.getText().toString())){
                                Toast.makeText(getActivity(),"Tournament Name is Required.",Toast.LENGTH_SHORT).show();
                                return;
                            }else if(TextUtils.isEmpty(newTournamentHost.getText().toString())) {
                                Toast.makeText(getActivity(),"Tournament Host is Required.",Toast.LENGTH_SHORT).show();
                                return;

                            }else if(TextUtils.isEmpty(newTournamentStartDate.getText().toString())) {
                                Toast.makeText(getActivity(),"Start Date is Required.",Toast.LENGTH_SHORT).show();
                                return;

                            }else if(TextUtils.isEmpty(newTournamentEndDate.getText().toString())) {
                                Toast.makeText(getActivity(),"End Date is Required.",Toast.LENGTH_SHORT).show();
                                return;

                            }
                            else {

                                addSports();
                            }

                        }
                    });
                    dialogBuilder.create();
                    dialogBuilder.show();

                    /////////////////////////////////////error////////////////////////////////////////////////////////
                    /*  capture our View elements for the start date function   */
                DialogFragment newFragment = new DatePickerFragment();
                    startDateDisplay = (TextView) newTournament.findViewById(R.id.start_date);
                    startPickDate = newTournament.findViewById(R.id.pickStartDate);



                    startPickDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Launch our DatePicker when the button is clicked


                            // creating DialogFragment which creates DatePickerDialog
                            newFragment.setTargetFragment(TournamentsFragment.this,0);  // Passing this fragment DatePickerFragment.
                            // As i figured out this is the best way to keep the reference to calling activity when using FRAGMENT.
                            show(newFragment);
                            startorend=true;

                        }

                        private void show(DialogFragment newFragment) {
                            newFragment.show(getFragmentManager(),"datePicker");
                        }
                    });


//        /* capture our View elements for the end date function */
                    endDateDisplay = newTournament.findViewById(R.id.end_date);
                    endPickDate = newTournament.findViewById(R.id.pickEndDate);
//
//
                    endPickDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           // DialogFragment newFragment = new DatePickerFragment();
                            // creating DialogFragment which creates DatePickerDialog
                            newFragment.setTargetFragment(TournamentsFragment.this,0);  // Passing this fragment DatePickerFragment.
                            // As i figured out this is the best way to keep the reference to calling activity when using FRAGMENT.
                            newFragment.show(getFragmentManager(),"datePicker");
                            startorend=false;

                        }
                    });

//
            }
        });



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTournamentsFragment fragment=new MyTournamentsFragment();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherTournamentsFragment fragment=new OtherTournamentsFragment();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });
        return root;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        StringBuilder sb = new StringBuilder().append(dayOfMonth).append("/").append(month + 1).append("/").append(year);
        String formattedDate = sb.toString();
        if (startorend) {
            startDateDisplay.setText(formattedDate);
        } else {
            endDateDisplay.setText(formattedDate);
        }


    }


    /////////////////////////////////////////////error///////////////////////////////////////////////////////
    private void addSports(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Sports");
        userSports.clear();
        builder.setMultiChoiceItems(R.array.sportsList, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                String[]sports=getResources().getStringArray(R.array.sportsList);

                if(isChecked)
                {
                    userSports.add(sports[which]);
                }
                else if(userSports.contains(sports[which])){
                    userSports.remove(sports[which]);

                }

            }
        });
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(userSports.isEmpty())
                {
                    Toast.makeText(getActivity(),"Select atleast One Sport!",Toast.LENGTH_SHORT).show();
                }else {

                    addTournamentManager();
                }





            }
        });


        builder.create();
        builder.show();
    }

    private void addTournamentManager()
    {
        AlertDialog.Builder newbuilder=new AlertDialog.Builder(getActivity());

        final View tournamentManager=getLayoutInflater().inflate(R.layout.create_tournament_manager,null);
        tournamentManagerName1=tournamentManager.findViewById(R.id.tournamentManagerName);
        tournamentManagerNumber1=tournamentManager.findViewById(R.id.tournamentManagerPhone);
        newbuilder.setView(tournamentManager);
        newbuilder.setTitle("Add Tournament Manager");
        newbuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String tn = newTournamentName.getText().toString();
                String th = newTournamentHost.getText().toString();
                String tsd = newTournamentStartDate.getText().toString();

                String ted = newTournamentEndDate.getText().toString();
                String tmn = tournamentManagerName1.getText().toString();
                String tmp = tournamentManagerNumber1.getText().toString();


                if (TextUtils.isEmpty(tmn)) {
                    Toast.makeText(getActivity(), "Manager Name is Required", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(tmp)) {
                    Toast.makeText(getActivity(), "Manager Phone Number is Required", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tmp.length() != 10) {
                    Toast.makeText(getActivity(), "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    String sportsadded = "";
                    for (String sport : userSports) {
                        sportsadded = sportsadded + sport + ",";
                    }

                    Log.d(TAG, "sports" + sportsadded);

                    firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String ID = firebaseUser.getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("Tournament Name", tn);
                    user.put("Tournament Host", th);

                    user.put("Starting Date", tsd);
                    user.put("Ending Date", ted);
                    user.put("Tournament Manager Name", tmn);
                    user.put("Tournament Manager Phone Number", tmp);
                    user.put("Sports Included", sportsadded);
                    user.put("User ID", ID);
                    Log.d(TAG, "name" + ID);

                    firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Tournament Created Successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "Failed to create");
                            }


                        }
                    });


                }
            }
        });

        newbuilder.create();
        newbuilder.show();

    }


}