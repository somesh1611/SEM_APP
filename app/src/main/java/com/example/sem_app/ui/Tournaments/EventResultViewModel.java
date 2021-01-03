package com.example.sem_app.ui.Tournaments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventResultViewModel extends ViewModel {


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
    public LiveData<HashMap<String,Object>> getResultDetails(String tid,String sname,String rname,String p1) {
        MutableLiveData<HashMap<String, Object>> result = new MutableLiveData<>();
        HashMap<String, Object> map = new HashMap<>();
        FirebaseFirestore.getInstance().collection(sname).document(tid).collection(rname)
                .whereEqualTo("Player1",p1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map rmap = document.getData();
                        if(rmap.containsKey("Tie"))
                        {
                            map.put("Tie",rmap.get("Tie"));
                            map.put("Score1",rmap.get("Score1"));
                            map.put("Score2",rmap.get("Score2"));
                            result.setValue(map);
                        }else if(rmap.containsKey("Winner"))
                        {

                            map.put("Score1",rmap.get("Score1"));
                            map.put("Score2",rmap.get("Score2"));
                            map.put("Winner",rmap.get("Winner"));

                            if(rmap.containsKey("SET1"))
                            {
                                map.put("SET1",rmap.get("SET1"));

                            }
                            if(rmap.containsKey("SET2"))
                            {
                                map.put("SET2",rmap.get("SET2"));

                            }
                            if(rmap.containsKey("SET3"))
                            {
                                map.put("SET3",rmap.get("SET3"));

                            }
                            if(rmap.containsKey("SET4"))
                            {
                                map.put("SET4",rmap.get("SET4"));

                            }
                            if(rmap.containsKey("SET5"))
                            {
                                map.put("SET5",rmap.get("SET5"));

                            }

                            result.setValue(map);

                        }
                    }
                }

            }
        });
        return result;
    }


}
