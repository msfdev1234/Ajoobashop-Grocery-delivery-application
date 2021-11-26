package com.madmobiledevs.ajooba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.essam.simpleplacepicker.MapActivity;
import com.essam.simpleplacepicker.utils.SimplePlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.madmobiledevs.ajooba.Dialogs.TimingsChsDialog;
import com.madmobiledevs.ajooba.LoadingDialougs.LoadingDialog;
import com.madmobiledevs.ajooba.Prevalent.Prevalent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import io.paperdb.Paper;

import static com.madmobiledevs.ajooba.HomeFragment.MY_PERMISSIONS_REQUEST_LOCATION;

public class DeliveryOptionsActivity extends AppCompatActivity {

    private CheckBox today_CheckBox, tomorrow_CheckBox;
    private TextView day_TxtVw, timing_TxtVw, locationTextView;
    private RelativeLayout timing_Chs_Btn;

    private String time;

    private Button pay_Btn, select_Location_Btn;

    private Activity activity;

    private RelativeLayout map_Icon_Bn;

//    private double lat,longt;

//    private String address;

    private LoadingDialog loadingDialog;

    String lat;
    String longt;
    String type;
    String address;
    String hno;
    String flatNo;
    String apartment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_options);

        address = "";

        activity = this;

        loadingDialog = new LoadingDialog(DeliveryOptionsActivity.this);

        today_CheckBox = findViewById(R.id.today_CheckBox);
        tomorrow_CheckBox = findViewById(R.id.tomorrow_CheckBox);
        timing_TxtVw = findViewById(R.id.timing_TxtVw_DelOpt);

        day_TxtVw = findViewById(R.id.sample_1_DelOpt);
        timing_Chs_Btn = findViewById(R.id.timing_Chs_Btn);
        pay_Btn = findViewById(R.id.proceedToPay_Btn_DelOpt);

        map_Icon_Bn = findViewById(R.id.Rel_layout_Map);
        locationTextView = findViewById(R.id.location_TxtVw_DelOpt);
        select_Location_Btn = findViewById(R.id.select_Location_Btn);

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();

        int current_Hour = (int) calendar.get(Calendar.HOUR_OF_DAY);

        if (current_Hour <= 23 && current_Hour >= 21){

            tomorrow_CheckBox.setChecked(true);
            today_CheckBox.setChecked(false);

            today_CheckBox.setEnabled(false);
            today_CheckBox.setClickable(false);
            today_CheckBox.setAlpha(.5f);

            if (today_CheckBox.isChecked()){
                day_TxtVw.setText(today_CheckBox.getText());
                setTimeTextVw(today_CheckBox.getText().toString());
            } else if (tomorrow_CheckBox.isChecked()){
                day_TxtVw.setText(tomorrow_CheckBox.getText());
                setTimeTextVw(tomorrow_CheckBox.getText().toString());
            }

        }
        else {

            today_CheckBox.setChecked(true);
            tomorrow_CheckBox.setChecked(false);

            today_CheckBox.setEnabled(true);
            today_CheckBox.setClickable(true);
            today_CheckBox.setAlpha(1);

            tomorrow_CheckBox.setEnabled(true);
            tomorrow_CheckBox.setClickable(true);
            tomorrow_CheckBox.setAlpha(1);

            if (today_CheckBox.isChecked()){
                day_TxtVw.setText(today_CheckBox.getText());
                setTimeTextVw(today_CheckBox.getText().toString());
            } else if (tomorrow_CheckBox.isChecked()){
                day_TxtVw.setText(tomorrow_CheckBox.getText());
                setTimeTextVw(tomorrow_CheckBox.getText().toString());
            }


        }

       today_CheckBox.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               today_CheckBox.setChecked(true);
               tomorrow_CheckBox.setChecked(false);
               day_TxtVw.setText(today_CheckBox.getText());
               setTimeTextVw(today_CheckBox.getText().toString());
               
           }
       });

       tomorrow_CheckBox.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               tomorrow_CheckBox.setChecked(true);
               today_CheckBox.setChecked(false);
               day_TxtVw.setText(tomorrow_CheckBox.getText());
           }
       });

       timing_Chs_Btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String day = "";
               if (today_CheckBox.isChecked()){
                   day = today_CheckBox.getText().toString();
               } else if (tomorrow_CheckBox.isChecked()){
                   day = tomorrow_CheckBox.getText().toString();
               }


               createDialog(day);
           }
       });

       pay_Btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (address.equals("")){
                   chooseAddressActivity();
               } else {
                   Intent intent = new Intent(activity ,PaymentAndOrderPlaceActivity.class);

                   intent.putExtra("day", day_TxtVw.getText().toString());
                   intent.putExtra("timeSlot", timing_TxtVw.getText().toString());
                   intent.putExtra("type", type);
                   intent.putExtra("lat", lat);
                   intent.putExtra("longt", longt);
                   intent.putExtra("address", address);
                   intent.putExtra("hno", hno);
                   intent.putExtra("flatNo", flatNo);
                   intent.putExtra("apartment", apartment);

                   startActivity(intent);
               }

           }
       });


       map_Icon_Bn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               showMap();
           }
       });

       select_Location_Btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               chooseAddressActivity();
           }
       });

    }

    private void chooseAddressActivity() {
        Intent intent = new Intent(DeliveryOptionsActivity.this,AddressActivity.class);
        startActivityForResult(intent, 2); // suppose requestCode == 2

    }



    private void showMap() {

        Intent intent = new Intent(DeliveryOptionsActivity.this, MapActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(SimplePlacePicker.API_KEY,"AIzaSyCgGa22KPL4COnRqYfC3BjERA6UvNzPLp8");
        bundle.putString(SimplePlacePicker.COUNTRY,"India");
        bundle.putString(SimplePlacePicker.LANGUAGE,"English");

        intent.putExtras(bundle);
        startActivityForResult(intent, SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE);

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        lat = data.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA,1);
//        longt = data.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA,1);
//        address =  data.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS);
//
//        if (Double.toString(lat)!=null && Double.toString(longt)!=null){
//            locationTextView.setText(address);
//
//            if (!(Paper.book().read(Prevalent.UserPhoneKey) == "")){
//                updateUserLocation(lat , longt , address , locationTextView);
//            }
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (2) : {
                if (resultCode == Activity.RESULT_OK) {

                    lat = data.getStringExtra("Lat");
                    longt = data.getStringExtra("Longt");
                    type = data.getStringExtra("Type");
                    address = data.getStringExtra("Address");
                    hno = data.getStringExtra("Hno");
                    flatNo = data.getStringExtra("FlatNo");
                    apartment = data.getStringExtra("Apartment");


                    if (lat!=null && longt!=null){
                        locationTextView.setText(type+"\n"+address);
                        updateUserLocation(Double.parseDouble(lat), Double.parseDouble(longt), address, locationTextView);

                    }

                }
                break;
            }
        }
    }



    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
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
                                ActivityCompat.requestPermissions(DeliveryOptionsActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
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

    private void updateUserLocation(double lat, double longt, final String address, final TextView locationTextView) {
        loadingDialog.startLoadingDialog();

        DatabaseReference user_Location_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Paper.book().read(Prevalent.UserPhoneKey).toString());

        HashMap<String, Object> userLocationMap= new HashMap<>();
        userLocationMap.put("latitude",Double.toString(lat));
        userLocationMap.put("longitude",Double.toString(longt));
        userLocationMap.put("address",address);
        userLocationMap.put("type",type);

        user_Location_Ref.updateChildren(userLocationMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        loadingDialog.dismissDialog();
                        locationTextView.setText(address);
                    }
                });

    }

    private void setTimeTextVw(String text) {

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();

        int current_Hour = (int) calendar.get(Calendar.HOUR_OF_DAY);

        if (text.equals("Today")){

            if (current_Hour >= 7 && current_Hour < 9 ){
                timing_TxtVw.setText("7:00 AM - 9:00 AM");
            }
            else if (current_Hour >= 9 && current_Hour < 11){
                timing_TxtVw.setText("9:00 AM - 11:00 AM");
            }
            else if (current_Hour >= 11 && current_Hour < 13){
                timing_TxtVw.setText("11:00 AM - 1:00 PM");
            }
            else if (current_Hour >= 13 && current_Hour < 15){
                timing_TxtVw.setText("1:00 PM - 3:00 PM");
            }
            else if (current_Hour >= 15 && current_Hour < 17){
                timing_TxtVw.setText("3:00 PM - 5:00 PM");
            }
            else if (current_Hour >= 17 && current_Hour < 19){
                timing_TxtVw.setText("5:00 PM - 7:00 PM");
            }
            else if (current_Hour >= 19 && current_Hour < 21){
                timing_TxtVw.setText("7:00 PM - 9:00 PM");
            }
            else {
                timing_TxtVw.setText("7:00 AM - 9:00 AM");
            }

        }
        else if (text.equals("Tomorrow")){
            timing_TxtVw.setText("7:00 AM - 9:00 AM");
        }
    }

    private void createDialog(String day) {
        final TimingsChsDialog timingsChsDialog = new TimingsChsDialog(this, day);
        timingsChsDialog.show();


        check_With_Current_Time(timingsChsDialog, timingsChsDialog.time1_Btn, timingsChsDialog.time2_Btn, timingsChsDialog.time3_Btn, timingsChsDialog.time4_Btn, timingsChsDialog.time5_Btn, timingsChsDialog.time6_Btn, timingsChsDialog.time7_Btn);

        timingsChsDialog.time1_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = timingsChsDialog.time1_Btn.getText().toString();
                timingsChsDialog.dismiss();
                timing_TxtVw.setText(time);
            }
        });
        timingsChsDialog.time2_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = timingsChsDialog.time2_Btn.getText().toString();
                timingsChsDialog.dismiss();
                timing_TxtVw.setText(time);
            }
        });
        timingsChsDialog.time3_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = timingsChsDialog.time3_Btn.getText().toString();
                timingsChsDialog.dismiss();
                timing_TxtVw.setText(time);
            }
        });
        timingsChsDialog.time4_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = timingsChsDialog.time4_Btn.getText().toString();
                timingsChsDialog.dismiss();
                timing_TxtVw.setText(time);
            }
        });
        timingsChsDialog.time5_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = timingsChsDialog.time5_Btn.getText().toString();
                timingsChsDialog.dismiss();
                timing_TxtVw.setText(time);
            }
        });
        timingsChsDialog.time6_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = timingsChsDialog.time6_Btn.getText().toString();
                timingsChsDialog.dismiss();
                timing_TxtVw.setText(time);
            }
        });
        timingsChsDialog.time7_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = timingsChsDialog.time7_Btn.getText().toString();
                timingsChsDialog.dismiss();
                timing_TxtVw.setText(time);
            }
        });
    }

    private void check_With_Current_Time(TimingsChsDialog timingsChsDialog, TextView txtVw1, TextView txtVw2, TextView txtVw3, TextView txtVw4, TextView txtVw5, TextView txtVw6, TextView txtVw7 ){

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();


       // Double current_Hour = calendar.get(Calendar.HOUR_OF_DAY);

        int current_Hour = (int) calendar.get(Calendar.HOUR_OF_DAY);

      //  int current_Hour = 14;


        if (timingsChsDialog.day_TxtVw.getText().equals("Today")){

            if (current_Hour >= 7 && current_Hour < 9 ){
                txtVw1.setEnabled(true);
                txtVw1.setClickable(true);
                txtVw1.setAlpha(1);

                txtVw2.setEnabled(true);
                txtVw2.setClickable(true);
                txtVw2.setAlpha(1);

                txtVw3.setEnabled(true);
                txtVw3.setClickable(true);
                txtVw3.setAlpha(1);

                txtVw4.setEnabled(true);
                txtVw4.setClickable(true);
                txtVw4.setAlpha(1);

                txtVw5.setEnabled(true);
                txtVw5.setClickable(true);
                txtVw5.setAlpha(1);

                txtVw6.setEnabled(true);
                txtVw6.setClickable(true);
                txtVw6.setAlpha(1);

                txtVw7.setEnabled(true);
                txtVw7.setClickable(true);
                txtVw7.setAlpha(1);

            }
            else if (current_Hour >= 9 && current_Hour < 11){
                txtVw1.setEnabled(false);
                txtVw1.setClickable(false);
                txtVw1.setAlpha(.4f);

                txtVw2.setEnabled(true);
                txtVw2.setClickable(true);
                txtVw2.setAlpha(1);

                txtVw3.setEnabled(true);
                txtVw3.setClickable(true);
                txtVw3.setAlpha(1);

                txtVw4.setEnabled(true);
                txtVw4.setClickable(true);
                txtVw4.setAlpha(1);

                txtVw5.setEnabled(true);
                txtVw5.setClickable(true);
                txtVw5.setAlpha(1);

                txtVw6.setEnabled(true);
                txtVw6.setClickable(true);
                txtVw6.setAlpha(1);

                txtVw7.setEnabled(true);
                txtVw7.setClickable(true);
                txtVw7.setAlpha(1);
            }
            else if (current_Hour >= 11 && current_Hour < 13){
                txtVw1.setEnabled(false);
                txtVw1.setClickable(false);
                txtVw1.setAlpha(.4f);

                txtVw2.setEnabled(false);
                txtVw2.setClickable(false);
                txtVw2.setAlpha(.4f);

                txtVw3.setEnabled(true);
                txtVw3.setClickable(true);
                txtVw3.setAlpha(1);

                txtVw4.setEnabled(true);
                txtVw4.setClickable(true);
                txtVw4.setAlpha(1);

                txtVw5.setEnabled(true);
                txtVw5.setClickable(true);
                txtVw5.setAlpha(1);

                txtVw6.setEnabled(true);
                txtVw6.setClickable(true);
                txtVw6.setAlpha(1);

                txtVw7.setEnabled(true);
                txtVw7.setClickable(true);
                txtVw7.setAlpha(1);

            }
            else if (current_Hour >= 13 && current_Hour < 15){
                txtVw1.setEnabled(false);
                txtVw1.setClickable(false);
                txtVw1.setAlpha(.4f);

                txtVw2.setEnabled(false);
                txtVw2.setClickable(false);
                txtVw2.setAlpha(.4f);

                txtVw3.setEnabled(false);
                txtVw3.setClickable(false);
                txtVw3.setAlpha(.4f);

                txtVw4.setEnabled(true);
                txtVw4.setClickable(true);
                txtVw4.setAlpha(1);

                txtVw5.setEnabled(true);
                txtVw5.setClickable(true);
                txtVw5.setAlpha(1);

                txtVw6.setEnabled(true);
                txtVw6.setClickable(true);
                txtVw6.setAlpha(1);

                txtVw7.setEnabled(true);
                txtVw7.setClickable(true);
                txtVw7.setAlpha(1);

            }
            else if (current_Hour >= 15 && current_Hour < 17){
                txtVw1.setEnabled(false);
                txtVw1.setClickable(false);
                txtVw1.setAlpha(.4f);

                txtVw2.setEnabled(false);
                txtVw2.setClickable(false);
                txtVw2.setAlpha(.4f);

                txtVw3.setEnabled(false);
                txtVw3.setClickable(false);
                txtVw3.setAlpha(.4f);

                txtVw4.setEnabled(false);
                txtVw4.setClickable(false);
                txtVw4.setAlpha(.4f);

                txtVw5.setEnabled(true);
                txtVw5.setClickable(true);
                txtVw5.setAlpha(1);

                txtVw6.setEnabled(true);
                txtVw6.setClickable(true);
                txtVw6.setAlpha(1);

                txtVw7.setEnabled(true);
                txtVw7.setClickable(true);
                txtVw7.setAlpha(1);

            }
            else if (current_Hour >= 17 && current_Hour < 19){
                txtVw1.setEnabled(false);
                txtVw1.setClickable(false);
                txtVw1.setAlpha(.4f);

                txtVw2.setEnabled(false);
                txtVw2.setClickable(false);
                txtVw2.setAlpha(.4f);

                txtVw3.setEnabled(false);
                txtVw3.setClickable(false);
                txtVw3.setAlpha(.4f);

                txtVw4.setEnabled(false);
                txtVw4.setClickable(false);
                txtVw4.setAlpha(.4f);

                txtVw5.setEnabled(false);
                txtVw5.setClickable(false);
                txtVw5.setAlpha(.4f);

                txtVw6.setEnabled(true);
                txtVw6.setClickable(true);
                txtVw6.setAlpha(1);

                txtVw7.setEnabled(true);
                txtVw7.setClickable(true);
                txtVw7.setAlpha(1);

            }
            else if (current_Hour >= 19 && current_Hour < 21){
                txtVw1.setEnabled(false);
                txtVw1.setClickable(false);
                txtVw1.setAlpha(.4f);

                txtVw2.setEnabled(false);
                txtVw2.setClickable(false);
                txtVw2.setAlpha(.4f);

                txtVw3.setEnabled(false);
                txtVw3.setClickable(false);
                txtVw3.setAlpha(.4f);

                txtVw4.setEnabled(false);
                txtVw4.setClickable(false);
                txtVw4.setAlpha(.4f);

                txtVw5.setEnabled(false);
                txtVw5.setClickable(false);
                txtVw5.setAlpha(.4f);

                txtVw6.setEnabled(false);
                txtVw6.setClickable(false);
                txtVw6.setAlpha(.4f);

                txtVw7.setEnabled(true);
                txtVw7.setClickable(true);
                txtVw7.setAlpha(1);
            }
        }

        else if (timingsChsDialog.day_TxtVw.getText().equals("Tomorrow")){

            txtVw1.setEnabled(true);
            txtVw1.setClickable(true);
            txtVw1.setAlpha(1);

            txtVw2.setEnabled(true);
            txtVw2.setClickable(true);
            txtVw2.setAlpha(1);

            txtVw3.setEnabled(true);
            txtVw3.setClickable(true);
            txtVw3.setAlpha(1);

            txtVw4.setEnabled(true);
            txtVw4.setClickable(true);
            txtVw4.setAlpha(1);

            txtVw5.setEnabled(true);
            txtVw5.setClickable(true);
            txtVw5.setAlpha(1);

            txtVw6.setEnabled(true);
            txtVw6.setClickable(true);
            txtVw6.setAlpha(1);

            txtVw7.setEnabled(true);
            txtVw7.setClickable(true);
            txtVw7.setAlpha(1);

        }
    }


}