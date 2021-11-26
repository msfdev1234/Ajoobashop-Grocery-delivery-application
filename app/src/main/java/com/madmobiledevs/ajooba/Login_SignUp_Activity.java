package com.madmobiledevs.ajooba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login_SignUp_Activity extends AppCompatActivity {

    Button chs_Login_Btn, chs_SignUp_Btn;

    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__sign_up_);

        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Container_Profile,new LoginFragment()).commit();

        selectedFragment = null;

        chs_Login_Btn = findViewById(R.id.chs_login_Btn);
        chs_SignUp_Btn = findViewById(R.id.chs_SignUp_Btn);

        chs_Login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chs_SignUp_Btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                chs_Login_Btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.whiteColor));
                chs_SignUp_Btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.whiteColor));
                chs_Login_Btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Container_Profile,new LoginFragment()).commit();
            }
        });

        chs_SignUp_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chs_Login_Btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                chs_SignUp_Btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.whiteColor));
                chs_Login_Btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.whiteColor));
                chs_SignUp_Btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Container_Profile,new SignUpFragment()).commit();
            }
        });

    }
}