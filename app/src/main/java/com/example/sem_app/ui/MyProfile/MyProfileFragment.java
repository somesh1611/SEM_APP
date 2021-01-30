package com.example.sem_app.ui.MyProfile;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sem_app.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyProfileFragment extends Fragment {

    public static final String TAG = "TAG";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextView profile_name,profile_mail,profile_year,profile_branch;
    FloatingActionButton fab_profile;
    EditText nameEdit;
    Spinner branch_edit,year_edit;
    ArrayList field=new ArrayList();


    private MyProfileViewModel myProfileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        profile_name=root.findViewById(R.id.profile_name_text);
        profile_mail=root.findViewById(R.id.mail_text);
        profile_year=root.findViewById(R.id.year_text);
        profile_branch=root.findViewById(R.id.branch_text);
        fab_profile=root.findViewById(R.id.fab_profile_edit);






        fab_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
                builder1.setTitle("Select Fields to be Updated");
                field.clear();
                builder1.setMultiChoiceItems(R.array.updateProfile, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        String[]item=getResources().getStringArray(R.array.updateProfile);

                        if(isChecked)
                        {
                            field.add(item[which]);
                        }
                        else if(field.contains(item[which])){
                            field.remove(item[which]);

                        }

                    }
                });
                builder1.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(field.isEmpty())
                        {
                            Toast.makeText(getActivity(),"Select atleast One Field to be updated!",Toast.LENGTH_SHORT).show();
                        }else {
                            update();
                        }


                    }
                });


                builder1.create();
                builder1.show();

                //////////////////////////////////////////////////////////////////////////////////

            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myProfileViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        myProfileViewModel.getDetails().observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                profile_name.setText(stringObjectHashMap.get("Name").toString());
                profile_mail.setText(stringObjectHashMap.get("Email").toString());
                profile_branch.setText(stringObjectHashMap.get("Branch").toString());
                profile_year.setText(stringObjectHashMap.get("Year").toString());
            }
        });


    }

    public void update()
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        final View profile_edit=getLayoutInflater().inflate(R.layout.profile_editor,null);
        nameEdit=profile_edit.findViewById(R.id.edit_name);
        year_edit=profile_edit.findViewById(R.id.edit_year);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.cls,android.R.layout.simple_list_item_activated_1);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        year_edit.setAdapter(adapter);
        //  year_edit.setOnItemSelectedListener(getActivity());
        branch_edit=profile_edit.findViewById(R.id.edit_branch);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.branch,android.R.layout.simple_list_item_activated_1);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        branch_edit.setAdapter(adapter2);
        //  branch_edit.setOnItemSelectedListener(this);
        builder.setView(profile_edit);
        builder.setTitle("Update your Profile");

        if(!field.contains("Name"))
        {
            nameEdit.setVisibility(View.GONE);
        }
        if(!field.contains("Branch"))
        {
            branch_edit.setVisibility(View.GONE);
        }
        if(!field.contains("Year"))
        {
            year_edit.setVisibility(View.GONE);
        }

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Map user_update_info=new HashMap();
                String un,uyear,ubranch;


                if(field.contains("Name"))
                {
                    un=nameEdit.getText().toString();
                    if(TextUtils.isEmpty(un))
                    {
                        Toast.makeText(getActivity(),"Name is Required!",Toast.LENGTH_SHORT).show();
                        return;

                    }else {
                        user_update_info.put("Name", un);
                    }
                }
                if(field.contains("Branch"))
                {
                    ubranch = branch_edit.getSelectedItem().toString();
                    user_update_info.put("Branch", ubranch);

                }
                if(field.contains("Year"))
                {
                    uyear = year_edit.getSelectedItem().toString();
                    user_update_info.put("Year", uyear);

                }


               firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseAuth= FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String ID = firebaseUser.getUid();


                DocumentReference acc_ref1=firebaseFirestore.collection("Users").document(ID);
                acc_ref1.update(user_update_info).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        myProfileViewModel.getDetails().observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
                            @Override
                            public void onChanged(HashMap<String, Object> stringObjectHashMap) {

                                profile_name.setText(stringObjectHashMap.get("Name").toString());
                                profile_mail.setText(stringObjectHashMap.get("Email").toString());
                                profile_branch.setText(stringObjectHashMap.get("Branch").toString());
                                profile_year.setText(stringObjectHashMap.get("Year").toString());

                            }
                        });
                       // getFragmentManager().beginTransaction().detach(MyProfileFragment.this).attach(MyProfileFragment.this).commit();
                        Toast.makeText(getActivity(),"Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        builder.create();
        builder.show();

    }

}