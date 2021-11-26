package com.madmobiledevs.ajooba;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.madmobiledevs.ajooba.LoadingDialougs.LoadingDialog;

import java.util.HashMap;

public class SignUpFragment extends Fragment {

    TextInputEditText phone_Number_EdtTxt_SignUp, name_EdtTxt_SignUp;
    TextInputLayout layout_phone_Number_SignUp, layout_name_SignUp;

    Button signUpButton;

    LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.signupfragment,container,false);

        loadingDialog =new LoadingDialog(getActivity());

        phone_Number_EdtTxt_SignUp = v.findViewById(R.id.Phn_Input_EdtText_signup);
        layout_phone_Number_SignUp = v.findViewById(R.id.phone_input_layout_signup);
        name_EdtTxt_SignUp = v.findViewById(R.id.name_Input_EdtText_signup);
        layout_name_SignUp= v.findViewById(R.id.name_input_layout_signup);

        signUpButton = v.findViewById(R.id.signUp_Btn);

        signUpButton.setAlpha(.7f);
        signUpButton.setClickable(false);
        signUpButton.setEnabled(false);

        phone_Number_EdtTxt_SignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        name_EdtTxt_SignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (signUpButton.isEnabled()){
                    loadingDialog.startLoadingDialog();
                    String phoneNumber = phone_Number_EdtTxt_SignUp.getText().toString();
                    String name = name_EdtTxt_SignUp.getText().toString();

                    valiDateInputData(phoneNumber, name);

                }

            }
        });

        return v;
    }

    private void valiDateInputData(final String phoneNumber, final String name) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phoneNumber).exists())){
                    loadingDialog.dismissDialog();
                    Intent intent = new Intent(getActivity(),CodeVerificationActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("name", name_EdtTxt_SignUp.getText().toString());

                    intent.putExtra("activity", "signUp");
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(), "User with this number already exists", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkInput() {
        if (!TextUtils.isEmpty(phone_Number_EdtTxt_SignUp.getText()) && phone_Number_EdtTxt_SignUp.getText().toString().length()==10 && !TextUtils.isEmpty(name_EdtTxt_SignUp.getText())){
            signUpButton.setAlpha(1f);
            signUpButton.setClickable(true);
            signUpButton.setEnabled(true);
        }else {
            signUpButton.setAlpha(0.7f);
            signUpButton.setClickable(false);
            signUpButton.setEnabled(false);
        }
    }


}
