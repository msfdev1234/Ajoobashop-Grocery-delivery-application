package com.ajoobashop.ajooba.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajoobashop.ajooba.Interface.ItemClickListener;
import com.ajoobashop.ajooba.R;

public class OrderProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    private ItemClickListener itemClickListener;

    public ImageView product_imageView;
    public TextView product_Name;
    public TextView product_Quantity;
    public TextView product_Quantity_Unit;


    public TextView product_Rate;
    public TextView product_Multiple;

    public OrderProductsViewHolder(@NonNull View itemView) {
        super(itemView);

        product_Name = itemView.findViewById(R.id.product_Name_Order_Products);
        product_imageView = itemView.findViewById(R.id.product_Image_Order_Products);
        product_Quantity = itemView.findViewById(R.id.product_Quantity_Order_Products);
        product_Quantity_Unit = itemView.findViewById(R.id.product_Quantity_Units_Order_Products);

        product_Rate = itemView.findViewById(R.id.product_Rate_TxtVw_Order_Products);
        product_Multiple = itemView.findViewById(R.id.multiple_TxtVw_Order_Products);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
