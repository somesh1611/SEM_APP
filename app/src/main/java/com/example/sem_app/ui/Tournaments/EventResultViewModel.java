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

public class EventResultViewModel extends ViewModel {


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
    public LiveData<HashMap<String,Object>> getResultDetails(String tid,String sname,String rname,String p1) {
        MutableLiveData<HashMap<String, Object>> result = new MutableLiveData<>();
        HashMap<String, Object> map = new HashMap<>();
        FirebaseFirestore.getInstance().collection(sname).document(tid).collection(rname)
                .document(p1).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                       // Map rmap = (task.getResult()).getData();
                        map.put("Player1",task.getResult().get("Player1"));
                        map.put("Player2",task.getResult().get("Player2"));
                        if(task.getResult().contains("Tie"))
                        {
                            map.put("Tie",task.getResult().get("Tie"));
                            map.put("Score1",task.getResult().get("Score1"));
                            map.put("Score2",task.getResult().get("Score2"));

                        }else if(task.getResult().contains("Winner"))
                        {

                            map.put("Score1",task.getResult().get("Score1"));
                            map.put("Score2",task.getResult().get("Score2"));
                            map.put("Winner",task.getResult().get("Winner"));

                            if(task.getResult().contains("SET1"))
                            {
                                map.put("SET1",task.getResult().get("SET1"));

                            }
                            if(task.getResult().contains("SET2"))
                            {
                                map.put("SET2",task.getResult().get("SET2"));

                            }
                            if(task.getResult().contains("SET3"))
                            {
                                map.put("SET3",task.getResult().get("SET3"));

                            }
                            if(task.getResult().contains("SET4"))
                            {
                                map.put("SET4",task.getResult().get("SET4"));

                            }
                            if(task.getResult().contains("SET5"))
                            {
                                map.put("SET5",task.getResult().get("SET5"));

                            }



                        }

                    result.setValue(map);

                }

            }
        });

        return result;
    }


}
