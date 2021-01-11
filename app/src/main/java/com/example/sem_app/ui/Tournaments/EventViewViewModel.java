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
import java.util.Map;

public class EventViewViewModel extends ViewModel {

   /* public LiveData<HashMap<String,Object>> getEventDetails(String tid) {
        MutableLiveData<HashMap<String, Object>> retval1 = new MutableLiveData<>();
        HashMap<String, Object> map = new HashMap<>();
        //////////////////////////////////////////////////////////////////////////////
        ArrayList allusers = new ArrayList();
        allusers.clear();
        FirebaseFirestore.getInstance().collection("Users")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
    }*/

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

    public LiveData<String> getWinner(String tid,String sname,Boolean isTeam)
    {
        MutableLiveData<String> eventWinner = new MutableLiveData<>();
        final String[] winner = new String[1];
        final String[] wsy1 = new String[1];
        final String[] wsb1 = new String[1];
        ArrayList arrayList=new ArrayList();
        arrayList.add(2);
        arrayList.add(4);
        arrayList.add(8);
        arrayList.add(16);
        arrayList.add(32);
        arrayList.add(64);
        arrayList.add(128);
        FirebaseFirestore.getInstance().collection(sname).document(tid).collection("Round1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    if (!task.getResult().isEmpty()) {

                        int count=0;
                        int n =(task.getResult().size())*2;
                        int a=n;
                        while (n > 0) {
                            n = n / 2;
                            count++;
                        }

                        if (arrayList.contains((a))) {
                            count = count - 1;
                        }
                        FirebaseFirestore.getInstance().collection(sname).document(tid).collection("Round"+count)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        Map map = document.getData();
                                        if (map.containsKey("Winner")) {
                                            String w = map.get("Winner").toString();
                                            eventWinner.setValue(w);
                                        }
                                    }
                                }
                            }
                        });

                    }
                }

            }
        });

        return eventWinner;
    }
}
