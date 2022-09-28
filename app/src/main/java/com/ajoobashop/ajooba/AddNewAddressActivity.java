package com.ajoobashop.ajooba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.essam.simpleplacepicker.MapActivity;
import com.essam.simpleplacepicker.utils.SimplePlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.ajoobashop.ajooba.LoadingDialougs.LoadingDialog;
import com.ajoobashop.ajooba.Prevalent.Prevalent;

import java.util.HashMap;

import io.paperdb.Paper;

public class AddNewAddressActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private double lat,longt;
    LoadingDialog loadingDialog;

    private String address = "";
    private String type_address = "";

    Button addAddress_Btn;
    TextView home_Btn, office_Btn, other_Btn;

    TextInputLayout hno_input_Layout, apartment_input_Layout, flatNo_input_Layout, nickName_input_Layout;
    TextInputEditText hno_input,apartment_input, flatNo_input, nickName_input;

    TextView locateMe_Btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);

        loadingDialog = new LoadingDialog(AddNewAddressActivity.this);
        Paper.init(this);

        home_Btn = findViewById(R.id.home_Btn);
        office_Btn = findViewById(R.id.office_Btn);
        other_Btn = findViewById(R.id.others_Btn);

        hno_input_Layout = findViewById(R.id.HouseNo_EdtTxt_Layout);
        apartment_input_Layout = findViewById(R.id.AprtName_EdtTxt_Layout);
        flatNo_input_Layout = findViewById(R.id.FlatNo_EdtTxt_Layout);
        nickName_input_Layout = findViewById(R.id.NickName_EdtTxt_Layout);

        hno_input = findViewById(R.id.HouseNo_Name_EdtTxt);
        apartment_input = findViewById(R.id.AprtName_EdtTxt);
        flatNo_input = findViewById(R.id.FlatNo_EdtTxt);
        nickName_input = findViewById(R.id.NickName_EdtTxt);

        locateMe_Btn = findViewById(R.id.locate_Me_Btn);

        addAddress_Btn = findViewById(R.id.add_Btn_Adr_Db);

        home_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type_address = home_Btn.getText().toString();
                nickName_input_Layout.setVisibility(View.INVISIBLE);
                home_Btn.setBackgroundResource(R.drawable.address_type_bg);
                office_Btn.setBackgroundResource(R.drawable.address_type_plain_bg);
                other_Btn.setBackgroundResource(R.drawable.address_type_plain_bg);
            }
        });

        office_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type_address = office_Btn.getText().toString();
                nickName_input_Layout.setVisibility(View.INVISIBLE);
                office_Btn.setBackgroundResource(R.drawable.address_type_bg);
                home_Btn.setBackgroundResource(R.drawable.address_type_plain_bg);
                other_Btn.setBackgroundResource(R.drawable.address_type_plain_bg);
            }
        });

        other_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickName_input_Layout.setVisibility(View.VISIBLE);
                type_address = "";
                other_Btn.setBackgroundResource(R.drawable.address_type_bg);
                home_Btn.setBackgroundResource(R.drawable.address_type_plain_bg);
                office_Btn.setBackgroundResource(R.drawable.address_type_plain_bg);
            }
        });

        locateMe_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    checkLocationPermission();

            }
        });


        addAddress_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               validateData();
            }
        });

    }

    private void validateData() {

        if (hno_input.getText().toString().equals("")){
            hno_input_Layout.setError("*Hno is mandatory");
        }
        else if (apartment_input.getText().toString().equals("")){
            apartment_input_Layout.setError("*mandatory");
        }
        else if (type_address.equals("")){
            if (nickName_input_Layout.getVisibility() == View.VISIBLE){
                if (nickName_input.getText().toString().equals("")){
                    Toast.makeText(this, "Enter Nick name for other address", Toast.LENGTH_SHORT).show();
                } else {
                    type_address = nickName_input.getText().toString();
                }

            }
            else {
                Toast.makeText(this, "Choose Nick name for address", Toast.LENGTH_SHORT).show();
            }

        }
       else {
            if (address.equals("")){

                checkLocationPermission();

            } else {

                storeDataBase();
            }
        }


    }

    private void storeDataBase() {

        loadingDialog.startLoadingDialog();

        HashMap<String,Object> address_Map= new HashMap<>();
        address_Map.put("hno", hno_input.getText().toString());
        address_Map.put("apartment", apartment_input.getText().toString());
        if (!flatNo_input.getText().toString().equals("")){
            address_Map.put("flatNo", flatNo_input.getText().toString());
        } else {
            address_Map.put("flatNo", " ");
        }
        address_Map.put("type", type_address);
        address_Map.put("address", address);
        address_Map.put("lat", Double.toString(lat));
        address_Map.put("longt", Double.toString(longt));
        address_Map.put("isDefault", "0");

        FirebaseDatabase.getInstance().getReference().child("Addresses")
                .child(Paper.book().read(Prevalent.UserPhoneKey).toString())
                .child(type_address)
                .updateChildren(address_Map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddNewAddressActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
                loadingDialog.dismissDialog();

                onBackPressed();
            }
        });

    }

    private void showMap() {

        Intent intent = new Intent(AddNewAddressActivity.this, MapActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(SimplePlacePicker.API_KEY,"AIzaSyCgGa22KPL4COnRqYfC3BjERA6UvNzPLp8");
        bundle.putString(SimplePlacePicker.COUNTRY,"India");
        bundle.putString(SimplePlacePicker.LANGUAGE,"English");

        intent.putExtras(bundle);
        startActivityForResult(intent, SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        lat = data.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA,1);
        longt = data.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA,1);
        address =  data.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS);

        if (Double.toString(lat)!=null && Double.toString(longt)!=null){
            locateMe_Btn.setText(address);

        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((AddNewAddressActivity.this),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Allow Location Permision?")
                        .setMessage("Use Your Location")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AddNewAddressActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(AddNewAddressActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {

            showMap();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    showMap();
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        showMap();

                        //Request location updates:
                        LocationManager locationManager;
                        //   locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
            }
        }
    }
}