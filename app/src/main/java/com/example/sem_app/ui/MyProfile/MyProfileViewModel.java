package com.example.sem_app.ui.MyProfile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MyProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public MyProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MyProfile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<HashMap<String,Object>> getDetails()
    {
       MutableLiveData<HashMap<String,Object>> retval=new MutableLiveData<>();
       HashMap<String,Object> map=new HashMap<>();
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {

                    map.put("Name",task.getResult().get("Name"));
                    map.put("Email",task.getResult().get("Email"));
                    map.put("Year",task.getResult().get("Year"));
                    map.put("Branch",task.getResult().get("Branch"));

                    retval.setValue(map);
                }

            }
        });
       return retval;

    }

}

