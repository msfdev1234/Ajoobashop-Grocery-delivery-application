package com.ajoobashop.ajooba.ViewHolder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajoobashop.ajooba.Interface.ItemClickListener;
import com.ajoobashop.ajooba.R;

public class Adresses_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView type_address_Txtw, default_Txtw, name_TxtVw, hNo_TxtVw, flatNo_TxtVw, address_TxtVw, phone_TxtVw;
    public ImageView delete_Btn;

    public RelativeLayout view_Whole;

    public CheckBox checkBox_address ;


    private ItemClickListener itemClickListener;

    public Adresses_ViewHolder(@NonNull View itemView) {
        super(itemView);

        type_address_Txtw = itemView.findViewById(R.id.type_address_Addresses);
        default_Txtw = itemView.findViewById(R.id.default_Txtw_Addresses);
        name_TxtVw = itemView.findViewById(R.id.name_TxtVw_Addresses);
        hNo_TxtVw = itemView.findViewById(R.id.hNo_TxtVw_Addresses);
        flatNo_TxtVw = itemView.findViewById(R.id.flatNo_TxtVw_Addresses);
        address_TxtVw = itemView.findViewById(R.id.address_TxtVw_Addresses);
        phone_TxtVw = itemView.findViewById(R.id.phone_TxtVw_Addresses);
        delete_Btn = itemView.findViewById(R.id.delete_Btn_Addresses);
        checkBox_address = itemView.findViewById(R.id.cash_checkbox_Addresses);
        view_Whole = itemView.findViewById(R.id.view_Whole_Addresses);


    }

    @Override
    public void onClick(View view) {

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
