package com.madmobiledevs.ajooba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.madmobiledevs.ajooba.Dialogs.OrderPlaced_Dialog;
import com.madmobiledevs.ajooba.LoadingDialougs.LoadingDialog;
import com.madmobiledevs.ajooba.Prevalent.Prevalent;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;

import static com.madmobiledevs.ajooba.R.style.full_screen_dialog;

public class PaymentAndOrderPlaceActivity extends AppCompatActivity implements PaymentResultListener {

    RelativeLayout cod_Layout_Btn, pay_Online_Btn;
    CheckBox checkBox_cod , checkBox_payment;

    private String orderId_For_Test;

    Button placeOrder_Or_Pay_Btn;

    private List<String> productIds;
    private List<String> productQuantities;
    private List<String> productNames;
    private List<String> productRates;
    private List<String> productCategoryNames;
    private List<String> productMultiples;
    private List<String> productSubCategoryNames;
    private List<String> productUnits;
    private List<String> productImages;

    private LoadingDialog loadingDialog;

    private TextView Grand_Total_Amount_TxtVw;
    private TextView cart_Value_TextVw;
    private TextView delivery_Value_TxtVw;

    private int total_Cart_Value, delivery_Charge, grand_Total, user_Orders_No;
    private String name_User, phone_Number;

    private String day, timeSlot;
    private String  Full_Date;
    private String date_Single;
    private int itemCount;

