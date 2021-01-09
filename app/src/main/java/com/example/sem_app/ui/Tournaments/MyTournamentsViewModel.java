package com.example.sem_app.ui.Tournaments;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyTournamentsViewModel extends ViewModel {

    public LiveData<List<Tournament>> getTournamentsList(){
        MutableLiveData<List<Tournament>> retval = new MutableLiveData<>();
        List<Tournament> tournamentsList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Sports Tournaments").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("My Tournaments")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                tournamentsList.clear();

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                            Tournament tournament = new Tournament(document.get("Tournament Name").toString(),
                                    document.get("Tournament Host").toString(),
                                    document.get("Starting Date").toString(),
                                    document.get("Ending Date").toString());
                            tournamentsList.add(tournament);

                    }

                    retval.setValue(tournamentsList);


                }
            }
        });

        return retval;

    }

    public LiveData<List<String>> getTournamentId() {

        MutableLiveData<List<String>> retval1 = new MutableLiveData<>();
        List<String> tournamentIdList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("Sports Tournaments").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("My Tournaments")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

               tournamentIdList.clear();

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                       tournamentIdList.add(document.getId());

                    }
                    retval1.setValue(tournamentIdList);
                }
            }
        });
        return retval1;
    }

}
