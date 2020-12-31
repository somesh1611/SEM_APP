package com.example.sem_app.ui.Tournaments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class TournamentViewViewModel extends ViewModel {

    public LiveData<HashMap<String,Object>> getTournamentDetails(String tid,Boolean a)
    {
        MutableLiveData<HashMap<String,Object>> retval1=new MutableLiveData<>();
        HashMap<String,Object> map=new HashMap<>();
        if(a)
        {

            FirebaseFirestore.getInstance().collection("Sports Tournaments").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("My Tournaments").document(tid)
            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {

                        map.put("Tournament Name",task.getResult().get("Tournament Name"));
                        map.put("Tournament Host",task.getResult().get("Tournament Host"));
                        map.put("Start Date",task.getResult().get("Starting Date"));
                        map.put("End Date",task.getResult().get("Ending Date"));
                        map.put("Manager Name",task.getResult().get("Tournament Manager Name"));
                        map.put("Manager Phone",task.getResult().get("Tournament Manager Phone Number"));

                        retval1.setValue(map);

                    }
                }
            });
        } else {
            ArrayList allusers = new ArrayList();
            allusers.clear();
            CollectionReference usercolref=FirebaseFirestore.getInstance().collection("Users");
            usercolref.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    for(DocumentSnapshot Snapshot:value)
                    {

                        allusers.add(Snapshot.getId());

                    }
                if(allusers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    allusers.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
                }

                    for(Object i:allusers) {

                     FirebaseFirestore.getInstance().collection("Sports Tournaments").document(i.toString())
                             .collection("My Tournaments")
                             .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                 @Override
                                 public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                     for (DocumentSnapshot Snapshot : value) {
                                         String did = Snapshot.getId();
                                         if(did==tid)
                                         {

                                                      map.put("Tournament Name", Snapshot.get("Tournament Name"));
                                                      map.put("Tournament Host", Snapshot.get("Tournament Host"));
                                                      map.put("Start Date", Snapshot.get("Starting Date"));
                                                      map.put("End Date", Snapshot.get("Ending Date"));
                                                      map.put("Manager Name",Snapshot.get("Tournament Manager Name"));
                                                      map.put("Manager Phone", Snapshot.get("Tournament Manager Phone Number"));
                                                      retval1.setValue(map);

                                                      break;

                                         }
                                     }

                                 }
                             });
                    }
                }
            });
        }
        return retval1;
    }
}
