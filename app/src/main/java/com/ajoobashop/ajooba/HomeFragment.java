package com.ajoobashop.ajooba;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.essam.simpleplacepicker.MapActivity;
import com.essam.simpleplacepicker.utils.SimplePlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ajoobashop.ajooba.LoadingDialougs.LoadingDialog;
import com.ajoobashop.ajooba.Prevalent.Prevalent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;

public class HomeFragment extends Fragment {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LoadingDialog loadingDialog;

    TextView locationTextView;
    Context thiscontext;

    ImageSlider imageSlider;

    DatabaseReference CategoryRef;


    private double lat,longt;

    String type;
    String address;
    String hno;
    String flatNo;
    String apartment;

    private ImageView profile_ImgView;

    private CardView vegetable_Btn, fruits_Btn, grocery_Btn, diary_Btn;

    private String currentVersion, latestVersion;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home,container,false);

        thiscontext = container.getContext();

        loadingDialog = new LoadingDialog(getActivity());

        imageSlider = v.findViewById(R.id.Image_Slider);

        showImageSlider();

        locationTextView = v.findViewById(R.id.Your_Location_TxtView);
        
        vegetable_Btn = v.findViewById(R.id.vegetable_Chs_Btn_Home);
        fruits_Btn = v.findViewById(R.id.fruits_Chs_Btn_Home);
        grocery_Btn = v.findViewById(R.id.grocery_Chs_Btn_Home);
        diary_Btn = v.findViewById(R.id.dairy_Chs_Btn_Home);


        CategoryRef = FirebaseDatabase.getInstance().getReference().child("Categories");

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        profile_ImgView = v.findViewById(R.id.profile_Show);



//        layoutManager= new LinearLayoutManager(getContext());

        locationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Paper.book().read(Prevalent.UserPhoneKey)!=""){
                    Intent intent = new Intent(getActivity(), AddressActivity.class);
                    startActivityForResult(intent, 2); // suppose requestCode == 2
                } else {
                    Activity selectedActivity;
                    selectedActivity = new Login_SignUp_Activity();
                    Intent intent = new Intent(getActivity(), selectedActivity.getClass());
                    startActivity(intent);
                }



            }
        });

        profile_ImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity selectedActivity;

                if (Paper.book().read(Prevalent.UserPhoneKey)!=""){
                    selectedActivity = new ProfileActivity();
                } else {
                    selectedActivity = new Login_SignUp_Activity();
                }
                Intent intent = new Intent(getActivity(), selectedActivity.getClass());
                startActivity(intent);
            }
        });

        vegetable_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), Vegeteable_SubCategory_Activity.class);
                intent.putExtra("category", "vegetables");

                startActivity(intent);
            }
        });

        fruits_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), Vegeteable_SubCategory_Activity.class);
                intent.putExtra("category", "fruits");

                startActivity(intent);
            }
        });

        grocery_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), Vegeteable_SubCategory_Activity.class);
                intent.putExtra("category", "groceries");

                startActivity(intent);
            }
        });

        diary_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), Vegeteable_SubCategory_Activity.class);
                intent.putExtra("category", "diary");

                startActivity(intent);
            }
        });

        checkShopStatus();



        return v;
    }

    private void checkShopStatus(){
        loadingDialog.startLoadingDialog();

        DatabaseReference shop_Ref = FirebaseDatabase.getInstance().getReference().child("values");

        shop_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                latestVersion = dataSnapshot.child("version").getValue().toString();

               if (dataSnapshot.child("shopStatus").getValue().toString().equals("online")){

                   updateAvailabilityCheck();

               } else if (dataSnapshot.child("shopStatus").getValue().toString().equals("offline")){
                   loadingDialog.dismissDialog();
                   showOffline_Dialog();
               }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateAvailabilityCheck()  {

        if (latestVersion.equals(getCurrentVersion())){


            if (Paper.book().read(Prevalent.UserPhoneKey)!=""){
                set_Address_Initial();
            } else {
                loadingDialog.dismissDialog();
            }
        } else {
            loadingDialog.dismissDialog();

            show_Update_Dialog();
        }

    }

    private String getCurrentVersion(){
        PackageManager pm = getContext().getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(getContext().getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        return currentVersion;
    }

    private void show_Update_Dialog(){


        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("New Version Available")
                .setMessage("Please, update app to new version to continue using.")
                .setPositiveButton("update now", null) //Set to null. We override the onclick
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.madmobiledevs.ajooba&hl=en_IN&gl=US"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);

                        //Dismiss once everything is OK.

                    }
                });
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(dialogBackNavigationOnKey);
        dialog.show();

    }

    private void showOffline_Dialog() {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.dialog_shop_offline, null);
        final AlertDialog dialog_Offline = new AlertDialog.Builder(getActivity()).create();
        dialog_Offline.setView(deleteDialogView);
        dialog_Offline.setCanceledOnTouchOutside(false);

        dialog_Offline.setOnKeyListener(dialogBackNavigationOnKey);

        dialog_Offline.show();



    }
    private DialogInterface.OnKeyListener dialogBackNavigationOnKey = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {


                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                // move other fragment

                return true;
            }
            return false;
        }
    };

    private void set_Address_Initial(){


        DatabaseReference user_Location_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Paper.book().read(Prevalent.UserPhoneKey).toString());

        user_Location_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("address").exists()){
                    String address = dataSnapshot.child("address").getValue().toString();
                    locationTextView.setText(address);
                    loadingDialog.dismissDialog();


                } else {
                    loadingDialog.dismissDialog();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showImageSlider() {

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.img1));
        slideModels.add(new SlideModel(R.drawable.img3));
        slideModels.add(new SlideModel(R.drawable.img5));

        imageSlider.setImageList(slideModels, true);

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

                    lat = Double.parseDouble(data.getStringExtra("Lat"));
                    longt = Double.parseDouble(data.getStringExtra("Longt"));
                    type = data.getStringExtra("Type");
                    address = data.getStringExtra("Address");
                    hno = data.getStringExtra("Hno");
                    flatNo = data.getStringExtra("FlatNo");
                    apartment = data.getStringExtra("Apartment");


                    if (Double.toString(lat)!=null && Double.toString(longt)!=null){
                        locationTextView.setText(address);
                        updateUserLocation(lat, longt, address, locationTextView);

                    }

                }
                break;
            }
        }
    }

    private void checkIfSignedIn() {

    }

    private void updateUserLocation(double lat, double longt, final String address, final TextView locationTextView) {
        loadingDialog.startLoadingDialog();

        DatabaseReference user_Location_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Paper.book().read(Prevalent.UserPhoneKey).toString());

        HashMap<String, Object> userLocationMap= new HashMap<>();
        userLocationMap.put("latitude",Double.toString(lat));
        userLocationMap.put("longitude",Double.toString(longt));
        userLocationMap.put("address",address);

        user_Location_Ref.updateChildren(userLocationMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        loadingDialog.dismissDialog();
                    }
                });

    }


    private void showMap() {

        Intent intent = new Intent(getActivity(), MapActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(SimplePlacePicker.API_KEY,"AIzaSyCgGa22KPL4COnRqYfC3BjERA6UvNzPLp8");
        bundle.putString(SimplePlacePicker.COUNTRY,"India");
        bundle.putString(SimplePlacePicker.LANGUAGE,"English");

        intent.putExtras(bundle);
        startActivityForResult(intent, SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE);

    }






    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Allow Location Permision?")
                        .setMessage("Use Your Location")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
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
                    if (ContextCompat.checkSelfPermission(getContext(),
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
