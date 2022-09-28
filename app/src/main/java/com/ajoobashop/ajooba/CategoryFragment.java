package com.ajoobashop.ajooba;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class CategoryFragment extends Fragment {

    private CardView vegetable_Btn, fruits_Btn, grocery_Btn, diary_Btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category,container,false);

        vegetable_Btn = v.findViewById(R.id.vegetable_Chs_Btn_Category);
        fruits_Btn = v.findViewById(R.id.fruits_Chs_Btn_Category);
        grocery_Btn = v.findViewById(R.id.grocery_Chs_Btn_Category);
        diary_Btn = v.findViewById(R.id.dairy_Chs_Btn_Category);


        vegetable_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), Vegeteable_SubCategory_Activity.class);
                intent.putExtra("category", "vegetables");

                startActivity(intent);
            }
        });
        fruits_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), Vegeteable_SubCategory_Activity.class);
                intent.putExtra("category", "fruits");

                startActivity(intent);
            }
        });

        grocery_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), Vegeteable_SubCategory_Activity.class);
                intent.putExtra("category", "groceries");

                startActivity(intent);
            }
        });

        diary_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), Vegeteable_SubCategory_Activity.class);
                intent.putExtra("category", "diary");

                startActivity(intent);
            }
        });

        return v;

    }
}
