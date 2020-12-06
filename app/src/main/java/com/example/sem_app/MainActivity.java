package com.example.sem_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    EditText newTournamentName,newTournamentHost,newTournamentStartDate,newTournamentEndDate,tournamentManagerName1,tournamentManagerNumber1;
    private TextView startDateDisplay;
    private TextView endDateDisplay;
    private ImageButton startPickDate;
    private ImageButton endPickDate;
    private Calendar startDate;
    private Calendar endDate;
    static final int DATE_DIALOG_ID = 0;
    private TextView activeDateDisplay;
    private Calendar activeDate;
    TextView userName;
    private TextView userMail;
    private String creator;
    private String tournamentNumber;
    public int count;


    //Boolean[]checkedSports;
    ArrayList<String> userSports=new ArrayList<String>();

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewTournament();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        String id = firebaseUser.getUid();


        DocumentReference acc_ref=firebaseFirestore.collection("Users").document(id);
        acc_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    String name=documentSnapshot.getString("Name");
                    String mail=documentSnapshot.getString("Email");
                    Log.d(TAG, "document:");
                    userName=findViewById(R.id.user_name);
                    userName.setText(name);
                    userMail=findViewById(R.id.user_mail);
                    userMail.setText(mail);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void createNewTournament(){
        dialogBuilder=new AlertDialog.Builder(this);
        final View newTournament=getLayoutInflater().inflate(R.layout.new_tournament,null);
        newTournamentName=newTournament.findViewById(R.id.tournamentName);
        newTournamentHost=newTournament.findViewById(R.id.tournamenthost);
        newTournamentStartDate=newTournament.findViewById(R.id.start_date);
        newTournamentEndDate=newTournament.findViewById(R.id.end_date);

        //next=newTournament.findViewById(R.id.next_button);
        //cancel=newTournament.findViewById(R.id.cancel_button);
        dialogBuilder.setView(newTournament);
        dialogBuilder.setTitle("New Tournament");
        //dialog=dialogBuilder.create();

        //dialog.setTitle("New Tournament");
        //dialog.show();


        dialogBuilder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                addSports();

            }
        });
        dialogBuilder.create();
        dialogBuilder.show();

        /*  capture our View elements for the start date function   */
        startDateDisplay = (TextView) newTournament.findViewById(R.id.start_date);
        startPickDate = newTournament.findViewById(R.id.pickStartDate);

        /* get the current date */
        startDate = Calendar.getInstance();

        startPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch our DatePicker when the button is clicked
                showDateDialog(startDateDisplay, startDate);

            }
        });


//        /* capture our View elements for the end date function */
        endDateDisplay = newTournament.findViewById(R.id.end_date);
        endPickDate = newTournament.findViewById(R.id.pickEndDate);
//
//        /* get the current date */
        endDate = Calendar.getInstance();

        endPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Date Picker
                showDateDialog(endDateDisplay, endDate);
            }
        });

//        /* add a click listener to the button   */

        /* display the current date (this method is below)  */
        updateDisplay(startDateDisplay, startDate);
        updateDisplay(endDateDisplay, endDate);
    }
    private void updateDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("/")
                        .append(date.get(Calendar.MONTH) + 1).append("/")
                        .append(date.get(Calendar.YEAR)).append(" "));


    }

    public void showDateDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;

        showDialog(DATE_DIALOG_ID);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeDate.set(Calendar.YEAR, year);
            Log.d(TAG, "count"+year);


            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
            unregisterDateDisplay();
        }
    };
    private void unregisterDateDisplay() {
        activeDateDisplay = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }

    private void addSports(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
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

                addTournamentManager();



             /*   String tn=newTournamentName.getText().toString();
                String th=newTournamentHost.getText().toString();
                String tsd=newTournamentStartDate.getText().toString();
                String syear=newTournamentStartDate.getText().toString();
                String ted=newTournamentEndDate.getText().toString();




                String[]tournamentYear=syear.split("/");
                String date=tournamentYear[0]+"-"+tournamentYear[1];


                String sportsadded="";
                for (String sport:userSports)
                {
                    sportsadded=sportsadded+sport+",";
                }

                Log.d(TAG, "sports"+sportsadded);

                firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseAuth= FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String ID = firebaseUser.getUid();


                // DocumentReference documentReference = firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments").;
                Map<String,Object> user = new HashMap<>();
                user.put("Tournament Name",tn);
                user.put("Tournament Host",th);
                Log.d(TAG, "name"+th);
                user.put("Starting Date",tsd);
                user.put("Ending Date",ted);
                user.put("Sports Included",sportsadded);

                firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Tournament Created Successfully!", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d(TAG,"Failed to create");
                        }


                    }
                });

            */


            }
        });


        builder.create();
        builder.show();
    }

    private void addTournamentManager()
    {
        AlertDialog.Builder newbuilder=new AlertDialog.Builder(this);

        final View tournamentManager=getLayoutInflater().inflate(R.layout.create_tournament_manager,null);
        tournamentManagerName1=tournamentManager.findViewById(R.id.tournamentManagerName);
        tournamentManagerNumber1=tournamentManager.findViewById(R.id.tournamentManagerPhone);
        newbuilder.setView(tournamentManager);
        newbuilder.setTitle("Add Tournament Manager");
        newbuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String tn=newTournamentName.getText().toString();
                String th=newTournamentHost.getText().toString();
                String tsd=newTournamentStartDate.getText().toString();

                String ted=newTournamentEndDate.getText().toString();
                String tmn=tournamentManagerName1.getText().toString();
                String tmp=tournamentManagerNumber1.getText().toString();

                String sportsadded="";
                for (String sport:userSports)
                {
                    sportsadded=sportsadded+sport+",";
                }

                Log.d(TAG, "sports"+sportsadded);

                firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseAuth= FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String ID = firebaseUser.getUid();

                Map<String,Object> user = new HashMap<>();
                user.put("Tournament Name",tn);
                user.put("Tournament Host",th);
                Log.d(TAG, "name"+th);
                user.put("Starting Date",tsd);
                user.put("Ending Date",ted);
                user.put("Tournament Manager Name",tmn);
                user.put("Tournament Manager Phone Number",tmp);
                user.put("Sports Included",sportsadded);

                firebaseFirestore.collection("Sports Tournaments").document(ID).collection("My Tournaments").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Tournament Created Successfully!", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d(TAG,"Failed to create");
                        }


                    }
                });



            }
        });

        newbuilder.create();
        newbuilder.show();

    }

}