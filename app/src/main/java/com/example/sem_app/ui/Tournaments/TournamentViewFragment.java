package com.example.sem_app.ui.Tournaments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TournamentViewFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    TextView Tname,Thost,Tstart,Tend,TMname,TMnumber;
    private String TAG;
    public String status;
    private String tournamentid;
    Button button;
    String sdate,edate;
    ArrayList aspect = new ArrayList();
    Boolean isAdmin,isOver;

    FloatingActionButton fab,fab1,fab2;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Boolean isFABOpen=false;
    EditText tNameEdit,tHostEdit,tStartEdit,tEndEdit,tMEdit,tMNEdit;
    ImageButton startPickDate,endPickDate;
    TextView startDateDisplay,endDateDisplay;
    boolean startorend1;

    Date start = null;
    Date end = null;
    Date datePreviousDate = null;
    Date current = null;
    int MILLIS_IN_DAY;
    SimpleDateFormat dateFormat;

    TournamentViewViewModel tournamentViewViewModel;

    public TournamentViewFragment(String tid,boolean a) {

        tournamentid=tid;
        isAdmin=a;

    }

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
        fab = root.findViewById(R.id.fab_menu);
        fab1 = root.findViewById(R.id.fab_edit);
        fab2 = root.findViewById(R.id.fab_delete);

        if(isAdmin)
        {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        String today = day+"/"+month+"/"+year;

        MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            current = dateFormat.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String ID = firebaseUser.getUid();
        ////////////////////////////////////////////////////////////////////////////////////////////
        firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments").document(tournamentid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                status="";
                if(task.isSuccessful())
                {
                    String tend = task.getResult().getString("Ending Date");
                    String tstart = task.getResult().getString("Starting Date");
                    try
                    {
                        start = dateFormat.parse(tstart);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    try
                    {
                        end = dateFormat.parse(tend);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    String previousDate = dateFormat.format(start.getTime() - MILLIS_IN_DAY);

                    try
                    {
                        datePreviousDate = dateFormat.parse(previousDate);

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    if((!current.after(end))&&(!current.after(datePreviousDate)))
                    {
                        fab.setVisibility(View.VISIBLE);
                        fab1.setVisibility(View.VISIBLE);
                        fab2.setVisibility(View.VISIBLE);


                    }else{

                        fab2.setVisibility(View.VISIBLE);
                    }

                }

            }
        });

        }


        ///////////////////////////////////////////////////////////////////////////////////////////////


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
                new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Delete This Tournament")
                        .setMessage("Are you sure?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore.getInstance().collection("Sports Tournaments").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .collection("My Tournaments").document(tournamentid)
                                            .delete().addOnSuccessListener(new OnSuccessListener() {
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

                            }
                        }).setNegativeButton("no", null).show();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getActivity(),"mi "+Tstart.getText(),Toast.LENGTH_SHORT).show();

               TournamentEventsFragment fragment=new TournamentEventsFragment(tournamentid,isAdmin,Tstart.getText().toString(),Tend.getText().toString());
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack("back");
                transaction.commit();

            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tournamentViewViewModel = new ViewModelProvider(this).get(TournamentViewViewModel.class);
        tournamentViewViewModel.getTournamentDetails(tournamentid,isAdmin).observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                    if(!stringObjectHashMap.isEmpty()) {
                        Tname.setText(stringObjectHashMap.get("Tournament Name").toString());
                        Thost.setText(stringObjectHashMap.get("Tournament Host").toString());
                        Tstart.setText(stringObjectHashMap.get("Start Date").toString());
                        Tend.setText(stringObjectHashMap.get("End Date").toString());
                        TMname.setText(stringObjectHashMap.get("Manager Name").toString());
                        TMnumber.setText(stringObjectHashMap.get("Manager Phone").toString());
                    }
            }
        });

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
                    if(TextUtils.isEmpty(tun))
                    {
                        Toast.makeText(getActivity(),"Tournament Name is Required!",Toast.LENGTH_SHORT).show();
                        return;

                    }else {
                        tupdate_info.put("Tournament Name", tun);
                    }
                }

                if(aspect.contains("Tournament Host"))
                {
                    tuh=tHostEdit.getText().toString();
                    if(TextUtils.isEmpty(tuh))
                    {
                        Toast.makeText(getActivity(),"Host Name is Required!",Toast.LENGTH_SHORT).show();
                        return;

                    }else {
                        tupdate_info.put("Tournament Host", tuh);
                    }
                }
                if(aspect.contains("Tournament Manager Name"))
                {
                    tumn=tMEdit.getText().toString();
                    if(TextUtils.isEmpty(tumn))
                    {
                        Toast.makeText(getActivity(),"Manager Name is Required!",Toast.LENGTH_SHORT).show();
                        return;

                    }else {
                        tupdate_info.put("Tournament Manager Name", tumn);
                    }

                }
                if(aspect.contains("Tournament Manager Phone Number"))
                {
                    tump=tMNEdit.getText().toString();
                    if(TextUtils.isEmpty(tump))
                    {
                        Toast.makeText(getActivity(),"Manager Phone is Required!",Toast.LENGTH_SHORT).show();
                        return;

                    }else {
                        tupdate_info.put("Tournament Manager Phone Number", tump);
                    }
                }
                 if(aspect.contains("Tournament Start Date"))
                {
                    tusd=tStartEdit.getText().toString();
                    if(TextUtils.isEmpty(tusd))
                    {
                        Toast.makeText(getActivity(),"Start Date is Required!",Toast.LENGTH_SHORT).show();
                        return;

                    }else {
                        tupdate_info.put("Starting Date", tusd);
                    }
                }
                if(aspect.contains("Tournament End Date"))
                {
                    tued = tEndEdit.getText().toString();
                    if(TextUtils.isEmpty(tued))
                    {
                        Toast.makeText(getActivity(),"End Date is Required!",Toast.LENGTH_SHORT).show();
                        return;

                    }else {
                        tupdate_info.put("Ending Date", tued);
                    }
                }


       FirebaseFirestore.getInstance().collection("Sports Tournaments").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
       .collection("My Tournaments").document(tournamentid).update(tupdate_info).addOnSuccessListener(new OnSuccessListener() {
                   @Override
                   public void onSuccess(Object o) {
                      tournamentViewViewModel.getTournamentDetails(tournamentid,isAdmin).observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
                      @Override
                      public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                       Tname.setText(stringObjectHashMap.get("Tournament Name").toString());
                       Thost.setText(stringObjectHashMap.get("Tournament Host").toString());
                       Tstart.setText(stringObjectHashMap.get("Start Date").toString());
                       Tend.setText(stringObjectHashMap.get("End Date").toString());
                       TMname.setText(stringObjectHashMap.get("Manager Name").toString());
                       TMnumber.setText(stringObjectHashMap.get("Manager Phone").toString());
                     }
                  });
                   closeFABMenu();
                  Toast.makeText(getActivity(),"Tournament Updated Successfully", Toast.LENGTH_SHORT).show();
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