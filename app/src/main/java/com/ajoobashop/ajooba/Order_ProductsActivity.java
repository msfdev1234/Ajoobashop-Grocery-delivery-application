package com.ajoobashop.ajooba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ajoobashop.ajooba.LoadingDialougs.LoadingDialog;
import com.ajoobashop.ajooba.Model.OrderItems;
import com.ajoobashop.ajooba.Prevalent.Prevalent;
import com.ajoobashop.ajooba.ViewHolder.OrderProductsViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class Order_ProductsActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;

    private String order_Id;
    private String totalAmount, grandTotal, deliveryCharge;
    private int item_Count;

    private TextView Grand_Total_Amount_TxtVw;
    private TextView cart_Value_TextVw;
    private TextView delivery_Value_TxtVw;
    private TextView items_Total_TxtVw;

    private RecyclerView recyclerView_order_Products;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__products);
        Paper.init(getApplicationContext());
        loadingDialog = new LoadingDialog(this);

        cart_Value_TextVw = findViewById(R.id.Cart_Value_Order_Products);
        delivery_Value_TxtVw = findViewById(R.id.Delivery_Value_Order_Products);
        Grand_Total_Amount_TxtVw = findViewById(R.id.Total_Value_Order_Products);
        items_Total_TxtVw = findViewById(R.id.items_TextVw_Order_Items);

        recyclerView_order_Products = findViewById(R.id.recycler_View_Order_Products);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_order_Products.setLayoutManager(layoutManager);


        order_Id= getIntent().getStringExtra("order_Id");

        getOrder_Details();
        show_Order_Products();

    }

    private void getOrder_Details(){

        DatabaseReference order_Amounts_Ref = FirebaseDatabase.getInstance().getReference().child("orders").child(Paper.book().read(Prevalent.UserPhoneKey).toString());

        order_Amounts_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    totalAmount = dataSnapshot.child(order_Id).child("totalAmount").getValue().toString();
                    deliveryCharge = dataSnapshot.child(order_Id).child("deliveryCharge").getValue().toString();
                    grandTotal = dataSnapshot.child(order_Id).child("grandTotal").getValue().toString();
                }

                cart_Value_TextVw.setText("Rs "+totalAmount);
                delivery_Value_TxtVw.setText("Rs "+deliveryCharge);
                Grand_Total_Amount_TxtVw.setText("Rs "+grandTotal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference order_Items_Ref = FirebaseDatabase.getInstance().getReference().child("orderProducts").child(order_Id);

        order_Items_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    item_Count = (int) dataSnapshot.getChildrenCount();
                }

                items_Total_TxtVw.setText(Integer.toString(item_Count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void show_Order_Products(){

        loadingDialog.startLoadingDialog();

        final DatabaseReference order_Products_ref = FirebaseDatabase.getInstance().getReference().child("orderProducts").child(order_Id);

        FirebaseRecyclerOptions<OrderItems> options =
                new FirebaseRecyclerOptions.Builder<OrderItems>()
                        .setQuery(order_Products_ref, OrderItems.class)
                        .build();

        FirebaseRecyclerAdapter<OrderItems, OrderProductsViewHolder> adapter =
                new FirebaseRecyclerAdapter<OrderItems, OrderProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderProductsViewHolder orderProductsViewHolder, int i, @NonNull OrderItems orderItems) {

                        Picasso.get().load(orderItems.images).
                                into(orderProductsViewHolder.product_imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        loadingDialog.dismissDialog();
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });


                        orderProductsViewHolder.product_Name.setText(orderItems.name);
                        orderProductsViewHolder.product_Quantity.setText(orderItems.quantity);
                        orderProductsViewHolder.product_Quantity_Unit.setText(orderItems.units);
                        orderProductsViewHolder.product_Rate.setText(orderItems.rate);
                        orderProductsViewHolder.product_Multiple.setText(orderItems.multiple);

                    }

                    @NonNull
                    @Override
                    public OrderProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items_layout, parent, false);
                        OrderProductsViewHolder orderProductsViewHolder = new OrderProductsViewHolder(view);
                        return orderProductsViewHolder;

                    }
                };

        recyclerView_order_Products.setAdapter(adapter);
        adapter.startListening();

    }

}