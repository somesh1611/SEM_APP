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
import java.util.List;

public class EventRoundOneViewModel extends ViewModel {

    public EventRoundOneViewModel()
    {

    }





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
