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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.madmobiledevs.ajooba.LoadingDialougs.LoadingDialog;

public class LoginFragment extends Fragment {

    TextInputEditText phone_Number_EdtTxt;
    TextInputLayout layout_phone_Number;


    Button loginInButton;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login,container,false);

        phone_Number_EdtTxt = v.findViewById(R.id.Phn_Input_EdtText);
        layout_phone_Number = v.findViewById(R.id.phone_input_layout);
        loadingDialog =new LoadingDialog(getActivity());

        loginInButton = v.findViewById(R.id.login_Btn);

        loginInButton.setAlpha(.7f);
        loginInButton.setClickable(false);
        loginInButton.setEnabled(false);

        phone_Number_EdtTxt.addTextChangedListener(new TextWatcher() {
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

        loginInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginInButton.isEnabled()){
                    loadingDialog.startLoadingDialog();
                    String phoneNumber = phone_Number_EdtTxt.getText().toString();

                    valiDateInputData(phoneNumber);

                }

            }
        });

        return v;
    }

    private void valiDateInputData(final String phoneNumber) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("Users").child(phoneNumber).exists())){
                    loadingDialog.dismissDialog();
                    Intent intent = new Intent(getActivity(),CodeVerificationActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("activity", "login");
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(), "Create Your Account first", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkInput() {
        if (!TextUtils.isEmpty(phone_Number_EdtTxt.getText()) && phone_Number_EdtTxt.getText().toString().length()==10){
                loginInButton.setAlpha(1f);
                loginInButton.setClickable(true);
                loginInButton.setEnabled(true);
        }else {
                loginInButton.setAlpha(0.7f);
                loginInButton.setClickable(false);
                loginInButton.setEnabled(false);
        }
    }
}
