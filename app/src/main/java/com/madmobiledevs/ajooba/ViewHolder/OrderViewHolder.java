package com.madmobiledevs.ajooba.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madmobiledevs.ajooba.Interface.ItemClickListener;
import com.madmobiledevs.ajooba.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView day_Date_TxtVw, timeSlot_TxtVw, order_Id_TxtVw, amount_TxtVw , items_TxtVw, order_Status_TxtVw, order_Status_Dot_TxtVw;
    public View dot_Vw;

    private ItemClickListener listener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        day_Date_TxtVw = itemView.findViewById(R.id.date_Day_TxtVw);
        timeSlot_TxtVw = itemView.findViewById(R.id.time_Slot_Active_Orders);
        order_Id_TxtVw = itemView.findViewById(R.id.order_Id_Active_Orders);
        amount_TxtVw = itemView.findViewById(R.id.amount_Active_Orders);
        items_TxtVw = itemView.findViewById(R.id.items_Total_Active_Orders);
        order_Status_TxtVw = itemView.findViewById(R.id.order_Status_Active_Orders);

        order_Status_Dot_TxtVw = itemView.findViewById(R.id.order_Status_Dot_TxtVw);
        dot_Vw = itemView.findViewById(R.id.dot_actv);

    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(com.madmobiledevs.ajooba.Interface.ItemClickListener listener){
        this.listener = listener;
    }

}
