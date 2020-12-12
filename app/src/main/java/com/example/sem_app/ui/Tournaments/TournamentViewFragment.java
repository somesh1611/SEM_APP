package com.example.sem_app.ui.Tournaments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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


public class TournamentViewFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    TextView Tname,Thost,Tstart,Tend,TMname,TMnumber;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private String TAG;
    public String documentid="";
    private String tournamentname;
    String selectsports;
    Button button;
    Map map;
    ArrayList allusers = new ArrayList();
    ArrayList aspect = new ArrayList();
    Boolean isAdmin;
    FloatingActionButton fab,fab1,fab2;
    Boolean isFABOpen=false;
    EditText tNameEdit,tHostEdit,tStartEdit,tEndEdit,tMEdit,tMNEdit;
    ImageButton startPickDate,endPickDate;
    TextView startDateDisplay,endDateDisplay;
    boolean startorend1;
    String id;
    String id1;


    public TournamentViewFragment()
    {

    }


    public TournamentViewFragment(Object tname,boolean a) {

        tournamentname=tname.toString();
        isAdmin=a;
    }

   /* /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TournamentViewFragment.
     */
    // TODO: Rename and change types and number of parameters
  /*  public static TournamentViewFragment newInstance(String param1, String param2) {
        TournamentViewFragment fragment = new TournamentViewFragment();
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
    } */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_tournament_view, container, false);
        Tname = root.findViewById(R.id.tournament_name_text);
        Thost = root.findViewById(R.id.host_text);
        Tstart = root.findViewById(R.id.start_date_text);
        Tend = root.findViewById(R.id.end_date_text);
        TMname = root.findViewById(R.id.manager_name_text);
        TMnumber = root.findViewById(R.id.manager_phone_text);
        button = root.findViewById(R.id.see_event_button);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String ID = firebaseUser.getUid();

        CollectionReference colref=firebaseFirestore.collection("Users");
        colref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allusers.clear();

                for(DocumentSnapshot Snapshot:value)
                {

                    allusers.add(Snapshot.getId());

                }
              /*  if(allusers.contains(ID))
                {
                    allusers.remove(ID);
                }*/

                for(Object i:allusers) {

                   CollectionReference collref = firebaseFirestore.collection("Sports Tournaments").document(i.toString()).collection("My Tournaments");
                    Query query=collref.whereEqualTo("Tournament Name", tournamentname);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    map=document.getData();
                                    String name = map.get("Tournament Name").toString();

                                    Tname.setText(name);

                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }
                    });
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    map=document.getData();

                                    String mname = map.get("Tournament Manager Name").toString();

                                    TMname.setText(mname);

                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }


                    });
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    map=document.getData();

                                    String mnumber = map.get("Tournament Manager Phone Number").toString();

                                    TMnumber.setText(mnumber);

                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }


                    });
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    map=document.getData();

                                    String host = map.get("Tournament Host").toString();

                                    Thost.setText(host);

                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }


                    });
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    map=document.getData();

                                    String sdate = map.get("Starting Date").toString();

                                    Tstart.setText(sdate);
                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }


                    });
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    map=document.getData();

                                    String edate = map.get("Ending Date").toString();

                                    Tend.setText(edate);

                                }
                            } else {

                                Log.d(TAG,"invalid");


                            }
                        }


                    });



                    ////////////////////////////////////////////////////////////////////

                }


            }
        });



        //////////////////////////////////////////////////////////////////////////////////////////////////




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TournamentEventsFragment fragment=new TournamentEventsFragment(tournamentname,isAdmin);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();

            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////

        fab = root.findViewById(R.id.fab_menu);
        fab1 = root.findViewById(R.id.fab_edit);
        fab2 = root.findViewById(R.id.fab_delete);

        if(!isAdmin)
        {
            fab.setVisibility(View.GONE);
            fab1.setVisibility(View.GONE);
            fab2.setVisibility(View.GONE);
        }else{
            fab.setVisibility(View.VISIBLE);
            fab1.setVisibility(View.VISIBLE);
            fab2.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"Delete",Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Delete This Tournament")
                        .setMessage("Are you sure?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                CollectionReference collref2 = firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments");
                                Query query2 = collref2.whereEqualTo("Tournament Name", tournamentname);
                                query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                                        if (task.isSuccessful()) {

                                            for (QueryDocumentSnapshot document : task.getResult()) {


                                                id1=document.getId();


                                            }
                                            DocumentReference doc_ref=firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments").document(id1);
                                            doc_ref.delete().addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                   // getFragmentManager().beginTransaction().detach(TournamentViewFragment.this).attach(TournamentViewFragment.this).commit();
                                                    TournamentsFragment fragment=new TournamentsFragment();
                                                    FragmentTransaction transaction=getFragmentManager().beginTransaction();
                                                    transaction.replace(R.id.nav_host_fragment,fragment);
                                                    transaction.addToBackStack("back");
                                                    transaction.commit();
                                                    Toast.makeText(getActivity(),"Tournament Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                        } else {

                                            Log.d(TAG,"invalid");


                                        }



                                    }
                                });
                            }
                        }).setNegativeButton("no", null).show();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"Edit",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
                builder1.setTitle("Select Aspects to be Updated");
                aspect.clear();
                builder1.setMultiChoiceItems(R.array.updateTournament, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        String[]item=getResources().getStringArray(R.array.updateTournament);

                        if(isChecked)
                        {
                            aspect.add(item[which]);
                        }
                        else if(aspect.contains(item[which])){
                            aspect.remove(item[which]);

                        }

                    }
                });
                builder1.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(aspect.isEmpty())
                        {
                            Toast.makeText(getActivity(),"Select atleast One Aspect to be updated!",Toast.LENGTH_SHORT).show();
                        }else {
                            update();
                        }


                    }
                });


                builder1.create();
                builder1.show();
            }
        });







        return root;
    }

    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_64));

        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_128));


    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);

    }
    public void update()
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        final View tournament_edit=getLayoutInflater().inflate(R.layout.tournament_editor,null);
        tNameEdit=tournament_edit.findViewById(R.id.edit_tname);
        tHostEdit=tournament_edit.findViewById(R.id.edit_thost);
        tStartEdit=tournament_edit.findViewById(R.id.edit_sdate);
        tEndEdit=tournament_edit.findViewById(R.id.edit_edate);
        tMEdit=tournament_edit.findViewById(R.id.edit_tmname);
        tMNEdit=tournament_edit.findViewById(R.id.edit_tmphone);
        startPickDate = tournament_edit.findViewById(R.id.edit_pickStartDate);
        endPickDate = tournament_edit.findViewById(R.id.edit_pickEndDate);

        builder.setView(tournament_edit);
        builder.setTitle("Update your Tournament");

        if(!aspect.contains("Tournament Name"))
        {
            tNameEdit.setVisibility(View.GONE);
        }
        if(!aspect.contains("Tournament Host"))
        {
            tHostEdit.setVisibility(View.GONE);
        }
        if(!aspect.contains("Tournament Start Date"))
        {
            tStartEdit.setVisibility(View.GONE);
         startPickDate.setVisibility(View.INVISIBLE);
        }
        if(!aspect.contains("Tournament End Date"))
        {
            tEndEdit.setVisibility(View.GONE);
            endPickDate.setVisibility(View.GONE);
        }
        if(!aspect.contains("Tournament Manager Name"))
        {
            tMEdit.setVisibility(View.GONE);
        }
        if(!aspect.contains("Tournament Manager Phone Number"))
        {
            tMNEdit.setVisibility(View.GONE);
        }


        DialogFragment newFragment1 = new DatePickerFragment();
        startDateDisplay = (TextView) tournament_edit.findViewById(R.id.edit_sdate);




        startPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newFragment1.setTargetFragment(TournamentViewFragment.this,0);  // Passing this fragment DatePickerFragment.
                // As i figured out this is the best way to keep the reference to calling activity when using FRAGMENT.
                show(newFragment1);
                startorend1=true;

            }

            private void show(DialogFragment newFragment) {
                newFragment1.show(getFragmentManager(),"datePicker");
            }
        });


