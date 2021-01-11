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

public class OtherTournamentsViewModel extends ViewModel {

    /*public LiveData<List<Tournament>> getTournamentsList() {
        MutableLiveData<List<Tournament>> retval = new MutableLiveData<>();
        List<Tournament> tournamentsList = new ArrayList<>();
        ArrayList allusers = new ArrayList();
        allusers.clear();
        FirebaseFirestore.getInstance().collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        tournamentsList.clear();
                        for (DocumentSnapshot Snapshot : value) {

                            allusers.add(Snapshot.getId());

                        }
                        if(allusers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        {
                            allusers.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        }
                        for (Object i : allusers)
                        {
                            FirebaseFirestore.getInstance().collection("Sports Tournaments").document(i.toString()).collection("My Tournaments")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {


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
                        }

                    }
                });
        return retval;
    }*/

    public LiveData<List<Tournament>> getTournamentsList() {
        MutableLiveData<List<Tournament>> retval = new MutableLiveData<>();
        List<Tournament> tournamentsList = new ArrayList<>();
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collectionGroup("My Tournaments")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if(document.get("User ID").toString().contentEquals(ID)) {


                        }else {
                            Tournament tournament = new Tournament(document.get("Tournament Name").toString(),
                                    document.get("Tournament Host").toString(),
                                    document.get("Starting Date").toString(),
                                    document.get("Ending Date").toString());
                            tournamentsList.add(tournament);
                        }

                    }
                    retval.setValue(tournamentsList);
                }


            }
        });
        return retval;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    /*public LiveData<List<String>> getTournamentId() {

        MutableLiveData<List<String>> retval1 = new MutableLiveData<>();
        List<String> tournamentIdList = new ArrayList<>();
        ArrayList allusers = new ArrayList();
        allusers.clear();
        FirebaseFirestore.getInstance().collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        tournamentIdList.clear();
                        for (DocumentSnapshot Snapshot : value) {

                            allusers.add(Snapshot.getId());

                        }
                        if(allusers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        {
                            allusers.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        }
                        for (Object i : allusers)
                        {
                            FirebaseFirestore.getInstance().collection("Sports Tournaments").document(i.toString()).collection("My Tournaments")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {


                                    if (task.isSuccessful()) {

                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            tournamentIdList.add(document.getId());

                                        }
                                        retval1.setValue(tournamentIdList);
                                    }
                                }
                            });
                        }

                    }
                });
        return retval1;
    }*/

    public LiveData<List<String>> getTournamentId() {

        MutableLiveData<List<String>> retval1 = new MutableLiveData<>();
        List<String> tournamentIdList = new ArrayList<>();
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collectionGroup("My Tournaments")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        if(document.get("User ID").toString().contentEquals(ID)) {


                        }else {

                            tournamentIdList.add(document.getId());
                        }

                    }
                    retval1.setValue(tournamentIdList);
                }

            }
        });

        return retval1;
    }
}
