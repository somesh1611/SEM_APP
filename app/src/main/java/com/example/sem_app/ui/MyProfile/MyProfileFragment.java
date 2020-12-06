package com.example.sem_app.ui.MyProfile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyProfileFragment extends Fragment {

    public static final String TAG = "TAG";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextView profile_name,profile_mail,profile_year,profile_branch;


    private MyProfileViewModel myProfileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myProfileViewModel =
                new ViewModelProvider(this).get(MyProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);
       /* final TextView textView = root.findViewById(R.id.text_gallery);
        myProfileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        }); */

        profile_name=root.findViewById(R.id.profile_name_text);
        profile_mail=root.findViewById(R.id.mail_text);
        profile_year=root.findViewById(R.id.year_text);
        profile_branch=root.findViewById(R.id.branch_text);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        String Id = firebaseUser.getUid();


        DocumentReference acc_ref=firebaseFirestore.collection("Users").document(Id);
        acc_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    String name=documentSnapshot.getString("Name");
                    String mail=documentSnapshot.getString("Email");
                    String year=documentSnapshot.getString("Year");
                    String branch=documentSnapshot.getString("Branch");


                    profile_name.setText(name);
                    profile_mail.setText(mail);
                    profile_year.setText(year);
                    profile_branch.setText(branch);
                }else{
                    Log.d(TAG, "document does not exist!");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to read data!");
            }
        });
        return root;
    }
}