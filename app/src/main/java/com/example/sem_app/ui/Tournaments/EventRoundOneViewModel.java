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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventRoundOneViewModel extends ViewModel {

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

    public LiveData<List<Draw>> getDrawsList(String tid,String sname,String rname){
        MutableLiveData<List<Draw>> retval = new MutableLiveData<>();
        List<Draw> drawList = new ArrayList<>();
        //Do the Query and populate the drawList object here.
        //Don't forget to call setvalue once the list is populated in onComplete/onSuccess.
        FirebaseFirestore.getInstance().collection(sname).document(tid).collection(rname)
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                drawList.clear();

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if(document.contains("Winner")) {

                            Draw draw1 = new Draw(document.getId(), document.get("Player1").toString(),
                                    document.get("Player2").toString(),
                                    document.get("Score1").toString(),
                                    document.get("Score2").toString(), document.get("Winner").toString());
                            drawList.add(draw1);
                        }else {
                            Draw draw2 = new Draw(document.getId(), document.get("Player1").toString(),
                                    document.get("Player2").toString());
                            drawList.add(draw2);
                        }


                    }

                    retval.setValue(drawList);


                }
            }
        });

        return retval;

    }

    public LiveData<List<String>> getDrawId(String tid,String sname,String rname) {

        MutableLiveData<List<String>> retval1 = new MutableLiveData<>();
        List<String> drawIdList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection(sname).document(tid).collection(rname)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                drawIdList.clear();

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        drawIdList.add(document.getId());

                    }
                    retval1.setValue(drawIdList);
                }
            }
        });
        return retval1;
    }


}
