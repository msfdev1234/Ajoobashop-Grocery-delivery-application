package com.ajoobashop.ajooba.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajoobashop.ajooba.Interface.ItemClickListener;
import com.ajoobashop.ajooba.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener itemClickListener;
    public TextView plus_Btn, minus_Btn, elegant_Number_TxtVw;

    public ImageView product_imageView_Cart;
    public TextView product_Name_Cart;
    public TextView product_Quantity_Cart;
    public TextView product_Quantity_Unit_Cart;

    public TextView product_Mrp_Cart;
    public TextView product_Rate_Cart;

    public ProgressBar progressBar;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        product_imageView_Cart = itemView.findViewById(R.id.product_Image_Cart);
        product_Name_Cart = itemView.findViewById(R.id.product_Name_TxtVw_Cart);
        product_Quantity_Cart = itemView.findViewById(R.id.product_Quantity_TxtVw_Cart);
        product_Quantity_Unit_Cart = itemView.findViewById(R.id.product_Quantity_Units_TxtVw_Cart);
        product_Mrp_Cart = itemView.findViewById(R.id.product_Mrp_TxtVw_Cart);
        product_Rate_Cart = itemView.findViewById(R.id.product_Rate_TxtVw_Cart);

        plus_Btn = itemView.findViewById(R.id.plus_Symbol_Cart);
        minus_Btn = itemView.findViewById(R.id.minus_Symbol_Cart);
        elegant_Number_TxtVw = itemView.findViewById(R.id.elegant_Number_Cart);

        progressBar = itemView.findViewById(R.id.progress_bar_elegant_cart);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