//        /* capture our View elements for the end date function */
        endDateDisplay = tournament_edit.findViewById(R.id.edit_edate);

//
//
        endPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                newFragment1.setTargetFragment(TournamentViewFragment.this,0);  // Passing this fragment DatePickerFragment.
                // As i figured out this is the best way to keep the reference to calling activity when using FRAGMENT.
                newFragment1.show(getFragmentManager(),"datePicker");
                startorend1=false;

            }
        });



        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Map tupdate_info=new HashMap();
                String tun,tuh,tumn,tump,tusd,tued;


                if(aspect.contains("Tournament Name"))
                {
                    tun=tNameEdit.getText().toString();
                    tupdate_info.put("Tournament Name",tun);
                }

                if(aspect.contains("Tournament Host"))
                {
                    tuh=tHostEdit.getText().toString();
                    tupdate_info.put("Tournament Host",tuh);
                }
                if(aspect.contains("Tournament Manager Name"))
                {
                    tumn=tMEdit.getText().toString();
                    tupdate_info.put("Tournament Manager Name",tumn);
                }
                if(aspect.contains("Tournament Manager Phone Number"))
                {
                    tump=tMNEdit.getText().toString();
                    tupdate_info.put("Tournament Manager Phone Number",tump);
                }
                 if(aspect.contains("Tournament Start Date"))
                {
                    tusd=tStartEdit.getText().toString();
                    tupdate_info.put("Starting Date",tusd);
                }
                if(aspect.contains("Tournament End Date"))
                {
                    tued = tEndEdit.getText().toString();
                   tupdate_info.put("Ending Date",tued);
                }

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                String ID = firebaseUser.getUid();

               /* DocumentReference acc_ref1=firebaseFirestore.collection("Users").document(ID);
                acc_ref1.update(user_update_info).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        getFragmentManager().beginTransaction().detach(MyProfileFragment.this).attach(MyProfileFragment.this).commit();
                        Toast.makeText(getActivity(),"Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });*/


                    CollectionReference collref1 = firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments");
                    Query query1 = collref1.whereEqualTo("Tournament Name", tournamentname);
                    query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {


                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    id=document.getId();


                                }
                                DocumentReference doc_ref=firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments").document(id);
                                doc_ref.update(tupdate_info).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        getFragmentManager().beginTransaction().detach(TournamentViewFragment.this).attach(TournamentViewFragment.this).commit();
                                        Toast.makeText(getActivity(),"Tournament Updated Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {

                                Log.d(TAG,"invalid");


                            }



                        }
                    });


            }
        });


        builder.create();
        builder.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        StringBuilder sb = new StringBuilder().append(dayOfMonth).append("/").append(month + 1).append("/").append(year);
        String formattedDate = sb.toString();
        if (startorend1) {
            startDateDisplay.setText(formattedDate);
        } else {
            endDateDisplay.setText(formattedDate);
        }

    }


}