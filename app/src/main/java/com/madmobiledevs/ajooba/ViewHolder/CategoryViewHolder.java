package com.madmobiledevs.ajooba.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madmobiledevs.ajooba.Interface.ItemClickListener;
import com.madmobiledevs.ajooba.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView categoryImage;
    public TextView categoryNameTextView;
    public ItemClickListener listener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryImage = itemView.findViewById(R.id.Category_Image_view);
        categoryNameTextView = itemView.findViewById(R.id.Category_Name_TextView);


    }

    public void setItemClickListener(com.madmobiledevs.ajooba.Interface.ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
}
