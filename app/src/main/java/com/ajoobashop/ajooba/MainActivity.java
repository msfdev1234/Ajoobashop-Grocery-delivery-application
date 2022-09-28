package com.ajoobashop.ajooba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ajoobashop.ajooba.Prevalent.Prevalent;

import io.paperdb.Paper;
public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        if (!Paper.book().contains(Prevalent.UserPhoneKey)){
            Paper.book().write(Prevalent.UserPhoneKey, "");
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Fragment_Contaioner,new HomeFragment()).commit();

        bottomNavigationView = findViewById(R.id.Bottom_NavigationView);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                Activity selectedActivity = null;

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_Category:
                        selectedFragment = new CategoryFragment();
                        break;
                    case R.id.nav_Basket:
                        if(!(Paper.book().read(Prevalent.UserPhoneKey).equals(""))){
                            selectedFragment = new CartFragment();
                        }
                        else {

                            if (Paper.book().read(Prevalent.UserPhoneKey)!=""){
                                selectedActivity = new ProfileActivity();
                            } else {
                                selectedActivity = new Login_SignUp_Activity();
                            }
                        }
                        break;
                    case R.id.nav_Profile:

                        if (Paper.book().read(Prevalent.UserPhoneKey)!=""){
                            selectedActivity = new ProfileActivity();
                        } else {
                            selectedActivity = new Login_SignUp_Activity();
                        }
                        break;
                }

                if (selectedFragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Contaioner,selectedFragment).commit();
                } else if (selectedActivity!=null){
                    Intent intent = new Intent(MainActivity.this, selectedActivity.getClass());
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}