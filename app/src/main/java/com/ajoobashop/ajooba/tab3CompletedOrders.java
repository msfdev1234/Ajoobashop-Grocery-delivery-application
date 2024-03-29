package com.ajoobashop.ajooba;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ajoobashop.ajooba.LoadingDialougs.LoadingDialog;
import com.ajoobashop.ajooba.Model.orders;
import com.ajoobashop.ajooba.Prevalent.Prevalent;
import com.ajoobashop.ajooba.ViewHolder.CompletedOrdersViewHolder;

import io.paperdb.Paper;

public class tab3CompletedOrders extends Fragment {

    private RecyclerView completed_Orders_RecyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference order_Ref;

    private LoadingDialog loadingDialog;

    private int item_Count;

    private ImageView no_Orders_Icon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab3_fragment_completed_orders, container, false);

        loadingDialog = new LoadingDialog(getActivity());

        order_Ref = FirebaseDatabase.getInstance().getReference().child("orders").child(Paper.book().read(Prevalent.UserPhoneKey).toString());

        completed_Orders_RecyclerView = view.findViewById(R.id.recyclerView_Compltd_Orders);
        no_Orders_Icon = view.findViewById(R.id.no_Orders_Icon_Completed);

        layoutManager = new LinearLayoutManager(getContext());
        completed_Orders_RecyclerView.setLayoutManager(layoutManager);
        return view;
    }

    private void checkAvailabilityItems() {

       loadingDialog.startLoadingDialog();

        DatabaseReference CartRef_ForCheck = FirebaseDatabase.getInstance().getReference().child("orders").child(Paper.book().read(Prevalent.UserPhoneKey).toString());
        CartRef_ForCheck.orderByChild("status_Order").equalTo("Completed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    loadingDialog.dismissDialog();
                    showAllOrders();

                } else {

                    loadingDialog.dismissDialog();
                    no_Orders_Icon.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {

        super.onStart();
        checkAvailabilityItems();

    }

    private void showAllOrders() {

        FirebaseRecyclerOptions<orders> options =
                new FirebaseRecyclerOptions.Builder<orders>()
                        .setQuery(order_Ref.orderByChild("status_Order").equalTo("Completed"), orders.class)
                        .build();
        FirebaseRecyclerAdapter<orders, CompletedOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<orders, CompletedOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CompletedOrdersViewHolder completedOrdersViewHolder, int i, @NonNull final orders orders) {



                        DatabaseReference items_Count_Ref = FirebaseDatabase.getInstance().getReference().child("orderProducts").child(orders.getOrderId());

                        items_Count_Ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                item_Count = (int) dataSnapshot.getChildrenCount();

                                completedOrdersViewHolder.items_TxtVw.setText(Integer.toString(item_Count));

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        completedOrdersViewHolder.timeSlot_TxtVw.setText(orders.getTimeSlot());
                        completedOrdersViewHolder.order_Id_TxtVw.setText("AJ-"+orders.getOrderId());
                        completedOrdersViewHolder.amount_TxtVw.setText("Rs "+orders.getGrandTotal());
                        completedOrdersViewHolder.day_Date_TxtVw.setText(orders.getFullDay());

                        completedOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getActivity(),Order_ProductsActivity.class);
                                intent.putExtra("order_Id", orders.getOrderId());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CompletedOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_order_layout, parent, false);

                        CompletedOrdersViewHolder completedOrdersViewHolder = new CompletedOrdersViewHolder(view);
                        return completedOrdersViewHolder;
                    }
                };

        completed_Orders_RecyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
