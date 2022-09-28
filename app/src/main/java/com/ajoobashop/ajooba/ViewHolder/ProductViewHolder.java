package com.ajoobashop.ajooba.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajoobashop.ajooba.Interface.ItemClickListener;
import com.ajoobashop.ajooba.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView product_imageView;
    public TextView product_Name;
    public TextView product_Quantity;
    public TextView product_Quantity_Unit, currentyUnavailable_TxtVw;


    public TextView product_Mrp;
    public TextView product_Rate;

    public ProgressBar progressBar, progressBar_for_Elegant;

    public RelativeLayout quantity_Chs_Btn;

    public RelativeLayout elegant_No_Btn;

    public Button add_to_cart_Btn;

    public ItemClickListener listener;

    public TextView minus_Btn, plus_Btn, number_elegant;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        product_imageView = (ImageView) itemView.findViewById(R.id.product_Image);
        product_Name =  itemView.findViewById(R.id.product_Name_TxtVw);
        product_Quantity =  itemView.findViewById(R.id.product_Quantity_TxtVw);
        product_Quantity_Unit = itemView.findViewById(R.id.product_Quantity_Units_TxtVw);

        product_Mrp =  itemView.findViewById(R.id.product_Mrp_TxtVw);
        product_Rate =  itemView.findViewById(R.id.product_Rate_TxtVw);

        add_to_cart_Btn = itemView.findViewById(R.id.Add_To_Cart_Btn);
        quantity_Chs_Btn = itemView.findViewById(R.id.Quantity_Chs_Btn);

        progressBar = itemView.findViewById(R.id.progress_bar_layout);

        elegant_No_Btn = itemView.findViewById(R.id.RL_elegant_number);

        minus_Btn = itemView.findViewById(R.id.minus_Symbol_products);
        plus_Btn = itemView.findViewById(R.id.plus_Symbol_products);
        number_elegant = itemView.findViewById(R.id.elegant_Number_products);

        currentyUnavailable_TxtVw = itemView.findViewById(R.id.currentyUnavailable_TxtVw);

        progressBar_for_Elegant = itemView.findViewById(R.id.progress_bar_elegant_products);


    }

    public void setItemClickListener(com.ajoobashop.ajooba.Interface.ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }
}
