package com.example.sem_app.ui.Tournaments;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class EventParticipationViewModel extends ViewModel {

    public LiveData<HashMap<String,Object>> getEventDetails(String tid,Boolean a) {
        MutableLiveData<HashMap<String, Object>> retval1 = new MutableLiveData<>();
        HashMap<String, Object> map = new HashMap<>();
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
                        retval1.setValue(map);

                    }
                }
            });
        } else{
            FirebaseFirestore.getInstance().collectionGroup("My Tournaments")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if(document.getId().contentEquals(tid)) {

                                map.put("Tournament Name", document.get("Tournament Name"));
                                retval1.setValue(map);
                                break;
                            }

                        }
                    }
                }
            });
        }

        return retval1;
    }
}
