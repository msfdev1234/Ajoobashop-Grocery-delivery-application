package com.ajoobashop.ajooba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ajoobashop.ajooba.LoadingDialougs.LoadingDialog;
import com.ajoobashop.ajooba.Prevalent.Prevalent;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class CodeVerificationActivity extends AppCompatActivity {
   private String phoneNumber, verificationId, name, activityFrom, countryCodePhoneNumber;
   private TextView hintCode;
   private Button verify_OTP_Btn;
   private FirebaseAuth mAuth;

   private ProgressBar progressBar;
   private PinView pinView;
   private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);


        verificationId = "";
        Paper.init(this);

        loadingDialog = new LoadingDialog(this);

        phoneNumber =getIntent().getStringExtra("phoneNumber");

        countryCodePhoneNumber ="+91"+ phoneNumber;

        name = getIntent().getStringExtra("name");

        activityFrom = getIntent().getStringExtra("activity");

        hintCode = findViewById(R.id.hint_code);
        progressBar = findViewById(R.id.progress_bar_Code);
        pinView = (PinView) findViewById(R.id.PinView_Code);
        verify_OTP_Btn = findViewById(R.id.Verify_OTP_Btn);

        mAuth = FirebaseAuth.getInstance();

        hintCode.setText(countryCodePhoneNumber);

        sendVerificationCode(countryCodePhoneNumber);

        verify_OTP_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code ;
                if (TextUtils.isEmpty(pinView.getText())){
                    pinView.setError("Enter Code");
                    pinView.requestFocus();
                }
                else if (pinView.getText().toString().length()<6){
                    pinView.setError("Enter Code");
                    pinView.requestFocus();
                }

                else {
                    code = pinView.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    VerifyCode(code);
                }


            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 100, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallback );
    }

    private void VerifyCode(String code){
        if (!verificationId.isEmpty()){
            loadingDialog.startLoadingDialog();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredentials(credential);
        }

    }

    private void signInWithCredentials(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (activityFrom.equals("signUp")){
                        progressBar.setVisibility(View.INVISIBLE);
                        updateDataBase(phoneNumber, name);
                    } else if (activityFrom.equals("login")){
                        progressBar.setVisibility(View.INVISIBLE);
                        loadingDialog.dismissDialog();
                        Toast.makeText(CodeVerificationActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Paper.book().write(Prevalent.UserPhoneKey,phoneNumber);
                        Intent intent = new Intent(CodeVerificationActivity.this, MainActivity.class);

                        intent.putExtra("isFrom", "login");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    loadingDialog.dismissDialog();
                    Toast.makeText(CodeVerificationActivity.this, "Invalid Code entry", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateDataBase(final String phoneNumber, String name) {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> userDataMap= new HashMap<>();
        userDataMap.put("name",name);
        userDataMap.put("phoneNumber",phoneNumber);
        userDataMap.put("cartTotal","0");
        userDataMap.put("orders","0");

        RootRef.child("Users").child(phoneNumber).updateChildren(userDataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(CodeVerificationActivity.this, "Congratulations Account created successfully", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                            Paper.book().write(Prevalent.UserPhoneKey,phoneNumber);
                            Intent intent = new Intent(CodeVerificationActivity.this, MainActivity.class);
                            intent.putExtra("isFrom", "signUp");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(CodeVerificationActivity.this, "Check your network connection", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }
                });

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code!=null){
                progressBar.setVisibility(View.VISIBLE);
                VerifyCode(code);
            } else {
                Toast.makeText(getApplicationContext(),"Verifying code Automatically",Toast.LENGTH_SHORT).show();

                signInWithCredentials(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            loadingDialog.dismissDialog();
            Toast.makeText(CodeVerificationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    };
}