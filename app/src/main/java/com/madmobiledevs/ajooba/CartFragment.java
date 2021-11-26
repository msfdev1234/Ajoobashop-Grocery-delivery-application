package com.madmobiledevs.ajooba;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.madmobiledevs.ajooba.LoadingDialougs.LoadingDialog;
import com.madmobiledevs.ajooba.Model.Cart;
import com.madmobiledevs.ajooba.Prevalent.Prevalent;
import com.madmobiledevs.ajooba.ViewHolder.CartViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.paperdb.Paper;

public class CartFragment extends Fragment {

    private LoadingDialog loadingDialog;

    RecyclerView recyclerView_Cart;
    RecyclerView.LayoutManager layoutManager;

    private String minValue;

    DatabaseReference CartRef;

    ImageView empty_Cart_ImgVw;

    ImageView back_Btn_ImageView;

    private RelativeLayout bottom_Cart_Layout;
    private TextView total_Amount_TxtVw_1;

    private int overAllTotalPrice=0;

    private int TotalAmount ;
    private Button checkOut_Btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart,container,false);

        Paper.init(v.getContext());
        loadingDialog = new LoadingDialog(getActivity());

        checkOut_Btn = v.findViewById(R.id.checkOut_Btn_cart);

        back_Btn_ImageView = v.findViewById(R.id.back_Btn_ImgView_Cart);

        total_Amount_TxtVw_1 = v.findViewById(R.id.total_Amount_TxtVw);

        CartRef = FirebaseDatabase.getInstance().getReference().child("cart").child(Paper.book().read(Prevalent.UserPhoneKey).toString());

        empty_Cart_ImgVw = v.findViewById(R.id.empty_Cart_Icon);

        recyclerView_Cart = v.findViewById(R.id.cart_Item_RecyclerView);
        layoutManager= new LinearLayoutManager(getContext());
        recyclerView_Cart.setLayoutManager(layoutManager);

        bottom_Cart_Layout = v.findViewById(R.id.bottom_Cart_Layout);

        

        if (getActivity().getClass().getSimpleName() .equals("MainActivity")){
            back_Btn_ImageView.setVisibility(View.INVISIBLE);
        }

        back_Btn_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              getActivity().onBackPressed();
            }
        });

        checkOut_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int total_Amount =  Integer.parseInt(total_Amount_TxtVw_1.getText().toString());
                if (!(total_Amount<Integer.parseInt(minValue))){
                    Intent intent = new Intent(getActivity(), DeliveryOptionsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Minimum order value should be greater than "+ minValue, Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;

    }



    @Override
    public void onStart() {
        super.onStart();

        getValues();
    }

    private void getValues(){
        if (!loadingDialog.isShowing()){
            loadingDialog.startLoadingDialog();
        }
        DatabaseReference value_Ref = FirebaseDatabase.getInstance().getReference().child("values");

        value_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i =1 ; i<2; i++){
                    minValue = dataSnapshot.child("minOrder").getValue().toString();
                }
                checkAvailabilityCartItems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkAvailabilityCartItems() {

        if (!loadingDialog.isShowing()){
            loadingDialog.startLoadingDialog();
        }

        DatabaseReference CartRef_ForCheck = FirebaseDatabase.getInstance().getReference().child("cart");
        CartRef_ForCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(Paper.book().read(Prevalent.UserPhoneKey).toString())) {

                    empty_Cart_ImgVw.setVisibility(View.INVISIBLE);
                    bottom_Cart_Layout.setVisibility(View.VISIBLE);

                    update_Cart_Total();
                    showCartItems();


                } else {
                    empty_Cart_ImgVw.setVisibility(View.VISIBLE);
                    bottom_Cart_Layout.setVisibility(View.INVISIBLE);
                    loadingDialog.dismissDialog();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void showCartItems() {

        FirebaseRecyclerOptions <Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(CartRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter <Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {

                        Picasso.get().load(cart.getImage())
                                .into(cartViewHolder.product_imageView_Cart, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        loadingDialog.dismissDialog();
                                    }
                                    @Override
                                    public void onError(Exception e) {
                                    }
                                });

                        cartViewHolder.product_Name_Cart.setText(cart.getPname());
                        cartViewHolder.product_Quantity_Cart.setText(cart.getQuantity());
                        cartViewHolder.product_Quantity_Unit_Cart.setText(cart.getUnits());
                        cartViewHolder.product_Mrp_Cart.setText(cart.getMrp());
                        cartViewHolder.product_Rate_Cart.setText(cart.getRate());
                        cartViewHolder.elegant_Number_TxtVw.setText(cart.getMultiple());

                        cartViewHolder.plus_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int i = Integer.parseInt(cartViewHolder.elegant_Number_TxtVw.getText().toString());

                                i = i+1;


                                if (i>6){
                                    Toast.makeText(getActivity(), "Cannot add more than this..", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    plus_elegentNumberChange(cartViewHolder.plus_Btn
                                            , cartViewHolder.minus_Btn
                                            , cartViewHolder.elegant_Number_TxtVw
                                            , cartViewHolder.progressBar
                                            , Paper.book().read(Prevalent.UserPhoneKey).toString()
                                            , cart.getPid()
                                            ,cart.getRate()
                                            ,cart.getMultiple()
                                            ,"+");


                                }
                            }
                        });

                        cartViewHolder.minus_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int i = Integer.parseInt(cartViewHolder.elegant_Number_TxtVw.getText().toString());
                                i = i-1;

                                if (i<1){

                                    remove_From_Cart(cartViewHolder.plus_Btn
                                            , cartViewHolder.minus_Btn
                                            ,cartViewHolder.progressBar
                                            ,cart.getPid()
                                            , Paper.book().read(Prevalent.UserPhoneKey).toString()
                                            , cart.getRate()
                                            , cart.getMultiple());

                                }
                                else {

                                    minus_elegentNumberChange(cartViewHolder.plus_Btn
                                            , cartViewHolder.minus_Btn
                                            , cartViewHolder.elegant_Number_TxtVw
                                            , cartViewHolder.progressBar
                                            , Paper.book().read(Prevalent.UserPhoneKey).toString()
                                            , cart.getPid()
                                            , cart.getRate()
                                            , cart.getMultiple()
                                            , "-");

                                }
                            }
                        });



                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout,parent, false);
                        CartViewHolder holder= new CartViewHolder(view);
                        return holder;
                    }
                };

        recyclerView_Cart.setAdapter(adapter);
        adapter.startListening();

    }

    private void lastCheck(){

        loadingDialog.startLoadingDialog();

        DatabaseReference CartRef_ForCheck = FirebaseDatabase.getInstance().getReference().child("cart");
        CartRef_ForCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(Paper.book().read(Prevalent.UserPhoneKey).toString())) {

                    empty_Cart_ImgVw.setVisibility(View.VISIBLE);
                    bottom_Cart_Layout.setVisibility(View.INVISIBLE);
                    loadingDialog.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void update_Cart_Total(){
        DatabaseReference cartTotal_Ref = FirebaseDatabase.getInstance().getReference().child("cart").child(Paper.book().read(Prevalent.UserPhoneKey).toString());
        TotalAmount = 0;
        cartTotal_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot ) {

                int TotalAmount_Loop = 0;

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    int item_Amount = Integer.parseInt(child.child("rate").getValue().toString()) * Integer.parseInt(child.child("multiple").getValue().toString());
                    TotalAmount_Loop += item_Amount;
                }

                setTotalAmount_Text(TotalAmount_Loop);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setTotalAmount_Text(int totalAmount_Loop) {

        if (totalAmount_Loop == 0){
            total_Amount_TxtVw_1.setText(Integer.toString(totalAmount_Loop));
            lastCheck();
        } else {
            total_Amount_TxtVw_1.setText(Integer.toString(totalAmount_Loop));
        }



    }

    private void minus_elegentNumberChange(final TextView plus_btn, final TextView minus_btn, final TextView elegant_number_txtVw, final ProgressBar progressBar, String UserPhoneKey, String pid, final String rate, final String multiple, final String sign) {

        progressBar.setVisibility(View.VISIBLE);

        plus_btn.setEnabled(false);
        minus_btn.setEnabled(false);
        plus_btn.setAlpha(.8f);
        minus_btn.setAlpha(.8f);

        DatabaseReference elegantNum_Ref = FirebaseDatabase.getInstance().getReference().child("cart").child(UserPhoneKey).child(pid).child("multiple");

        int i = Integer.parseInt(elegant_number_txtVw.getText().toString());

        i = i-1;

        elegantNum_Ref.setValue(Integer.toString(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);

                    plus_btn.setEnabled(true);
                    minus_btn.setEnabled(true);
                    plus_btn.setAlpha(1f);
                    minus_btn.setAlpha(1f);

                    int i = Integer.parseInt(elegant_number_txtVw.getText().toString());

                    i = i-1;

                    elegant_number_txtVw.setText(Integer.toString(i));

                    update_Cart_Total();

                }
            }
        });

    }

    private void remove_From_Cart(final TextView plus_btn, final TextView minus_btn, final ProgressBar progressBar, String pid, String UserPhoneKey, final String rate, final String multiple) {

        progressBar.setVisibility(View.VISIBLE);

        plus_btn.setEnabled(false);
        minus_btn.setEnabled(false);
        plus_btn.setAlpha(.8f);
        minus_btn.setAlpha(.8f);

        DatabaseReference elegantNum_Ref = FirebaseDatabase.getInstance().getReference().child("cart").child(UserPhoneKey).child(pid);

        elegantNum_Ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                progressBar.setVisibility(View.INVISIBLE);

                plus_btn.setEnabled(true);
                minus_btn.setEnabled(true);
                plus_btn.setAlpha(1f);
                minus_btn.setAlpha(1f);

                update_Cart_Total();

                Toast.makeText(getActivity(), "Item removed from cart", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void plus_elegentNumberChange(final TextView plus_Btn, final TextView minus_Btn, final TextView number_Elegant, final ProgressBar progressBar, String UserPhoneKey, String pid, final String rate, final String multiple, final String sign){

        progressBar.setVisibility(View.VISIBLE);
        loadingDialog.startLoadingDialog();

        plus_Btn.setEnabled(false);
        minus_Btn.setEnabled(false);
        plus_Btn.setAlpha(.8f);
        minus_Btn.setAlpha(.8f);

        DatabaseReference elegantNum_Ref = FirebaseDatabase.getInstance().getReference().child("cart").child(UserPhoneKey).child(pid).child("multiple");

        int i = Integer.parseInt(number_Elegant.getText().toString());

        i = i+1;

        elegantNum_Ref.setValue(Integer.toString(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);

                    plus_Btn.setEnabled(true);
                    minus_Btn.setEnabled(true);
                    plus_Btn.setAlpha(1f);
                    minus_Btn.setAlpha(1f);

                    int i = Integer.parseInt(number_Elegant.getText().toString());

                    i = i+1;

                    number_Elegant.setText(Integer.toString(i));

                    update_Cart_Total();

                }
            }
        });

    }

}