    private String latitude;
    private String longitude;
    private String type;
    private String address;
    private String hno;
    private String flatNo;
    private String apartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_and_order_place);

        day = getIntent().getStringExtra("day");
        timeSlot = getIntent().getStringExtra("timeSlot");
        type = getIntent().getStringExtra("type");
        latitude = getIntent().getStringExtra("lat");
        longitude = getIntent().getStringExtra("longt");
        hno = getIntent().getStringExtra("hno");
        address = getIntent().getStringExtra("address");
        apartment = getIntent().getStringExtra("apartment");
        flatNo = getIntent().getStringExtra("flatNo");


        Checkout.preload(getApplicationContext());
        itemCount = 0;

        Paper.init(this);

        productIds= new ArrayList<String>();
        productQuantities= new ArrayList<String>();
        productNames= new ArrayList<String>();
        productRates= new ArrayList<String>();
        productCategoryNames= new ArrayList<String>();
        productMultiples= new ArrayList<String>();
        productSubCategoryNames= new ArrayList<String>();
        productUnits= new ArrayList<String>();
        productImages= new ArrayList<String>();

        loadingDialog = new LoadingDialog(this);

        cod_Layout_Btn = findViewById(R.id.Lt_COD);
        pay_Online_Btn = findViewById(R.id.Lt_Payment);

        checkBox_cod = findViewById(R.id.cash_checkbox);
        checkBox_payment = findViewById(R.id.pay_CheckBox);

        placeOrder_Or_Pay_Btn = findViewById(R.id.Order_Place_Or_Pay_Btn_PamntOrder);

        Grand_Total_Amount_TxtVw = findViewById(R.id.Total_Value);
        cart_Value_TextVw = findViewById(R.id.Cart_Value);
        delivery_Value_TxtVw = findViewById(R.id.Delivery_Value);

        checkBox_cod.setChecked(false);
        checkBox_payment.setChecked(true);
        placeOrder_Or_Pay_Btn.setText("Pay Now");

        cod_Layout_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox_cod.setChecked(true);
                checkBox_payment.setChecked(false);
                placeOrder_Or_Pay_Btn.setText("Place Order");

            }
        });

        pay_Online_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox_cod.setChecked(false);
                checkBox_payment.setChecked(true);
                placeOrder_Or_Pay_Btn.setText("Pay Now");
            }
        });

        checkBox_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox_cod.setChecked(true);
                checkBox_payment.setChecked(false);
                placeOrder_Or_Pay_Btn.setText("Place Order");
            }
        });

        checkBox_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox_cod.setChecked(false);
                checkBox_payment.setChecked(true);
                placeOrder_Or_Pay_Btn.setText("Pay Now");
            }
        });

        placeOrder_Or_Pay_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeOrder_Or_Pay_Btn.getText().equals("Place Order")){

                    place_Order("null", "null");

                }
                else if (placeOrder_Or_Pay_Btn.getText().equals("Pay Now")){

                    startPayment(grand_Total,name_User);

                }
            }
        });
        get_Cart_Items_And_Total();

    }

    private void startPayment(double amount,String userName) {

        Activity activity = this;

        Checkout checkout = new Checkout();

        try {

            JSONObject options = new JSONObject();

            options.put("name", userName);
            options.put("description", " Payment");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", amount*100);//pass amount in currency subunits

            JSONObject preFill = new JSONObject();
            preFill.put("email", "YourMail@example.com");
            preFill.put("contact", Paper.book().read(Prevalent.UserPhoneKey).toString());

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch(Exception e) {
            Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void checkMethod(){



    }

    private void place_Order(String paymentStatus, String transactionId) {

        final LoadingDialog loadingDialog = new LoadingDialog(this);

        loadingDialog.startLoadingDialog();

 //       final String order_Id1 = Integer.toString(user_Orders_No + 1) + Paper.book().read(Prevalent.UserPhoneKey).toString();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dMyyHms");
        LocalDateTime now = LocalDateTime.now();

        String last_Digits_Phone = Paper.book().read(Prevalent.UserPhoneKey).toString().substring(Paper.book().read(Prevalent.UserPhoneKey).toString().length() - 4);

        final String order_Id = dtf.format(now) +"" +last_Digits_Phone;


        if (day.equals("Today")){
            Full_Date = new SimpleDateFormat("EEE, d MMM yyyy", java.util.Locale.ENGLISH).format(Calendar.getInstance().getTime());
            Full_Date = Full_Date.replace(",", "");

            date_Single = new SimpleDateFormat("d", java.util.Locale.ENGLISH).format(Calendar.getInstance().getTime());

        } else if (day.equals("Tomorrow")){

            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            Date tomorrow = calendar.getTime();

            Full_Date = new SimpleDateFormat("EEE, d MMM yyyy", java.util.Locale.ENGLISH).format(tomorrow);
            Full_Date = Full_Date.replace(",", "");

            date_Single = new SimpleDateFormat("d", java.util.Locale.ENGLISH).format(tomorrow);
        }

        orderId_For_Test = order_Id;

        DatabaseReference order_Ref = FirebaseDatabase.getInstance().getReference().child("orders").child(Paper.book().read(Prevalent.UserPhoneKey).toString()).child(order_Id);

        final HashMap<String, Object> order_Details_hashMap = new HashMap<>();

        order_Details_hashMap.put("totalAmount", Integer.toString(total_Cart_Value));
        order_Details_hashMap.put("deliveryCharge", Integer.toString(delivery_Charge));
        order_Details_hashMap.put("grandTotal", Integer.toString(grand_Total));
        order_Details_hashMap.put("orderId", order_Id);

        order_Details_hashMap.put("name", name_User);
        order_Details_hashMap.put("address", address);
        order_Details_hashMap.put("latitude", latitude);
        order_Details_hashMap.put("longitude", longitude);
        order_Details_hashMap.put("type", type);
        order_Details_hashMap.put("hno", hno);
        order_Details_hashMap.put("apartment", apartment);
        order_Details_hashMap.put("flatNo", flatNo);


        order_Details_hashMap.put("day", day);
        order_Details_hashMap.put("timeSlot", timeSlot);
        order_Details_hashMap.put("paymentStatus", paymentStatus);
        order_Details_hashMap.put("DeliveryPartner", "null");


        order_Details_hashMap.put("status_Order", "Active");

        order_Details_hashMap.put("user_status_Order", "waiting");
        order_Details_hashMap.put("fullDay", Full_Date);
        order_Details_hashMap.put("date_Single", date_Single);

        order_Details_hashMap.put("orderCount", Integer.toString(user_Orders_No+1));
        order_Details_hashMap.put("phoneNumber", Paper.book().read(Prevalent.UserPhoneKey).toString());
        order_Details_hashMap.put("itemCount", Integer.toString(itemCount));

        if (!transactionId.equals("null")){
            order_Details_hashMap.put("transactionId", transactionId);
        }

        order_Ref.updateChildren(order_Details_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                final DatabaseReference Admin_order_Ref = FirebaseDatabase.getInstance().getReference().child("Admin_orders").child(order_Id);

                Admin_order_Ref.updateChildren(order_Details_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DatabaseReference order_Products_Ref = FirebaseDatabase.getInstance().getReference().child("orderProducts");

                        for(int i=0 ; i<productIds.size() ; i++){
                            String pid,name,quantity,rate,categoryName,subCategoryName,multiple,units,images;

                            pid = productIds.get(i);
                            name = productNames.get(i);
                            quantity = productQuantities.get(i);
                            rate = productRates.get(i);
                            categoryName = productCategoryNames.get(i);
                            subCategoryName = productSubCategoryNames.get(i);
                            multiple = productMultiples.get(i);
                            units = productUnits.get(i);
                            images = productImages.get(i);

                            HashMap<String, Object> product_Details_Map = new HashMap<>();

                            product_Details_Map.put("pid", pid);
                            product_Details_Map.put("name", name);
                            product_Details_Map.put("quantity", quantity);
                            product_Details_Map.put("rate", rate);
                            product_Details_Map.put("categoryName", categoryName);
                            product_Details_Map.put("subCategoryName", subCategoryName);
                            product_Details_Map.put("multiple", multiple);
                            product_Details_Map.put("units", units);
                            product_Details_Map.put("images", images);

                            order_Products_Ref.child(order_Id).child(pid).updateChildren(product_Details_Map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()){
                                        update_Database(loadingDialog);
                                    }
                                }
                            });
                        }
                    }
                });

            }
        });
    }

    private void update_Database(final LoadingDialog loadingDialog){

        DatabaseReference cart_Ref = FirebaseDatabase.getInstance().getReference().child("cart").child(Paper.book().read(Prevalent.UserPhoneKey).toString());

        cart_Ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isComplete()){
                    DatabaseReference User_order_No_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Paper.book().read(Prevalent.UserPhoneKey).toString());

                    User_order_No_Ref.child("orders").setValue(Integer.toString(user_Orders_No+1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()){
                                showLoadingDialog(loadingDialog);
                            }
                        }
                    });
                }
            }
        });

    }

    private void showLoadingDialog(LoadingDialog loadingDialog) {

        OrderPlaced_Dialog orderPlaced_dialog = new OrderPlaced_Dialog(this, orderId_For_Test );
        orderPlaced_dialog.setCancelable(false);
        orderPlaced_dialog.setCanceledOnTouchOutside(false);
        orderPlaced_dialog.show();

//        Toast.makeText(PaymentAndOrderPlaceActivity.this, "order Placed Successfully", Toast.LENGTH_SHORT).show();
//
//        Intent intent = new Intent(this, MainActivity.class);
//        finish();
//        intent.putExtra("isFrom", "login");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//        loadingDialog.dismissDialog();
//        startActivity(intent);
    }

    private void get_Cart_Items_And_Total(){

        loadingDialog.startLoadingDialog();

        DatabaseReference cartItems_Get_Ref = FirebaseDatabase.getInstance().getReference().child("cart").child(Paper.book().read(Prevalent.UserPhoneKey).toString());


        cartItems_Get_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int TotalAmount_Loop = 0;

                itemCount = (int) dataSnapshot.getChildrenCount();

                for (DataSnapshot child : dataSnapshot.getChildren()){

                    productIds.add(child.child("pid").getValue().toString());
                    productNames.add(child.child("pname").getValue().toString());
                    productCategoryNames.add(child.child("categoryName").getValue().toString());
                    productSubCategoryNames.add(child.child("subCategoryName").getValue().toString());
                    productImages.add(child.child("image").getValue().toString());
                    productMultiples.add(child.child("multiple").getValue().toString());
                    productRates.add(child.child("rate").getValue().toString());
                    productQuantities.add(child.child("quantity").getValue().toString());
                    productUnits.add(child.child("units").getValue().toString());

                    int item_Amount = Integer.parseInt(child.child("rate").getValue().toString()) * Integer.parseInt(child.child("multiple").getValue().toString());
                    TotalAmount_Loop += item_Amount;
                }

                grand_Total += TotalAmount_Loop;

                DatabaseReference DeliveryCharges_Value_Ref = FirebaseDatabase.getInstance().getReference().child("values").child("deliveryCharge");

                DeliveryCharges_Value_Ref.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        int Delivery_charge = 0 ;

                        for (int i=1 ; i<2 ; i++){
                            Delivery_charge = Integer.parseInt(dataSnapshot.getValue().toString());
                        }

                        DatabaseReference User_order_No_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Paper.book().read(Prevalent.UserPhoneKey).toString());

                        User_order_No_Ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (int i=1 ; i<2 ; i++){

//                                    user_Orders_No = Integer.parseInt(dataSnapshot.child("orders").getValue().toString());

                                    name_User = dataSnapshot.child("name").getValue().toString();

//                                    address = dataSnapshot.child("address").getValue().toString();
//                                    latitude = dataSnapshot.child("latitude").getValue().toString();
//                                    longitude = dataSnapshot.child("longitude").getValue().toString();
                                }



                                DatabaseReference orders_Ref = FirebaseDatabase.getInstance().getReference().child("orders").child(Paper.book().read(Prevalent.UserPhoneKey).toString());

                                orders_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        int check_Count = (int) dataSnapshot.getChildrenCount();




                                        user_Orders_No = check_Count;

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        delivery_Charge = Delivery_charge;

                        grand_Total += delivery_Charge;

                        delivery_Value_TxtVw.setText("Rs "+Integer.toString(Delivery_charge));

                        Grand_Total_Amount_TxtVw.setText("Rs "+Integer.toString(grand_Total));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                total_Cart_Value = TotalAmount_Loop;

                cart_Value_TextVw.setText("Rs "+Integer.toString(TotalAmount_Loop));
                loadingDialog.dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {

        String transactionId = s;

        place_Order("paid",transactionId);
    }

    @Override
    public void onPaymentError(int i, String s) {


    }

}