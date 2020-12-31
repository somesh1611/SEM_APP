package com.example.sem_app.ui.Tournaments;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class EventRoundTwoViewModel extends ViewModel {
    public LiveData<HashMap<String,Object>> getEventDetails(String tid) {
        MutableLiveData<HashMap<String, Object>> retval1 = new MutableLiveData<>();
        HashMap<String, Object> map = new HashMap<>();
        //////////////////////////////////////////////////////////////////////////////
        ArrayList allusers = new ArrayList();
        allusers.clear();
        CollectionReference usercolref= FirebaseFirestore.getInstance().collection("Users");
        usercolref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for(DocumentSnapshot Snapshot:value)
                {

                    allusers.add(Snapshot.getId());

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
                                            retval1.setValue(map);
                                            break;

                                        }
                                    }

                                }
                            });
                }
            }
        });
        return retval1;
    }
}
