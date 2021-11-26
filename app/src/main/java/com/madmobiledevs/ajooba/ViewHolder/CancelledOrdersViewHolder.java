package com.madmobiledevs.ajooba.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madmobiledevs.ajooba.Interface.ItemClickListener;
import com.madmobiledevs.ajooba.R;

public class CancelledOrdersViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView day_Date_TxtVw, timeSlot_TxtVw, order_Id_TxtVw, amount_TxtVw , items_TxtVw, order_Status_TxtVw, order_Status_Dot_TxtVw;
    public View dot_Vw;

    private ItemClickListener listener;


    public CancelledOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        day_Date_TxtVw = itemView.findViewById(R.id.date_Day_TxtVw_Cancelled);
        timeSlot_TxtVw = itemView.findViewById(R.id.time_Slot_Cancelled_Orders);
        order_Id_TxtVw = itemView.findViewById(R.id.order_Id_Cancelled_Orders);
        amount_TxtVw = itemView.findViewById(R.id.amount_Cancelled_Orders);
        items_TxtVw = itemView.findViewById(R.id.items_Total_Cancelled_Orders);
        order_Status_TxtVw = itemView.findViewById(R.id.order_Status_Cancelled_Orders);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }


    public void setItemClickListener(com.madmobiledevs.ajooba.Interface.ItemClickListener listener){
        this.listener = listener;
    }
}
